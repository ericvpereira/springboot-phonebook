# ⚡ PHONEBOOK API // BACKEND SYSTEM

<p align="center">
  <img src="https://img.shields.io/badge/JAVA-17-00ff9d?style=for-the-badge&logo=openjdk&logoColor=white"/>
  <img src="https://img.shields.io/badge/SPRING_BOOT-3.5.3-00ff9d?style=for-the-badge&logo=springboot&logoColor=white"/>
  <img src="https://img.shields.io/badge/POSTGRESQL-17-00d9ff?style=for-the-badge&logo=postgresql&logoColor=white"/>
  <img src="https://img.shields.io/badge/JWT-AUTH-ff00ff?style=for-the-badge&logo=jsonwebtokens&logoColor=white"/>
  <img src="https://img.shields.io/badge/DOCKER-READY-00d9ff?style=for-the-badge&logo=docker&logoColor=white"/>
</p>

<p align="center">
  <strong>SECURE REST API FOR CONTACT MANAGEMENT</strong>
</p>

<p align="center">
  <code>JAVA BACKEND // SPRING SECURITY // JWT // JPA // POSTGRESQL // DOCKER</code>
</p>

---

## 🧬 SYSTEM OVERVIEW

**Phonebook API** é uma aplicação backend REST desenvolvida em Java com Spring Boot para gerenciamento de contatos.

O projeto foi construído com foco em práticas utilizadas no desenvolvimento de APIs profissionais:

* arquitetura em camadas
* RESTful API
* DTOs
* validação de dados
* persistência com JPA/Hibernate
* PostgreSQL
* migrations com Flyway
* autenticação com JWT
* autorização baseada em roles
* tratamento global de exceções
* testes automatizados
* documentação OpenAPI/Swagger
* containerização com Docker

---

## ⚙️ TECH STACK

| Tecnologia           | Utilização                    |
| -------------------- | ----------------------------- |
| ☕ Java 17            | Linguagem principal           |
| 🌱 Spring Boot 3.5.3 | Framework backend             |
| 🔐 Spring Security   | Autenticação e autorização    |
| 🎫 JWT               | Autenticação stateless        |
| 🗄️ Spring Data JPA  | Persistência                  |
| 🐘 PostgreSQL        | Banco de dados                |
| 🛫 Flyway            | Versionamento do banco        |
| 📦 Maven             | Gerenciamento de dependências |
| 🐳 Docker            | Containerização               |
| 📚 OpenAPI / Swagger | Documentação da API           |
| 🧪 JUnit / Mockito   | Testes automatizados          |
| 🔒 BCrypt            | Criptografia de senhas        |

---

# 🏗️ ARCHITECTURE

```text
                    ┌─────────────────────┐
                    │       CLIENT        │
                    │ Postman / Swagger   │
                    └──────────┬──────────┘
                               │
                               ▼
                    ┌─────────────────────┐
                    │    REST CONTROLLER  │
                    └──────────┬──────────┘
                               │
                               ▼
                    ┌─────────────────────┐
                    │       SERVICE       │
                    │ Business Logic      │
                    └──────────┬──────────┘
                               │
                               ▼
                    ┌─────────────────────┐
                    │     REPOSITORY      │
                    │   Spring Data JPA   │
                    └──────────┬──────────┘
                               │
                               ▼
                    ┌─────────────────────┐
                    │     POSTGRESQL      │
                    └─────────────────────┘
```

### Security Flow

```text
LOGIN
  │
  ▼
username + password
  │
  ▼
AuthenticationManager
  │
  ▼
BCrypt password validation
  │
  ▼
JWT generated
  │
  ▼
Authorization: Bearer <token>
  │
  ▼
JwtAuthenticationFilter
  │
  ▼
SecurityContext
  │
  ▼
Protected endpoint
```

---

# 🔐 SECURITY

A API utiliza **Spring Security + JWT** para autenticação stateless.

### Public endpoints

```http
POST /auth/register
POST /auth/login
```

### Authenticated endpoints

```http
GET    /contacts
GET    /contacts/{id}
POST   /contacts
PUT    /contacts/{id}
DELETE /contacts/{id}
```

### ADMIN endpoints

```http
/users/**
```

A autorização é baseada em roles:

```text
USER
 └── acesso aos contatos

ADMIN
 ├── acesso aos contatos
 └── acesso aos usuários
```

### HTTP Security

