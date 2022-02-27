# API Problem Spring Boot

[![CI workflow](https://github.com/montealegreluis/api-problem-spring-boot/actions/workflows/ci.yml/badge.svg)](https://github.com/montealegreluis/api-problem-spring-boot/actions/workflows/ci.yml)
[![Release workflow](https://github.com/montealegreluis/api-problem-spring-boot/actions/workflows/release.yml/badge.svg)](https://github.com/montealegreluis/api-problem-spring-boot/actions/workflows/release.yml)
[![semantic-release: conventional-commits](https://img.shields.io/badge/semantic--release-conventionalcommits-e10079?logo=semantic-release)](https://github.com/semantic-release/semantic-release)

*API Problem Spring Boot* is a package that makes it easy to produce
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

By default, exceptions stack traces are disabled, but you can override this behavior

```java
@ControllerAdvice
class ProblemApiExceptionHandler implements ApiProblemHandler {
  @Override
  public boolean includeStackTrace() {
    return true;
  }
}
```

You can also override how the API problem is built

```java
@ControllerAdvice
class ProblemApiExceptionHandler implements ApiProblemHandler {
  @Override
  public ResponseEntity<ApiProblem> handleThrowable(Throwable exception) {
    return ApiProblem.witDefaultType(Status.INTERNAL_SERVER_ERROR);  
  }
}
```

### Logging

Exceptions are logged using an [ActivityFeed](https://github.com/MontealegreLuis/activity-feed).
There are a few extension points that you can use to customize the way exceptions are logged.

An `ActivityFeed` is created for you by the default, but you can create your own

```java
@ControllerAdvice
class ProblemApiExceptionHandler implements ApiProblemHandler {
  @Override
  public ActivityFeed feed() {
      return new ActivityFeed(LoggerFactory.getLogger(YourApplication.class));
  }
}
```

You can also customize the `Activity` being logged

```java
@ControllerAdvice
class ProblemApiExceptionHandler implements ApiProblemHandler {
  @Override
  void logThrowable(Throwable exception) {
    final Activity activity =
        error(
            "internal-server-error",
            "An error occurred: " + exception.getMessage(),
            (context) -> context.put("exception", contextFrom(exception)));

    log(activity);
  }
}
```

## Contribute

Please refer to [CONTRIBUTING](https://github.com/MontealegreLuis/api-problem-spring-boot/blob/main/CONTRIBUTING.md) for information on how to contribute to API Problem Spring Boot.

## License

Released under the [BSD-3-Clause](https://github.com/MontealegreLuis/api-problem-spring-boot/blob/main/LICENSE).
