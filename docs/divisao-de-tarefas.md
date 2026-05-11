# Divisão de Tarefas — MedSys MVP

> Cada pessoa é responsável por criar os DTOs, services e controllers do seu domínio.
> Convenções obrigatórias: ver [guia-de-desenvolvimento.md](./guia-de-desenvolvimento.md).
> Regras de negócio: ver [regras-de-negocio.md](./regras-de-negocio.md).

---

## Ordem de execução recomendada

```
Pessoa 1 (Auth/Seg)  →  Pessoa 2 (Doctor/Specialty)  →  Pessoa 3 (Patient/Appointment)
```

A Pessoa 2 e a Pessoa 3 dependem da Pessoa 1 ter entregue `UserService` (criação interna de User) e a configuração de segurança rodando. A Pessoa 3 depende da Pessoa 2 ter entregue `DoctorAvailabilityService.isAvailable()`.

---

## Pessoa 1 — Segurança, Auth & User

**Responsabilidade:** configurar toda a camada de segurança do sistema (JWT, BCrypt, Spring Security) e os endpoints de autenticação.

### Dependência a adicionar no `pom.xml` (fazer primeiro)

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

### Pacotes a criar

```
com.devsolutions.medsys
├── config/
│   └── security/
│       ├── SecurityConfig.java
│       ├── JwtAuthFilter.java
│       └── JwtUtil.java
├── dto/
│   └── auth/
│       ├── LoginRequestDTO.java
│       ├── LoginResponseDTO.java
│       └── RegisterRequestDTO.java
└── service/
    ├── AuthService.java
    └── UserService.java
└── controller/
    └── AuthController.java
```

> `UserService` também expõe métodos internos usados pela Pessoa 2 (`DoctorService`) e Pessoa 3 (`PatientService`) para criar o `User` ao registrar médico/paciente. Combine a assinatura com o time antes de começar.

---

### DTOs

#### `LoginRequestDTO`
```java
record LoginRequestDTO(
    @NotBlank String email,
    @NotBlank String password
) {}
```

#### `LoginResponseDTO`
```java
record LoginResponseDTO(
    String token,
    String email,
    List<String> roles
) {}
```

#### `RegisterRequestDTO`
```java
record RegisterRequestDTO(
    @NotBlank @Email String email,
    @NotBlank @Size(min = 8) String password,
    @NotBlank String role   // "DOCTOR" | "PATIENT" | "ADMIN"
) {}
```

---

### `JwtUtil`

| Método | Descrição |
|---|---|
| `generateToken(String email, List<String> roles)` | Gera token JWT assinado com chave secreta (HS256). Expiração configurável via `application.yml`. |
| `extractEmail(String token)` | Extrai o subject (email) do token. |
| `isTokenValid(String token, UserDetails userDetails)` | Valida se token não expirou e pertence ao usuário. |

**Dica:** armazene a chave secreta e o tempo de expiração no `application.yml`:
```yaml
jwt:
  secret: "sua-chave-secreta-base64-minimo-256bits"
  expiration-ms: 86400000  # 24h
```

---

### `UserService`

| Método | Visibilidade | Descrição |
|---|---|---|
| `loadUserByUsername(String email)` | `public` | Implementa `UserDetailsService` do Spring Security. Busca o `User` pelo email e monta o `UserDetails` com as roles. |
| `createUser(String email, String password, String roleName)` | `public` | Cria um `User`, criptografa a senha com BCrypt, associa a `Role` e salva. **Usado internamente por `DoctorService` e `PatientService`.** |
| `deactivateUser(UUID userId)` | `public` | Seta `active = false` no `User`. |

**Dicas:**
- Lançar `UsernameNotFoundException` se o email não existir em `loadUserByUsername`.
- Verificar se o email já existe antes de criar (`UserRepository.findByEmail`). Se existir, lançar `IllegalArgumentException` ou uma exceção customizada `EmailAlreadyExistsException`.
- A `Role` deve ser buscada pelo nome no `RoleRepository`. Se não encontrada, lançar exceção informativa.

