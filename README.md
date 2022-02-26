# API Problem Spring Boot

*API Problem Spring Boot* is a set of libraries that makes it easy to produce
[`application/problem+json`](http://tools.ietf.org/html/rfc7807) responses in a Spring Boot
application.
It connects the [API Problem](https://github.com/MontealegreLuis/api-problem) and
[Spring Boot Web MVC's exception handling](https://spring.io/blog/2013/11/01/exception-handling-in-spring-mvc#using-controlleradvice-classes)

```java
@ControllerAdvice
class ExceptionHandling implements ProblemHandling {
}
```
