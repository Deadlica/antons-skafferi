# Antons Skafferi

Website and admin interface for the restaurant "Antons Skafferi".

Built with Jakarta Faces (JSF) on Payara Server 6. The frontend renders pages server-side and calls the separate REST backend ([`antons-skafferi-db`](https://github.com/Deadlica/antons-skafferi-db)) over HTTP. It also hosts narrow, GET-only proxies (`/image`, `/api-proxy/*`) so the browser can load event images and sales data without the backend being publicly reachable.

## What it does

The public site shows the week's lunch menu, à la carte menu, upcoming events, and customer reviews, and lets visitors book a table through a calendar that marks past and fully-booked days.

The admin panel covers employee management (add, update, retire), weekly shift scheduling, dish and menu editing, event creation with image upload, review moderation, and a monthly sales graph.

API reference and sample payloads are served at `/api-docs/` when the app is running — see [`src/main/webapp/api-docs/`](src/main/webapp/api-docs/) in the repo.

---

# Running

The easiest path is Docker. For IDE-based development, see "Development" below.

## Option A: Pull the prebuilt image

```bash
docker run -d --name antons-skafferi \
  -p 8081:8080 \
  -e BACKEND_HOST=<backend-host> \
  deadlica/antons-skafferi:latest
```

Verify:
```bash
curl -I http://localhost:8081/
```

`BACKEND_HOST` is the backend's IP address, or its container name if both containers share a Docker network.

## Option B: Build from source

```bash
git clone https://github.com/Deadlica/antons-skafferi.git
cd antons-skafferi
docker build -t antons-skafferi .
docker run -d --name antons-skafferi -p 8081:8080 \
  -e BACKEND_HOST=<backend-host> \
  antons-skafferi
```

## Configuration

Environment variables:

| Variable | Default | Purpose |
|---|---|---|
| `BACKEND_HOST` | `localhost` | IP address or container name of the backend |

---

# Development

## Requirements
- JDK 17
- Maven 3.9+
- Payara Server Full 6
- A running backend ([`antons-skafferi-db`](https://github.com/Deadlica/antons-skafferi-db)) on a reachable host

## Build the WAR

```bash
./mvnw -DskipTests package
# produces target/antons-skafferi-1.0-SNAPSHOT.war
```

## Run with Docker (rebuild after code changes)

```bash
docker build -t antons-skafferi .
docker stop antons-skafferi 2>/dev/null; docker rm antons-skafferi 2>/dev/null
docker run -d --name antons-skafferi -p 8081:8080 \
  -e BACKEND_HOST=<backend-host> \
  antons-skafferi
```

## Project layout
- `src/main/webapp/` — XHTML views, CSS, JS (site at `/`, admin at `/admin/`, docs at `/api-docs/`)
- `src/main/java/com/antonsskafferi/app/` — JSF managed beans, one per domain area
- `src/main/java/com/antonsskafferi/app/adminschedule/` — schedule model and HTTP adapter for the backend
- `ImageProxyServlet`, `ApiProxyServlet` — same-origin GET proxies for browser → backend
- `WEB-INF/glassfish-web.xml` — deploys the WAR at the root context (`/`)

## Prebuilt images
Published on Docker Hub as `deadlica/antons-skafferi:latest` and `:v2`.
