package com.montealegreluis.apiproblemspringboot.springboot;

import com.montealegreluis.apiproblemspringboot.springboot.validation.ValidationAdvice;

public interface ApiProblemHandler extends ThrowableAdvice, ValidationAdvice {}
