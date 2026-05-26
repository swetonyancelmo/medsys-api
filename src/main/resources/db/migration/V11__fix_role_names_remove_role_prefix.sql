-- BUG-01: Remove o prefixo ROLE_ dos nomes dos roles no banco.
--
-- Contexto: os roles foram inseridos com o prefixo já incluso (ROLE_PATIENT, ROLE_DOCTOR, ROLE_ADMIN).
-- O UserService.loadUserByUsername() adiciona o prefixo novamente em runtime,
-- resultando em "ROLE_ROLE_PATIENT" — authority que nunca existe no contexto do Spring Security.
--
-- Correção: manter o prefixo apenas no código (convenção do Spring Security),
-- armazenando apenas o nome do role sem prefixo no banco (PATIENT, DOCTOR, ADMIN).

UPDATE roles SET name = 'PATIENT' WHERE name = 'ROLE_PATIENT';
UPDATE roles SET name = 'DOCTOR'  WHERE name = 'ROLE_DOCTOR';
UPDATE roles SET name = 'ADMIN'   WHERE name = 'ROLE_ADMIN';