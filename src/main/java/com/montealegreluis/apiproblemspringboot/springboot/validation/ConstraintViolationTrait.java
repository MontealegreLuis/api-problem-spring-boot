package com.montealegreluis.apiproblemspringboot.springboot.validation;

import static com.montealegreluis.activityfeed.ActivityBuilder.*;
import static com.montealegreluis.apiproblem.ApiProblemBuilder.aProblem;

import com.montealegreluis.activityfeed.ActivityBuilder;
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

    enhanceConstraintViolationProblem(builder, exception, request);

    final ApiProblem problem = builder.build();

    final ActivityBuilder activityBuilder = builderForConstraintViolationActivity(exception);

    enhanceConstraintViolationActivity(activityBuilder, problem, request);

    log(activityBuilder.build());

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
        .with("code", "invalid-input")
        .with("errors", errors);
  }

  default void enhanceConstraintViolationProblem(
      final ApiProblemBuilder builder,
      final ConstraintViolationException exception,
      final NativeWebRequest request) {}

  default ActivityBuilder builderForConstraintViolationActivity(
      final ConstraintViolationException exception) {
    return anActivity()
        .warning()
        .withIdentifier("invalid-input")
        .withMessage("Invalid input provided");
  }

  default void enhanceConstraintViolationActivity(
      final ActivityBuilder builder, final ApiProblem problem, final NativeWebRequest request) {
    builder.with("errors", problem.getAdditionalProperties().get("errors"));
  }
}
