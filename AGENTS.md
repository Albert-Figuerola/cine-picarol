# Repository Guidelines

## Project Structure & Module Organization

Application code lives in `src/main/kotlin/com/albert/cinepicarol`. Features are grouped under `movie/` and `user/`, with controllers, use cases, domain models, persistence adapters, ports, mappers, and request/response DTOs kept in focused packages. Shared API responses and exception handling belong in `common/`; framework configuration belongs in `config/`.

Runtime configuration is under `src/main/resources`. Liquibase uses `db/changelog/db.changelog-master.xml` as its entry point and numbered changesets in `db/changelog/changes/`. Tests mirror feature packages under `src/test/kotlin`.

## Build, Test, and Development Commands

Use the checked-in Gradle wrapper and Java 21:

- `./gradlew bootRun` starts the API locally.
- `./gradlew test` runs the JUnit 5 test suite.
- `./gradlew build` compiles, tests, and creates the executable artifact.
- `./gradlew clean build` performs a clean verification build.

Local startup expects MySQL at `localhost:3306/cinepicarol`; Liquibase applies migrations automatically. Repository integration tests use Testcontainers, so Docker must be available.

## Coding Style & Naming Conventions

Follow standard Kotlin formatting with four-space indentation, trailing commas in multiline declarations, and explicit package organization. Use `PascalCase` for types, `camelCase` for functions and properties, and descriptive suffixes such as `Controller`, `UseCase`, `Port`, `Adapter`, `Entity`, `Request`, and `Response`. Keep domain objects independent of JPA entities and translate between layers through mapper functions. Prefer constructor injection. No formatter or linter is currently configured; match surrounding code and keep imports tidy.

## Testing Guidelines

Tests use JUnit 5, Spring Boot Test, MockMvc, Mockito Kotlin, and Testcontainers. Name files `<Subject>Test.kt` and use readable backtick test names such as ``should return 404 when movie does not exist``. Add unit tests for use-case behavior, `@WebMvcTest` coverage for HTTP validation and status codes, and repository tests for persistence changes. Run `./gradlew test` before submitting.

## Commit & Pull Request Guidelines

Recent commits use short, imperative summaries, sometimes prefixed by a category such as `Refactor:`. Keep each commit focused; examples include `Refactor: decouple use cases from JPA repository` and `Add validation tests for MovieController`.

Pull requests should explain the behavior change, note database or configuration impacts, link the relevant issue, and include test evidence. Add sample requests/responses for API changes and screenshots only when a visual artifact is affected.

## Security & Configuration

Do not commit real credentials. Override the development datasource settings with environment-specific configuration, and review Spring Security behavior whenever adding or changing endpoints.
