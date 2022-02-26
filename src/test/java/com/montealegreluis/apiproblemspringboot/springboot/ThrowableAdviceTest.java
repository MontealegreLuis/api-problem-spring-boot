package com.montealegreluis.apiproblemspringboot.springboot;

import static com.montealegreluis.apiproblem.Status.INTERNAL_SERVER_ERROR;
import static org.junit.jupiter.api.Assertions.*;

import com.montealegreluis.apiproblem.ApiProblem;
import com.montealegreluis.apiproblem.Status;
import java.net.URI;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

final class ThrowableAdviceTest {
  @Test
  void it_generates_a_problem_response_from_a_Throwable() {
    var advice = new ThrowableAdvice() {};

    var response = advice.handleThrowable(new RuntimeException("Something wen wrong"));

    var apiProblem = response.getBody();
    assertNotNull(apiProblem);
    assertEquals(Status.INTERNAL_SERVER_ERROR.code(), apiProblem.getStatus());
    assertEquals(Status.INTERNAL_SERVER_ERROR.reason(), apiProblem.getTitle());
    assertEquals(Status.INTERNAL_SERVER_ERROR.type(), apiProblem.getType());
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

    var response = advice.handleThrowable(new RuntimeException("Something wen wrong"));

    var apiProblem = response.getBody();
    assertNotNull(apiProblem);
    assertEquals(Status.INTERNAL_SERVER_ERROR.code(), apiProblem.getStatus());
    assertEquals(Status.INTERNAL_SERVER_ERROR.reason(), apiProblem.getTitle());
    assertEquals(Status.INTERNAL_SERVER_ERROR.type(), apiProblem.getType());
    assertEquals(1, apiProblem.getAdditionalProperties().size());
    assertTrue(apiProblem.getAdditionalProperties().containsKey("exception"));
  }

  @Test
  void it_generates_a_custom_problem_response_from_a_Throwable() {
    var advice =
        new ThrowableAdvice() {
          @Override
          public ResponseEntity<ApiProblem> handleThrowable(Throwable exception) {
            var problem = ApiProblem.witDefaultType(INTERNAL_SERVER_ERROR);

            return new ResponseEntity<>(problem, HttpStatus.INTERNAL_SERVER_ERROR);
          }
        };

    var response = advice.handleThrowable(new RuntimeException("Something wen wrong"));

    var apiProblem = response.getBody();
    assertNotNull(apiProblem);
    assertEquals(Status.INTERNAL_SERVER_ERROR.code(), apiProblem.getStatus());
    assertEquals(Status.INTERNAL_SERVER_ERROR.reason(), apiProblem.getTitle());
    assertEquals(URI.create("about:blank"), apiProblem.getType());
    assertEquals(0, apiProblem.getAdditionalProperties().size());
  }
}
