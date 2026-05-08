# 📋 Quadro de Tarefas - Projeto MedSys (Backend)

## 👨‍💻 Integrante 1: Segurança, Autenticação e Usuários

**Foco:** Garantir o controle de acesso, estruturar a segurança da API e o gerenciamento de usuários e perfis.

### Entidades/Migrations relacionadas:

- `Usuario` (V3)
- `Role` (V2)
- `Usuario_Role` (V4)

### 📌 Requisitos Atendidos:

- **RF04:** Cadastro de usuários (admin/atendente)
- **RF11:** Autenticação de usuários (login)
- **RF12:** Diferentes perfis de usuário (RBAC)
- **RNF01:** Criptografar dados sensíveis (senhas usando BCrypt)
- **RNF02:** Autenticação segura baseada em JWT
- **RNF03:** Controle de acesso por perfil (Spring Security)

### 🚀 Tarefas (Tasks):

- **Task 1.1:** Configurar a estrutura inicial do Spring Security.
- **Task 1.2:** Implementar a criptografia de senhas (BCryptPasswordEncoder).
- **Task 1.3:** Criar as entidades, repositórios e serviços para `Role` e `Usuario`.
- **Task 1.4:** Desenvolver o endpoint de **Login** (`/auth/login`) e geração do token JWT.
- **Task 1.5:** Criar os filtros de interceptação para validar o token JWT nas requisições seguras.
- **Task 1.6:** Implementar o CRUD de Usuários (criação de atendentes/admins).
- **Task 1.7:** Configurar autorização nas rotas (`@PreAuthorize` ou `SecurityFilterChain`) para Admin vs Atendente.

---

## 👨‍💻 Integrante 2: Cadastros Base e Entidades Principais

**Foco:** Desenvolver a base de dados central do sistema, envolvendo os pacientes, médicos (e especialidades) e adequação à privacidade de dados.

### Entidades/Migrations relacionadas:

- `Paciente` (V6)
- `Especialidade` (V1)
- `Medico` (V5)
*(Nota: O requisito fala de clínica/laboratório e exames, mas as migrations apontam para uma clínica com médicos, especialidades e consultas. O modelo foi adaptado com base nas migrations reais).*

### 📌 Requisitos Atendidos:

- **RF01:** Cadastro de pacientes
- **RF02:** Cadastro de clínicas/laboratórios (Neste contexto: Médicos e Especialidades)
- **RF09:** Atualização de dados de pacientes
- **RF14:** Busca de exames/médicos por tipo ou nome
- **RNF04:** Conformidade com a LGPD (Atenção aos dados sensíveis de pacientes)
- **RNF07:** Cache para consultas frequentes (ex: listar especialidades)

### 🚀 Tarefas (Tasks):

- **Task 2.1:** Criar entidades, repositórios e controllers para `Especialidade`. Implementar cache (`@Cacheable`) na listagem.
- **Task 2.2:** Criar entidades, repositórios e controllers para `Medico`.
- **Task 2.3:** Implementar o CRUD completo de Médicos, incluindo busca por especialidade/nome.
- **Task 2.4:** Criar entidades, repositórios e controllers para `Paciente`.
- **Task 2.5:** Implementar o CRUD completo de Pacientes (Atenção para não vazar dados sensíveis nos DTOs - LGPD).
- **Task 2.6:** Criar validações nos DTOs (Bean Validation) para CPF, Email, e campos obrigatórios de Médicos e Pacientes.

---

## 👨‍💻 Integrante 3: Regras de Negócio, Agendamentos e Relatórios

**Foco:** Desenvolver o "coração" do sistema, que é o motor de agendamentos, verificação de disponibilidades e fluxos de atendimento.

### Entidades/Migrations relacionadas:

- `Consulta` / Agendamento (V7)
- `Receita` (V8)
- `Disponibilidade_Medico` (V9)

### 📌 Requisitos Atendidos:

- **RF03:** Cadastro de exames/consultas
- **RF05:** Agendamento de consultas/exames
- **RF06:** Visualização de horários disponíveis
- **RF07:** Impedir agendamentos em horários já ocupados
- **RF08:** Cancelamento de agendamentos
- **RF10:** Histórico de exames/consultas realizados por paciente
- **RF13:** Listagem de agendamentos por data
- **RNF06:** Suportar múltiplos agendamentos sem inconsistência (Controle de concorrência)

### 🚀 Tarefas (Tasks):

- **Task 3.1:** Criar entidades e repositórios para `Disponibilidade_Medico`.
- **Task 3.2:** Implementar endpoint para buscar os **horários disponíveis** de um médico em uma data específica.
- **Task 3.3:** Criar entidades, repositórios e controllers para `Consulta` e `Receita`.
- **Task 3.4:** Implementar a lógica de criação de Agendamento (Consulta). **Importante:** Validar se o horário já está ocupado (Tratamento de concorrência).
- **Task 3.5:** Implementar a funcionalidade de Cancelamento de Agendamento (com regra para mudar status, e não apagar do banco).
- **Task 3.6:** Desenvolver endpoint para listar agendamentos por data (Visão da recepção/atendente).
- **Task 3.7:** Desenvolver endpoint que retorne o **histórico de consultas/receitas** de um determinado Paciente.

---

## ⚙️ Tarefas Transversais (Todos ou quem terminar primeiro)

- **Tratamento Global de Exceções:** Criar um `@ControllerAdvice` para padronizar os erros da API (400, 404, 403, 500).
- **Documentação da API:** Configurar o Swagger/OpenAPI (`springdoc-openapi`) para testar os endpoints.
- **RNF05:** Revisar queries pesadas (verificar índices no banco e N+1 queries do Hibernate) para garantir resposta < 3 segundos.
- **RNF08 / RNF09 / RNF10:** Configurar rotinas de banco (Backups diários pelo SGBD) e deploy da aplicação (Cloud/Docker).

