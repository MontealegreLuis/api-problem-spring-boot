# Method Argument Not Valid Advice

You can override how the API problem is built

```java
public ApiProblemBuilder builderForMethodArgumentNotValid(
  final MethodArgumentNotValidException exception) {

  return aProblem()
    .witDefaultType(UNPROCESSABLE_ENTITY)
    .build();
} 
```

You can include additional information from the current request to your API Problem instance.

```java
public void enhanceMethodArgumentNotValidProblem(
    final ApiProblemBuilder builder,
    final MethodArgumentNotValidException exception,
    final NativeWebRequest nativeRequest) {

    // Add more information to your Problem instance
    // builder.with("value", ...);
}
```

You can also completely override how `MethodArgumentNotValidException` objects are handled.
The example belows removes the exception information and the logging logic and only returns the response

```java
@ControllerAdvice
class ProblemApiExceptionHandler implements ApiProblemHandler {
  @Override
  public ResponseEntity<ApiProblem> handleMethodArgumentNotValid(
      final MethodArgumentNotValidException exception, 
      final NativeWebRequest request) {

    return new ResponseEntity(
        ApiProblem.witDefaultType(Status.UNPROCESSABLE_ENTITY),
        HttpStatus.UNPROCESSABLE_ENTITY);  
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
  public ActivityBuilder builderForMethodArgumentNotValidActivity(
    final MethodArgumentNotValidException exception) {
      return anActivity()
          .warning()
          .withIdentifier("invalid-input")
          .withMessage("An error occurred: " + exception.getMessage());
  }

  @Override
  public void enhanceMethodArgumentNotValidActivity(
    final ActivityBuilder builder, 
    final ApiProblem problem, 
    final NativeWebRequest request) {
    
    builder.with("code", problem.getAdditionalProperties().get("code"));
    // you could also include information from the request
  }
}
```
