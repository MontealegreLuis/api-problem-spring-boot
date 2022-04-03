package com.montealegreluis.apiproblemspringboot.springboot.validation;

import static com.montealegreluis.activityfeed.ActivityBuilder.anActivity;
import static com.montealegreluis.apiproblem.ApiProblemBuilder.aProblem;

import com.montealegreluis.activityfeed.ActivityBuilder;
import com.montealegreluis.apiproblem.ApiProblem;
import com.montealegreluis.apiproblem.ApiProblemBuilder;
import com.montealegreluis.apiproblem.Status;
import java.util.Map;

public interface WithValidationErrors {
  default ApiProblemBuilder unprocessableEntityProblem(final Map<String, String> errors) {
    return aProblem()
        .from(Status.UNPROCESSABLE_ENTITY)
        .withDetail("Invalid input provided")
        .with("code", "invalid-input")
        .with("errors", errors);
  }

  default ActivityBuilder invalidInputActivity() {
    return anActivity()
        .warning()
        .withIdentifier("invalid-input")
        .withMessage("Invalid input provided");
  }

  default void addValidationErrors(final ActivityBuilder builder, final ApiProblem problem) {
    builder.with("errors", problem.getAdditionalProperties().get("errors"));
  }
}
