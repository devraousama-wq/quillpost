# Quillpost

Self-hosted blogging and lightweight CMS built with Java 21 and Spring Boot 3.

Quillpost gives writers and small teams a modern, owned alternative to hosted blogging services. Authors draft in markdown, editors review and schedule posts, and readers get a fast public site with feeds and SEO metadata.

## Stack

- Java 21, Spring Boot 3.3
- PostgreSQL, Flyway, Spring Data JPA
- Spring Security with OAuth2 and JWT
- Thymeleaf public reader site and admin console
- Flexmark markdown, Caffeine cache

## Local setup

```bash
docker compose up -d postgres
cp .env.example .env
./mvnw spring-boot:run
```

Health check: `GET http://localhost:8080/actuator/health`

## Architecture

Layered packages under `com.quillpost`: web controllers split into `api`, `admin`, and `reader`; domain logic in feature modules (`content`, `editorial`, `media`, etc.); shared config and persistence helpers in `shared`.

## Demo seed

```bash
SPRING_PROFILES_ACTIVE=seed ./mvnw spring-boot:run
```

Creates a `demo` workspace with a published welcome post when the seed profile is active.

## OAuth2

Set `OAUTH2_GOOGLE_CLIENT_ID`, `OAUTH2_GITHUB_CLIENT_ID`, and matching secrets in `.env`.

## Editorial workflow

Draft → in review → scheduled/published → archived. Transitions are enforced server-side; drafts cannot publish directly.

## License

MIT
