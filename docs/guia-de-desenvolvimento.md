# Guia de Desenvolvimento — MedSys MVP

---

## Como rodar o projeto localmente

### 1. Subir o banco de dados

```bash
docker-compose up -d
```

Isso sobe um PostgreSQL 15 na porta `5433` com banco `medsys_db`, usuário `postgres` e senha `admin`.

### 2. Adicionar a dependência JWT no `pom.xml`

Ver `README.md` — seção "Dependência JWT". Fazer isso **antes** de qualquer outra tarefa.

### 3. Compilar e rodar

```bash
./mvnw spring-boot:run
```

O Flyway aplicará automaticamente as migrations V1–V9 na primeira execução.

### 4. Acessar a documentação da API (Swagger)

```
http://localhost:8080/swagger-ui/index.html
```

---

## Estrutura de pacotes completa (alvo MVP)

```
src/main/java/com/devsolutions/medsys/
│
├── MedsysApplication.java
│
├── config/
│   └── security/
│       ├── SecurityConfig.java          ← configuração do Spring Security
│       ├── JwtAuthFilter.java           ← filtro que valida JWT a cada request
│       └── JwtUtil.java                 ← geração e validação do token
│
├── controller/
│   ├── AuthController.java
│   ├── UserController.java              ← somente desativação (se necessário expor)
│   ├── DoctorController.java            ← inclui endpoints de availability
│   ├── SpecialtyController.java
│   ├── PatientController.java
│   ├── AppointmentController.java
│   └── PrescriptionController.java
│
├── service/
│   ├── AuthService.java
│   ├── UserService.java
│   ├── DoctorService.java
│   ├── SpecialtyService.java
│   ├── DoctorAvailabilityService.java
│   ├── PatientService.java
│   ├── AppointmentService.java
│   └── PrescriptionService.java
│
├── dto/
│   ├── auth/
│   │   ├── LoginRequestDTO.java
│   │   ├── LoginResponseDTO.java
│   │   └── RegisterRequestDTO.java
│   ├── doctor/
│   │   ├── DoctorRequestDTO.java
│   │   └── DoctorResponseDTO.java
│   ├── specialty/
│   │   ├── SpecialtyRequestDTO.java
│   │   └── SpecialtyResponseDTO.java
│   ├── availability/
│   │   ├── DoctorAvailabilityRequestDTO.java
│   │   └── DoctorAvailabilityResponseDTO.java
│   ├── patient/
│   │   ├── PatientRequestDTO.java
│   │   └── PatientResponseDTO.java
│   ├── appointment/
│   │   ├── AppointmentRequestDTO.java
│   │   └── AppointmentResponseDTO.java
│   └── prescription/
│       ├── PrescriptionRequestDTO.java
│       └── PrescriptionResponseDTO.java
│
├── exception/
│   ├── GlobalExceptionHandler.java      ← @RestControllerAdvice
│   ├── EmailAlreadyExistsException.java
│   ├── CrmAlreadyExistsException.java
│   ├── CpfAlreadyExistsException.java
│   ├── PrescriptionAlreadyExistsException.java
│   ├── SpecialtyNotFoundException.java
│   ├── DoctorNotFoundException.java
│   ├── PatientNotFoundException.java
│   ├── AppointmentNotFoundException.java
│   ├── PrescriptionNotFoundException.java
│   ├── DoctorNotAvailableException.java
│   ├── AppointmentConflictException.java
│   ├── InactiveDoctorException.java
│   ├── InactivePatientException.java
│   ├── AppointmentNotCompletedException.java
│   └── InvalidAppointmentStatusTransitionException.java
│
├── enums/
│   └── AppointmentStatus.java           ← já existe
│
├── model/                               ← já existe (não alterar)
│   ├── Appointment.java
│   ├── Doctor.java
│   ├── DoctorAvailability.java
│   ├── Patient.java
│   ├── Prescription.java
│   ├── Role.java
│   ├── Specialty.java
│   ├── User.java
│   ├── UserRole.java
│   └── UserRoleId.java
│
└── repository/                          ← já existe (não alterar)
    ├── AppointmentRepository.java
    ├── DoctorAvailabilityRepository.java
    ├── DoctorRepository.java
    ├── PatientRepository.java
    ├── PrescriptionRepository.java
    ├── RoleRepository.java
    ├── SpecialtyRepository.java
    ├── UserRepository.java
    └── UserRoleRepository.java
```

---

## Convenções de código

### Nomenclatura

| Elemento | Padrão | Exemplo |
|---|---|---|
| Classes | `PascalCase` | `AppointmentService` |
| Métodos e variáveis | `camelCase` | `scheduledAt`, `findById` |
| Constantes | `UPPER_SNAKE_CASE` | `MAX_DURATION_MIN` |
| Pacotes | `lowercase` | `com.devsolutions.medsys.service` |
| Endpoints | `kebab-case` | `/doctor-availabilities` |
| Campos no JSON | `camelCase` | `scheduledAt`, `doctorId` |

### DTOs

