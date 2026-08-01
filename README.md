# 🛒 E-Commerce Cart Backend

**CodTECH IT Solutions Internship Project**
**Intern ID:** CITS7505

## 📖 Overview
A robust, industry-standard RESTful API for an e-commerce shopping cart. Built with **Spring Boot 3**, secured via **Stateless JWT Authentication**, and backed by **PostgreSQL**. The project features comprehensive unit testing with **100% core test coverage** and is fully containerized using **Docker**.

## ✨ Key Features
* **Product & Category Management:** Full CRUD capabilities for catalogue management.
* **Shopping Cart Logic:** Add to cart, update quantities, and clear cart functionality.
* **Order Checkout System:** Stock validation, dynamic total calculation, and stock deduction.
* **Coupon & Discount Engine:** Validation based on expiry dates, active status, and minimum order requirements.
* **Role-Based Access Control (RBAC):** Admin-only routes vs User-only routes.
* **Stateless JWT Authentication:** Secure session-less API design.
* **Global Exception Handling:** Custom exception hierarchy with unified error responses.
* **Dockerized Environment:** Multi-stage `Dockerfile` and `docker-compose` for seamless database integration.

## 🛠️ Tech Stack
* **Language:** Java 21
* **Framework:** Spring Boot 4.1.0
* **Database:** PostgreSQL 16
* **Security:** Spring Security + JSON Web Tokens (JWT)
* **Testing:** JUnit 5, Mockito
* **Documentation:** Springdoc OpenAPI (Swagger UI)
* **DevOps:** Docker, Docker Compose, Maven

---

## 🚀 Getting Started

### Prerequisites
* Docker and Docker Compose installed.
* Java 21 and Maven installed (if running locally without Docker).

### 1. Run using Docker Compose (Recommended)
This will spin up both the PostgreSQL database and the Spring Boot application containers.
```bash
docker-compose up --build
```
The API will be available at: `http://localhost:8080`

### 2. Run Locally (Development)
If you prefer to run the application outside of Docker, ensure you have a local PostgreSQL instance running.

1. Update `src/main/resources/application.properties` with your local DB credentials.
2. Run the application:
```bash
# On Linux/macOS
./mvnw spring-boot:run

# On Windows
.\mvnw.cmd spring-boot:run
```

---

## 📚 API Documentation
Once the application is running, you can explore and test the API endpoints using the interactive Swagger UI:
* **Swagger UI:** [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)
* **OpenAPI JSON:** [http://localhost:8080/v3/api-docs](http://localhost:8080/v3/api-docs)

---

## 🧪 Testing
The project includes an exhaustive test suite covering all services and controllers using pure Mockito.

To run the tests and view the Surefire reports:
```bash
# On Linux/macOS
./mvnw clean test

# On Windows
.\mvnw.cmd clean test
```
**Current Test Coverage:** 47 Tests (0 Failures, 0 Errors)

---

## 📂 Architecture & Design Patterns
* **Controller-Service-Repository Pattern:** Clean separation of concerns.
* **DTO Pattern (Data Transfer Object):** Isolates database entities from API request/response payloads.
* **Mapper Pattern:** Custom mappers cleanly convert Entities to DTOs.
* **Filter Chain Security:** JWT extraction and validation via custom `OncePerRequestFilter`.

---
*Developed as part of the CodTECH IT Solutions Internship Program.*
