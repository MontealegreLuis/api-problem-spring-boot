# Contributing to API Problem Spring Boot

## Code of Conduct

This project is released with a [Contributor Code of Conduct](https://github.com/MontealegreLuis/api-problem-spring-boot/blob/main/CODE-OF-CONDUCT.md).
By participating in this project you agree to abide by its terms.

## Workflow

* Fork the project.
* Make your bug fix or feature addition.
* Add tests for it. This is important, so we don't break it in a future version unintentionally.
* Send a pull request. Bonus points for topic branches.

Pull requests for bug fixes must be based on the current stable branch whereas pull requests for new features must be based on the `main` branch.

### Git commit format

Commit messages must follow the [Conventional Commits Specification](https://www.conventionalcommits.org/en/v1.0.0-beta.2/) which is enforced as a Git pre-commit hook.

The allowed commit types are:

- `ci` for changes to GitHub Actions
- `chore` for changes to the Gradle configuration
- `docs` for updates or additions to the documentation
- `feat` for new features
- `fix` for a fix to an existing feature
- `refactor` for refactoring changes
- `style` for changes that only format code
- `test` for tests only changes

It is recommended to follow the [7 rules of a good commit message](https://chris.beams.io/posts/git-commit/#seven-rules).

## Coding Guidelines

This project follows the coding standards proposed in [Google Java Style Guide](https://google.github.io/styleguide/javaguide.html).

Run the following command if you modified either production code or tests.

```bash
make format
```

[Make](https://en.wikipedia.org/wiki/Make_(software)) will run the [Google Java Format Gradle plugin](https://github.com/sherter/google-java-format-gradle-plugin) in your `src` directory.

## Using API Problem Spring Boot from a Git checkout

The following commands can be used to perform the initial checkout of API Problem:

```bash
git clone git://github.com/MontealegreLuis/api-problem-spring-boot.git
cd api-problem-spring-boot
```

Retrieve API Problem dependencies using [Gradle](https://docs.gradle.org/current/userguide/userguide.html):

```bash
./gradlew assemble
```

## Running API Problem Code Quality Checks

You can run the tests, check for coding style issues and run the [mutation test suite](https://pitest.org/) as follows:

```bash
make check
```

`make` will run the same Gradle tasks ran by GitHub Actions.

## Reporting issues

Before opening a new ticket, please search through the [existing issues](https://github.com/MontealegreLuis/api-problem-spring-boot/issues).
