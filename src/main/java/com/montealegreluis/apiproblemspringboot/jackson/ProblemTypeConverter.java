package com.montealegreluis.apiproblemspringboot.jackson;

import com.fasterxml.jackson.databind.util.StdConverter;
import com.montealegreluis.apiproblem.ApiProblem;
import java.net.URI;

public class ProblemTypeConverter extends StdConverter<URI, URI> {
  @Override
  public URI convert(URI value) {
    return ApiProblem.DEFAULT_TYPE.equals(value) ? null : value;
  }
}
