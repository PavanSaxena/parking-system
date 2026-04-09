# Project Guidelines

## Project Snapshot (Current State)
- Tech stack: Spring Boot 4.0.2, Java 21, Maven Wrapper, Spring Data JPA, PostgreSQL, Lombok.
- App type: layered REST API for parking management.
- Documentation currently present: `HELP.md` (Spring-generated reference links) and `endpoints.json` (Postman collection).
- Build health right now: project does **not** compile (`./mvnw test` fails during `compile` with multiple symbol errors).

## Architecture
- Root package: `com.sps.parkingsystem`.
- Main layers:
  - `controller/`: REST endpoints and request handling.
  - `service/`: business logic and orchestration.
  - `repository/`: Spring Data JPA repositories.
  - `model/`: JPA entities.
  - `dto/`: request payload objects.
  - `enums/`: status/type enums.
- Main runtime config is in `src/main/resources/application.properties`.

## Build and Test
Use Maven Wrapper from repository root:
- Build/package: `./mvnw clean package`
- Test: `./mvnw test`
- Run app: `./mvnw spring-boot:run`

Current behavior (as of 2026-04-08):
- `./mvnw test` fails at compile stage before tests run.
- Frequent failures include missing getters/setters and missing repository symbols.

## Environment and Configuration
- Required DB environment variables (resolved from `.env` via `spring.config.import`):
  - `DB_URL`
  - `DB_USER`
  - `DB_PASSWORD`
- Database target: PostgreSQL (`org.postgresql.Driver`).
- JPA mode: `spring.jpa.hibernate.ddl-auto=update` (schema auto-update).

## Conventions
- Use constructor injection for services/controllers.
- Keep endpoint logic in controllers minimal; move behavior to services.
- Continue Spring Data naming convention for repository methods (`findBy...`, `findFirstBy...`).
- Keep validation at DTO/controller boundary (`@Valid`, Jakarta validation annotations).

## Known Pitfalls (Do Not Ignore)
- `ParkingOperatorRepository` is referenced in `ParkingService` but corresponding repository file is missing.
- `ParkingService` references `rateRepository` without a declared/injected field.
- `ParkingService` calls `ticket.setStatus(...)`, but `ParkingTicket` does not define a `status` field.
- Multiple compile errors indicate Lombok-generated accessors are not being resolved in this environment/build.
  - When editing model/DTO classes that rely on Lombok, verify Lombok setup before assuming generated methods exist.

## Agent Working Guidance
- Before implementing feature changes, run `./mvnw test` once to confirm baseline failure/success state.
- If task is unrelated to build repair, avoid broad refactors across all layers.
- Prefer small, layer-local changes with explicit impact (controller -> service -> repository).
- Treat `target/` as generated output; do not edit files inside it.

## Key References
- `pom.xml`: dependency/runtime versions and build plugins.
- `src/main/resources/application.properties`: runtime environment and JPA settings.
- `endpoints.json`: API request examples.
- `HELP.md`: generated framework reference links.
