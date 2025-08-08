#  Task Management System API

A Spring Boot–based REST API for managing tasks, including features for authentication, authorization, and filtering/sorting of tasks.  
Supports user roles, JWT-based authentication, and a PostgreSQL database running in Docker.

##  Features

### User Authentication & Authorization
- JWT-based authentication.
- Role-based access control.
- Only authenticated users can manage tasks.

### Task Management
- Create, update, delete, and list tasks.
- View task details by ID.
- Task attributes:
    - **Title** (required)
    - **Description**
    - **Status**: `TO_DO`, `IN_PROGRESS`, `COMPLETED`
    - **Due Date**
    - **Priority**: `LOW`, `MEDIUM`, `HIGH`
    - **Created By** (User ID)

### Filtering & Sorting
- Filter tasks by **status**, **priority**, and **due date**.


## Technologies Used

- Java 21
- Spring Boot
- Spring Security (JWT-based)
- PostgreSQL (via Docker)
- Maven
- Swagger (Springdoc OpenAPI)
- Docker & Docker Compose

##  Requirements

- Java 21+
- Maven 3.8+
- Docker

##  How to Run

###  Start with Docker
  docker-compose up

This will start the PostgreSQL database and the Spring Boot application.


## Authentication
Register User (no auth required)  
http  
POST http://localhost:8080/ejada/api/auth/register  
REQ json  
{
"username": "ejada_user_1",
"password": "ejada_pass_1",
"role": "ADMIN"
}
RES json  
{
"id": 1,
"username": "ejada_user_1",
"role": "ADMIN"
}


Login (returns JWT token)  
http  
POST http://localhost:8080/ejada/api/auth/login  
json  
{
"username": "ejada_user_1",
"password": "ejada_pass_1"
}
RES json  
{
"token": "eyJhbGciOiJIUzUxMiJ9..."
}  
Use the returned token in Authorization: Bearer <token> header for all protected requests.

## Swagger UI (API Docs)
http://localhost:8080/flapkap/swagger-ui/index.html

## Health Check
GET http://localhost:8080/ejada/api/actuator/health

## API Endpoints Summary
###  User
| Method | Endpoint                       | Role   | Description         |
|--------|--------------------------------|--------|---------------------|
| POST   | `/ejada/api/auth/register`     | Public | Register a new user |
| POST   | `/ejada/api/auth/login`        | Public | Login and get token |

---

###  Task
| Method | Endpoint                                | Role  | Description                  |
|--------|----------------------------------------|-------|------------------------------|
| POST   | `/ejada/api/tasks`                     | Auth  | Create a new task            |
| PUT    | `/ejada/api/tasks/{taskId}`            | Auth  | Update an existing task      |
| DELETE | `/ejada/api/tasks/{taskId}`            | Auth  | Delete a task by ID          |
| GET    | `/ejada/api/tasks/{taskId}`            | Auth  | Get task details by ID       |
| GET    | `/ejada/api/tasks`                     | Auth  | List all tasks (with filters)|
 


# Example Requests
Add Task  
POST /ejada/api/tasks  

REQ json  
{
"title": "EJADA_TASK_10",
"status": "IN_PROGRESS",
"priority": "MEDIUM",
"description": "TASK 10 DESCRIPTION"
}

RES json  
{
"id": 11,
"title": "EJADA_TASK_10",
"description": "TASK 10 DESCRIPTION",
"status": "IN_PROGRESS",
"dueDate": "2025-08-08",
"priority": "MEDIUM",
"createdUser": "ejada_user_1"
}

Update Task  
PUT /ejada/api/tasks/1  

REQ json  
{
"title": "EJADA_TASK_1_UPDATED",
"status": "IN_PROGRESS",
"priority": "MEDIUM",
"description": "TASK 1 DESCRIPTION UPDATED"
}


## Notes
All dates follow YYYY-MM-DD format.

All requests/responses use application/json.

Filtering is supported via query parameters in /tasks.

Only authenticated users can manage tasks.

## Testing
All endpoints have been tested using:

Postman