---

### `AuthService`

| Método | Descrição |
|---|---|
| `login(LoginRequestDTO dto)` → `LoginResponseDTO` | Autentica via `AuthenticationManager`, gera e retorna o JWT. |
| `register(RegisterRequestDTO dto)` → `void` | Chama `UserService.createUser(...)`. |

---

### `SecurityConfig`

- Desabilitar CSRF (API REST stateless).
- Liberar sem autenticação: `POST /auth/login`, `POST /auth/register`, `GET /swagger-ui/**`, `GET /v3/api-docs/**`.
- Exigir autenticação em todas as demais rotas.
- Registrar `JwtAuthFilter` antes do `UsernamePasswordAuthenticationFilter`.
- Expor o bean `AuthenticationManager` para injeção no `AuthService`.
- Configurar `PasswordEncoder` como bean (`BCryptPasswordEncoder`).

---

### `AuthController`

| Verbo | Rota | Método do service | Roles permitidas |
|---|---|---|---|
| `POST` | `/auth/login` | `AuthService.login(...)` | Público |
| `POST` | `/auth/register` | `AuthService.register(...)` | Público |

---

### `JwtAuthFilter`

- Extender `OncePerRequestFilter`.
- Ler o header `Authorization: Bearer <token>`.
- Validar o token com `JwtUtil`, carregar o `UserDetails` via `UserService` e setar o `SecurityContextHolder`.

---

---

## Pessoa 2 — Doctor, Specialty & DoctorAvailability

**Responsabilidade:** CRUD de médicos e especialidades, gestão de disponibilidade de agenda.

> **Pré-requisito:** `UserService.createUser(...)` da Pessoa 1 deve estar disponível.

### Pacotes a criar

```
com.devsolutions.medsys
├── dto/
│   ├── doctor/
│   │   ├── DoctorRequestDTO.java
│   │   └── DoctorResponseDTO.java
│   ├── specialty/
│   │   ├── SpecialtyRequestDTO.java
│   │   └── SpecialtyResponseDTO.java
│   └── availability/
│       ├── DoctorAvailabilityRequestDTO.java
│       └── DoctorAvailabilityResponseDTO.java
├── service/
│   ├── DoctorService.java
│   ├── SpecialtyService.java
│   └── DoctorAvailabilityService.java   ← já existe, completar
└── controller/
    ├── DoctorController.java
    └── SpecialtyController.java
```

---

### DTOs

#### `DoctorRequestDTO`
```java
record DoctorRequestDTO(
    @NotBlank String email,
    @NotBlank @Size(min = 8) String password,
    @NotBlank String name,
    @NotBlank @Size(max = 20) String crm,
    String phone,
    @NotNull UUID specialtyId,
    Integer appointmentDurationMin  // opcional, default 30
) {}
```

#### `DoctorResponseDTO`
```java
record DoctorResponseDTO(
    UUID id,
    String name,
    String crm,
    String phone,
    String specialtyName,
    Integer appointmentDurationMin,
    Boolean active
) {}
```

#### `SpecialtyRequestDTO`
```java
record SpecialtyRequestDTO(
    @NotBlank String name,
    String description
) {}
```

#### `SpecialtyResponseDTO`
```java
record SpecialtyResponseDTO(UUID id, String name, String description) {}
```

#### `DoctorAvailabilityRequestDTO`
```java
record DoctorAvailabilityRequestDTO(
    @NotNull Integer dayOfWeek,   // 1=Segunda ... 7=Domingo (ISO)
    @NotNull LocalTime startTime,
    @NotNull LocalTime endTime
) {}
```

#### `DoctorAvailabilityResponseDTO`
```java
record DoctorAvailabilityResponseDTO(
    UUID id,
    Integer dayOfWeek,
    LocalTime startTime,
    LocalTime endTime,
    boolean active
) {}
```

