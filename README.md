# erp-desktop-javafx-mysql-poc
Java 21 desktop ERP PoC: JavaFX UI, JDBC/MySQL CRUD, CSV/Excel export, basic tests, Gradle build.

Getting started (Windows)
-------------------------

Prerequisites:
- JDK 21 installed and on PATH (OpenJDK or similar)
- Docker Desktop (optional, for MySQL container)

Run the app locally (uses Gradle/OpenJFX plugin to supply JavaFX):

```powershell
# build and compile
.\n+./gradlew clean build

# run the JavaFX desktop app (launches UI on your machine)
./gradlew runApp
```

Use Docker Compose to bring up MySQL (optional)
------------------------------------------------

The repository contains `docker-compose.yml` and SQL scripts under `scripts/` to create the `customer` table and seed sample rows.

```powershell
# start MySQL container
docker compose up -d

# check container logs
docker compose logs -f db

# stop it when done
docker compose down
```

Tests
-----
- Unit tests are JUnit 5 based. Some DAO tests require a running MySQL instance (see Docker instructions). Consider using Testcontainers for fully isolated test runs.

Notes
-----
- The project uses the OpenJFX Gradle plugin to manage JavaFX runtime and module-path configuration.
- If you prefer a non-modular build (no `module-info.java`), remove the file and the OpenJFX plugin will still provide runtime libs, but I chose a modular setup compatible with Java 21.