```text
401 Unauthorized
→ usuário não autenticado

403 Forbidden
→ usuário autenticado sem permissão

409 Conflict
→ recurso duplicado

404 Not Found
→ recurso inexistente

400 Bad Request
→ dados inválidos
```

---

# 👤 AUTHENTICATION

## Register

```http
POST /auth/register
Content-Type: application/json
```

```json
{
  "username": "usuario",
  "password": "senha123"
}
```

A senha é armazenada utilizando **BCrypt**.

---

## Login

```http
POST /auth/login
Content-Type: application/json
```

```json
{
  "username": "usuario",
  "password": "senha123"
}
```

Resposta:

```json
{
  "token": "JWT_TOKEN"
}
```

Utilização:

```http
Authorization: Bearer JWT_TOKEN
```

---

# 📇 CONTACT MANAGEMENT

A API permite:

```text
CREATE
  ↓
READ
  ↓
UPDATE
  ↓
DELETE
```

Operações disponíveis:

```http
GET    /contacts
GET    /contacts/{id}
POST   /contacts
PUT    /contacts/{id}
DELETE /contacts/{id}
```

---

# 🧱 DATA MODEL

```text
┌──────────────────────┐
│        USERS         │
├──────────────────────┤
│ id                   │
│ username             │
│ password             │
│ role                 │
│ created_at           │
│ updated_at           │
└──────────────────────┘

          │

          │ authenticated user

          ▼

┌──────────────────────┐
│       CONTACTS       │
├──────────────────────┤
│ id                   │
│ contact_name         │
│ phone_number         │
│ email                │
│ type                 │
│ address              │
│ created_at           │
│ updated_at            │
└──────────────────────┘
```

---

# 🛫 DATABASE MIGRATIONS

O banco de dados utiliza **Flyway** para versionamento das alterações.

Exemplo:

```text
db/migration/

V1__create_contacts.sql
V2__create_addresses.sql
V3__create_users.sql
V4__...
V5__...
```

Isso permite manter o schema do banco versionado junto ao código da aplicação.

---

# 🚨 EXCEPTION HANDLING

A aplicação possui tratamento centralizado de exceções.

Exemplos:

```text
ContactNotFoundException
UserAlreadyExistsException
MethodArgumentNotValidException
DatabaseException
```

As respostas seguem uma estrutura padronizada.

Exemplo:

```json
{
  "timestamp": "2026-08-29T23:00:00",
  "status": 409,
  "error": "Usuário já existe"
}
```

---

# 🧪 TESTING

O projeto possui testes unitários e de integração cobrindo diferentes camadas da aplicação.

### Resultado atual

```text
========================================

        TEST SUITE

        Tests run: 34
        Failures: 0
        Errors: 0
        Skipped: 0

        STATUS: PASS

========================================
```

Os testes abrangem funcionalidades como:

```text
✓ Contact Service
✓ Authentication
✓ JWT Service
✓ JWT Filter
✓ UserDetailsService
✓ Security configuration
✓ Contact authorization
✓ Authentication / Authorization
✓ Application context
```

Cenários de segurança testados:

```text
✓ acesso sem JWT → 401
✓ usuário autenticado → acesso permitido
✓ usuário sem ROLE adequada → 403
✓ autenticação com JWT
```

---

# 🐳 DOCKER

A aplicação pode ser executada utilizando Docker Compose.

Arquitetura:

```text
┌─────────────────────┐
│    PHONEBOOK API    │
│      :8080          │
└──────────┬──────────┘
           │
           │ JDBC
           ▼
┌─────────────────────┐
│     POSTGRESQL      │
│       :5432         │
└─────────────────────┘
```

### Inicialização

```bash
docker compose up --build
```

### Executar em background

```bash
docker compose up -d --build
```

### Ver logs

```bash
docker compose logs -f phonebook-api
```

### Parar containers

```bash
docker compose down
```

---

# 🔧 CONFIGURATION

Variáveis sensíveis devem ser mantidas fora do código-fonte.

Exemplo:

```env
POSTGRES_DB=phonebook
POSTGRES_USER=postgres
POSTGRES_PASSWORD=your_password

SPRING_DATASOURCE_URL=jdbc:postgresql://postgres:5432/phonebook
SPRING_DATASOURCE_USERNAME=postgres
SPRING_DATASOURCE_PASSWORD=your_password

SECURITY_JWT_SECRET=your_secret
SECURITY_JWT_EXPIRATION=86400000
```

