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

public interface ThrowableAdvice extends LoggingTrait {
  @ExceptionHandler
  default ResponseEntity<ApiProblem> handleThrowable(Throwable exception) {
    final ApiProblemBuilder builder =
        aProblem()
            .withType(INTERNAL_SERVER_ERROR.type())
            .withStatus(INTERNAL_SERVER_ERROR.code())
            .withTitle(INTERNAL_SERVER_ERROR.reason());

    if (includeStackTrace()) {
      builder.withException(exception);
    }

    logThrowable(exception);

    return new ResponseEntity<>(builder.build(), HttpStatus.INTERNAL_SERVER_ERROR);
  }

  default boolean includeStackTrace() {
    return false;
  }

  default void logThrowable(Throwable exception) {
    final Activity activity =
        error(
            "application-error",
            exception.getMessage(),
            (context) -> context.put("exception", contextFrom(exception)));

    log(activity);
  }
}
