package com.montealegreluis.apiproblemspringboot.jackson;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_EMPTY;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.montealegreluis.apiproblem.Problem;
import java.net.URI;
import java.util.Map;

@JsonInclude(NON_EMPTY)
interface ApiProblemMixIn extends Problem {
  @JsonProperty("type")
  @JsonSerialize(converter = ProblemTypeConverter.class)
  @Override
  URI getType();

  @JsonProperty("title")
  @Override
  String getTitle();

  @JsonProperty("status")
  @Override
  Integer getStatus();

  @JsonProperty("details")
  @Override
  String getDetails();

  @JsonProperty("instance")
  @Override
  URI getInstance();

  @JsonAnyGetter
  @Override
  Map<String, Object> getAdditionalProperties();
}
