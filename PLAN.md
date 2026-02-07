# Personal Finance (Spring Boot + Angular) — Plano de Execução (Checklist)

Objetivo: construir um sistema de controle financeiro pessoal, começando simples e evoluindo.
Foco: aprender arquitetura, boas práticas e fluxo completo (API + Auth + UI + Docker).

> **Status atualizado:** até aqui você já tem Postgres via Docker, Spring Boot (Java 21 + Maven + Virtual Threads + application.properties), Flyway rodando, endpoint `/api/health`, camadas básicas, User + Auth (register/login) retornando tokens, e arquivo `rest.http`.

---

## Padrões obrigatórios (backend)
- [x] Java 21
- [x] Spring Boot (Servlet / MVC)
- [x] Virtual Threads habilitadas
- [x] Maven
- [x] `application.properties` (não usar yml)
- [x] Sem Lombok
- [x] Arquitetura: resources, dtos, entities, services, repositories
- [x] PostgreSQL via Docker
- [x] Migrations com Flyway
- [ ] Auth completo: JWT (access + refresh + endpoint refresh + proteção de rotas)
- [ ] OpenAPI/Swagger
- [x] Monorepo com `backend/` e `frontend/`

## Padrões sugeridos (frontend)
- [ ] Angular 15
- [ ] UI: Angular Material (tables, filtros, dashboard)
- [ ] Reactive Forms
- [ ] Auth: interceptor + guard + refresh flow
- [ ] Docker para rodar o front em dev (opcional) e build em prod

---

# Visão geral das etapas
- Etapa 0: Setup do monorepo e Docker base
- Etapa 1: Backend base (Spring Boot + Postgres + Flyway)
- Etapa 2: Camadas + User (Entity/Repository/Service) + endpoint dev
- Etapa 3: Auth (JWT access/refresh) — register/login
- Etapa 4: Refresh token endpoint + rotação
- Etapa 5: Proteger rotas com JWT (SecurityFilterChain + filtro)
- Etapa 6: CRUDs essenciais (Accounts, Categories, Transactions)
- Etapa 7: Filtros + paginação + ordenação
- Etapa 8: Dashboard (cards + gráfico + “a vencer”)
- Etapa 9: Frontend Angular (Auth + Tabelas + Dashboard)
- Etapa 10+: Evoluções (recorrência, tags, budgets, import CSV, relatórios)

---

# Etapa 0 — Estrutura + Docker (Concluída)
## 0.1 Estrutura de pastas
- [x] Criar estrutura:
  - [x] `backend/` (Spring Boot)
  - [x] `frontend/` (Angular — ainda não iniciado)
  - [x] `docker-compose.yml`
  - [x] `PLAN.md`
  - [x] `.gitignore`

## 0.2 Docker Compose (Postgres)
- [x] Criar `docker-compose.yml` com:
  - [x] postgres
  - [x] volume para persistência
  - [x] porta exposta (5432)
  - [x] variáveis (POSTGRES_DB, POSTGRES_USER, POSTGRES_PASSWORD)
- [x] Subir: `docker compose up -d`
- [x] Validar Postgres acessível

✅ Stop Point Etapa 0: concluído.

---

# Etapa 1 — Backend Base (Concluída)
## 1.1 Criar projeto Spring Boot (Maven / Java 21)
- [x] Projeto criado em `backend/` com dependências base (Web, JPA, Security, Validation, Flyway, Postgres)
- [x] Maven Wrapper funcionando (`./mvnw -v`)

## 1.2 Config `application.properties`
- [x] datasource postgres
- [x] jpa (ddl-auto none)
- [x] flyway enabled + locations
- [x] server.port

## 1.3 Virtual Threads (Java 21)
- [x] `spring.threads.virtual.enabled=true`

## 1.4 Flyway “primeira migration”
- [x] `V1__init.sql` (tabela `users`)
- [x] Flyway aplicou migrations com sucesso (schema public em v1)

## 1.5 Health endpoint + Security provisório
- [x] `GET /api/health` => `{ "status": "ok" }`
- [x] `SecurityFilterChain` provisório com `permitAll()` (para não travar o desenvolvimento)

✅ Stop Point Etapa 1: concluído.

---

# Etapa 2 — Camadas + User “dev-create” (Concluída)
## 2.1 Camadas (padrão)
- [x] `resources/`
- [x] `dtos/` (organizado por feature: `dtos/auth`, `dtos/user`, `dtos/common`)
- [x] `entities/`
- [x] `services/`
- [x] `repositories/`