O arquivo `.env` **não deve ser versionado**.

Para configuração de referência:

```text
.env.example
```

---

# 📚 API DOCUMENTATION

A API utiliza OpenAPI/Swagger para documentação dos endpoints.

Após iniciar a aplicação:

```text
/swagger-ui/index.html
```

Documentação OpenAPI:

```text
/v3/api-docs
```

---

# 📁 PROJECT STRUCTURE

```text
src/
├── main/
│   ├── java/
│   │   └── com/eric/phonebook/
│   │       ├── config/
│   │       ├── controllers/
│   │       ├── dto/
│   │       ├── entities/
│   │       ├── enums/
│   │       ├── exceptions/
│   │       ├── repositories/
│   │       ├── security/
│   │       └── services/
│   │
│   └── resources/
│       ├── db/
│       │   └── migration/
│       └── application.properties
│
└── test/
    └── java/
        └── com/eric/phonebook/
```

---

# 🚀 RUN LOCALLY

### 1. Clone

```bash
git clone https://github.com/ericvpereira/springboot-phonebook.git
```

### 2. Enter project

```bash
cd springboot-phonebook
```

### 3. Configure environment

```text
.env.example → .env
```

Configure suas credenciais locais.

### 4. Run tests

Windows:

```bash
.\mvnw.cmd clean test
```

Linux/macOS:

```bash
./mvnw clean test
```

### 5. Start application

```bash
.\mvnw.cmd spring-boot:run
```

Ou:

```bash
docker compose up --build
```

---

# 📡 API FLOW

```text
                    ┌──────────────┐
                    │    CLIENT    │
                    └──────┬───────┘
                           │
                    POST /auth/login
                           │
                           ▼
                  ┌──────────────────┐
                  │  SPRING SECURITY │
                  └────────┬─────────┘
                           │
                           ▼
                     JWT GENERATED
                           │
                           ▼
                  Authorization Header
                           │
                           ▼
                ┌─────────────────────┐
                │ JWT AUTHENTICATION  │
                │       FILTER        │
                └──────────┬──────────┘
                           │
                           ▼
                   SECURITY CONTEXT
                           │
                           ▼
                  ┌─────────────────┐
                  │ CONTACT SERVICE │
                  └────────┬────────┘
                           │
                           ▼
                    ┌────────────┐
                    │ POSTGRESQL │
                    └────────────┘
```

---

# 🧠 ENGINEERING PRACTICES

O projeto aplica conceitos importantes de desenvolvimento backend:

```text
✓ SOLID
✓ Separation of Concerns
✓ DTO Pattern
✓ Layered Architecture
✓ Dependency Injection
✓ REST principles
✓ Authentication / Authorization
✓ Password Hashing
✓ Database Migration
✓ Exception Handling
✓ Automated Testing
✓ Environment Variables
✓ Containerization
```

---

# 📈 PROJECT STATUS

```text
[████████████████████] 100%

CORE API              ✓
CRUD CONTACTS         ✓
POSTGRESQL            ✓
JPA / HIBERNATE       ✓
FLYWAY                ✓
JWT                   ✓
SPRING SECURITY       ✓
ROLE AUTHORIZATION    ✓
EXCEPTION HANDLING    ✓
VALIDATION            ✓
SWAGGER               ✓
DOCKER                ✓
AUTOMATED TESTS       ✓
```

---

# 🎯 PURPOSE

Este projeto foi desenvolvido como parte da minha evolução profissional como **Java Backend Developer**, com foco em construir uma API próxima de cenários encontrados em aplicações reais.

O objetivo não foi apenas implementar um CRUD, mas evoluir a aplicação adicionando:

```text
CRUD
 ↓
DATABASE
 ↓
VALIDATION
 ↓
EXCEPTION HANDLING
 ↓
SECURITY
 ↓
JWT
 ↓
ROLE AUTHORIZATION
 ↓
TESTING
 ↓
DOCKER
 ↓
DOCUMENTATION
```

---

# 👨‍💻 AUTHOR

## Eric Vieira

**Java Backend Developer**

Foco atual:

```text
Java
Spring Boot
Spring Security
REST APIs
JPA / Hibernate
PostgreSQL
Docker
Testing
Backend Architecture
```

---

<p align="center">

### ⚡ SYSTEM ONLINE

```text
JAVA BACKEND // BUILD // TEST // DEPLOY
```

**Built with Java + Spring Boot**

</p>