---

### `SpecialtyService`

| Método | Descrição |
|---|---|
| `create(SpecialtyRequestDTO dto)` → `SpecialtyResponseDTO` | Salva nova especialidade. |
| `listAll()` → `List<SpecialtyResponseDTO>` | Retorna todas as especialidades. |

---

### `DoctorService`

| Método | Descrição |
|---|---|
| `create(DoctorRequestDTO dto)` → `DoctorResponseDTO` | Chama `UserService.createUser(email, password, "DOCTOR")`, depois cria e salva `Doctor`. Verifica se o CRM já existe. |
| `findById(UUID id)` → `DoctorResponseDTO` | Busca médico ativo pelo ID. Lança exceção se não encontrar. |
| `listBySpecialty(UUID specialtyId)` → `List<DoctorResponseDTO>` | Usa `DoctorRepository.findBySpecialtyIdAndActiveTrue(...)`. |
| `deactivate(UUID id)` → `void` | Seta `active = false`. Também chama `UserService.deactivateUser(...)`. |

**Dicas:**
- Verificar existência da `Specialty` antes de criar o doctor (`SpecialtyRepository.findById`).
- CRM único: usar `DoctorRepository.findByCrm(...)` antes de salvar.

---

### `DoctorAvailabilityService`

| Método | Visibilidade | Descrição |
|---|---|---|
| `saveAvailability(UUID doctorId, DoctorAvailabilityRequestDTO dto)` → `DoctorAvailabilityResponseDTO` | `public` | Cria ou atualiza a disponibilidade de um dia da semana. Um médico tem no máximo 1 registro por dia. |
| `listByDoctor(UUID doctorId)` → `List<DoctorAvailabilityResponseDTO>` | `public` | Retorna disponibilidades ativas do médico. |
| `isAvailable(UUID doctorId, LocalDateTime dateTime)` → `boolean` | `public` | **Método crítico usado pelo `AppointmentService`.** Verifica se o médico atende no dia da semana e se o horário cabe dentro de `startTime–endTime`. |

**Lógica de `isAvailable`:**
```
dayOfWeek = dateTime.getDayOfWeek().getValue()  // 1=Seg, 7=Dom
availability = findByDoctorIdAndDayOfWeek(doctorId, dayOfWeek)
return availability.isPresent()
    && availability.get().isActive()
    && !dateTime.toLocalTime().isBefore(availability.get().getStartTime())
    && !dateTime.toLocalTime().isAfter(availability.get().getEndTime())
```

---

### `DoctorController`

| Verbo | Rota | Método | Roles |
|---|---|---|---|
| `POST` | `/doctors` | `DoctorService.create(...)` | `ADMIN` |
| `GET` | `/doctors/{id}` | `DoctorService.findById(...)` | `ADMIN`, `PATIENT` |
| `GET` | `/doctors?specialtyId=` | `DoctorService.listBySpecialty(...)` | `ADMIN`, `PATIENT` |
| `DELETE` | `/doctors/{id}` | `DoctorService.deactivate(...)` | `ADMIN` |
| `POST` | `/doctors/{id}/availability` | `DoctorAvailabilityService.saveAvailability(...)` | `ADMIN`, `DOCTOR` |
| `GET` | `/doctors/{id}/availability` | `DoctorAvailabilityService.listByDoctor(...)` | Autenticado |

---

### `SpecialtyController`

| Verbo | Rota | Método | Roles |
|---|---|---|---|
| `POST` | `/specialties` | `SpecialtyService.create(...)` | `ADMIN` |
| `GET` | `/specialties` | `SpecialtyService.listAll()` | Autenticado |

---

---

## Pessoa 3 — Patient, Appointment & Prescription

**Responsabilidade:** CRUD de pacientes, todo o fluxo de agendamento e emissão de receita.

> **Pré-requisitos:**
> - `UserService.createUser(...)` da Pessoa 1.
> - `DoctorAvailabilityService.isAvailable(...)` da Pessoa 2.

