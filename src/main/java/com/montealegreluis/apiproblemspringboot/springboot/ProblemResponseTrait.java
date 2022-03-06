package com.montealegreluis.apiproblemspringboot.springboot;

import com.montealegreluis.apiproblem.ApiProblem;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

public interface ProblemResponseTrait {
  MediaType PROBLEM_CONTENT_TYPE = MediaType.parseMediaType("application/problem+json");

  default ResponseEntity<ApiProblem> problemResponse(
      final ApiProblem problem, final HttpStatus status) {
    return ResponseEntity.status(status).contentType(PROBLEM_CONTENT_TYPE).body(problem);
  }
}
