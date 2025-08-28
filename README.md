# 🚤 Full-stack Coding Challenge – Boats Application (OWT)

This repository contains a minimal full-stack application built with **Angular** (frontend) and **Spring Boot + Gradle** (backend) using IntelliJ as the go-to IDE.  
The app demonstrates end-to-end flow: **login → view boats → create/update/delete boats → detail view**, with JWT-based authentication.

---

## 📦 Running the application

You can run the project in **two ways**:

### Option 1: Docker Compose
```bash
docker compose up --build
```
- Backend available at: http://localhost:8080
- Frontend available at: http://localhost:4200

### Option 2: Run frontend & backend separately

**Backend (Boats API)**
```bash
cd backend
./gradlew clean bootRun
```

**Frontend (Boats UI)**
```bash
cd frontend
npm install
ng serve
```

---

## 👤 Test user

For simplicity, only **one in-memory user** exists:

```
username: admin
password: 123456789
```

When you log in successfully, a **JWT access token** is issued and stored in browser **localStorage**. Logging out clears it.  
⚠️ This is **not production-grade auth**. It’s enough to satisfy the challenge requirements without over-engineering.

---

## ✅ Implemented scope

- **Authentication / Authorization**
    - Login with username & password
    - JWT required for all protected endpoints
    - Logout clears local token
- **Boat management (CRUD)**
    - List all boats
    - Create a new boat
    - Update existing boat
    - Delete a boat
    - View boat details
- **Frontend features**
    - Angular routing (login screen, overview, detail, form pages)
    - Auth guard + interceptor (attach JWT, redirect on 401)
    - Reactive forms with minimal validation
    - Angular Material components (table, form, buttons)
    - Minimal error handling for feedback
- **Backend features**
    - REST endpoints under `/api/boats`
    - DTOs with validation
    - Service layer with clean separation
    - Global exception handler returning consistent JSON errors
    - Spring Security with stateless JWT filter
    - In-memory user for authentication

---

## 📄 OpenAPI Specification

The API is described in **OpenAPI** (`specs/boat-api.yaml`).  
This was used to align frontend and backend and to generate TypeScript types in Angular.

---

## ⚖️ Design choices & trade-offs

- **Minimal scope:** Only implemented exactly what was required. No refresh tokens, no user registration, no real DB.
- **JWT in localStorage:** Chosen for demo simplicity. In real projects, HttpOnly cookies or token rotation would be safer.
- **H2 in-memory database:** Keeps setup friction-free. Real deployments would use Postgres/MySQL.

---

## 🧪 Testing

- **Backend:** A couple of `MockMvc` integration tests for the happy path (authentication + fetching boat resources).
- **Frontend:** No automated tests for the sake of simplicity, only minimal error handling to provide user feedback.

---

## 🚀 Next steps (if this were a real project)

- Overhaul authentication/authorization flow using **OAuth2 + OpenID Connect**, ideally with **PKCE-enhanced authorization code flow** (since this is a SPA).
- Add **refresh tokens** and proper user management.
- Persist users & boats in a relational DB.
- Role-based access control
- Comprehensive **end-to-end testing** (Cypress/Playwright).
- CI/CD pipeline with build, tests, and deployment.
- Harden security (password hashing, secrets management, production-ready configs).
- Containerization for production (multi-stage Docker build).

---

## 🗂 Repo structure

```
boat-app/
  backend/         # Spring Boot API (Gradle)
  frontend/        # Angular UI
  specs/           # OpenAPI contract (boat-api.yaml)
  docker-compose.yml
  README.md
```

---