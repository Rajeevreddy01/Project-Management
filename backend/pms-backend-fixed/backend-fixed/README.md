# Project Management System - Backend

A full-featured REST API backend for a Project Management System built with **Spring Boot 3**, **Spring Security**, **JWT**, and **JPA/Hibernate**.

## Tech Stack

- **Java 17**
- **Spring Boot 3.2**
- **Spring Security** (JWT-based stateless auth)
- **Spring Data JPA** + **H2** (dev) / **MySQL** (prod)
- **Lombok**
- **Maven**

## Getting Started

### Prerequisites
- Java 17+
- Maven 3.8+

### Run the Application
```bash
./mvnw spring-boot:run
```
The server starts at: `http://localhost:8080`

### H2 Console (Dev)
Visit: `http://localhost:8080/h2-console`  
JDBC URL: `jdbc:h2:mem:pmsdb`

---

## API Endpoints

### Auth
| Method | URL | Description |
|--------|-----|-------------|
| POST | `/api/auth/login` | Login, returns JWT |
| POST | `/api/auth/register` | Register new user |

### Users
| Method | URL | Role |
|--------|-----|------|
| GET | `/api/users` | ADMIN/MANAGER |
| GET | `/api/users/{id}` | Authenticated |
| GET | `/api/users/me` | Authenticated |
| PUT | `/api/users/{id}` | ADMIN |
| DELETE | `/api/users/{id}` | ADMIN |

### Projects
| Method | URL | Role |
|--------|-----|------|
| GET | `/api/projects` | Authenticated |
| GET | `/api/projects/{id}` | Authenticated |
| POST | `/api/projects` | ADMIN/MANAGER |
| PUT | `/api/projects/{id}` | ADMIN/MANAGER |
| DELETE | `/api/projects/{id}` | ADMIN |

### Tasks
| Method | URL | Role |
|--------|-----|------|
| GET | `/api/tasks` | Authenticated |
| GET | `/api/tasks/{id}` | Authenticated |
| POST | `/api/tasks` | Authenticated |
| PUT | `/api/tasks/{id}` | Authenticated |
| PATCH | `/api/tasks/{id}/status` | Authenticated |
| DELETE | `/api/tasks/{id}` | ADMIN/MANAGER |

### Dashboard
| Method | URL | Description |
|--------|-----|-------------|
| GET | `/api/dashboard/stats` | Global stats |
| GET | `/api/dashboard/projects` | Project breakdown |
| GET | `/api/dashboard/tasks` | Task breakdown + overdue |
| GET | `/api/dashboard/me` | Current user's dashboard |

---

## Sample Login

```json
POST /api/auth/login
{
  "username": "admin",
  "password": "admin123"
}
```

Use the returned `token` in: `Authorization: Bearer <token>`

---

## Default Users

| Username | Password | Role |
|----------|----------|------|
| admin | admin123 | ADMIN |
| jsmith | user123 | MANAGER |
| ajonas | user123 | USER |
| mbrown | user123 | USER |

---

## Query Parameters

- `GET /api/projects?status=IN_PROGRESS`
- `GET /api/projects?keyword=ecommerce`
- `GET /api/tasks?projectId=1`
- `GET /api/tasks?assigneeId=3`
- `GET /api/tasks?status=TODO`
- `GET /api/tasks?overdue=true`

