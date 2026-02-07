# Personal Finance — Spring Boot (Java 21) + Angular

Sistema de **controle financeiro pessoal** para estudo e evolução incremental, com foco em **boas práticas**, arquitetura em camadas e autenticação moderna.

> Monorepo: `backend/` (Spring Boot) + `frontend/` (Angular)

---

## Stack

### Backend
- Java **21**
- Spring Boot
- Maven
- Virtual Threads
- Spring Data JPA (Hibernate)
- Spring Security
- Flyway
- PostgreSQL
- Sem Lombok

### Frontend (planejado)
- Angular 15
- Angular Material
- Reactive Forms

---

## Estrutura do repositório

```
personal-finance/
  backend/             # Spring Boot API
  frontend/            # Angular (a iniciar)
  docker-compose.yml   # Postgres local
  PLAN.md              # Checklist do projeto
  .gitignore
```

Dentro do backend usamos camadas:

```
backend/src/main/java/com/system/personalfinance/
  config/
  resources/
  dtos/
    auth/
    user/
    common/
  entities/
  services/
  repositories/
  exceptions/
```

---

## Pré-requisitos

- Docker + Docker Compose
- JDK 21 instalado (ex: Zulu 21)
- (Opcional) Maven global (o projeto já usa Maven Wrapper: `./mvnw`)

---

## Subir o banco (Postgres via Docker)

Na **raiz** do projeto:

```bash
docker compose up -d
docker compose ps
```

Padrão do banco (definido no `docker-compose.yml`):
- DB: `personal_finance`
- User: `finance`
- Password: `finance`
- Port: `5432`

---

## Rodar o backend

```bash
cd backend
./mvnw clean spring-boot:run
```

API por padrão em:
- `http://localhost:8080`

Health check:
- `GET http://localhost:8080/api/health`

---

## Migrations (Flyway)

Migrations ficam em:

`backend/src/main/resources/db/migration`

Ao subir a API, o Flyway aplica automaticamente as migrations.

---

## Autenticação (status atual)

✅ Implementado:
- `POST /api/auth/register` → retorna `accessToken` + `refreshToken`
- `POST /api/auth/login` → retorna `accessToken` + `refreshToken`

🚧 Em evolução:
- `POST /api/auth/refresh` (pendente)
- Proteção de rotas via JWT (pendente)

> Durante o desenvolvimento inicial, o `SecurityFilterChain` está configurado com `permitAll()` para não travar as etapas.

---

## Testes rápidos com `rest.http`

Existe um arquivo `rest.http` (recomendado manter em `backend/rest.http`) com requests para:
- Health
- Create user (dev)
- Register/Login (auth)

No IntelliJ ou VS Code você consegue executar os requests diretamente.

---

## Configurações (application.properties)

Arquivo:
`backend/src/main/resources/application.properties`

Inclui:
- Datasource Postgres
- Flyway
- Virtual threads
- Parâmetros JWT (issuer, tempos, secret)

Exemplo de configs JWT:

```properties
security.jwt.issuer=personal-finance
security.jwt.access-token-minutes=15
security.jwt.refresh-token-days=7
security.jwt.secret=CHANGE_ME_TO_A_LONG_RANDOM_SECRET_WITH_32+_CHARS

---

## Checklist do projeto

O andamento completo está em:
- `PLAN.md`

---

## Licença

Projeto de estudo.
