# Banking Transaction System (Mini Core Banking)

A production-style full-stack mini core banking application built with Spring Boot and React.js. It demonstrates secure account operations, atomic money transfers, and paginated transaction history.

## Project Overview

This project models core banking operations:
- Account onboarding with minimum opening balance validation
- Account inquiry
- Deposit and withdrawal flows with business validations
- Money transfer between accounts with transaction atomicity via `@Transactional`
- Transaction history with pagination, sorting, and type-based filtering

## Tech Stack

### Backend (`backend/`)
- Java 17
- Spring Boot 3
- Spring Data JPA (Hibernate)
- MySQL
- Spring Transactions
- Spring Validation
- Springdoc OpenAPI / Swagger UI

### Frontend (`frontend/`)
- React.js (Functional Components)
- React Hooks (`useState`, `useEffect`)
- Axios
- React Router
- Responsive CSS

## System Architecture

### Backend Layered Design
- **Controller Layer**: Exposes REST APIs and handles request/response models.
- **Service Layer**: Contains business logic for account and transaction operations.
- **Repository Layer**: Data access with Spring Data JPA.
- **DTO + Mapper Layer**: DTOs are used for API contracts, entities are internal.
- **Exception Layer**: Custom exceptions + centralized global exception handling with `@ControllerAdvice`.

### Frontend Design
- Page-level route architecture (`Dashboard`, `Transactions`)
- Reusable form/table components
- Centralized API abstraction (`src/api/bankingApi.js`)
- Unified loading/error status handling in pages

## Database Schema

### 1) `accounts`
- `id` (PK)
- `account_number` (Unique index)
- `full_name`
- `email` (Unique index)
- `balance`
- `created_at`

### 2) `transactions`
- `id` (PK)
- `account_id` (FK -> accounts.id)
- `counterparty_account_id` (nullable FK -> accounts.id)
- `type` (DEPOSIT, WITHDRAWAL, TRANSFER)
- `amount`
- `post_balance`
- `description`
- `created_at`

Indexes are included on transaction `type`, `created_at`, and `account_id` for efficient filtering and sorting.

## API Endpoints

Base URL: `http://localhost:8080/api/v1`

### Account APIs
- `POST /accounts` - Create account
- `GET /accounts/{accountNumber}` - Fetch account details
- `POST /accounts/{accountNumber}/deposit` - Deposit money
- `POST /accounts/{accountNumber}/withdraw` - Withdraw money

### Transaction APIs
- `POST /transactions/transfer` - Transfer money
- `GET /transactions` - List transactions (supports `page`, `size`, `sortBy`, `direction`, `type`, `accountNumber`)

### Swagger
- Swagger UI: `http://localhost:8080/swagger-ui.html`
- OpenAPI JSON: `http://localhost:8080/v3/api-docs`

## Frontend Features

- Dashboard for account snapshot and recent transactions
- Account creation form
- Account lookup form
- Deposit / withdraw forms
- Transfer form
- Transaction history table
- Dedicated transactions page with pagination + sorting + filter controls
- User-friendly API error feedback from backend exception responses

## Local Setup

## 1) Backend

```bash
cd backend
mvn spring-boot:run
```

Update DB credentials in `backend/src/main/resources/application.yml` if needed.

## 2) Frontend

```bash
cd frontend
cp .env.example .env
npm install
npm run dev
```

App URL: `http://localhost:5173`

## Example cURL Requests

Create account:
```bash
curl -X POST http://localhost:8080/api/v1/accounts \
  -H "Content-Type: application/json" \
  -d '{"fullName":"Alice Jones","email":"alice@example.com","initialDeposit":1000}'
```

Transfer money:
```bash
curl -X POST http://localhost:8080/api/v1/transactions/transfer \
  -H "Content-Type: application/json" \
  -d '{"fromAccountNumber":"BA1234567890","toAccountNumber":"BA0987654321","amount":150,"description":"Rent"}'
```

---
Built as a senior-grade demonstration project for clean architecture, transactional consistency, and practical full-stack API consumption.
