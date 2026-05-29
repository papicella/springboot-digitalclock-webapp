---
name: create-local-container
description: >-
  Build and create a local Docker container for this Spring Boot app using the
  Docker CLI (does not start the container). The user supplies a name used for
  both the image tag and container name. Use when the user asks to
  containerize, dockerize, or create a local Docker container, or invokes the
  create-local-container skill.
---

# Create Local Container

Containerize this application with the Docker CLI. The user supplies a **name** that is used for both the built image tag and the container name — they must match.

## Required input

- `NAME` — Docker image tag and container name (e.g. `digitalclock-dev`). Ask if not provided.

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

Tag the image with the user-supplied `NAME` (defaults to `:latest`). Always pass `--platform linux/amd64` — the `eclipse-temurin:17-jre-alpine` base image has no ARM64 manifest, so this is required on Apple Silicon:

```bash
docker build --platform linux/amd64 -t NAME .
```

Replace `NAME` with the user-provided name (e.g. `-t digitalclock-dev`).

### 4. Remove any existing container (best effort)

Attempt to remove a container with the same name. Do not check first — just run the command and continue regardless of outcome (no error if it does not exist):

```bash
docker rm -f NAME || true
```

### 5. Create the container (do not start)

Map host port `8080` to the app (`server.port=8080`). Use the same `NAME` for both the container name and the image reference. Use `docker create` — **do not** use `docker run`:

```bash
docker create --name NAME -p 8080:8080 NAME
```

Replace both `NAME` placeholders with the user-provided name.

### 6. Verify

```bash
docker ps -a --filter name=NAME
```

Report the container name, image (same `NAME`), and that it was created but **not started**. To start it later: `docker start NAME` → `http://localhost:8080/`.

## Useful follow-up commands

```bash
docker start NAME
docker logs -f NAME
docker stop NAME
docker rm NAME
```

## Project specifics

| Item | Value |
|------|-------|
| JAR | `target/digitalclock-1.0.0.jar` |
| Base image | `eclipse-temurin:17-jre-alpine` |
| Build platform | `linux/amd64` (required on ARM64 hosts) |
| Exposed port | `8080` |
| Image tag | User-supplied `NAME` (same as container name) |

Do not change the `Dockerfile` unless the user asks. Use `docker` CLI commands only (not docker-compose) unless the user requests otherwise.
