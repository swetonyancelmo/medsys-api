# 🏥 MedSys — API de Gestão de Consultas Médicas

**MedSys** é uma API REST para gestão de consultas médicas em clínicas. O sistema cobre o ciclo completo de atendimento: cadastro de pacientes e médicos, configuração de especialidades e disponibilidade de agenda, agendamento e cancelamento de consultas e emissão de receitas médicas.

O domínio da API é em português, voltado para clínicas que precisam de um backend confiável com autenticação por perfil de acesso (RBAC), cache e proteção contra abusos.

---

## 📋 Sumário

- [Tecnologias](#-tecnologias)
- [Funcionalidades](#-funcionalidades)
- [Arquitetura e Entidades](#-arquitetura-e-entidades)
- [Pré-requisitos](#-pré-requisitos)
- [Como Executar](#-como-executar)
- [Docker Hub](#-docker-hub)
- [Variáveis de Ambiente](#-variáveis-de-ambiente)
- [Endpoints](#-endpoints)
- [Segurança](#-segurança)
- [Testes](#-testes)
- [Equipe](#-equipe)

---

## 🛠 Tecnologias

| Tecnologia | Versão | Finalidade |
|---|---|---|
| Java | 21 | Runtime da aplicação |
| Spring Boot | 4.0.6 | Framework principal |
| Spring Web MVC | (Boot) | Camada REST |
| Spring Data JPA + Hibernate | (Boot) | Persistência ORM |
| Spring Security | (Boot) | Autenticação e autorização |
| Spring Cache + Redis | (Boot) | Cache de consultas frequentes |
| Jakarta Bean Validation | (Boot) | Validação de DTOs |
| PostgreSQL | 15 | Banco de dados relacional |
| Flyway | (Boot) | Migrações de schema |
| JJWT | 0.12.6 | Geração e validação de tokens JWT |
| Bucket4j | 7.6.0 | Rate limiting no endpoint de login |
| SpringDoc OpenAPI | 3.0.2 | Documentação Swagger da API |
| Lombok | (Boot) | Redução de boilerplate |
| JaCoCo | 0.8.12 | Relatório de cobertura de testes |
| H2 Database | (Boot) | Banco em memória para testes |
| Docker / Docker Compose | — | Infraestrutura local (PostgreSQL + Redis) |

---

## ✨ Funcionalidades

### 🔐 Autenticação
- Login com e-mail e senha, retornando JWT
- Registro público de pacientes
- Registro de médicos e atendentes (restrito a ATENDENTE)
- Rate limiting: máximo de 10 tentativas de login por IP a cada minuto

### 👤 Pacientes
- Cadastro, atualização e remoção de pacientes
- Listagem paginada com ordenação por nome
- Busca individual por ID

### 🩺 Médicos
- Cadastro, atualização e remoção de médicos
- Listagem paginada com ordenação por nome
- Busca individual por ID
- Filtragem por especialidade

### 🗓️ Disponibilidade de Médicos
- Registro de disponibilidade por dia da semana (com horário início/fim)
- Listagem de disponibilidades ativas por médico
- Desativação de disponibilidade (sem exclusão física)

### 📅 Consultas
- Agendamento com validação de disponibilidade do médico e horário
- Prevenção de double booking (índices únicos parciais)
- Busca por ID, por médico (com filtro de período) e por paciente
- Listagem de agenda por período
- Cancelamento (soft delete por status)

### 💊 Receitas Médicas
- Emissão de receitas vinculadas a uma consulta
- Busca por ID da receita ou por ID da consulta

### 🏪 Clínicas
- Cadastro, atualização e remoção de clínicas/laboratórios
- Listagem paginada com filtro por nome

### 🔬 Especialidades
- Cadastro de especialidades médicas
- Listagem (com filtro por nome) e busca por ID
- Listagem de médicos por especialidade

---

## 🏗 Arquitetura e Entidades

### Estrutura de pacotes

```
com.devsolutions.medsys
├── MedsysApplication.java
├── config/
│   ├── AppConfig.java              # PasswordEncoder BCrypt
│   ├── DataInitializer.java        # Cria atendente padrão no boot
│   ├── RedisConfig.java            # TTLs de cache por domínio
│   ├── SwaggerConfig.java          # Configuração OpenAPI
│   └── security/
│       ├── SecurityConfig.java     # Filter chain, RBAC, rotas públicas
│       ├── JwtAuthFilter.java      # Valida JWT e popula SecurityContext
│       ├── JwtUtil.java            # Geração/validação de tokens
│       └── LoginRateLimitFilter.java  # Rate limiting com Bucket4j
├── controller/
│   └── docs/                       # Interfaces Swagger (*ControllerDocs)
├── dto/                            # Records de Request/Response por domínio
├── enums/                          # AppointmentStatus, etc.
├── exception/
│   ├── BusinessException.java      # Regras de negócio → 409
│   ├── ResourceNotFoundException.java → 404
│   └── GlobalExceptionHandler.java
├── mapper/                         # Conversão Entity ↔ DTO (@Component)
├── model/                          # Entidades JPA
├── repository/                     # Interfaces Spring Data JPA
└── service/                        # Lógica de negócio
```

### Migrações Flyway

| Versão | Descrição |
|---|---|
| V1 | Tabela `specialty` |
| V2 | Tabela `roles` + seed das roles iniciais |
| V3 | Tabela `users` |
| V4 | Tabela `user_roles` (associação N:N) |
| V5 | Tabela `doctor` |
| V6 | Tabela `patient` |
| V7 | Tabela `appointment` |
| V8 | Tabela `prescription` |
| V9 | Tabela `doctor_availability` |
| V10 | Índices únicos parciais — anti double booking em `appointment` |
| V11 | Corrige nomes de roles (remove prefixo `ROLE_` duplicado) |
| V12 | Renomeia role `ADMIN` → `ATENDENTE` |
| V13 | Adiciona `updated_at` em `patient` e `doctor` |
| V14 | Tabela `clinic` |
| V15 | Corrige tipo da coluna `appointment.status` para VARCHAR |

### Diagrama de entidades (simplificado)

```
User ──< UserRole >── Role
  │
  ├── Patient (1:1 User)
  │
  └── Doctor (1:1 User) ── Specialty
         └── DoctorAvailability (por dia da semana)
                │
Appointment (Doctor + Patient + scheduledAt + status)
         └── Prescription (0..1 por consulta)

Clinic (cadastro independente)
```

---

## 📦 Pré-requisitos

- **JDK 21**
- **Maven** (ou use o wrapper `./mvnw` incluso no projeto)
- **Docker** e **Docker Compose**

---

## 🚀 Como Executar

### 1. Clonar o repositório

```bash
git clone <url-do-repositório>
cd medsys
```

### 2. Subir os serviços de infraestrutura

```bash
docker compose up -d
```

Isso inicializa:
- **PostgreSQL 15** — porta `5433` (host) → `5432` (container), banco `medsys_db`
- **Redis 7** — porta `6379`

### 3. Configurar variáveis de ambiente (opcional)

As variáveis têm valores padrão de desenvolvimento. Para sobrescrever, exporte antes de rodar:

```bash
export DB_PASSWORD=sua_senha
export JWT_SECRET=seu_segredo_base64_256bits
export JWT_EXPIRATION_MS=86400000
```

### 4. Executar a aplicação

```bash
./mvnw spring-boot:run
```

A aplicação sobe em `http://localhost:8080`.

### 5. Primeiro acesso

O `DataInitializer` cria automaticamente um atendente padrão na primeira inicialização:

| Campo | Valor |
|---|---|
| E-mail | `atendente@medsys.com` |
| Senha | `Medsys@2026` |

Faça login em `POST /auth/login` para obter o JWT e use `Authorization: Bearer <token>` nas demais requisições.

### 6. Acessar a documentação Swagger

```
http://localhost:8080/swagger-ui.html
```

OpenAPI JSON: `http://localhost:8080/v3/api-docs`

---

## 🐳 Docker Hub

A imagem da aplicação está publicada no Docker Hub e pode ser usada diretamente, sem necessidade de clonar o repositório ou fazer build local.

**Imagem:** [`swetony/medsys:latest`](https://hub.docker.com/r/swetony/medsys)

### Puxar a imagem

```bash
docker pull swetony/medsys:latest
```

### Executar com Docker Compose (recomendado)

A forma mais simples é usar o `docker-compose.yml` do repositório, que já sobe PostgreSQL, Redis e a aplicação juntos:

```bash
docker compose up -d
```

### Executar apenas o container da API

Se preferir rodar somente a API (assumindo PostgreSQL e Redis já disponíveis):

```bash
docker run -d \
  -p 8080:8080 \
  -e DB_PASSWORD=sua_senha \
  -e JWT_SECRET=seu_segredo_base64 \
  -e JWT_EXPIRATION_MS=86400000 \
  swetony/medsys:latest
```

### Detalhes da imagem

| Característica | Detalhe |
|---|---|
| Build | Multi-stage (builder + runtime separados) |
| Base (build) | `eclipse-temurin:21-jdk-alpine` |
| Base (runtime) | `eclipse-temurin:21-jre-alpine` |
| Porta exposta | `8080` |
| Tamanho | Otimizado — apenas JRE na imagem final |

---

## ⚙️ Variáveis de Ambiente

| Variável | Descrição | Valor padrão |
|---|---|---|
| `DB_PASSWORD` | Senha do PostgreSQL | `admin` |
| `JWT_SECRET` | Segredo Base64 (mín. 256 bits) para assinatura JWT | valor de dev no `application.yml` |
| `JWT_EXPIRATION_MS` | Tempo de expiração do token em milissegundos | `86400000` (24 horas) |

> ⚠️ **Nunca use os valores padrão em produção.** Sempre defina `JWT_SECRET` e `DB_PASSWORD` via variáveis de ambiente seguras.

---

## 📡 Endpoints

Base URL: `http://localhost:8080`

### 🔐 Auth — `/auth`

| Método | Rota | Descrição | Perfil |
|---|---|---|---|
| POST | `/auth/login` | Autenticar e obter JWT | Público |
| POST | `/auth/register/patient` | Registrar novo paciente | Público |
| POST | `/auth/register/doctor` | Registrar novo médico | ATENDENTE |
| POST | `/auth/register/atendente` | Registrar novo atendente | ATENDENTE |

### 👤 Pacientes — `/patients`

| Método | Rota | Descrição | Perfil |
|---|---|---|---|
| POST | `/patients` | Cadastrar paciente | ATENDENTE |
| GET | `/patients?page=0&size=12&direction=asc` | Listar pacientes (paginado) | ATENDENTE |
| GET | `/patients/{id}` | Buscar paciente por ID | ATENDENTE, DOCTOR, PATIENT |
| PATCH | `/patients/{id}` | Atualizar dados do paciente | ATENDENTE |
| DELETE | `/patients/{id}` | Remover paciente | ATENDENTE |

### 🩺 Médicos — `/doctors`

| Método | Rota | Descrição | Perfil |
|---|---|---|---|
| POST | `/doctors` | Cadastrar médico | ATENDENTE |
| GET | `/doctors?page=0&size=12&direction=asc` | Listar médicos (paginado) | ATENDENTE, DOCTOR |
| GET | `/doctors/{id}` | Buscar médico por ID | ATENDENTE, DOCTOR |
| PATCH | `/doctors/{id}` | Atualizar dados do médico | ATENDENTE |
| DELETE | `/doctors/{id}` | Remover médico | ATENDENTE |

### 🔬 Especialidades — `/specialties`

| Método | Rota | Descrição | Perfil |
|---|---|---|---|
| POST | `/specialties` | Cadastrar especialidade | ATENDENTE |
| GET | `/specialties?name=` | Listar especialidades (filtro opcional por nome) | ATENDENTE, DOCTOR, PATIENT |
| GET | `/specialties/{id}` | Buscar especialidade por ID | ATENDENTE, DOCTOR, PATIENT |
| GET | `/specialties/{id}/doctors` | Listar médicos de uma especialidade | ATENDENTE, DOCTOR, PATIENT |

### 🗓️ Disponibilidade — `/doctor-availabilities`

| Método | Rota | Descrição | Perfil |
|---|---|---|---|
| POST | `/doctor-availabilities` | Registrar disponibilidade | ATENDENTE, DOCTOR |
| GET | `/doctor-availabilities/doctor/{doctorId}` | Listar disponibilidades ativas do médico | ATENDENTE, DOCTOR |
| PATCH | `/doctor-availabilities/{id}/disable` | Desativar disponibilidade | ATENDENTE, DOCTOR |

### 📅 Consultas — `/appointments`

| Método | Rota | Descrição | Perfil |
|---|---|---|---|
| POST | `/appointments` | Agendar consulta | ATENDENTE |
| GET | `/appointments/{id}` | Buscar consulta por ID | ATENDENTE, DOCTOR, PATIENT |
| GET | `/appointments/doctor/{id}?start=&end=` | Listar consultas do médico por período | ATENDENTE, DOCTOR |
| GET | `/appointments/findByPatient/{id}` | Histórico de consultas do paciente | ATENDENTE, DOCTOR, PATIENT |
| GET | `/appointments/range?start=&end=` | Listar todas as consultas de um período | ATENDENTE |
| PATCH | `/appointments/{id}/cancel` | Cancelar consulta | ATENDENTE |

> **Formato de datas:** `start=2026-05-26T08:00:00&end=2026-05-26T18:00:00` (ISO 8601 DATE_TIME)

### 💊 Receitas — `/prescriptions`

| Método | Rota | Descrição | Perfil |
|---|---|---|---|
| POST | `/prescriptions` | Emitir receita médica | DOCTOR |
| GET | `/prescriptions/{id}` | Buscar receita por ID | ATENDENTE, DOCTOR, PATIENT |
| GET | `/prescriptions/appointment/{appointmentId}` | Buscar receita de uma consulta | ATENDENTE, DOCTOR, PATIENT |

### 🏪 Clínicas — `/clinics`

| Método | Rota | Descrição | Perfil |
|---|---|---|---|
| POST | `/clinics` | Cadastrar clínica | ATENDENTE |
| GET | `/clinics?name=&page=0&size=12&direction=asc` | Listar clínicas (paginado, filtro por nome) | ATENDENTE, DOCTOR, PATIENT |
| GET | `/clinics/{id}` | Buscar clínica por ID | ATENDENTE, DOCTOR, PATIENT |
| PATCH | `/clinics/{id}` | Atualizar clínica | ATENDENTE |
| DELETE | `/clinics/{id}` | Remover clínica | ATENDENTE |

---

## 🔒 Segurança

### JWT (JSON Web Token)

- Tokens gerados com **JJWT 0.12.6** usando chave HMAC-SHA256
- O token carrega `subject` (e-mail) e `roles` como claims
- Validade configurável via `JWT_EXPIRATION_MS` (padrão: 24h)
- `JwtAuthFilter` intercepta toda requisição, valida o token e popula o `SecurityContext` **sem consultar o banco** a cada request
- Token inválido ou expirado → `401` com mensagem JSON padronizada

### BCrypt

- Senhas armazenadas com hash **BCrypt** via `PasswordEncoder` configurado em `AppConfig`

### RBAC — Controle de Acesso por Papel

| Role | Capabilities |
|---|---|
| `ATENDENTE` | CRUD administrativo completo (pacientes, médicos, clínicas, especialidades, agendamentos, cancelamentos, registro de usuários) |
| `DOCTOR` | Disponibilidade, emissão de receitas, leitura de consultas e pacientes |
| `PATIENT` | Leitura dos próprios dados, histórico de consultas e receitas |

- `@EnableMethodSecurity` ativo — cada endpoint usa `@PreAuthorize`
- Roles armazenadas sem prefixo `ROLE_` no banco; o prefixo é adicionado em runtime

### Rate Limiting (Bucket4j)

- Proteção contra força bruta no endpoint `POST /auth/login`
- **Limite:** 10 requisições por minuto por IP
- Detecção de IP com suporte ao header `X-Forwarded-For`
- Excedido o limite → `429 Too Many Requests` com mensagem em português

### Sessão Stateless

- `SessionCreationPolicy.STATELESS` — sem sessão HTTP
- CSRF desabilitado (API REST com JWT)

### Cache (Redis)

| Cache | TTL |
|---|---|
| `specialties` | 60 minutos |
| `prescriptions` | 60 minutos |
| `clinics` | 30 minutos |
| `doctors` | 15 minutos |
| `doctors-by-specialty` | 15 minutos |
| `doctor-availabilities` | 10 minutos |
| `appointments` | 5 minutos |

---

## 🧪 Testes

### Executar

```bash
./mvnw test
```

### Gerar relatório de cobertura (JaCoCo)

O relatório é gerado automaticamente durante `./mvnw test` e fica em:

```
target/site/jacoco/index.html
```

### Estrutura dos testes

| Camada | Ferramentas |
|---|---|
| Contexto da aplicação | `@SpringBootTest` (smoke test) |
| Repositórios | `@DataJpaTest` com H2 em memória |
| Services | JUnit 5 + Mockito (`@ExtendWith(MockitoExtension.class)`) |
| Controllers | `@WebMvcTest` + `spring-security-test` |
| Configurações | JUnit 5 + Mockito |
| DTOs e Mappers | Testes unitários puros |
| Exceções | `GlobalExceptionHandler` coberto por testes isolados |
| Segurança | `JwtUtil`, `JwtAuthFilter` e `LoginRateLimitFilter` testados individualmente |

### Arquivos de teste cobertos

- **Repositories:** Appointment, Clinic, DoctorAvailability, Doctor, Patient, Prescription, Role, Specialty, User
- **Services:** Appointment, Auth, Clinic, DoctorAvailability, Doctor, Patient, Prescription, User
- **Controllers:** AppointmentController
- **Config/Security:** AppConfig, DataInitializer, SwaggerConfig, JwtUtil, JwtAuthFilter, LoginRateLimitFilter
- **DTOs:** Auth, Clinic, Doctor, DoctorAvailability, Patient, Prescription, Specialty, User
- **Mappers:** Appointment, DoctorAvailability, Doctor, Patient, Prescription

---

## 👥 Equipe

| Nome | Papel |
|---|---|
| **Swetony Ancelmo** | Desenvolvedor Backend |
| **David Lima** | Desenvolvedor Backend |
| **Marlon Adriano** | Desenvolvedor Backend |
