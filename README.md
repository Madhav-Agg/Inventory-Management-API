# Inventory Management REST API

A RESTful backend application built with **Java, Spring Boot, Spring Data JPA, and MySQL** to manage products, maintain stock levels, and support inventory-related operations.

The API provides product management, stock updates, low-stock identification, category filtering, and input validation through structured REST endpoints.

## Features

- **Product Management:** Create, retrieve, update, and delete product records.
- **Stock Management:** Increase or decrease product quantities while preventing negative stock levels.
- **Low-Stock Alerts:** Retrieve products whose quantities fall below a specified threshold.
- **Search and Filtering:** Search products by name and filter them by category.
- **SKU Validation:** Prevent duplicate stock-keeping unit identifiers and return an appropriate conflict response.
- **Input Validation:** Validate incoming requests to maintain data consistency.
- **Centralized Error Handling:** Return structured JSON error responses for common API failures.
- **API Testing:** Includes a Postman collection for testing the available endpoints.

## Tech Stack

| Technology | Purpose |
|---|---|
| Java 17 | Programming language |
| Spring Boot 3 | Application framework |
| Spring Web | REST API development |
| Spring Data JPA | Database access and persistence |
| Hibernate | ORM implementation |
| MySQL | Relational database |
| Bean Validation | Request validation |
| Lombok | Reduces boilerplate code |
| Maven | Dependency management and build automation |
| Postman | API testing |

## Project Architecture

The application follows a layered architecture to separate HTTP request handling, business logic, database operations, and error handling.

```text
src/main/java/com/akash/inventory/
├── controller/    # REST endpoints
├── service/       # Business logic
├── repository/    # Database access
├── model/         # Product entity
└── exception/     # Custom exceptions and global handler
```

### How It Works

1. A client sends an HTTP request to an API endpoint.
2. The controller receives the request and passes the relevant data to the service layer.
3. The service layer performs business logic, including stock validation.
4. The repository interacts with MySQL through Spring Data JPA.
5. The application returns an HTTP response containing the result or a structured error.

This separation makes the application easier to maintain, test, and troubleshoot.

## Getting Started

### Prerequisites

Install the following software:

- Java Development Kit (JDK) 17 or later
- Maven
- MySQL Server
- Postman (optional, for API testing)

### 1. Clone the Repository

```bash
git clone https://github.com/Akashvallamkonda/inventory-management-api.git
cd inventory-management-api
```

### 2. Configure MySQL

Ensure your MySQL server is running.

The application uses the following default database configuration:

| Setting | Default |
|---|---|
| Database name | `inventory_db` |
| Database username | `root` |
| Database password | Set using an environment variable |
| Database host | `localhost` |
| Database port | `3306` |

Set your MySQL password before starting the application.

**Windows PowerShell:**

```powershell
$env:DB_PASSWORD="your_mysql_password"
```

If required, configure a different username:

```powershell
$env:DB_USER="root"
```

The application also supports configuration for the database host, port, and name through the environment variables documented in the project.

Do not commit real database credentials to GitHub.

### 3. Run the Application

From the project root, execute:

```bash
mvn spring-boot:run
```

According to the project documentation, the database `inventory_db` and the `products` table are created automatically.

Once the application starts, the API is available at:

```text
http://localhost:8080/api/products
```

## API Endpoints

The following endpoints are documented by the application.

| Method | Endpoint | Description |
|---|---|---|
| `POST` | `/api/products` | Create a product |
| `GET` | `/api/products` | Retrieve all products |
| `GET` | `/api/products/{id}` | Retrieve a product by ID |
| `PUT` | `/api/products/{id}` | Update a product |
| `PATCH` | `/api/products/{id}/stock?change=5` | Increase or decrease stock |
| `GET` | `/api/products/low-stock?threshold=10` | Find products below a stock threshold |
| `DELETE` | `/api/products/{id}` | Delete a product |

### Search and Filtering

Retrieve products by category:

```http
GET /api/products?category=Electronics
```

Search products by name:

```http
GET /api/products?search=Keyboard
```

### Stock Updates

Increase stock by five units:

```http
PATCH /api/products/1/stock?change=5
```

Decrease stock by three units:

```http
PATCH /api/products/1/stock?change=-3
```

The API includes protection against negative stock levels.

### Low-Stock Identification

Retrieve products with quantities below ten units:

```http
GET /api/products/low-stock?threshold=10
```

## Example API Request

Create a product by sending a `POST` request to `/api/products`.

**Request body:**

```json
{
  "name": "Keyboard",
  "sku": "KB-001",
  "quantity": 25,
  "price": 799.00,
  "category": "Electronics"
}
```

The request creates a product with a unique SKU, its available quantity, price, and category, provided the submitted data passes validation.

## Error Handling

The application uses structured JSON error responses for API failures.

For example, a request for a product that does not exist may return:

```json
{
  "timestamp": "2026-09-29T11:30:00",
  "status": 404,
  "error": "Product not found: 99"
}
```

The actual timestamp and error details depend on the request and application response.

The application also documents a `409 Conflict` response for duplicate SKUs.

## Testing with Postman

A Postman collection is included in the repository:

```text
postman_collection.json
```

To test the API:

1. Start the Spring Boot application and MySQL server.
2. Import `postman_collection.json` into Postman.
3. Send requests to the local API.
4. Verify the returned HTTP status codes and response bodies.
5. Test valid operations as well as invalid inputs, missing product IDs, duplicate SKUs, and stock updates that would result in negative quantities.

## Running Tests

Run the project's test suite with Maven:

```bash
mvn test
```

Review the test output to determine whether the existing tests pass.

## Learning Outcomes

This project provides practical exposure to:

- Building RESTful APIs with Spring Boot.
- Implementing CRUD operations using Spring Data JPA.
- Connecting an application to a MySQL database.
- Separating controller, service, and repository responsibilities.
- Validating request data and handling API errors.
- Implementing business rules for inventory stock updates.
- Testing HTTP endpoints using Postman.
- Running and building a Java application with Maven.

## Future Improvements

Potential extensions include:

- Add pagination and sorting to product listings.
- Introduce unit and integration tests for stock-related business rules.
- Add API documentation using OpenAPI and Swagger UI.
- Implement role-based authentication and authorization.
- Add an audit history for stock changes.
- Measure API response times under a defined test workload.

These are possible future enhancements and are not claimed as existing features.

## License and Attribution

This README documents the functionality described in the project repository. Refer to the repository's license and attribution requirements before redistributing or modifying the source code.

Original repository: https://github.com/Akashvallamkonda/inventory-management-api
