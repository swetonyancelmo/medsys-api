# Regras de Negócio — MedSys MVP

> Este arquivo descreve todas as regras de negócio do sistema. Cada regra indica onde ela deve ser implementada (service) e como lidar com violações (exceções).

---

## RN-01: Unicidade de Email

**Onde:** `UserService.createUser`

Um email só pode ser cadastrado uma vez no sistema. Se já existir um `User` com o mesmo email, deve-se lançar uma exceção antes de prosseguir.

```
UserRepository.findByEmail(email) → se presente → lançar EmailAlreadyExistsException
```

---

## RN-02: Unicidade de CRM

**Onde:** `DoctorService.create`

O CRM é o identificador profissional único do médico no Brasil. Dois médicos não podem compartilhar o mesmo CRM.

```
DoctorRepository.findByCrm(crm) → se presente → lançar CrmAlreadyExistsException
```

---

## RN-03: Unicidade de CPF

**Onde:** `PatientService.create`

O CPF é o identificador único do paciente. Não podem existir dois pacientes com o mesmo CPF.

```
PatientRepository.findByCpf(cpf) → se presente → lançar CpfAlreadyExistsException
```

---

## RN-04: Criptografia de Senha

**Onde:** `UserService.createUser`

A senha nunca deve ser armazenada em texto puro. Usar `BCryptPasswordEncoder` (já configurado como bean em `SecurityConfig`) antes de persistir.

```java
user.setPassword(passwordEncoder.encode(rawPassword));
```

---

## RN-05: Médico deve pertencer a uma Especialidade existente

**Onde:** `DoctorService.create`

Ao cadastrar um médico, a `Specialty` informada deve existir no banco. Se não existir, lançar exceção informativa.

```
SpecialtyRepository.findById(specialtyId) → se vazio → lançar SpecialtyNotFoundException
```

---

## RN-06: Disponibilidade de horário do médico

**Onde:** `DoctorAvailabilityService.saveAvailability`

- Um médico pode ter **no máximo uma janela de atendimento por dia da semana**.
- O `endTime` deve ser **estritamente posterior** ao `startTime` (a entidade já possui `isScheduleValid()` para isso).
- Se já existir uma disponibilidade para aquele dia, **atualizar** em vez de criar novo registro.

```
findByDoctorIdAndDayOfWeek(doctorId, dayOfWeek):
  → se presente: atualizar startTime, endTime, active
  → se ausente: criar novo registro
```

**Referência:** `DoctorAvailability.isScheduleValid()` já implementado na entidade.

---

## RN-07: Agendamento só dentro da janela de atendimento

**Onde:** `AppointmentService.schedule` → chama `DoctorAvailabilityService.isAvailable`

O horário da consulta (`scheduledAt`) deve cair dentro da janela de atendimento do médico no dia da semana correspondente. Se o médico não atender naquele dia ou o horário estiver fora da janela, rejeitar o agendamento.

```
dayOfWeek = scheduledAt.getDayOfWeek().getValue()
availability = findByDoctorIdAndDayOfWeek(doctorId, dayOfWeek)
se não encontrado ou inativo → lançar DoctorNotAvailableException
se scheduledAt.toLocalTime() < startTime → lançar DoctorNotAvailableException
se scheduledAt.toLocalTime() > endTime → lançar DoctorNotAvailableException
```

---

## RN-08: Sem conflito de horário para o médico

**Onde:** `AppointmentService.schedule`

Um médico não pode ter duas consultas agendadas para o mesmo horário. A constraint `uq_doctor_scheduled_at` já existe no banco, mas a validação deve ocorrer na camada de serviço antes para retornar uma mensagem clara ao usuário.

```
AppointmentRepository.findByDoctorIdAndScheduledAt(doctorId, scheduledAt)
  → se presente → lançar AppointmentConflictException
```

---

## RN-09: Somente médicos e pacientes ativos podem participar de consultas

**Onde:** `AppointmentService.schedule`

Antes de criar um agendamento, verificar que o médico e o paciente têm `active = true`. Usuários desativados não participam de novas consultas.

```
doctor.getActive() == false → lançar InactiveDoctorException
patient.getActive() == false → lançar InactivePatientException
```

---

## RN-10: Fluxo de status da consulta

**Onde:** `AppointmentService` (confirm, complete, cancel)

O status da consulta segue um fluxo unidirecional:

```
SCHEDULED → CONFIRMED → COMPLETED
     ↓             ↓
  CANCELLED     CANCELLED
```