- Usar **Java Records** para DTOs (imutáveis, mais simples).
- Anotações de validação Jakarta ficam nos campos do record.
- DTOs de request e response são **classes separadas** — nunca reutilizar o mesmo DTO para os dois sentidos.
- **Nunca** retornar entidades JPA diretamente nos controllers — sempre mapear para DTO.

### Services

- Toda lógica de negócio fica no service, nunca no controller.
- Validações de negócio (unicidade, status, existência) ficam no service.
- Validações de formato (campo obrigatório, tamanho, regex) ficam no DTO com anotações Jakarta.
- Injeção de dependência via construtor (Lombok `@RequiredArgsConstructor`).

### Controllers

- Anotar com `@RestController` + `@RequestMapping("/rota-base")`.
- Usar `@PreAuthorize("hasRole('ROLE_NAME')")` para controle de acesso por método.
- Retornar `ResponseEntity<T>` com o status HTTP correto.
- Não fazer lógica de negócio — só chamar o service e retornar a resposta.

```java
@PostMapping
public ResponseEntity<AppointmentResponseDTO> schedule(
        @RequestBody @Valid AppointmentRequestDTO dto) {
    return ResponseEntity.status(HttpStatus.CREATED)
            .body(appointmentService.schedule(dto));
}
```

### Tratamento de erros

Usar um `@RestControllerAdvice` centralizado (`GlobalExceptionHandler`) para:
- Mapear exceções de negócio para o status HTTP correto.
- Retornar um body padronizado em toda resposta de erro:

```json
{
  "error": "APPOINTMENT_CONFLICT",
  "message": "O médico já possui uma consulta agendada para este horário."
}
```

---

## Migrations Flyway

As migrations existentes (V1–V9) **não devem ser alteradas**. Se precisar de nova coluna ou índice, criar uma nova migration com a versão seguinte (ex.: `V10__...sql`).

```
src/main/resources/db/migration/
├── V1__create_table_specialty.sql
├── V2__create_table_roles.sql
├── V3__create_table_users.sql
├── V4__create_table_user_roles.sql
├── V5__create_table_doctor.sql
├── V6__create_table_patient.sql
├── V7__create_table_appointment.sql
├── V8__create_table_prescription.sql
└── V9__create_table_doctor_availability.sql
```

> **Atenção:** nunca editar uma migration já aplicada. O Flyway compara o checksum e vai rejeitar o startup.

---

## Roles iniciais no banco

Os perfis `ADMIN`, `DOCTOR` e `PATIENT` precisam existir na tabela `roles` antes de qualquer cadastro. Criar uma migration de seed:

```sql
-- V10__seed_roles.sql
INSERT INTO roles (id, name) VALUES
  (gen_random_uuid(), 'ADMIN'),
  (gen_random_uuid(), 'DOCTOR'),
  (gen_random_uuid(), 'PATIENT')
ON CONFLICT DO NOTHING;
```

> Essa migration deve ser criada pela **Pessoa 1** junto com a configuração de segurança.

---

## Variáveis de ambiente recomendadas

Para não deixar credenciais no `application.yml`, usar variáveis de ambiente em produção:

```yaml
spring:
  datasource:
    url: ${DB_URL:jdbc:postgresql://localhost:5433/medsys_db}
    username: ${DB_USER:postgres}
    password: ${DB_PASSWORD:admin}

jwt:
  secret: ${JWT_SECRET:coloque-uma-chave-segura-aqui-minimo-256-bits}
  expiration-ms: ${JWT_EXPIRATION_MS:86400000}
```

---

## Checklist de entrega por pessoa

### Pessoa 1

- [ ] Adicionar dependências JWT no `pom.xml`
- [ ] Criar `V10__seed_roles.sql`
- [ ] Implementar `JwtUtil` (geração + validação)
- [ ] Implementar `JwtAuthFilter`
- [ ] Implementar `SecurityConfig`
- [ ] Implementar `UserService` (createUser, deactivateUser, loadUserByUsername)
- [ ] Implementar `AuthService` (login, register)
- [ ] Criar DTOs de auth
- [ ] Implementar `AuthController`
- [ ] Criar `GlobalExceptionHandler` (base)
- [ ] Testar login e acesso a rota protegida via Swagger

### Pessoa 2

- [ ] Criar DTOs de specialty, doctor, availability
- [ ] Implementar `SpecialtyService` + `SpecialtyController`
- [ ] Implementar `DoctorService` + `DoctorController`
- [ ] Completar `DoctorAvailabilityService` (saveAvailability, listByDoctor, **isAvailable**)
- [ ] Adicionar endpoints de availability no `DoctorController`
- [ ] Testar criação de médico e configuração de agenda via Swagger

### Pessoa 3

- [ ] Criar DTOs de patient, appointment, prescription
- [ ] Implementar `PatientService` + `PatientController`
- [ ] Implementar `AppointmentService` + `AppointmentController`
- [ ] Implementar `PrescriptionService` + `PrescriptionController`
- [ ] Testar fluxo completo: agendar → confirmar → concluir → emitir receita via Swagger
