# FIFA Orders Service – Spring Boot + GraphQL

This project implements a RESTful backend for the FIFA Orders module using Spring Boot, Spring Data JPA, and PostgreSQL.

## Technology Stack

- Java 17
- Spring Boot 3.2.3
- Spring Data JPA
- PostgreSQL
- Swagger/OpenAPI for API documentation

## Project Structure

The project follows a standard layered architecture:

- **Model**: Entity classes representing database tables
- **Repository**: Data access layer using Spring Data JPA
- **Service**: Business logic layer
- **Controller**: REST API endpoints

## GraphQL API

All operations are exposed through a single HTTP endpoint:

```
POST /graphql
```

Use tools like GraphQL Playground, Insomnia, or the Apollo Explorer to run the following operations.

### Queries

```graphql
# List orders (basic details)
query Orders {
  orders {
    success
    message
    orders {
      orderId
      supplierId
      dcNumber
      poNumber
      isUrgent
      dueDate
    }
  }
}
```
- `GET /api/orders/{id}` - Get order by ID
- `POST /api/orders` - Create a new order
- `PUT /api/orders/{id}` - Update an existing order
- `DELETE /api/orders/{id}` - Delete an order

```graphql
# Single order with items
query Order($id: ID!) {
  order(id: $id) {
    success
    order {
      orderId
      supplierId
      isUrgent
      dueDate
      orderItems {
        itemId
        productStyle
        productSize
        orderQty
        isAccessories
      }
    }
  }
}
```

### Mutations

- `GET /api/orders/type/{orderType}` - Find orders by type
- `GET /api/orders/company/{companyId}` - Find orders by company
- `GET /api/orders/supplier/{supplierId}` - Find orders by supplier
- `GET /api/orders/customer/{customerId}` - Find orders by customer
- `GET /api/orders/urgent` - Find urgent orders
- `GET /api/orders/due-date?startDate=yyyy-MM-dd&endDate=yyyy-MM-dd` - Find orders by due date range
- `GET /api/orders/product?productName=name` - Find orders by product name

## Setup and Configuration

### Prerequisites

- Java 17 or higher
- Maven
- PostgreSQL

### Database Setup

1. Create a PostgreSQL database named `fifa`
2. Update the database connection details in `application.properties` if needed

### Running the Application

```bash

# Build the project
mvn clean install

# Run the application
mvn spring-boot:run
```

The application will start on port 8080.

### API Documentation

Swagger UI is available at: http://localhost:8080/swagger-ui.html

API docs are available at: http://localhost:8080/api-docs

## Domain Model

`Order` type (see `orders.graphqls`):

- `orderId`: Primary key (`ID!`)
- `supplierId`: FK to supplier company
- `orderItem`: Human-readable reference / code
- `orderType`: Category (e.g. INTERNAL/EXTERNAL)
- `orderService`: Service description
- `companyId`: FK to company
- `personId`: FK to requester
- `customerId`: FK to customer
- `materialId`: FK to material (optional)
- `isUrgent`: Boolean
- `isForeign`: Boolean
- `dueDate`: ISO string
- `deliveryDate`: ISO string
- `dcNumber`, `poNumber`: External references
- `orderItems`: `[OrderItem!]!`
- `isUrgent`: `true` if the order has high priority
- `isForeign`: `true` when the order is placed with an overseas supplier
- `isAccessories`: `true` when the order is only for accessories
- `dueDate`: Planned completion / delivery date (ISO-8601 string)
- `deliveryDate`: Actual delivery date (ISO-8601 string)
- `orderQty`: Total quantity ordered
- `deliverQty`: Quantity delivered so far
- `completeQty`: Quantity marked as completed
- `qcQty`: Quantity that passed quality check
- `billQty`: Quantity eligible for billing
- `qtyUom`: Unit of measure (e.g. pcs, meters, kg)
- `unitRate`: Price per unit (`Float`)
- `currency`: ISO currency code (e.g. INR, USD)
