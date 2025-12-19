# Spring Boot Microservices: Event-Driven Kafka & Redis System

A high-performance microservices demonstration implementing an event-driven architecture for user registration and notification workflows using **Kafka**, **Redis**, and **MySQL**.

## Architecture Overview

The system consists of two main services and a shared infrastructure:

1.  **User Service (Port 8081):**
    *   Handles User CRUD operations and profile management.
    *   **Caching:** Uses **Redis** to store and retrieve user profiles, significantly reducing database load.
    *   **Producer:** Upon user creation, it persists a `PROCESSING` event in MySQL and publishes a message to **Kafka** (topic: `user.email.send`).
    *   **Confirmation Listener:** Listens for `user.email.sent` to update the local event status to `FINISHED`.

2.  **Email Service (Port 8083):**
    *   **Consumer:** Subscribes to `user.email.send` events.
    *   **Notification:** Sends welcome emails via **Mailhog** (SMTP).
    *   **Event Tracking:** Maintains its own event log in MySQL to ensure idempotency.
    *   **Confirmation:** Publishes an acknowledgement back to Kafka once the email is successfully dispatched.

3.  **Infrastructure:**
    *   **Kafka:** Message broker for asynchronous inter-service communication.
    *   **Redis:** Distributed cache for the User Service.
    *   **MySQL:** Relational database for persistent storage of users and event logs.
    *   **Mailhog:** Email testing tool with a Web UI to capture outgoing SMTP traffic.

## Tech Stack

- **Java 17**
- **Spring Boot 3.4.0**
- **Spring Data JPA** (Hibernate 7)
- **Spring Kafka** (Pub/Sub messaging)
- **Spring Data Redis** (Caching)
- **WebClient** (Reactive inter-service REST calls)
- **MapStruct** (Entity-DTO mapping)
- **Docker & Docker Compose** (Containerization)

## Features

- **Asynchronous Workflow:** User registration is decoupled from email sending via Kafka.
- **Idempotent Consumers:** The Email Service checks event UUIDs in the database to prevent duplicate processing.
- **Distributed Caching:** Seamless Redis integration with automatic cache expiration.
- **Inter-service REST:** Uses `WebClient` for synchronous "Request-Response" patterns between services.
- **Global Error Handling:** Standardized error responses across all services.

## Setup and Running

### Prerequisites
- Docker and Docker Compose installed.
- JDK 17+ (if building locally).

### Running with Docker

1. **Build the Services:**
   ```bash
   # Build User Service (Maven)
   cd kafka-redis-test-user-service
   ./mvnw clean package -DskipTests

   # Build Email Service (Gradle)
   cd ../kafka-test-email-service
   ./gradlew build -x test
   ```

2. **Start the Infrastructure:**
   ```bash
   # From the root directory containing docker-compose.yml
   docker compose up --build
   ```

### Accessing the System
- **User Service API:** `http://localhost:8081/swagger-ui/index.html`
- **Email Service API:** `http://localhost:8083`
- **Mailhog Web UI:** `http://localhost:8025` (View sent emails here)

## API Reference

### User Service (8081)
| Method | Endpoint | Description |
| :--- | :--- | :--- |
| `POST` | `/api/users/` | Create user & trigger Kafka event |
| `GET` | `/api/users/{id}` | Get user (Cached via Redis) |
| `GET` | `/api/users/` | Paginated list of users |
| `PATCH` | `/api/users/{id}` | Update user & invalidate cache |
| `GET` | `/tests/responsedata` | Test endpoint for incoming WebClient calls |

### Email Service (8083)
| Method | Endpoint | Description |
| :--- | :--- | :--- |
| `GET` | `/tests/firenforget` | Trigger an async "Fire and Forget" call to User Service |
| `GET` | `/tests/requestdata` | Synchronously request data from User Service via WebClient |

## Configuration Details

The services are configured to automatically wait for the MySQL database to be ready before starting. 
Key environment variables used in `docker-compose.yml`:
- `SPRING_KAFKA_BOOTSTRAP_SERVERS`: `kafka:29092`
- `SPRING_DATA_REDIS_HOST`: `redis`
- `USER_SERVICE_URL`: `http://user-service:8081`

---