## 2.2 User Entity + Repository
- [x] `User` entity mapeada para tabela `users`
- [x] `UserRepository` com `findByEmail` e `existsByEmail`

## 2.3 UserService + endpoint temporário
- [x] `PasswordEncoder` (BCrypt)
- [x] `UserService.create(...)`
- [x] `POST /api/users/dev-create` (temporário) retornando `UserResponse` (DTO)
- [x] Arquivo `rest.http` com testes básicos

✅ Stop Point Etapa 2: concluído.

---

# Etapa 3 — Auth (Register/Login) com JWT (Concluída)
## 3.1 Banco / entidades do refresh
- [x] Migration `V2__refresh_tokens.sql`
- [x] Entity `RefreshToken`
- [x] Repository `RefreshTokenRepository`

## 3.2 JWT libs + config
- [x] Dependências JJWT adicionadas no `pom.xml`
- [x] `application.properties`: issuer, access-token-minutes, refresh-token-days, secret

## 3.3 Services e endpoints
- [x] `JwtService.generateAccessToken(userId, email)`
- [x] `AuthService.register(...)` e `AuthService.login(...)`
- [x] `POST /api/auth/register` retorna `{accessToken, refreshToken}`
- [x] `POST /api/auth/login` retorna `{accessToken, refreshToken}`

✅ Stop Point Etapa 3: concluído.

---

# Etapa 4 — Refresh endpoint + rotação (Pendente)
## 4.1 DTO e endpoint
- [ ] Criar DTO `RefreshRequest`
- [ ] Implementar `AuthService.refresh(...)`:
  - [ ] validar refresh token (hash no banco)
  - [ ] validar expiração
  - [ ] rotacionar (apagar antigo e emitir novo par)
- [ ] `POST /api/auth/refresh` retorna novo `{accessToken, refreshToken}`

✅ Stop Point Etapa 4:
- [ ] refresh funciona e token antigo não funciona mais.

---

# Etapa 5 — Proteger rotas com JWT (Pendente)
## 5.1 Security “de verdade”
- [ ] Atualizar `SecurityFilterChain`:
  - [ ] liberar `/api/health` e `/api/auth/**`
  - [ ] exigir auth no resto (`authenticated()`)
- [ ] Criar filtro JWT:
  - [ ] ler `Authorization: Bearer ...`
  - [ ] validar assinatura/expiração
  - [ ] colocar usuário autenticado no `SecurityContext`

✅ Stop Point Etapa 5:
- [ ] endpoints protegidos retornam 401 sem token e 200 com token válido.

---

# Etapa 6 — CRUD Essencial (Pendente)
## 6.1 Entidades
- [ ] `Account`
- [ ] `Category`
- [ ] `Transaction` (INCOME/EXPENSE, valor, data, descrição, user_id)

## 6.2 Endpoints
- [ ] Accounts CRUD
- [ ] Categories CRUD
- [ ] Transactions CRUD

✅ Stop Point Etapa 6:
- [ ] CRUDs funcionando e isolados por usuário.

---

# Etapa 7 — Filtros, paginação e ordenação (Pendente)
- [ ] `GET /api/transactions` com filtros (from/to/type/accountId/categoryId/q) + page/size/sort

---

# Etapa 8 — Dashboard (Pendente)
- [ ] `GET /api/dashboard/summary?month=YYYY-MM`
- [ ] `GET /api/dashboard/by-category?month=YYYY-MM`
- [ ] `GET /api/dashboard/upcoming?days=14`

---

# Etapa 9 — Frontend Angular (Pendente)
## 9.1 Setup Angular + Material
- [ ] criar projeto em `frontend/`
- [ ] Angular Material + layout base

## 9.2 Auth UI
- [ ] Login/Register
- [ ] Guard + Interceptor JWT
- [ ] Refresh automático (quando 401)

## 9.3 Transactions UI
- [ ] Tabela (paginada, sort)
- [ ] Filtros (Reactive Forms)
- [ ] Create/Edit

## 9.4 Dashboard UI
- [ ] Cards + gráfico + “a vencer”

---

# Etapas futuras (quando quiser evoluir)
- [ ] Recorrência
- [ ] Tags
- [ ] Orçamentos por categoria
- [ ] Import CSV
- [ ] Export / relatórios
- [ ] Auditoria + soft delete (opcional)
- [ ] Testes (unit + integration)
