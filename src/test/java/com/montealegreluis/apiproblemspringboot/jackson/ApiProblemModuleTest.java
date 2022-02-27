package com.montealegreluis.apiproblemspringboot.jackson;

import static org.junit.jupiter.api.Assertions.*;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.montealegreluis.apiproblem.ApiProblem;
import com.montealegreluis.apiproblem.ApiProblemBuilder;
import com.montealegreluis.apiproblem.Status;
import java.net.URI;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

final class ApiProblemModuleTest {
  @Test
  void it_serializes_an_API_problem_with_a_type() throws JsonProcessingException {
    var problem = ApiProblem.from(Status.NOT_FOUND);

    assertEquals(
        "{\"title\":\"Not Found\",\"status\":404,\"type\":\"https://www.w3.org/Protocols/rfc2616/rfc2616-sec10.html#sec10.4.5\"}",
        mapper.writeValueAsString(problem));
  }

  @Test
  void it_serializes_an_API_problem_without_a_type() throws JsonProcessingException {
    var problem = ApiProblem.witDefaultType(Status.NOT_FOUND);

    assertEquals("{\"title\":\"Not Found\",\"status\":404}", mapper.writeValueAsString(problem));
  }

  @Test
  void it_serializes_an_API_problem_with_all_reserved_properties() throws JsonProcessingException {
    ApiProblem problem =
        ApiProblemBuilder.aProblem()
            .withTitle("Out of Stock")
            .withStatus(Status.BAD_REQUEST.code())
            .withType(Status.BAD_REQUEST.type())
            .withInstance(URI.create("https://example.org/out-of-stock"))
            .withDetail("Item B00027Y5QG is no longer available")
            .build();

    assertEquals(
        "{\"title\":\"Out of Stock\",\"status\":400,\"type\":\"https://www.w3.org/Protocols/rfc2616/rfc2616-sec10.html#sec10.4.1\",\"detail\":\"Item B00027Y5QG is no longer available\",\"instance\":\"https://example.org/out-of-stock\"}",
        mapper.writeValueAsString(problem));
  }

  @Test
  void it_serializes_a_problem_with_additional_properties() throws JsonProcessingException {
    ApiProblem problem =
        ApiProblemBuilder.aProblem()
            .withTitle("Out of Stock")
            .withType(Status.BAD_REQUEST.type())
            .withStatus(Status.BAD_REQUEST.code())
            .withType(Status.BAD_REQUEST.type())
            .withDetail("Item B00027Y5QG is no longer available")
            .withInstance(URI.create("https://example.org/out-of-stock"))
            .with("product", "B00027Y5QG")
            .build();

    assertEquals(
        "{\"title\":\"Out of Stock\",\"status\":400,\"type\":\"https://www.w3.org/Protocols/rfc2616/rfc2616-sec10.html#sec10.4.1\",\"detail\":\"Item B00027Y5QG is no longer available\",\"instance\":\"https://example.org/out-of-stock\",\"product\":\"B00027Y5QG\"}",
        mapper.writeValueAsString(problem));
  }

  @Test
  void it_knows_its_name() {
    var module = new ApiProblemModule();

    assertEquals("ApiProblemModule", module.getModuleName());
  }

  @BeforeEach
  void let() {
    mapper = new ObjectMapper().registerModule(new ApiProblemModule());
  }

  private ObjectMapper mapper;
}