### Pacotes a criar

```
com.devsolutions.medsys
├── dto/
│   ├── patient/
│   │   ├── PatientRequestDTO.java
│   │   └── PatientResponseDTO.java
│   ├── appointment/
│   │   ├── AppointmentRequestDTO.java
│   │   └── AppointmentResponseDTO.java
│   └── prescription/
│       ├── PrescriptionRequestDTO.java
│       └── PrescriptionResponseDTO.java
├── service/
│   ├── PatientService.java
│   ├── AppointmentService.java
│   └── PrescriptionService.java
└── controller/
    ├── PatientController.java
    ├── AppointmentController.java
    └── PrescriptionController.java
```

---

### DTOs

#### `PatientRequestDTO`
```java
record PatientRequestDTO(
    @NotBlank String email,
    @NotBlank @Size(min = 8) String password,
    @NotBlank String name,
    @NotBlank @Pattern(regexp = "\\d{11}") String cpf,
    String phone,
    LocalDate birthDate,
    String address
) {}
```

#### `PatientResponseDTO`
```java
record PatientResponseDTO(
    UUID id,
    String name,
    String cpf,
    String phone,
    LocalDate birthDate,
    String address,
    Boolean active
) {}
```

#### `AppointmentRequestDTO`
```java
record AppointmentRequestDTO(
    @NotNull UUID doctorId,
    @NotNull UUID patientId,
    @NotNull @Future LocalDateTime scheduledAt,
    String reason
) {}
```

#### `AppointmentResponseDTO`
```java
record AppointmentResponseDTO(
    UUID id,
    String doctorName,
    String patientName,
    LocalDateTime scheduledAt,
    AppointmentStatus status,
    String reason,
    String notes
) {}
```

#### `PrescriptionRequestDTO`
```java
record PrescriptionRequestDTO(
    @NotBlank String description,
    @NotBlank String medications,
    LocalDate expiresAt
) {}
```

#### `PrescriptionResponseDTO`
```java
record PrescriptionResponseDTO(
    UUID id,
    UUID appointmentId,
    String description,
    String medications,
    LocalDate expiresAt,
    LocalDateTime createdAt
) {}
```

---

### `PatientService`

| Método | Descrição |
|---|---|
| `create(PatientRequestDTO dto)` → `PatientResponseDTO` | Chama `UserService.createUser(email, password, "PATIENT")`, depois cria e salva `Patient`. Verifica se CPF já existe. |
| `findById(UUID id)` → `PatientResponseDTO` | Busca paciente ativo. Lança exceção se não encontrar. |
| `update(UUID id, PatientRequestDTO dto)` → `PatientResponseDTO` | Atualiza nome, telefone, endereço e data de nascimento. CPF e email **não** podem ser alterados. |
| `deactivate(UUID id)` → `void` | Seta `active = false`. Também chama `UserService.deactivateUser(...)`. |

**Dica:** CPF único — usar `PatientRepository.findByCpf(...)` antes de salvar.

---

### `AppointmentService`

| Método | Descrição |
|---|---|
| `schedule(AppointmentRequestDTO dto)` → `AppointmentResponseDTO` | Valida médico ativo + paciente ativo + disponibilidade + conflito de horário. Cria a consulta com status `SCHEDULED`. |
| `confirm(UUID id)` → `void` | Muda status de `SCHEDULED` → `CONFIRMED`. Somente se ainda estiver `SCHEDULED`. |
| `complete(UUID id)` → `void` | Muda status de `CONFIRMED` → `COMPLETED`. Somente se ainda estiver `CONFIRMED`. |
| `cancel(UUID id)` → `void` | Muda status para `CANCELLED`. Só é permitido se o status for `SCHEDULED` ou `CONFIRMED`. |
| `listByPatient(UUID patientId)` → `List<AppointmentResponseDTO>` | Usa `AppointmentRepository.findByPatientIdOrderByScheduledAtDesc(...)`. |
| `listByDate(LocalDate date)` → `List<AppointmentResponseDTO>` | Usa `findByScheduledAtBetweenOrderByScheduledAtAsc(...)` com intervalo do dia. |
| `listByDoctor(UUID doctorId, LocalDate date)` → `List<AppointmentResponseDTO>` | Usa `findByDoctorIdAndScheduledAtBetween(...)`. |

