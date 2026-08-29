# 📱 Phonebook API

> **API REST para gerenciamento de contatos, desenvolvida com Java e Spring Boot, com autenticação JWT, autorização por perfil, PostgreSQL, Flyway e Docker.**

![Java](https://img.shields.io/badge/Java-17-orange?style=for-the-badge\&logo=openjdk)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.5.3-6DB33F?style=for-the-badge\&logo=springboot)
![Spring Security](https://img.shields.io/badge/Spring%20Security-JWT-6DB33F?style=for-the-badge\&logo=springsecurity)
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-17-336791?style=for-the-badge\&logo=postgresql)
![Docker](https://img.shields.io/badge/Docker-Compose-2496ED?style=for-the-badge\&logo=docker)
![Maven](https://img.shields.io/badge/Maven-Build-C71A36?style=for-the-badge\&logo=apachemaven)

---

## 📋 Sobre o projeto

O **Phonebook API** é uma aplicação backend desenvolvida para praticar e demonstrar conceitos fundamentais do desenvolvimento de APIs REST com Java e Spring Boot.

A aplicação permite o gerenciamento de contatos e usuários, utilizando autenticação baseada em **JWT (JSON Web Token)** e autorização baseada em roles.

O projeto foi desenvolvido com foco em:

* arquitetura em camadas;
* boas práticas de desenvolvimento backend;
* segurança de APIs REST;
* persistência de dados;
* versionamento de banco de dados;
* tratamento global de exceções;
* testes automatizados;
* containerização com Docker.

---

## 🚀 Funcionalidades

### 🔐 Autenticação e autorização

* Cadastro de usuários
* Login com usuário e senha
* Geração de token JWT
* Validação de JWT
* Senhas armazenadas utilizando BCrypt
* Autorização baseada em roles
* Perfil `USER`
* Perfil `ADMIN`
* Proteção dos endpoints da API
* Retorno `401 Unauthorized` para usuários não autenticados
* Retorno `403 Forbidden` para usuários sem permissão

### 📇 Contatos

* Criar contato
* Buscar contato por ID
* Listar contatos
* Atualizar contato
* Excluir contato
* Busca por nome
* Busca por e-mail
* Associação de endereço
* Validação dos dados recebidos

### 🛡️ Tratamento de exceções

A aplicação possui tratamento global de exceções para retornar respostas HTTP padronizadas.

Exemplos:

```text
400 Bad Request
401 Unauthorized
403 Forbidden
404 Not Found
409 Conflict
```

---

## 🏗️ Arquitetura

O projeto utiliza uma arquitetura organizada em camadas:

```text
src
└── main
    └── java
        └── com.eric.phonebook
            ├── config
            ├── controllers
            ├── dto
            ├── entities
            ├── enums
            ├── exceptions
            ├── repositories
            ├── security
            └── services
```

### Principais responsabilidades

| Camada         | Responsabilidade                     |
| -------------- | ------------------------------------ |
| `controllers`  | Receber e responder requisições HTTP |
| `services`     | Regras de negócio                    |
| `repositories` | Acesso aos dados                     |
| `entities`     | Entidades JPA                        |
| `dto`          | Transferência de dados               |
| `security`     | Autenticação e JWT                   |
| `exceptions`   | Tratamento de exceções               |
| `config`       | Configurações da aplicação           |

---

## 🔑 Segurança

A API utiliza **Spring Security + JWT** para autenticação e autorização.

Fluxo simplificado:

```text
Cliente
   │
   │ POST /auth/login
   ▼
AuthController
   │
   ▼
AuthService
   │
   ▼
AuthenticationManager
   │
   ▼
UserDetailsService
   │
   ▼
PostgreSQL
   │
   ▼
JWT Token
```

Depois do login:

```text
Authorization: Bearer <JWT>
```

O token é interceptado pelo:

```text
JwtAuthenticationFilter
```

que valida o token e estabelece a autenticação do usuário.

---

## 👥 Controle de acesso

A aplicação possui dois perfis:

```text
USER
ADMIN
```

Exemplo de autorização:

```text
/auth/**       → Público

/contacts/**   → USER ou ADMIN

/users/**      → ADMIN

/outros        → Autenticado
```

### Respostas de segurança

```text
Sem JWT
   ↓
401 Unauthorized

JWT válido + permissão
   ↓
200 OK

JWT válido + sem permissão
   ↓
403 Forbidden
```

---

## 🗄️ Banco de dados

O projeto utiliza:

**PostgreSQL 17**

O versionamento do banco é realizado através do:

**Flyway**

As alterações estruturais do banco são controladas por migrations.

Exemplo:

```text
db/migration
├── V1__...
├── V2__...
├── V3__...
├── ...
└── V8__...
```

Durante a inicialização, o Flyway verifica automaticamente o estado do banco e executa as migrations pendentes.

---

## 🐳 Docker

A aplicação pode ser executada utilizando Docker Compose.

Arquitetura:

```text
┌──────────────────────────────┐
│       Docker Compose         │
│                              │
│  ┌────────────────────────┐  │
│  │    phonebook-api       │  │
│  │    Spring Boot         │  │
│  │    Port 8080           │  │
│  └───────────┬────────────┘  │
│              │               │
│              ▼               │
│  ┌────────────────────────┐  │
│  │     PostgreSQL 17      │  │
│  │     Port 5432          │  │
│  └────────────────────────┘  │
└──────────────────────────────┘
```

### Executar o projeto

Clone o repositório:

```bash
git clone https://github.com/ericvpereira/springboot-phonebook.git
```

Entre na pasta:

```bash
cd springboot-phonebook
```

Configure o arquivo `.env` com suas credenciais.

Depois execute:

```bash
docker compose up --build
```

A API estará disponível em:

```text
http://localhost:8080
```

Para parar os containers:

```bash
docker compose down
```

---

## ⚙️ Executando sem Docker

É possível executar a aplicação utilizando Maven.

No Windows:

```powershell
.\mvnw clean package
```

Executar os testes:

```powershell
.\mvnw clean test
```

Executar a aplicação:

```powershell
.\mvnw spring-boot:run
```

---

## 🧪 Testes

O projeto possui testes automatizados utilizando:

* JUnit 5
* Mockito
* Spring Boot Test
* Spring Security Test
* MockMvc
* H2 para cenários de teste

Atualmente:

```text
34 testes
0 falhas
0 erros
0 ignorados
```

Resultado:

```text
BUILD SUCCESS
```

Os testes cobrem diferentes partes da aplicação, incluindo:

* Services
* Controllers
* autenticação
* JWT
* Spring Security
* autorização
* tratamento de acesso
* regras de negócio

---

## 🔎 Exemplos de endpoints

### 🔐 Registrar usuário

```http
POST /auth/register
```

Exemplo:

```json
{
  "username": "usuario",
  "password": "123456"
}
```

---

### 🔑 Login

```http
POST /auth/login
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
  "token": "JWT_TOKEN"
}
```

---

### 📇 Listar contatos

```http
GET /contacts
```

Necessário:

```http
Authorization: Bearer JWT_TOKEN
```

---

### ➕ Criar contato

```http
POST /contacts
```

Necessário:

```http
Authorization: Bearer JWT_TOKEN
```

---

### 🔄 Atualizar contato

```http
PUT /contacts/{id}
```

Necessário:

```http
Authorization: Bearer JWT_TOKEN
```

---

### ❌ Excluir contato

```http
DELETE /contacts/{id}
```

Necessário:

```http
Authorization: Bearer JWT_TOKEN
```

---

## 📚 Documentação da API

A aplicação utiliza **OpenAPI / Swagger** para documentação dos endpoints.

Com a aplicação executando, acesse:

```text
http://localhost:8080/swagger-ui/index.html
```

A documentação permite visualizar e testar os endpoints da API.

---

## 🧰 Tecnologias utilizadas

### Backend

* Java 17
* Spring Boot 3.5.3
* Spring Web
* Spring Data JPA
* Hibernate
* Spring Validation
* Spring Security
* JWT

### Banco de dados

* PostgreSQL 17
* Flyway

### Testes

* JUnit 5
* Mockito
* Spring Boot Test
* Spring Security Test
* MockMvc
* H2

### Infraestrutura

* Docker
* Docker Compose
* Maven

### Documentação

* OpenAPI
* Swagger UI

---

## 📂 Estrutura do projeto

```text
phonebook/
│
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/eric/phonebook/
│   │   │       ├── config/
│   │   │       ├── controllers/
│   │   │       ├── dto/
│   │   │       ├── entities/
│   │   │       ├── enums/
│   │   │       ├── exceptions/
│   │   │       ├── repositories/
│   │   │       ├── security/
│   │   │       └── services/
│   │   │
│   │   └── resources/
│   │       ├── db/
│   │       │   └── migration/
│   │       └── application.properties
│   │
│   └── test/
│
├── .env.example
├── .gitignore
├── docker-compose.yml
├── Dockerfile
├── mvnw
├── mvnw.cmd
├── pom.xml
└── README.md
```

---

## 🔒 Variáveis de ambiente

Informações sensíveis não devem ser versionadas.

Utilize:

```text
.env
```

para as configurações locais.

O projeto disponibiliza:

```text
.env.example
```

como modelo das variáveis necessárias.

Exemplo:

```env
POSTGRES_DB=phonebook
POSTGRES_USER=postgres
POSTGRES_PASSWORD=sua_senha

SPRING_DATASOURCE_URL=jdbc:postgresql://postgres:5432/phonebook
SPRING_DATASOURCE_USERNAME=postgres
SPRING_DATASOURCE_PASSWORD=sua_senha

SECURITY_JWT_SECRET=sua_chave_secreta
SECURITY_JWT_EXPIRATION=86400000
```

> ⚠️ Nunca publique senhas, tokens ou chaves JWT reais no GitHub.

---

## 📈 Objetivos de aprendizado

Este projeto foi desenvolvido para consolidar conhecimentos em:

```text
Java
  ↓
Spring Boot
  ↓
REST API
  ↓
JPA / Hibernate
  ↓
PostgreSQL
  ↓
Flyway
  ↓
Spring Security
  ↓
JWT
  ↓
Testes
  ↓
Docker
```

---

## 🚧 Possíveis evoluções

Algumas melhorias podem ser adicionadas futuramente:

* Paginação de contatos
* Ordenação e filtros avançados
* Refresh Token
* Rate limiting
* Cache
* CI/CD com GitHub Actions
* Monitoramento e observabilidade
* Testcontainers para testes com PostgreSQL real
* Deploy em cloud

Essas funcionalidades não são necessárias para o funcionamento atual da aplicação.

---

## 👨‍💻 Autor

**Eric Vieira Pereira**

Desenvolvedor Backend Java em formação, com foco em:

```text
Java
Spring Boot
Spring Security
APIs REST
PostgreSQL
Docker
Testes
```

### GitHub

https://github.com/ericvpereira

---

## ⭐ Considerações finais

Este projeto representa minha evolução no desenvolvimento backend utilizando o ecossistema Java e Spring.

O objetivo principal foi construir uma API REST completa, aplicando conceitos utilizados em aplicações reais, como:

* autenticação;
* autorização;
* persistência;
* validação;
* tratamento de exceções;
* migrations;
* testes automatizados;
* containerização;
* documentação de API.

Se este projeto foi útil ou interessante, considere deixar uma ⭐ no repositório.

---

<p align="center">
  <strong>☕ Java • Spring Boot • PostgreSQL • Docker • JWT</strong>
</p>
