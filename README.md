# API Problem Spring Boot

[![CI workflow](https://github.com/montealegreluis/api-problem-spring-boot/actions/workflows/ci.yml/badge.svg)](https://github.com/montealegreluis/api-problem-spring-boot/actions/workflows/ci.yml)
[![Release workflow](https://github.com/montealegreluis/api-problem-spring-boot/actions/workflows/release.yml/badge.svg)](https://github.com/montealegreluis/api-problem-spring-boot/actions/workflows/release.yml)
[![semantic-release: conventional-commits](https://img.shields.io/badge/semantic--release-conventionalcommits-e10079?logo=semantic-release)](https://github.com/semantic-release/semantic-release)

*API Problem Spring Boot* makes it easy to produce
[`application/problem+json`](http://tools.ietf.org/html/rfc7807) responses in a Spring Boot
application.
It connects the [API Problem](https://github.com/MontealegreLuis/api-problem-spring-boot) package and
[Spring Boot Web MVC's exception handling](https://spring.io/blog/2013/11/01/exception-handling-in-spring-mvc#using-controlleradvice-classes)

## Installation

1. [Authenticating to GitHub Packages](https://github.com/MontealegreLuis/api-problem-spring-boot/blob/main/docs/installation/authentication.md)
2. [Maven](https://github.com/MontealegreLuis/api-problem-spring-boot/blob/main/docs/installation/maven.md)
3. [Gradle](https://github.com/MontealegreLuis/api-problem-spring-boot/blob/main/docs/installation/gradle.md)

## Usage

### Jackson Module for API Problems

Make sure you register the `ApiProblemModule` with your `ObjectMapper`:

```java
ObjectMapper mapper = new ObjectMapper()
  .registerModule(new ApiProblemModule());
```

### Controller Advice

A typical usage would look like this:

```java
@ControllerAdvice
class ProblemApiExceptionHandler implements ApiProblemHandler {
}
```

All default behaviour can be customized or overriden as explained in the entries below. 

- Validation Advice
  - [Constraint Violation](docs/constraint-violation-advice.md)
  - [Method Argument Not Valid](docs/method-argument-not-valid-advice.md)
- [Throwable Advice](docs/throwable-advice.md)

## Contribute

Please refer to [CONTRIBUTING](https://github.com/MontealegreLuis/api-problem-spring-boot/blob/main/CONTRIBUTING.md) for information on how to contribute to API Problem Spring Boot.

## License

Released under the [BSD-3-Clause](https://github.com/MontealegreLuis/api-problem-spring-boot/blob/main/LICENSE).
