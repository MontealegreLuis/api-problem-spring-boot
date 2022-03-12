package com.montealegreluis.apiproblemspringboot.springboot.validation;

import static com.montealegreluis.activityfeed.ActivityBuilder.anActivity;
import static com.montealegreluis.apiproblem.ApiProblemBuilder.aProblem;
import static com.montealegreluis.apiproblem.Status.UNPROCESSABLE_ENTITY;
import static net.logstash.logback.marker.Markers.appendEntries;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;

import com.montealegreluis.activityfeed.Activity;
import com.montealegreluis.activityfeed.ActivityBuilder;
import com.montealegreluis.activityfeed.ActivityFeed;
import com.montealegreluis.apiproblem.ApiProblem;
import com.montealegreluis.apiproblem.ApiProblemBuilder;
import java.net.URI;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Path;
import javax.validation.metadata.ConstraintDescriptor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.springframework.http.MediaType;
import org.springframework.web.context.request.NativeWebRequest;

final class ValidationAdviceTest {
  @Test
  void it_generates_a_problem_response_from_a_constraint_violation() {
    var advice = new ValidationAdvice() {};

    var response = advice.handleConstraintViolation(constraintViolationException, null);

    var apiProblem = response.getBody();
    assertNotNull(apiProblem);
    assertEquals(UNPROCESSABLE_ENTITY.code(), apiProblem.getStatus());
    assertEquals(UNPROCESSABLE_ENTITY.reason(), apiProblem.getTitle());
    assertEquals(UNPROCESSABLE_ENTITY.type(), apiProblem.getType());
    assertEquals(1, apiProblem.getAdditionalProperties().size());
    assertTrue(apiProblem.getAdditionalProperties().containsKey("errors"));
    assertInstanceOf(Map.class, apiProblem.getAdditionalProperties().get("errors"));
    @SuppressWarnings("unchecked")
    Map<String, String> errors =
        (Map<String, String>) apiProblem.getAdditionalProperties().get("errors");
    assertTrue(errors.containsKey("firstName"));
    assertEquals("value is required", errors.get("firstName"));
    assertTrue(errors.containsKey("lastName"));
    assertEquals("value is required", errors.get("lastName"));
    assertTrue(errors.containsKey("email"));
    assertEquals("value is required", errors.get("email"));
  }

  @Test
  void it_generates_a_custom_problem_response_from_a_constraint_violation_exception() {
    var advice =
        new ValidationAdvice() {
          @Override
          public ApiProblemBuilder builderForConstraintViolation(
              ConstraintViolationException exception) {
            return aProblem().witDefaultType(UNPROCESSABLE_ENTITY);
          }
        };

    var response = advice.handleConstraintViolation(constraintViolationException, null);

    var apiProblem = response.getBody();
    assertNotNull(apiProblem);
    assertEquals(UNPROCESSABLE_ENTITY.code(), apiProblem.getStatus());
    assertEquals(UNPROCESSABLE_ENTITY.reason(), apiProblem.getTitle());
    assertEquals(URI.create("about:blank"), apiProblem.getType());
    assertEquals(0, apiProblem.getAdditionalProperties().size());
  }

  @Test
  void it_logs_a_constraint_violation_exception_before_creating_API_problem_response() {
    var activity =
        Activity.error(
            "invalid-input", "Invalid input provided", (context) -> context.put("errors", errors));
    var logger = mock(Logger.class);
    when(logger.isWarnEnabled()).thenReturn(true);
    var aFeed = new ActivityFeed(logger);
    var advice =
        new ValidationAdvice() {
          @Override
          public ActivityFeed feed() {
            return aFeed;
          }
        };

    advice.handleConstraintViolation(constraintViolationException, null);

    verify(logger, times(1)).warn(appendEntries(activity.context()), activity.message());
  }

  @Test
  void it_customizes_logging_event_before_returning_error_response() {
    var activity =
        Activity.error(
            "validation-error",
            "Input didn't pass validation",
            (context) -> {
              context.put("errors", errors);
              context.put("code", "unprocessable-entity");
            });
    var logger = mock(Logger.class);
    when(logger.isWarnEnabled()).thenReturn(true);
    var aFeed = new ActivityFeed(logger);
    var advice =
        new ValidationAdvice() {
          @Override
          public ActivityFeed feed() {
            return aFeed;
          }

          @Override
          public void enhanceConstraintViolationProblem(
              final ApiProblemBuilder builder, final NativeWebRequest nativeRequest) {
            builder.with("code", "unprocessable-entity");
          }

          @Override
          public ActivityBuilder builderForConstraintViolationActivity(
              final ConstraintViolationException exception) {
            return anActivity()
                .warning()
                .withIdentifier("validation-error")
                .withMessage("Input didn't pass validation");
          }

          @Override
          public void enhanceConstraintViolationActivity(
              final ActivityBuilder builder,
              final ApiProblem problem,
              final NativeWebRequest request) {
            builder
                .with("errors", problem.getAdditionalProperties().get("errors"))
                .with("code", problem.getAdditionalProperties().get("code"));
          }
        };

    advice.handleConstraintViolation(constraintViolationException, null);

    verify(logger, times(1)).warn(appendEntries(activity.context()), activity.message());
  }

  @Test
  void it_returns_a_422_status_code_and_problem_content_type_header() {
    var advice = new ValidationAdvice() {};

    var response = advice.handleConstraintViolation(constraintViolationException, null);

    assertEquals(422, response.getStatusCodeValue());
    assertEquals(
        MediaType.parseMediaType("application/problem+json"),
        response.getHeaders().getContentType());
  }

  @BeforeEach
  void let() {
    var violations = new HashSet<ConstraintViolation<String>>();
    violations.add(new RequiredValueConstraintViolation("firstName"));
    violations.add(new RequiredValueConstraintViolation("lastName"));
    violations.add(new RequiredValueConstraintViolation("email"));
    constraintViolationException = new ConstraintViolationException(violations);
    errors = new HashMap<>();
    errors.put("lastName", "value is required");
    errors.put("firstName", "value is required");
    errors.put("email", "value is required");
  }

  private ConstraintViolationException constraintViolationException;
  private HashMap<String, String> errors;

  private static class RequiredValueConstraintViolation implements ConstraintViolation<String> {

    private final String propertyPath;

    public RequiredValueConstraintViolation(String propertyPath) {
      this.propertyPath = propertyPath;
    }

    @Override
    public String getMessage() {
      return "value is required";
    }

    @Override
    public Path getPropertyPath() {
      return new Path() {
        @Override
        public Iterator<Node> iterator() {
          return null;
        }

        @Override
        public String toString() {
          return propertyPath;
        }
      };
    }

    @Override
    public String getMessageTemplate() {
      return null;
    }

    @Override
    public String getRootBean() {
      return null;
    }

    @Override
    public Class<String> getRootBeanClass() {
      return null;
    }

    @Override
    public Object getLeafBean() {
      return null;
    }

    @Override
    public Object[] getExecutableParameters() {
      return new Object[0];
    }

    @Override
    public Object getExecutableReturnValue() {
      return null;
    }

    @Override
    public Object getInvalidValue() {
      return null;
    }

    @Override
    public ConstraintDescriptor<?> getConstraintDescriptor() {
      return null;
    }

    @Override
    public <U> U unwrap(Class<U> type) {
      return null;
    }
  }
}
