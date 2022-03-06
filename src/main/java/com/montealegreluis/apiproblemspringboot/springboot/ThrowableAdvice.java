package com.montealegreluis.apiproblemspringboot.springboot;

import static com.montealegreluis.activityfeed.Activity.error;
import static com.montealegreluis.activityfeed.ExceptionContextFactory.contextFrom;
import static com.montealegreluis.apiproblem.ApiProblemBuilder.aProblem;
import static com.montealegreluis.apiproblem.Status.INTERNAL_SERVER_ERROR;

import com.montealegreluis.activityfeed.Activity;
import com.montealegreluis.apiproblem.ApiProblem;
import com.montealegreluis.apiproblem.ApiProblemBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.NativeWebRequest;

public interface ThrowableAdvice extends LoggingTrait, ProblemResponseTrait {
  /**
   * Creates an API Problem response from a `Throwable` instance
   *
   * <p>This method creates a Problem response executing the following steps
   *
   * <ul>
   *   <li>Sets the status code to 500
   *   <li>Includes the exception message in the `detail` property
   *   <li>Checks if the exception stack trace should be included in the response (disabled by
   *       default)
   *   <li>Logs the exception information
   *   <li>Returns an Internal Server Error response with the API problem created in previous steps
   * </ul>
   */
  @ExceptionHandler
  default ResponseEntity<ApiProblem> handleThrowable(
      final Throwable exception, final NativeWebRequest request) {
    final ApiProblemBuilder builder = builderForThrowable(exception);

    enhanceThrowableProblem(builder, request);

    final ApiProblem problem = builder.build();

    final Activity throwableActivity = createThrowableActivity(exception, problem, request);

    log(throwableActivity);

    return problemResponse(problem, HttpStatus.INTERNAL_SERVER_ERROR);
  }

  /**
   * Override this method if you want to customize how the exception information is included in the
   * API Problem object
   */
  default ApiProblemBuilder builderForThrowable(final Throwable exception) {
    final ApiProblemBuilder builder =
        aProblem().from(INTERNAL_SERVER_ERROR).withDetail(exception.getMessage());

    if (includeStackTrace()) {
      builder.withException(exception);
    }

    return builder;
  }

  /**
   * Override this method if you need to append additional properties to your Throwable API Problem
   *
   * <p>You can use the current HTTP request to gather additional information and enhance your API
   * Problem response
   */
  default void enhanceThrowableProblem(
      final ApiProblemBuilder builder, final NativeWebRequest nativeRequest) {}

  /**
   * Override this method if you need to choose a log level for your logging event and to log
   * additional information beside the exception itself
   *
   * <p>You can use the problem to be returned as response, and the original HTTP request to enhance
   * your logging event
   */
  default Activity createThrowableActivity(
      final Throwable exception, final ApiProblem problem, final NativeWebRequest request) {
    return error(
        "application-error",
        exception.getMessage(),
        (context) -> context.put("exception", contextFrom(exception)));
  }

  /**
   * Override this method if you want to conditionally determine when include an exception in a
   * problem response
   *
   * <p>Exceptions are not included in the API Problem response by default
   */
  default boolean includeStackTrace() {
    return false;
  }
}
