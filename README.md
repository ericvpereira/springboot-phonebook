# 📱 Phonebook API

> API REST para gerenciamento de contatos, desenvolvida com **Java e Spring Boot**, com autenticação e autorização utilizando **Spring Security + JWT**, persistência em **PostgreSQL**, controle de versões do banco com **Flyway** e execução containerizada com **Docker**.

---

## 🚀 Sobre o projeto

O **Phonebook API** é uma aplicação backend desenvolvida para praticar e demonstrar conceitos fundamentais do desenvolvimento de APIs REST com Java e Spring Boot.

A aplicação permite o gerenciamento de contatos e usuários, utilizando autenticação baseada em JWT e controle de acesso por perfil.

O projeto foi desenvolvido com foco em boas práticas de organização, separação de responsabilidades, validação de dados, tratamento de exceções, persistência de dados e testes automatizados.

### 🎯 Objetivos

* Desenvolver uma API REST utilizando Spring Boot;
* Aplicar arquitetura em camadas;
* Trabalhar com Spring Data JPA e Hibernate;
* Implementar autenticação utilizando JWT;
* Implementar autorização baseada em roles;
* Utilizar PostgreSQL como banco de dados;
* Gerenciar migrations utilizando Flyway;
* Criar testes automatizados;
* Documentar a API com Swagger/OpenAPI;
* Containerizar a aplicação utilizando Docker.

---

# 🛠️ Tecnologias utilizadas

### Backend

