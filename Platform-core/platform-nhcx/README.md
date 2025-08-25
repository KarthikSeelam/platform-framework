# Platform NHCX Microservice

## Overview

The `platform-nhcx` microservice is a Spring Boot application responsible for integrating with the National Health Claim Exchange (NHCX) sandbox. It implements the HCX protocol for Coverage Eligibility and Pre-Authorization flows.

This service handles the complexities of the HCX protocol, including:
- JWE/JWS for payload encryption and signing.
- Construction of FHIR R4 bundles for requests and responses.
- Management of the `x-hcx-*` protocol headers.
- Asynchronous communication patterns with callback endpoints.

## Tech Stack

- **Java 17**
- **Spring Boot 3**
- **Spring WebFlux** (for reactive HTTP clients)
- **Spring Data JPA** (with MySQL)
- **Spring for Apache Kafka**
- **HAPI FHIR** (for FHIR R4)
- **Nimbus JOSE+JWT** (for JWE/JWS)
- **Spring Cloud** (Consul for config, Vault for secrets)
- **Testcontainers** (for integration testing)
- **WireMock** (for mocking external APIs)
- **Docker**

## Building the Service

To build the service, run the following command from the `Platform-core` root directory:

```bash
mvn clean install
```

To build the service and skip tests:

```bash
mvn clean install -DskipTests
```

## Running Locally

### Prerequisites

- Docker and Docker Compose
- Java 17
- Maven

### Using Docker Compose

The easiest way to run the service and all its dependencies (MySQL, Kafka, Consul, Vault) is by using the `docker-compose.yml` file located in the `Platform-core` root directory.

From the `Platform-core` root, run:

```bash
docker-compose up --build
```

This will:
1. Build the Docker image for the `platform-nhcx` service.
2. Start all the required infrastructure services.
3. Start the `platform-nhcx` service.

The service will be available at `http://localhost:8085`.

### Configuration

The service uses Consul for configuration management and Vault for secrets.

- **Consul**: Configuration is stored in Consul's KV store under `config/platform-nhcx`.
- **Vault**: Secrets are stored in Vault under `secret/platform-nhcx`.

When running with Docker Compose, the service will connect to the Consul and Vault containers automatically. You will need to populate Consul and Vault with the required configuration and secrets.

**Required Vault Secrets (`secret/platform-nhcx`):**
- `spring.datasource.username`
- `spring.datasource.password`
- `hcx.sender.signing.private-key-pem`
- `hcx.recipient.encryption.public-key-pem`
- `hcx.encryption.private-key-pem`

**Required Consul KV (`config/platform-nhcx`):**
- `hcx.gateway.base-path`
- `hcx.sender.code`
- `hcx.recipient.code`
- `kafka.topic.nhcx-events`
- `kafka.topic.nhcx-audit`


## API Endpoints

The service exposes the following internal API endpoints:

### Coverage Eligibility

- `POST /nhcx/coverage-eligibility/check`: Submit a coverage eligibility check.
- `GET /nhcx/coverage-eligibility/status/{correlationId}`: Check the status of a request.

### Pre-Authorization

- `POST /nhcx/preauth/submit`: Submit a pre-authorization request.
- `GET /nhcx/preauth/status/{correlationId}`: Check the status of a request.

### Callbacks

The service exposes the following public callback endpoints for HCX to call:

- `POST /nhcx/callback/coverage-eligibility/on_check`
- `POST /nhcx/callback/preauth/on_submit`

## Testing

### Unit Tests

To run the unit tests, execute the following command from the `Platform-core` root:

```bash
mvn test
```

### Integration Tests

The integration tests use Testcontainers to spin up real dependencies (MySQL, Kafka). To run the integration tests, simply run the Maven test command as above.

```bash
mvn test
```
