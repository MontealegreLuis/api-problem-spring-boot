package com.montealegreluis.apiproblemspringboot.springboot.validation;

import static com.montealegreluis.apiproblem.ApiProblemBuilder.aProblem;

import com.montealegreluis.activityfeed.Activity;
import com.montealegreluis.apiproblem.ApiProblem;
import com.montealegreluis.apiproblem.ApiProblemBuilder;
import com.montealegreluis.apiproblem.Status;
import com.montealegreluis.apiproblemspringboot.springboot.LoggingTrait;
import com.montealegreluis.apiproblemspringboot.springboot.ProblemResponseTrait;
import java.util.Map;
import java.util.stream.Collectors;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.NativeWebRequest;

public interface ConstraintViolationTrait extends LoggingTrait, ProblemResponseTrait {
  @ExceptionHandler
  default ResponseEntity<ApiProblem> handleConstraintViolation(
      final ConstraintViolationException exception, final NativeWebRequest request) {
    final ApiProblemBuilder builder = builderForConstraintViolation(exception);

    enhanceConstraintViolationProblem(builder, request);

    final ApiProblem problem = builder.build();

    final Activity activity = createConstraintViolationActivity(exception, problem, request);

    log(activity);

    return problemResponse(problem, HttpStatus.UNPROCESSABLE_ENTITY);
  }

  default ApiProblemBuilder builderForConstraintViolation(
      final ConstraintViolationException exception) {
    final Map<String, String> errors =
        exception.getConstraintViolations().stream()
            .collect(
                Collectors.toMap(
                    (violation) -> violation.getPropertyPath().toString(),
                    ConstraintViolation::getMessage));

    return aProblem()
        .from(Status.UNPROCESSABLE_ENTITY)
        .withDetail("Invalid input provided")
        .with("errors", errors);
  }

  default void enhanceConstraintViolationProblem(
      final ApiProblemBuilder builder, final NativeWebRequest request) {}

  default Activity createConstraintViolationActivity(
      final ConstraintViolationException exception,
      final ApiProblem problem,
      final NativeWebRequest request) {
    return Activity.warning(
        "invalid-input",
        "Invalid input provided",
        (context) -> context.put("errors", problem.getAdditionalProperties().get("errors")));
  }
}
