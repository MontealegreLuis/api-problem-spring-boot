package com.montealegreluis.apiproblemspringboot.springboot;

import static com.montealegreluis.activityfeed.ExceptionContextFactory.contextFrom;
import static com.montealegreluis.apiproblem.Status.INTERNAL_SERVER_ERROR;
import static net.logstash.logback.marker.Markers.appendEntries;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.montealegreluis.activityfeed.Activity;
import com.montealegreluis.activityfeed.ActivityFeed;
import com.montealegreluis.apiproblem.ApiProblem;
import com.montealegreluis.apiproblem.ApiProblemBuilder;
import java.net.URI;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.context.request.NativeWebRequest;

final class ThrowableAdviceTest {
  @Test
  void it_generates_a_problem_response_from_a_Throwable() {
    var advice = new ThrowableAdvice() {};

    var response = advice.handleThrowable(new RuntimeException("Something wen wrong"), null);

    var apiProblem = response.getBody();
    assertNotNull(apiProblem);
    assertEquals(INTERNAL_SERVER_ERROR.code(), apiProblem.getStatus());
    assertEquals(INTERNAL_SERVER_ERROR.reason(), apiProblem.getTitle());
    assertEquals(INTERNAL_SERVER_ERROR.type(), apiProblem.getType());
    assertEquals(0, apiProblem.getAdditionalProperties().size());
  }

  @Test
  void it_includes_an_exception_and_its_stacktrace_in_a_problem() {
    var advice =
        new ThrowableAdvice() {
          @Override
          public boolean includeStackTrace() {
            return true;
          }
        };

    var response = advice.handleThrowable(new RuntimeException("Something wen wrong"), null);

    var apiProblem = response.getBody();
    assertNotNull(apiProblem);
    assertEquals(INTERNAL_SERVER_ERROR.code(), apiProblem.getStatus());
    assertEquals(INTERNAL_SERVER_ERROR.reason(), apiProblem.getTitle());
    assertEquals(INTERNAL_SERVER_ERROR.type(), apiProblem.getType());
    assertEquals(1, apiProblem.getAdditionalProperties().size());
    assertTrue(apiProblem.getAdditionalProperties().containsKey("exception"));
  }

  @Test
  void it_generates_a_custom_problem_response_from_a_Throwable() {
    var advice =
        new ThrowableAdvice() {
          @Override
          public ResponseEntity<ApiProblem> handleThrowable(
              final Throwable exception, final NativeWebRequest request) {
            var problem = ApiProblem.witDefaultType(INTERNAL_SERVER_ERROR);

            return new ResponseEntity<>(problem, HttpStatus.INTERNAL_SERVER_ERROR);
          }
        };

    var response = advice.handleThrowable(new RuntimeException("Something wen wrong"), null);

    var apiProblem = response.getBody();
    assertNotNull(apiProblem);
    assertEquals(INTERNAL_SERVER_ERROR.code(), apiProblem.getStatus());
    assertEquals(INTERNAL_SERVER_ERROR.reason(), apiProblem.getTitle());
    assertEquals(URI.create("about:blank"), apiProblem.getType());
    assertEquals(0, apiProblem.getAdditionalProperties().size());
  }

  @Test
  void it_logs_Throwable_before_creating_API_problem_response() {
    var exception = new RuntimeException("Something went wrong");
    var activity =
        Activity.error(
            "application-error",
            exception.getMessage(),
            (context) -> context.put("exception", contextFrom(exception)));
    var logger = mock(Logger.class);
    when(logger.isErrorEnabled()).thenReturn(true);
    var aFeed = new ActivityFeed(logger);
    var advice =
        new ThrowableAdvice() {
          @Override
          public ActivityFeed feed() {
            return aFeed;
          }
        };

    advice.handleThrowable(exception, null);

    verify(logger, times(1)).error(appendEntries(activity.context()), activity.message());
  }

  @Test
  void it_customizes_logging_event_before_returning_error_response() {
    var exception = new RuntimeException("Something went wrong");
    var activity =
        Activity.error(
            "application-error",
            exception.getMessage(),
            (context) -> {
              context.put("exception", contextFrom(exception));
              context.put("code", "internal-server-error");
            });
    var logger = mock(Logger.class);
    when(logger.isErrorEnabled()).thenReturn(true);
    var aFeed = new ActivityFeed(logger);
    var advice =
        new ThrowableAdvice() {
          @Override
          public ActivityFeed feed() {
            return aFeed;
          }

          @Override
          public void enhanceThrowableProblem(
              final ApiProblemBuilder builder, final NativeWebRequest nativeRequest) {
            builder.with("code", "internal-server-error");
          }

          @Override
          public Activity createThrowableActivity(
              final Throwable exception, final ApiProblem problem, final NativeWebRequest request) {
            return Activity.error(
                "application-error",
                exception.getMessage(),
                (context) -> {
                  context.put("exception", contextFrom(exception));
                  context.put("code", problem.getAdditionalProperties().get("code"));
                });
          }
        };

    advice.handleThrowable(exception, null);

    verify(logger, times(1)).error(appendEntries(activity.context()), activity.message());
  }

  @Test
  void it_returns_a_500_status_code_and_problem_content_type_header() {
    var advice = new ThrowableAdvice() {};

    var response = advice.handleThrowable(new RuntimeException("Something wen wrong"), null);

    assertEquals(500, response.getStatusCodeValue());
    assertEquals(
        MediaType.parseMediaType("application/problem+json"),
        response.getHeaders().getContentType());
  }
}
