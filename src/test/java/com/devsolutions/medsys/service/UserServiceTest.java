package com.devsolutions.medsys.service;

import com.devsolutions.medsys.model.Role;
import com.devsolutions.medsys.model.User;
import com.devsolutions.medsys.repository.RoleRepository;
import com.devsolutions.medsys.repository.UserRepository;
import com.devsolutions.medsys.repository.UserRoleRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @InjectMocks
    private UserService service;

    @Mock
    private UserRepository userRepository;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private UserRoleRepository userRoleRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Test
    void deveCriarUsuarioComSucesso() {
        String email = "test@email.com";
        String password = "123";
        String roleName = "PATIENT";

        Role role = new Role();
        role.setId(UUID.randomUUID());
        role.setName(roleName);

        User user = User.builder()
                .id(UUID.randomUUID())
                .email(email)
                .password("encoded")
                .active(true)
                .build();

        when(userRepository.findByEmail(email))
                .thenReturn(Optional.empty());

        when(roleRepository.findByName(roleName))
                .thenReturn(Optional.of(role));

        when(passwordEncoder.encode(password))
                .thenReturn("encoded");

        when(userRepository.save(any(User.class)))
                .thenReturn(user);

        User result = service.createUser(email, password, roleName);

        assertNotNull(result);
        verify(userRepository).save(any(User.class));
        verify(userRoleRepository).save(any());
    }

    @Test
    void deveFalharQuandoEmailJaExiste() {
        String email = "test@email.com";

        when(userRepository.findByEmail(email))
                .thenReturn(Optional.of(new User()));

        assertThrows(IllegalArgumentException.class,
                () -> service.createUser(email, "123", "PATIENT"));
    }

    @Test
    void deveFalharQuandoRoleNaoExiste() {
        String email = "test@email.com";

        when(userRepository.findByEmail(email))
                .thenReturn(Optional.empty());

        when(roleRepository.findByName("PATIENT"))
                .thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class,
                () -> service.createUser(email, "123", "PATIENT"));
    }

    @Test
    void deveDesativarUsuario() {
        UUID userId = UUID.randomUUID();

        User user = new User();
        user.setId(userId);
        user.setActive(true);

        when(userRepository.findById(userId))
                .thenReturn(Optional.of(user));

        service.deactivateUser(userId);

        assertFalse(user.isActive());
        verify(userRepository).save(user);
    }

    @Test
    void deveFalharAoDesativarUsuarioInexistente() {
        UUID userId = UUID.randomUUID();

        when(userRepository.findById(userId))
                .thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class,
                () -> service.deactivateUser(userId));
    }

    @Test
    void deveCarregarUsuarioPorEmail() {
        String email = "test@email.com";

        User user = User.builder()
                .id(UUID.randomUUID())
                .email(email)
                .password("encoded")
                .active(true)
                .build();

        Role role = new Role();
        role.setName("PATIENT");

        when(userRepository.findByEmail(email))
                .thenReturn(Optional.of(user));

        UserDetails result = service.loadUserByUsername(email);

        assertNotNull(result);
        assertEquals(email, result.getUsername());
    }

    @Test
    void deveFalharAoCarregarUsuarioInexistente() {
        String email = "test@email.com";

        when(userRepository.findByEmail(email))
                .thenReturn(Optional.empty());

        assertThrows(UsernameNotFoundException.class,
                () -> service.loadUserByUsername(email));
    }
}