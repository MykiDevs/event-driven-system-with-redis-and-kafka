# Event-Driven System with Redis And Kafka

A simple demonstration of event-driven architecture for user registration and notification workflows using Kafka, Redis, and MySQL.

## Architecture Overview

The system consists of two main services and a shared infrastructure:

1.  **User Service:**
    *   Handles User CRUD operations and profile management.
    *   **Caching:** Uses **Redis** to store and retrieve user profiles, significantly reducing database load.
    *   **Producer:** Upon user creation, it persists a `PROCESSING` event in MySQL and publishes a message to **Kafka** (topic: `user.email.send`).
    *   **Confirmation Listener:** Listens for `user.email.sent` to update the local event status to `FINISHED`.

2.  **Email Service:**
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
- **Spring Data JPA**
- **Spring Kafka**
- **Spring Data Redis**
- **Docker & Docker Compose**

## Getting Started

### Prerequisites
- Docker
- Java 17+

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

2. **Using Docker Compose:**
   ```bash
   docker compose up --build
   ```

### Accessing the System
- **User Service API:** `http://localhost:8081/swagger-ui/index.html`
- **Email Service API:** `http://localhost:8083`
- **Mailhog Web UI:** `http://localhost:8025` (View sent emails here)

## API Reference

### User Service
| Method | Endpoint | Description |
| :--- | :--- | :--- |
| `POST` | `/api/users/` | Create user & trigger Kafka event |
| `GET` | `/api/users/{id}` | Get user (Cached via Redis) |
| `GET` | `/api/users/` | Paginated list of users |
| `PATCH` | `/api/users/{id}` | Update user & invalidate cache |
| `GET` | `/tests/responsedata` | Test endpoint |

### Email Service
| Method | Endpoint | Description |
| :--- | :--- | :--- |
| `GET` | `/tests/firenforget` | Trigger an async "Fire and Forget" call to User Service |
| `GET` | `/tests/requestdata` | Synchronously request data from User Service |
## License
[MIT](https://github.com/MykiDevs/event-driven-system-with-redis-and-kafka/blob/main/LICENSE)