| Transição | Pré-condição |
|---|---|
| `SCHEDULED` → `CONFIRMED` | Status atual deve ser `SCHEDULED` |
| `CONFIRMED` → `COMPLETED` | Status atual deve ser `CONFIRMED` |
| `SCHEDULED` → `CANCELLED` | Status atual deve ser `SCHEDULED` |
| `CONFIRMED` → `CANCELLED` | Status atual deve ser `CONFIRMED` |
| Qualquer → `COMPLETED` já foi | Não permitido reverter ou reprocessar |

Se a transição for inválida, lançar `InvalidAppointmentStatusTransitionException` com mensagem descritiva.

---

## RN-11: Receita somente para consulta COMPLETED

**Onde:** `PrescriptionService.create`

Uma receita médica (`Prescription`) só pode ser emitida para uma consulta com status `COMPLETED`. Emitir receita para consultas `SCHEDULED`, `CONFIRMED` ou `CANCELLED` deve ser rejeitado.

```
appointment.getStatus() != COMPLETED → lançar AppointmentNotCompletedException
```

---

## RN-12: Uma consulta, uma receita

**Onde:** `PrescriptionService.create`

Cada consulta pode ter **no máximo uma** receita médica. A constraint `unique` já existe no banco (`fk_prescription_appointment`), mas validar antes para mensagem clara.

```
PrescriptionRepository.findByAppointmentId(appointmentId)
  → se presente → lançar PrescriptionAlreadyExistsException
```

---

## RN-13: CPF e Email não podem ser alterados

**Onde:** `PatientService.update`

Após o cadastro, o CPF do paciente e o email do `User` vinculado são imutáveis. O método `update` deve aceitar apenas: `name`, `phone`, `birthDate`, `address`.

---

## RN-14: Agendamento deve ser em data futura

**Onde:** `AppointmentRequestDTO` + `AppointmentService.schedule`

O campo `scheduledAt` deve ser no futuro. A anotação `@Future` no DTO já faz a validação básica. No service, garantir também que o horário não é `null`.

---

## RN-15: Controle de acesso por perfil (RBAC)

**Onde:** `SecurityConfig` + anotações `@PreAuthorize` nos controllers

| Perfil | Descrição |
|---|---|
| `ADMIN` | Acesso total — pode criar médicos, desativar usuários, ver todos os agendamentos |
| `DOCTOR` | Pode ver/confirmar/concluir suas próprias consultas, emitir receitas, gerenciar sua agenda |
| `PATIENT` | Pode criar sua própria conta, agendar e cancelar suas consultas, ver suas receitas |

> **Dica:** Para MVP, o controle pode ser feito via `hasRole(...)` no `SecurityConfig` (`.requestMatchers(...).hasRole(...)`) ou com `@PreAuthorize("hasRole('ADMIN')")` nos métodos do controller. A segunda abordagem é mais granular e recomendada.

---

## RN-16: Token JWT e sessão stateless

**Onde:** `SecurityConfig`, `JwtUtil`, `JwtAuthFilter`

- A API não mantém sessão no servidor (`SessionCreationPolicy.STATELESS`).
- Toda requisição autenticada deve enviar o header `Authorization: Bearer <token>`.
- O token deve conter: `sub` (email), `roles` (lista), `iat` (emissão), `exp` (expiração).
- Expiração recomendada: 24 horas para MVP.

---

## Resumo de exceções sugeridas

Criar no pacote `com.devsolutions.medsys.exception`:

| Classe | HTTP | Quando lançar |
|---|---|---|
| `EmailAlreadyExistsException` | 409 Conflict | Email duplicado |
| `CrmAlreadyExistsException` | 409 Conflict | CRM duplicado |
| `CpfAlreadyExistsException` | 409 Conflict | CPF duplicado |
| `PrescriptionAlreadyExistsException` | 409 Conflict | Receita duplicada |
| `SpecialtyNotFoundException` | 404 Not Found | Especialidade não existe |
| `DoctorNotFoundException` | 404 Not Found | Médico não existe |
| `PatientNotFoundException` | 404 Not Found | Paciente não existe |
| `AppointmentNotFoundException` | 404 Not Found | Consulta não existe |
| `PrescriptionNotFoundException` | 404 Not Found | Receita não existe |
| `DoctorNotAvailableException` | 422 Unprocessable | Médico sem janela no horário |
| `AppointmentConflictException` | 409 Conflict | Horário já ocupado |
| `InactiveDoctorException` | 422 Unprocessable | Médico desativado |
| `InactivePatientException` | 422 Unprocessable | Paciente desativado |
| `AppointmentNotCompletedException` | 422 Unprocessable | Consulta não concluída |
| `InvalidAppointmentStatusTransitionException` | 422 Unprocessable | Transição de status inválida |

> **Dica:** Criar um `@RestControllerAdvice` (ex.: `GlobalExceptionHandler`) que captura essas exceções e retorna um body padronizado `{ "error": "...", "message": "..." }` com o status HTTP correto.
