---
name: create-local-container
description: >-
  Build and run this Spring Boot app as a local Docker container using the
  Docker CLI. Use when the user asks to containerize, dockerize, or run the
  app in Docker locally, or invokes the create-local-container skill.
---

# Create Local Container

Containerize this application with the Docker CLI. The user supplies the **container name** to use locally.

## Required input

- `CONTAINER_NAME` — Docker container name (e.g. `digitalclock-dev`). Ask if not provided.

## Workflow

Run from the project root (`springboot-digitalclock-webapp`).

### 1. Preflight

```bash
docker version
```

If Docker is unavailable, stop and report the error.

### 2. Build the JAR

The `Dockerfile` copies `target/digitalclock-1.0.0.jar`. Build it first:

```bash
mvn clean package -DskipTests
```

### 3. Build the image

Use a fixed local image tag for this project:

```bash
docker build -t digitalclock:local .
```

### 4. Replace an existing container (same name)

If a container named `CONTAINER_NAME` already exists, remove it before creating a new one:

```bash
docker rm -f CONTAINER_NAME
```

Only run when `docker ps -a --format '{{.Names}}' | grep -x CONTAINER_NAME` matches.

### 5. Run the container

Map host port `8080` to the app (`server.port=8080`):

```bash
docker run -d --name CONTAINER_NAME -p 8080:8080 digitalclock:local
```

Replace `CONTAINER_NAME` with the user-provided name.

### 6. Verify

```bash
docker ps --filter name=CONTAINER_NAME
curl -s -o /dev/null -w "%{http_code}" http://localhost:8080/
```

Report the container name, image (`digitalclock:local`), and URL `http://localhost:8080/`.

## Useful follow-up commands

```bash
docker logs -f CONTAINER_NAME
docker stop CONTAINER_NAME
docker rm CONTAINER_NAME
```

## Project specifics

| Item | Value |
|------|-------|
| JAR | `target/digitalclock-1.0.0.jar` |
| Base image | `eclipse-temurin:17-jre-alpine` |
| Exposed port | `8080` |
| Image tag | `digitalclock:local` |

Do not change the `Dockerfile` unless the user asks. Use `docker` CLI commands only (not docker-compose) unless the user requests otherwise.