**Lógica de `schedule` — validações em ordem:**
1. Doctor existe e `active = true`
2. Patient existe e `active = true`
3. `DoctorAvailabilityService.isAvailable(doctorId, scheduledAt)` → lançar exceção se `false`
4. `AppointmentRepository.findByDoctorIdAndScheduledAt(doctorId, scheduledAt)` → lançar exceção se já ocupado

---

### `PrescriptionService`

| Método | Descrição |
|---|---|
| `create(UUID appointmentId, PrescriptionRequestDTO dto)` → `PrescriptionResponseDTO` | Verifica se a consulta existe e está `COMPLETED`. Verifica se já não tem receita (`findByAppointmentId`). Cria e salva a `Prescription`. |
| `findByAppointment(UUID appointmentId)` → `PrescriptionResponseDTO` | Retorna a receita vinculada. Lança exceção se não existir. |

---

### `AppointmentController`

| Verbo | Rota | Método | Roles |
|---|---|---|---|
| `POST` | `/appointments` | `schedule(...)` | `ADMIN`, `PATIENT` |
| `PATCH` | `/appointments/{id}/confirm` | `confirm(...)` | `ADMIN`, `DOCTOR` |
| `PATCH` | `/appointments/{id}/complete` | `complete(...)` | `ADMIN`, `DOCTOR` |
| `PATCH` | `/appointments/{id}/cancel` | `cancel(...)` | `ADMIN`, `DOCTOR`, `PATIENT` |
| `GET` | `/appointments?patientId=` | `listByPatient(...)` | `ADMIN`, `DOCTOR`, `PATIENT` |
| `GET` | `/appointments?date=` | `listByDate(...)` | `ADMIN`, `DOCTOR` |
| `GET` | `/appointments?doctorId=&date=` | `listByDoctor(...)` | `ADMIN`, `DOCTOR` |

---

### `PatientController`

| Verbo | Rota | Método | Roles |
|---|---|---|---|
| `POST` | `/patients` | `PatientService.create(...)` | `ADMIN` |
| `GET` | `/patients/{id}` | `PatientService.findById(...)` | `ADMIN`, `DOCTOR` |
| `PUT` | `/patients/{id}` | `PatientService.update(...)` | `ADMIN`, `PATIENT` |
| `DELETE` | `/patients/{id}` | `PatientService.deactivate(...)` | `ADMIN` |

---

### `PrescriptionController`

| Verbo | Rota | Método | Roles |
|---|---|---|---|
| `POST` | `/appointments/{id}/prescription` | `PrescriptionService.create(...)` | `ADMIN`, `DOCTOR` |
| `GET` | `/appointments/{id}/prescription` | `PrescriptionService.findByAppointment(...)` | `ADMIN`, `DOCTOR`, `PATIENT` |

---

## Resumo visual

```
Pessoa 1 (Marlon)           Pessoa 2 (David)                 Pessoa 3 (Swetony)
─────────────────           ────────────────                 ──────────────────
SecurityConfig              SpecialtyService                 PatientService
JwtUtil                     SpecialtyController              PatientController
JwtAuthFilter               DoctorService                    AppointmentService
UserService  ◄──────────────DoctorService (usa createUser)───PatientService (usa createUser)
AuthService                 DoctorController                 AppointmentController
AuthController              DoctorAvailabilityService ◄──────AppointmentService (usa isAvailable)
                            (dentro de DoctorController)     PrescriptionService
                                                             PrescriptionController
```