![Java](https://img.shields.io/badge/Java-17-orange?style=for-the-badge\&logo=openjdk)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.5.3-brightgreen?style=for-the-badge\&logo=springboot)
![Spring Security](https://img.shields.io/badge/Spring%20Security-6.x-brightgreen?style=for-the-badge\&logo=springsecurity)
![Hibernate](https://img.shields.io/badge/Hibernate-ORM-blue?style=for-the-badge\&logo=hibernate)

### Banco de dados

![PostgreSQL](https://img.shields.io/badge/PostgreSQL-17-blue?style=for-the-badge\&logo=postgresql)
![Flyway](https://img.shields.io/badge/Flyway-Database%20Migration-red?style=for-the-badge)

### Segurança

![JWT](https://img.shields.io/badge/JWT-Authentication-black?style=for-the-badge\&logo=jsonwebtokens)

### Testes

![JUnit](https://img.shields.io/badge/JUnit-5-green?style=for-the-badge\&logo=junit5)
![Mockito](https://img.shields.io/badge/Mockito-Testes-yellow?style=for-the-badge)

### Documentação e infraestrutura

![Swagger](https://img.shields.io/badge/Swagger-OpenAPI-85EA2D?style=for-the-badge\&logo=swagger)
![Docker](https://img.shields.io/badge/Docker-Container-blue?style=for-the-badge\&logo=docker)
![Maven](https://img.shields.io/badge/Maven-Build-red?style=for-the-badge\&logo=apachemaven)
![Git](https://img.shields.io/badge/Git-Version%20Control-orange?style=for-the-badge\&logo=git)

---

# 🏗️ Arquitetura

O projeto utiliza uma arquitetura organizada em camadas, separando responsabilidades entre controllers, services, repositories, entidades e componentes de segurança.

```text
src/main/java/com/eric/phonebook
│
├── config
│   ├── DataInitializer.java
│   ├── OpenApiConfig.java
│   └── SecurityConfig.java
│
├── controllers
│   ├── AuthController.java
│   ├── ContactController.java
│   └── UserController.java
│
├── dto
│   └── auth
│       ├── LoginRequest.java
│       ├── LoginResponse.java
│       └── RegisterRequest.java
│
├── entities
│   ├── Address.java
│   ├── BaseEntity.java
│   ├── Contact.java
│   └── User.java
│
├── enums
│   ├── ContactType.java
│   └── Role.java
│
├── exceptions
│   ├── ContactNotFoundException.java
│   ├── DatabaseException.java
│   ├── UserAlreadyExistsException.java
│   └── handlers
│       ├── GlobalExceptionHandler.java
│       └── StandardError.java
│
├── repositories
│   ├── ContactRepository.java
│   └── UserRepository.java
│
├── security
│   ├── CustomUserDetailsService.java
│   ├── JwtAuthenticationFilter.java
│   └── JwtService.java
│
└── services
    ├── AuthService.java
    └── ContactService.java
```

---

# 🔐 Segurança

A API utiliza **Spring Security + JWT** para autenticação e autorização.

O fluxo de autenticação funciona da seguinte maneira:

```text
                    ┌──────────────┐
                    │    Cliente   │
                    └──────┬───────┘
                           │
                           │ POST /auth/login
                           ▼
                    ┌──────────────┐
                    │ AuthService  │
                    └──────┬───────┘
                           │
                           ▼
                 AuthenticationManager
                           │
                           ▼
                CustomUserDetailsService
                           │
                           ▼
                    ┌──────────────┐
                    │  PostgreSQL  │
                    └──────────────┘
                           │
                           ▼
                    Credenciais OK
                           │
                           ▼
                      JwtService
                           │
                           ▼
                     JWT Token
                           │
                           ▼
                    ┌──────────────┐
                    │    Cliente   │
                    └──────┬───────┘
                           │
                     Authorization:
                     Bearer <token>
                           │
                           ▼
                JwtAuthenticationFilter
                           │
                           ▼
                    Endpoint protegido
```

## 👤 Roles

A aplicação possui dois níveis de acesso:

### USER

Usuários comuns podem acessar os endpoints protegidos de contatos.

### ADMIN

Administradores possuem permissões adicionais, incluindo acesso aos endpoints de usuários.

As regras são configuradas através do Spring Security.

---

# 🔑 Autenticação

## Registrar usuário

```http
POST /auth/register
Content-Type: application/json
```

Exemplo:

```json
{
  "username": "usuario",
  "password": "123456"
}
```

---

## Login

```http
POST /auth/login
Content-Type: application/json
```

Exemplo:

```json
{
  "username": "usuario",
  "password": "123456"
}
```

Resposta:

```json
{
  "token": "eyJhbGciOiJIUz..."
}
```

O token deve ser enviado nas requisições protegidas:

```http
Authorization: Bearer <JWT>
```

---

# 📡 Endpoints principais

## 🔓 Autenticação

| Método | Endpoint         | Acesso  |
| ------ | ---------------- | ------- |
| POST   | `/auth/register` | Público |
| POST   | `/auth/login`    | Público |

---

## 📇 Contatos

| Método | Endpoint         | Acesso      |
| ------ | ---------------- | ----------- |
| GET    | `/contacts`      | Autenticado |
| GET    | `/contacts/{id}` | Autenticado |
| POST   | `/contacts`      | Autenticado |
| PUT    | `/contacts/{id}` | Autenticado |
| DELETE | `/contacts/{id}` | Autenticado |

> Os endpoints disponíveis podem variar de acordo com a implementação atual do controller.

---

## 👥 Usuários

| Método | Endpoint    | Acesso |
| ------ | ----------- | ------ |
| GET    | `/users/**` | ADMIN  |

---

# 🚦 Tratamento de respostas HTTP

A API utiliza códigos HTTP apropriados para representar o resultado das operações.

|             Código | Situação                            |
| -----------------: | ----------------------------------- |
|           `200 OK` | Requisição processada com sucesso   |
|      `201 Created` | Recurso criado                      |
|  `400 Bad Request` | Dados inválidos                     |
| `401 Unauthorized` | Usuário não autenticado             |
|    `403 Forbidden` | Usuário autenticado sem permissão   |
|    `404 Not Found` | Recurso não encontrado              |
|     `409 Conflict` | Conflito, como usuário já existente |

Exemplo de conflito:

```json
{
  "timestamp": "2026-08-29T22:20:00",
  "status": 409,
  "message": "Usuário já existe"
}
```

---

# 🧩 Tratamento global de exceções

As exceções da aplicação são centralizadas através de `@ControllerAdvice`.

Entre os cenários tratados estão:

* `ContactNotFoundException`
* `UserAlreadyExistsException`
* Erros de validação
* Respostas padronizadas da API

Isso evita que a aplicação retorne respostas inconsistentes para diferentes erros.

---

# 🗄️ Banco de dados

O projeto utiliza:

**PostgreSQL 17**

A persistência é realizada através de:

* Spring Data JPA
* Hibernate
* PostgreSQL Driver

O Hibernate está configurado para validar a estrutura do banco:

```properties
spring.jpa.hibernate.ddl-auto=validate
```

A criação e alteração das tabelas é responsabilidade do **Flyway**.

---

# 🛫 Flyway

As alterações do banco são controladas através de migrations versionadas.

Exemplo:

```text
src/main/resources/db/migration/

V1__...
V2__...
V3__...
...
V8__...
```

Na inicialização da aplicação, o Flyway verifica as migrations disponíveis e aplica somente aquelas que ainda não foram executadas.

Isso permite manter o banco sincronizado com o código da aplicação.

---

# 🐳 Docker

A aplicação pode ser executada utilizando Docker Compose.

A arquitetura de containers é:

```text
┌─────────────────────────────┐
│       Docker Compose        │
│                             │
│  ┌───────────────────────┐  │
│  │    phonebook-api      │  │
│  │    Spring Boot        │  │
│  │    Port: 8080         │  │
│  └───────────┬───────────┘  │
│              │              │
│              ▼              │
│  ┌───────────────────────┐  │
│  │     phonebook-db      │  │
│  │     PostgreSQL 17     │  │
│  │     Port: 5432        │  │
│  └───────────────────────┘  │
│                             │
└─────────────────────────────┘
```

## Executando com Docker

Clone o projeto:

```bash
git clone https://github.com/ericvpereira/springboot-phonebook.git
```

Entre no diretório:

```bash
cd springboot-phonebook
```

Execute:

```bash
docker compose up --build
```

A aplicação ficará disponível em:

```text
http://localhost:8080
```

Para executar em segundo plano:

```bash
docker compose up -d --build
```

Para parar os containers:

```bash
docker compose down
```

---

# 📚 Swagger / OpenAPI

A API possui documentação através do **Swagger/OpenAPI**.

Após iniciar a aplicação, acesse:

```text
http://localhost:8080/swagger-ui/index.html
```

O Swagger permite:

* Visualizar os endpoints;
* Consultar parâmetros;
* Enviar requisições;
* Testar autenticação;
* Testar endpoints protegidos;
* Visualizar modelos de request/response.

Para endpoints protegidos, utilize o botão **Authorize** e informe:

```text
Bearer <JWT>
```

---

# 🧪 Testes

O projeto possui testes automatizados utilizando JUnit e ferramentas do ecossistema Spring.

Resultado atual da suíte:

```text
Tests run: 34
Failures: 0
Errors: 0
Skipped: 0

BUILD SUCCESS
```

Os testes abrangem componentes importantes da aplicação, incluindo:

* Services;
* Segurança;
* JWT;
* Autenticação;
* Regras de negócio.

---

# ▶️ Executando localmente

## Pré-requisitos

Para executar sem Docker, é necessário possuir:

* Java 17+
* Maven ou Maven Wrapper
* PostgreSQL

Clone o projeto:

```bash
git clone https://github.com/ericvpereira/springboot-phonebook.git
```

Entre no diretório:

```bash
cd springboot-phonebook
```

Execute os testes:

### Windows

```powershell
.\mvnw clean test
```

### Linux/macOS

```bash
./mvnw clean test
```

Execute a aplicação:

### Windows

```powershell
.\mvnw spring-boot:run
```

### Linux/macOS

```bash
./mvnw spring-boot:run
```

---

# 🔧 Configuração

As configurações sensíveis devem ser fornecidas através de variáveis de ambiente.

Exemplo:

```env
POSTGRES_DB=phonebook
POSTGRES_USER=postgres
POSTGRES_PASSWORD=sua_senha

JWT_SECRET=sua_chave_secreta
JWT_EXPIRATION=3600000
```

> ⚠️ Não coloque senhas, tokens ou chaves JWT reais no GitHub.

---

# 📂 Estrutura de camadas

O projeto segue uma separação de responsabilidades:

```text
Controller
    ↓
Service
    ↓
Repository
    ↓
Database
```

### Controller

Responsável por receber as requisições HTTP e retornar as respostas.

### Service

Responsável pelas regras de negócio.

### Repository

Responsável pelo acesso aos dados através do Spring Data JPA.

### Entity

Representa as entidades persistidas no banco de dados.

### DTO

Responsável por transportar dados entre cliente e aplicação sem expor diretamente as entidades.

### Security

Responsável por:

* autenticação;
* geração de JWT;
* validação do token;
* carregamento dos usuários;
* autorização.

### Exceptions

Centraliza as exceções e respostas de erro da API.

---

# 📈 Possíveis melhorias futuras

Algumas funcionalidades podem ser adicionadas futuramente:

* Paginação de contatos;
* Ordenação e filtros avançados;
* Refresh Token;
* Rate Limiting;
* Auditoria de operações;
* Testes de integração adicionais;
* CI/CD com GitHub Actions;
* Monitoramento com Spring Boot Actuator;
* Logs estruturados;
* Deploy em ambiente cloud.

Essas funcionalidades não são necessárias para o funcionamento atual da API, mas representam possíveis evoluções do projeto.

---

# 🎯 O que este projeto demonstra

Este projeto demonstra conhecimentos práticos em:

```text
Java
 │
 ├── POO
 ├── Collections
 ├── Exceptions
 └── Boas práticas

Spring Boot
 │
 ├── REST API
 ├── Dependency Injection
 ├── Validation
 └── Configuration

Spring Data
 │
 ├── JPA
 ├── Hibernate
 └── Repositories

Security
 │
 ├── Spring Security
 ├── Authentication
 ├── Authorization
 ├── Roles
 └── JWT

Database
 │
 ├── PostgreSQL
 └── Flyway

DevOps
 │
 ├── Docker
 └── Docker Compose

Testing
 │
 ├── JUnit
 ├── Mockito
 └── Spring Security Test

Documentation
 │
 └── Swagger / OpenAPI
```

---

# 👨‍💻 Autor

**Eric Vieira Pereira**

Desenvolvedor com foco em **Java Backend** e desenvolvimento de APIs REST utilizando o ecossistema Spring.

### Tecnologias de interesse

```text
Java
Spring Boot
Spring Security
JPA / Hibernate
PostgreSQL
Docker
REST API
JWT
Git
```

---

## 📌 Projeto

**Phonebook API**

🔗 GitHub:

https://github.com/ericvpereira/springboot-phonebook

---

⭐ Se este projeto foi útil ou interessante, considere deixar uma estrela no repositório.

---

### 📄 Licença

Este projeto foi desenvolvido para fins de estudo, prática e portfólio.
