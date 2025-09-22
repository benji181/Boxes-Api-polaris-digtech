# Delivery Boxes REST API

A Spring Boot REST API service for managing delivery boxes and their items. This service allows clients to create boxes, load them with items, and monitor their status and battery levels.

---

## Features

- Create delivery boxes with weight limits and battery capacity
- Load boxes with items (with weight and battery validation)
- Check loaded items for any box
- Get available boxes for loading
- Monitor battery levels
- Comprehensive validation and error handling
- H2 in-memory database with sample data

---

## Requirements

- Java 17 or higher
- **Gradle**

---

## Build Instructions

1. Clone or extract the project
2. Navigate to the project directory
3. Build the project:

   ```bash
   ./gradlew clean build
   ```

---

## Run Instructions

1. Start the application:

   ```bash
   ./gradlew bootRun
   ```

2. The API will be available at: `http://localhost:8081`

3. H2 Console (for database inspection): `http://localhost:8081/h2-console`
   - JDBC URL: `jdbc:h2:mem:testdb`
   - Username: `sa`
   - Password: (leave empty)

---

## Test Instructions

Run the unit tests:

```bash
./gradlew test
```

---

## API Endpoints

### 1. Create a Box
**POST** `/api/boxes`

Request body:
```json
{
  "txref": "BOX001",
  "weightLimit": 500,
  "batteryCapacity": 85
}
```

Response:
```json
{
  "id": 1,
  "txref": "BOX001",
  "weightLimit": 500,
  "batteryCapacity": 85,
  "state": "IDLE",
  "currentWeight": 0
}
```

### 2. Load Box with Item
**POST** `/api/boxes/{txref}/load`

Request body:
```json
{
  "name": "sample-item",
  "weight": 100,
  "code": "ITEM001"
}
```

Response:
```json
{
  "id": 1,
  "txref": "BOX001",
  "weightLimit": 500,
  "batteryCapacity": 85,
  "state": "LOADED",
  "currentWeight": 100
}
```

### 3. Get Loaded Items
**GET** `/api/boxes/{txref}/items`

Response:
```json
[
  {
    "id": 1,
    "name": "sample-item",
    "weight": 100,
    "code": "ITEM001"
  }
]
```

### 4. Get Available Boxes for Loading
**GET** `/api/boxes/available`

Response:
```json
[
  {
    "id": 1,
    "txref": "BOX001",
    "weightLimit": 500,
    "batteryCapacity": 85,
    "state": "IDLE",
    "currentWeight": 0
  }
]
```

### 5. Check Battery Level
**GET** `/api/boxes/{txref}/battery`

Response:
```json
85
```

### 6. Get Box Details
**GET** `/api/boxes/{txref}`

Response:
```json
{
  "id": 1,
  "txref": "BOX001",
  "weightLimit": 500,
  "batteryCapacity": 85,
  "state": "IDLE",
  "currentWeight": 0
}
```

---

## Business Rules

1. **Weight Limit**: Boxes cannot be loaded with more weight than their limit (max 500gr)
2. **Battery Requirement**: Boxes cannot enter LOADING state if battery is below 25%
3. **State Management**: Boxes follow the state flow: `IDLE → LOADING → LOADED → DELIVERING → DELIVERED → RETURNING`
4. **Validation**: All inputs are validated according to the specified constraints

---

## Sample Data

The application preloads sample boxes on startup:
- BOX001: 500gr limit, 85% battery (available for loading)
- BOX002: 300gr limit, 45% battery, LOADED state
- BOX003: 400gr limit, 20% battery (cannot load - low battery)
- BOX004: 250gr limit, 90% battery (available for loading)

---

## Error Handling

The API provides clear error responses:
- 400 Bad Request: Validation errors, business rule violations
- 404 Not Found: Box not found
- 409 Conflict: Duplicate txref
- 500 Internal Server Error: Unexpected errors

---

## Architecture

- **Controller Layer**: REST endpoints and request/response handling
- **Service Layer**: Business logic and validation
- **Repository Layer**: Data access using Spring Data JPA
- **Entity Layer**: JPA entities with validation annotations
- **DTO Layer**: Data transfer objects for API contracts
- **Exception Handling**: Global exception handler for consistent error responses

---

## Testing

The project includes unit tests for the service layer covering:
- Successful operations
- Error scenarios
- Business rule validation
- Edge cases

---

## Technology Stack

- Spring Boot 3.5.6
- Spring Data JPA
- Spring Validation
- H2 Database
- JUnit 5
- Mockito
- **Gradle**

---

## Assumptions & Design Choices

- Each box is uniquely identified using a transaction reference (txref).
- A box cannot be loaded if its battery is below 25%, to prevent delivery failures.
- A box cannot exceed its weight limit when loading items.
- Items must have a unique code per box to avoid duplicates.
- Box lifecycle is modeled as: `IDLE → LOADING → LOADED → DELIVERING → DELIVERED → RETURNING`.
- For persistence, an in-memory H2 database is used for simplicity. In production, this could be replaced with MySQL/PostgreSQL.
- Error handling returns clear validation messages instead of generic errors.

---
a comprehensive Spring Boot application provides all the requested functionality with proper validation, error handling, and a clean architecture. The service includes sample data, unit tests, and detailed documentation for easy setup and usage.

Thanks
