package com.devsolutions.medsys.config;

import com.devsolutions.medsys.repository.UserRepository;
import com.devsolutions.medsys.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

/**
 * Cria o primeiro atendente do sistema na inicialização, caso ainda não exista.
 * Executa apenas uma vez; nas inicializações seguintes, o e-mail já estará cadastrado
 * e a criação será ignorada.
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class DataInitializer implements ApplicationRunner {

    private static final String DEFAULT_EMAIL    = "atendente@medsys.com";
    private static final String DEFAULT_PASSWORD = "Medsys@2026";

    private final UserRepository userRepository;
    private final UserService    userService;

    @Override
    public void run(ApplicationArguments args) {
        if (userRepository.findByEmail(DEFAULT_EMAIL).isPresent()) {
            return;
        }

        userService.createUser(DEFAULT_EMAIL, DEFAULT_PASSWORD, "ATENDENTE");

        log.warn("===========================================================");
        log.warn("  ATENDENTE PADRÃO CRIADO — ALTERE A SENHA EM PRODUÇÃO!");
        log.warn("  E-mail : {}", DEFAULT_EMAIL);
        log.warn("  Senha  : {}", DEFAULT_PASSWORD);
        log.warn("===========================================================");
    }
}