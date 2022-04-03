package com.montealegreluis.apiproblemspringboot.springboot.validation;

import com.montealegreluis.activityfeed.ActivityBuilder;
import com.montealegreluis.apiproblem.ApiProblem;
import com.montealegreluis.apiproblem.ApiProblemBuilder;
import com.montealegreluis.apiproblemspringboot.springboot.LoggingTrait;
import com.montealegreluis.apiproblemspringboot.springboot.ProblemResponseTrait;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.NativeWebRequest;

public interface MethodArgumentNotValidTrait
    extends LoggingTrait, ProblemResponseTrait, WithValidationErrors {
  @ExceptionHandler
  default ResponseEntity<ApiProblem> handleMethodArgumentNotValid(
      final MethodArgumentNotValidException exception, final NativeWebRequest request) {
    final ApiProblemBuilder builder = builderForMethodArgumentNotValid(exception);

    enhanceMethodArgumentNotValidProblem(builder, exception, request);

    final ApiProblem problem = builder.build();

    final ActivityBuilder activityBuilder = builderForMethodArgumentNotValidActivity(exception);

    enhanceMethodArgumentNotValidActivity(activityBuilder, problem, request);

    log(activityBuilder.build());

    return problemResponse(problem, HttpStatus.UNPROCESSABLE_ENTITY);
  }

  default ApiProblemBuilder builderForMethodArgumentNotValid(
      final MethodArgumentNotValidException exception) {
    final Map<String, String> errors =
        exception.getBindingResult().getFieldErrors().stream()
            .collect(Collectors.toMap(FieldError::getField, FieldError::getDefaultMessage));

    return unprocessableEntityProblem(errors);
  }

  default void enhanceMethodArgumentNotValidProblem(
      final ApiProblemBuilder builder,
      final MethodArgumentNotValidException exception,
      final NativeWebRequest request) {}

  default ActivityBuilder builderForMethodArgumentNotValidActivity(
      MethodArgumentNotValidException exception) {
    return invalidInputActivity();
  }

  default void enhanceMethodArgumentNotValidActivity(
      ActivityBuilder builder, ApiProblem problem, NativeWebRequest request) {
    addValidationErrors(builder, problem);
  }
}
