# MedSys — Documentação de Desenvolvimento (MVP)

Sistema de agendamento médico desenvolvido em Spring Boot + PostgreSQL.

---

## Índice

| Arquivo | Descrição |
|---|---|
| [divisao-de-tarefas.md](./divisao-de-tarefas.md) | Divisão detalhada de tarefas entre os 3 desenvolvedores |
| [regras-de-negocio.md](./regras-de-negocio.md) | Regras de negócio do MVP com dicas de implementação |
| [guia-de-desenvolvimento.md](./guia-de-desenvolvimento.md) | Estrutura de pacotes, convenções, como rodar o projeto |

---

## Visão Geral do Projeto

O **MedSys** é uma API REST para gerenciamento de consultas médicas. O MVP contempla:

- Autenticação com JWT e controle de acesso por perfil (RBAC)
- Cadastro e gestão de médicos, pacientes e especialidades
- Configuração de disponibilidade de agenda do médico
- Agendamento, confirmação, cancelamento e conclusão de consultas
- Emissão de receita médica vinculada a uma consulta concluída

---

## Stack

| Camada | Tecnologia |
|---|---|
| Backend | Spring Boot 4.0.6 / Java 25 |
| Banco de dados | PostgreSQL 15 |
| Migração | Flyway |
| Segurança | Spring Security + JWT (JJWT 0.12.6) |
| ORM | Spring Data JPA / Hibernate |
| Validação | Spring Validation (Jakarta) |
| Documentação API | SpringDoc OpenAPI 3 |
| Boilerplate | Lombok |
| Infraestrutura local | Docker Compose |

---

## Entidades existentes

```
User ──< UserRole >── Role
  │
  ├── Doctor ──< DoctorAvailability
  │      └──< Appointment >── Patient
  │                └── Prescription
  │
  └── Patient
```

- **User** — credenciais de acesso (email + senha)
- **Role** — papéis: `ADMIN`, `DOCTOR`, `PATIENT`
- **Doctor** — médico vinculado a um User e a uma Specialty
- **Specialty** — especialidade médica
- **DoctorAvailability** — janela de atendimento por dia da semana
- **Patient** — paciente vinculado a um User
- **Appointment** — consulta agendada entre Doctor e Patient
- **Prescription** — receita médica de uma consulta concluída

---

## Fluxo central do MVP

```
POST /auth/register  →  cria User + role PATIENT/DOCTOR
POST /auth/login     →  retorna JWT

POST /doctors/{id}/availability  →  define horários do médico
POST /appointments               →  agenda consulta (valida disponibilidade + conflito)
PATCH /appointments/{id}/status  →  CONFIRMED | COMPLETED | CANCELLED
POST /appointments/{id}/prescription  →  cria receita (exige status COMPLETED)
```

---

## Dependência JWT — adicionar no `pom.xml`

> Esta dependência ainda não está no projeto. Deve ser adicionada antes de iniciar a Tarefa 1.

```xml
<dependency>
    <groupId>io.jsonwebtoken</groupId>
    <artifactId>jjwt-api</artifactId>
    <version>0.12.6</version>
</dependency>
<dependency>
    <groupId>io.jsonwebtoken</groupId>
    <artifactId>jjwt-impl</artifactId>
    <version>0.12.6</version>
    <scope>runtime</scope>
</dependency>
<dependency>
    <groupId>io.jsonwebtoken</groupId>
    <artifactId>jjwt-jackson</artifactId>
    <version>0.12.6</version>
    <scope>runtime</scope>
</dependency>
```
