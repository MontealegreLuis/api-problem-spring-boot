# Throwable Advice

Exceptions stack traces can be included in the API Problem body.
Stack traces aren't included by default, but you can override this behavior.

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
public ApiProblemBuilder builderForThrowable(Throwable exception) {
  return aProblem().from(INTERNAL_SERVER_ERROR);
}
```

You can include additional information from the current request to your API Problem instance.

```java
public void enhanceThrowableProblem(
    ApiProblemBuilder builder, 
    NativeWebRequest nativeRequest) {

    // Add more information to your Problem instance
    // builder.with("value", ...);
}
```


You can also completely override how `Throwable` objects are handled.
The example belows removes the exception information and the logging logic and only returns the response

```java
@ControllerAdvice
class ProblemApiExceptionHandler implements ApiProblemHandler {
  @Override
  public ResponseEntity<ApiProblem> handleThrowable(
      Throwable exception, NativeWebRequest request) {

    return new ResponseEntity(
        ApiProblem.witDefaultType(Status.INTERNAL_SERVER_ERROR),
        HttpStatus.INTERNAL_SERVER_ERROR);  
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
      final var logger = LoggerFactory.getLogger(YourApplication.class);
      return new ActivityFeed(logger);
  }
}
```

You can also customize the `Activity` being logged

```java
@ControllerAdvice
class ProblemApiExceptionHandler implements ApiProblemHandler {
  @Override
  public ActivityBuilder builderForThrowableActivity(final Throwable exception) {
    return anActivity()
      .withIdentifier("internal-server-error")
      .withMessage("An error occurred: " + exception.getMessage())  
      .withException(exception);
  }

  @Override
  public void enhanceThrowableProblemActivity(
    ActivityBuilder builder, 
    ApiProblem problem, 
    NativeWebRequest request) {
    builder.with("code", problem.getAdditionalProperties().get("code"));
    // You could also add information from the current request
  }
}
```
