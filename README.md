# Parking System Backend

Spring Boot REST API for parking management with layered architecture (`controller -> service -> repository -> database`).

## Prerequisites

- Java 21
- MySQL 8+
- Maven Wrapper (`./mvnw`)

## Environment Variables

Create/update `.env` at project root:

```dotenv
DB_URL=jdbc:mysql://localhost:3306/parking_system?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC
DB_USER=parking_user
DB_PASSWORD=your_password
```

## Run Locally

```bash
./mvnw clean test
./mvnw spring-boot:run
```

## Key Endpoints

- `POST /parking/entry`
- `POST /parking/exit`
- `POST /payments`
- `GET /slots`
- `GET /slots/available`
- `GET /slots/occupied`
- `GET /slots/type/{slotType}`
- `GET /slots/stats`
- `GET /reports/active`
- `GET /reports/revenue`
- `GET /reports/occupancy`
- `GET /reports/tickets`

## Example API Calls

```bash
curl -X POST http://localhost:8080/parking/entry \
  -H 'Content-Type: application/json' \
  -d '{"vehicleNumber":"KA01AB1234","operatorId":"OP-1"}'
```

```bash
curl -X POST http://localhost:8080/payments \
  -H 'Content-Type: application/json' \
  -d '{"ticketId":"TICKET-ID"}'
```

