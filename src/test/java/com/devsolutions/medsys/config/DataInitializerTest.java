package com.devsolutions.medsys.config;

import com.devsolutions.medsys.model.User;
import com.devsolutions.medsys.repository.UserRepository;
import com.devsolutions.medsys.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.ApplicationArguments;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DataInitializerTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserService userService;

    @Mock
    private ApplicationArguments args;

    private DataInitializer dataInitializer;

    private static final String DEFAULT_EMAIL    = "atendente@medsys.com";
    private static final String DEFAULT_PASSWORD = "Medsys@2026";

    @BeforeEach
    void setUp() {
        dataInitializer = new DataInitializer(userRepository, userService);
    }

    @Test
    void run_quandoUsuarioJaExiste_naoChama_createUser() throws Exception {
        User usuarioExistente = User.builder().email(DEFAULT_EMAIL).build();
        when(userRepository.findByEmail(DEFAULT_EMAIL)).thenReturn(Optional.of(usuarioExistente));

        dataInitializer.run(args);

        verify(userService, never()).createUser(anyString(), anyString(), anyString());
    }

    @Test
    void run_quandoUsuarioNaoExiste_chama_createUser() throws Exception {
        when(userRepository.findByEmail(DEFAULT_EMAIL)).thenReturn(Optional.empty());

        dataInitializer.run(args);

        verify(userService, times(1)).createUser(DEFAULT_EMAIL, DEFAULT_PASSWORD, "ATENDENTE");
    }
}
