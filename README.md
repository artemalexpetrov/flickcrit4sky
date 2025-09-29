Flick Critique
==============

Flick Critique is a movie rating application built with Java and Spring Boot. The application provides a platform
where users rate movies.

---

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

2) Spin up the docker containers
   ```bash
   docker-compose up --build
   ```
3) Navigate to `http://localhost:8080`

4) Access the API ([Swagger](http://127.0.0.1:8080/docs/swagger-ui/index.html), Postman, http.requests)

5) Troubleshooting: ensure there are no port conflicts (adjust env file)

---

# How to Build & Test

Build and test the application using Gradle

   ```bash
   ./gradlew build
   ```

To run the application manually (e.g. with `java -jar ...`) you have to provide mandatory configuration parameters:



---

# Useful links

| Service    | Links                                      | Default Credentials (.env.dist)                 | Notes & Tips                                                               |
|------------|--------------------------------------------|-------------------------------------------------|----------------------------------------------------------------------------|
| Postgres   | jdbc:postgresql://localhost:5432/flickcrit | user/passw0rd                                   | In case you want to observe the data                                       |
| Prometheus | http://localhost:9090                      | admin/passw0rd                                  | Example queries:<br/>CPU Usage: system_cpu_usage * 100                     |
| Grafana    | http://localhost:3000                      | admin/admin<br/>(require to change after login) | The provisioned dashboard: JVM SpringBoot3                                 |
| Kibana     | http://localhost:5601                      | elastic/elastic                                 | Indexes: `app-logs-<YYYY.MM.dd>`                                           |
| Actuator   | http://<container_ip>:8888/actuator        | _Unprotected_                                   | Used in internal docker network for metrics scrapping, not exposed outside |



