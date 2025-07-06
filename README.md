# demo-auth
# Spring Boot Role-Based Authentication with Audit Logging

## Table DDLs

CREATE TABLE users (
    id SERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
    username VARCHAR(100) NOT NULL,
    phone VARCHAR(20),
    address TEXT,
    password VARCHAR(255) NOT NULL,
    role_id INT NOT NULL,
    FOREIGN KEY (role_id) REFERENCES roles(id)
);

CREATE TABLE roles (
    id SERIAL PRIMARY KEY,
    name VARCHAR(50) NOT NULL UNIQUE
);

CREATE TABLE api_audit_logs (
    id SERIAL PRIMARY KEY,
    endpoint VARCHAR(255) NOT NULL,
    request_payload TEXT,
    response_payload TEXT,
    http_method VARCHAR(10),
    username VARCHAR(100),
    timestamp TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

## 🧾 Overview

This is a **Spring Boot-based RESTful API** project that implements:
- User & Role management (CRUD)
- JWT-based Authentication
- Role-based Authorization
- API Audit Logging
- Swagger (OpenAPI 3.0) Documentation

It uses PostgreSQL as the backend database and supports secure access via JWT tokens. All incoming API calls are logged to the database for audit purposes.

---

## 🏗️ Technologies Used

| Tool/Framework    | Version        |
|------------------|----------------|
| Spring Boot      | 2.6+           |
| Spring Security  | 5.6+           |
| PostgreSQL       | 13+            |
| Swagger (Springdoc OpenAPI) | 1.6+   |
| Lombok           | ✓              |
| Jackson          | ✓              |
| JWT              | ✓              |

---

## 🔐 Role-Based Access

| Role          | Permissions                  |
|---------------|------------------------------|
| `Admin`       | Full access (User & Role CRUD) |
| `Manager`     | Can manage users & roles     |
| `Normal User` | View-only access             |

---

## 📦 Features

✅ JWT Authentication  
✅ Role-Based Authorization  
✅ User and Role CRUD APIs  
✅ API Audit Logging (endpoint, payloads, user, timestamp)  
✅ Swagger UI Integration  
✅ CORS Enabled for frontend integration  
✅ Error handling with proper status codes

---

## 🔑 Authentication

Login via `/auth/signin` with:

```json
{
  "userName": "admin@example.com",
  "password": "admin123"
}
