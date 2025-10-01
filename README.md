[![Java CI with Gradle](https://github.com/artemalexpetrov/flickcrit4sky/actions/workflows/workflow.yml/badge.svg)](https://github.com/artemalexpetrov/flickcrit4sky/actions/workflows/workflow.yml/badge.svg)![JaCoCo Coverage](.github/badges/jacoco.svg)

Flick Critique
==============

Flick Critique is a simplified movie rating application built with Java and Spring Boot. The application provides a platform
where users rate movies. The solution aims to demonstrate proficiency through a practical project that covers a 
spectrum of Java-related topics, including OOP, Design Patterns, Testing, Database Integration, API Design,
Dockerization and Observability.

---

## Table of Contents

<!-- TOC -->
* [Pre-Requisites](#pre-requisites)
* [How to Run](#how-to-run)
* [How to Build & Test](#how-to-build--test)
* [Data Ingestion](#data-ingestion)
* [REST API Endpoints](#rest-api-endpoints)
* [Useful links](#useful-links)
* [Known Issues](#known-issues)
* [Further Development](#further-development)
<!-- TOC -->

# Pre-Requisites

* Java 21
* Docker
* Docker Compose v2.20+

---

# How to Run

1) Prepare the environment configuration using the `.env.dist` file
   ```bash
   cp .env.dist .env
   ```
   The file contains fake "secrets" **just for the demo purposes**.

2) Spin up the docker containers
   ```bash
   docker-compose up --build
   ```
3) Access the API, e.g. `http://127.0.0.1:8080/api/v1/movies` \
   For details check [Swagger](http://127.0.0.1:8080/docs/swagger-ui/index.html), [Postman](./docs/flickcrit.postman_collection.json) or [OpenAPI JSON](./docs/flickcrit.open-api.json) \
   **IMPORTANT**: The link to the Swagger UI intentionally points to the local instance! 

### Troubleshooting
* Ensure there are no port conflicts. By default, the application uses default ports for services. Adjust the `.env` file if needed
* Also there are [known issues](#known-issues) that make some services unavailable

---

# How to Build & Test

Build and test the application without the provided Docker stack using just Gradle

   ```bash
   ./gradlew build
   ```

To run the application manually (e.g. with `java -jar ...`) you have to provide mandatory configuration parameters, such
as PostgreSQL and Redis connection details using properties file or environment variables. 
```properties
spring.datasource.url=jdbc:postgresql://database-host:5432/dbname
spring.datasource.username=<username>
spring.datasource.password=<password>
spring.data.redis.host=redis-host
spring.data.redis.port=6379
spring.data.redis.username=<username> # if required
spring.data.redis.password=<password> # if required
jwt.secret-key=<your secret key>      # min 512 bytes
```

---

# Data Ingestion

The application automatically imports initial data when the `data-boostrap` profile is enabled (by default, it is
included in
the Docker stack).
The imported dataset includes 16 users, 152 movies, and random ratings from each user to each movie.

Here are credentials for the default users:

| Email               | Password      | Role  |
|---------------------|---------------|-------|
| admin@flickcrit.com | administrat0r | ADMIN |
| user1@flickcrit.com | user1         | USER  |
| user2@flickcrit.com | user2         | USER  |

---

# REST API Endpoints

There are some endpoint to start using the application right away.
For complete list of available action and usage examples kindly
check [Swagger](http://127.0.0.1:8080/docs/swagger-ui/index.html) or use the
prepared [Postman](./docs/flickcrit.postman_collection.json) collection.


| Method     | Path                       | Description                                              | Require Auth / Role |
|------------|----------------------------|----------------------------------------------------------|---------------------|
| **Auth**   |                            |                                                          |                     |
| POST       | /api/v1/auth/sign-in       | Exchange user's credentials for a JWT token              | No                  |
| POST       | /api/v1/auth/sign-up       | Register a new user                                      | No                  |
| POST       | /api/v1/auth/refresh-token | Exchange a refresh token for a new JWT token pair        | No                  |
| **Movies** |                            |                                                          |                     |
| GET        | /api/v1/movies             | Get a paginated list of all movies                       | No                  |
| GET        | /api/v1/movies/{id}        | Get movie by ID                                          | No                  |
| GET        | /api/v1/movies/top         | Get top rated movies                                     | No                  |
| POST       | /api/v1/movies             | Create a new movie                                       | Yes / Admin         |
| PUT        | /api/v1/movies/{id}        | Update an existing movie                                 | Yes / Admin         |
| DELETE     | /api/v1/movies/{id}        | Delete a movie                                           | Yes / Admin         |
| **Rating** |                            |                                                          |                     |
| GET        | /api/v1/movies/{id}/rating | Get an average rating of a movie by ID                   | No                  |
| POST       | /api/v1/movies/{id}/rating | Get an average rating of a movie by ID                   | Yes                 |
| DELETE     | /api/v1/movies/{id}/rating | Remove user's previously given rating from a movie by ID | Yes                 |
| **Users**  |                            |                                                          |                     |
| GET        | /api/v1/users              | Get a paginated list of users                            | Yes / Admin         |
| GET        | /api/v1/users/{id}         | Get a user by ID                                         | Yes / Admin         |
| DELETE     | /api/v1/users/{id}         | Delete a user by ID                                      | Yes / Admin         |

---

# Useful links

| Service    | Links                                      | Default Credentials (.env.dist)                 | Notes & Tips                                                               |
|------------|--------------------------------------------|-------------------------------------------------|----------------------------------------------------------------------------|
| Postgres   | jdbc:postgresql://localhost:5432/flickcrit | user/passw0rd                                   | In case you want to observe the data                                       |
| Prometheus | http://localhost:9090                      | admin/passw0rd                                  | Example queries:<br/>CPU Usage: system_cpu_usage * 100                     |
| Grafana    | http://localhost:3000                      | admin/admin<br/>(require to change after login) | The provisioned dashboard: JVM SpringBoot3                                 |
| Kibana     | http://localhost:5601                      | elastic/elastic                                 | Indexes: `app-logs-<YYYY.MM.dd>`                                           |
| Actuator   | http://<container_ip>:8888/actuator        | _Unprotected_                                   | Used in internal docker network for metrics scrapping, not exposed outside |


# Known Issues
* Unable to run the dockerized ELK stack on ARM-based Mac devices
* Running with Podman may require additional configuration
* (to be fixed) Admin user can delete himself even though he is an only admin

# Further Development
* Introduce token invalidation
* Introduce UUID for the User model to avoid exposure of PII data in authentication token
* Introduce shared cache and a proper strategy to calculate and store movie ratings considering potential intensive loading
* Introduce a load balancer to distribute requests across multiple instances
* Implement rate limiting and throttling mechanisms to prevent abuse and ensure fair usage