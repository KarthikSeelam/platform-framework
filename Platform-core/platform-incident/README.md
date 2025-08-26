# Incident Management Service (`platform-incident`)

## 1. Overview

The `platform-incident` service is a Spring Boot microservice responsible for managing incidents within the iCAN platform. It provides a RESTful API for creating, fetching, and managing incident data.

This service is a module within the `platform-core` multi-module Maven project and inherits common configurations and dependencies from the `platform-core` parent.

## 2. Build & Run

### Prerequisites

*   Java 17
*   Maven 3.x
*   A running MySQL database instance

### Database Setup

The service expects a MySQL database named `platform_incident`. You can configure the connection details in the `application.yml` file or by setting the following environment variables:

*   `DB_URL`: The full JDBC URL (e.g., `jdbc:mysql://localhost:3306/platform_incident`)
*   `DB_USER`: The database username
*   `DB_PASSWORD`: The database password

The database schema will be automatically created on startup via Hibernate's `create-drop` mechanism.

### Build

To build the service, navigate to the root of the `Platform-core` project and run:

```bash
mvn clean install
```

### Run

You can run the service directly from the command line using the generated JAR file:

```bash
java -jar platform-incident/target/platform-incident-1.0.0.jar
```

The service will start on port 8080 by default.

## 3. API Endpoints

### 3.1. Create Incident

*   **Endpoint:** `POST /incidents`
*   **Description:** Creates a new incident.

**Example Request:**

```json
{
  "title": "Payment service down on checkout",
  "description": "Customers cannot complete checkout; 502 from payment gateway.",
  "priority": "HIGH",
  "reportedBy": "jane.doe@acme.com",
  "tags": ["checkout", "payment"],
  "attachments": []
}
```

**Example Response (201 Created):**

```json
{
  "id": "e7b7a5d0-6c3a-4a42-9b0c-1d2f0b6b1f8a",
  "title": "Payment service down on checkout",
  "description": "Customers cannot complete checkout; 502 from payment gateway.",
  "priority": "HIGH",
  "status": "OPEN",
  "reportedBy": "jane.doe@acme.com",
  "tags": null,
  "createdAt": "2025-08-26T06:15:27Z",
  "updatedAt": "2025-08-26T06:15:27Z"
}
```

### 3.2. Fetch Incidents (Paginated)

*   **Endpoint:** `GET /incidents`
*   **Description:** Fetches a paginated list of incidents with optional filtering.
*   **Query Parameters:**
    *   `page`: Page number (default: 0)
    *   `size`: Page size (default: 20)
    *   `sort`: Sort order (e.g., `createdAt,desc`)
    *   `status`: Filter by incident status (e.g., `OPEN`, `CLOSED`)
    *   `priority`: Filter by priority (e.g., `HIGH`)
    *   `reportedBy`: Filter by the reporter's email.

**Example Response (200 OK):**

```json
{
  "content": [
    {
      "id": "e7b7a5d0-6c3a-4a42-9b0c-1d2f0b6b1f8a",
      "title": "Payment service down on checkout",
      "description": "Customers cannot complete checkout; 502 from payment gateway.",
      "priority": "HIGH",
      "status": "OPEN",
      "reportedBy": "jane.doe@acme.com",
      "tags": null,
      "createdAt": "2025-08-26T06:15:27Z",
      "updatedAt": "2025-08-26T06:15:27Z"
    }
  ],
  "page": 0,
  "size": 20,
  "totalElements": 1,
  "totalPages": 1
}
```

### 3.3. Fetch Incident by ID

*   **Endpoint:** `GET /incidents/{id}`
*   **Description:** Fetches a single incident by its UUID.

**Example Response (200 OK):** (Same as the create response)

## 4. Swagger / OpenAPI

The OpenAPI documentation is available while the service is running:

*   **Swagger UI:** [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)
*   **API Docs (JSON):** [http://localhost:8080/v3/api-docs](http://localhost:8080/v3/api-docs)

## 5. Imported Artifacts from `iassure-backend`

This service was created by reusing the following domain concepts from the `iassure-backend` repository:

*   **Entity:** `Iassure/src/main/java/com/iassure/incident/entity/Incidents.java` -> `Incident.java`
*   **Enum:** `Iassure/src/main/java/com/iassure/constants/IncidentStatus.java` -> `IncidentStatus.java`
*   **Repository Interface:** `Iassure/src/main/java/com/iassure/incident/repository/IncidentDetailsRepository.java` was used as a reference for creating a new, clean `IncidentRepository.java`.

## 6. Exclusions

The following components from the `iassure-backend` incident module were intentionally **not** imported to ensure a clean, modern implementation aligned with platform standards:
*   Controllers
*   Services (`*ServiceImpl`, `*DAOImpl`)
*   DAOs
*   Custom native queries and stored procedure calls in repositories

This approach ensures that the new `platform-incident` service is decoupled from the legacy business logic of the `iassure-backend`.
