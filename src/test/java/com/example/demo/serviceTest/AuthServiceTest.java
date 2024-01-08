package com.example.demo.serviceTest;

import com.example.demo.entity.UserEntity;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.AuthService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;
import static org.mockito.ArgumentMatchers.any;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

@SpringBootTest
@AutoConfigureMockMvc(addFilters=false)
@ActiveProfiles("test")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class AuthServiceTest {

    @InjectMocks
    private AuthService authService;

    @Mock
    private UserRepository userRepository;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testRegisterUser() {
        UserEntity userToRegister = new UserEntity();
        userToRegister.setUserName("John Doe");
        userToRegister.setEmail("john@example.com");
        userToRegister.setPassword("password");

        when(userRepository.save(any(UserEntity.class))).thenReturn(userToRegister);

        UserEntity registeredUser = authService.registerUser(userToRegister);

        assertNotNull(registeredUser);
        assertEquals("John Doe", registeredUser.getUserName());
        assertEquals("john@example.com", registeredUser.getEmail());
        assertEquals("password", registeredUser.getPassword());

        verify(userRepository, times(1)).save(any(UserEntity.class));
    }

    @Test
    public void testLoginUser() {
        UserEntity testUser = new UserEntity();
        testUser.setEmail("john@example.com");
        testUser.setPassword("password");

        when(userRepository.findByEmailAndPassword("john@example.com", "password")).thenReturn(testUser);

        UserEntity authenticatedUser = authService.loginUser("john@example.com", "password");

        assertNotNull(authenticatedUser);
        assertEquals("john@example.com", authenticatedUser.getEmail());
        assertEquals("password", authenticatedUser.getPassword());

        verify(userRepository, times(1)).findByEmailAndPassword("john@example.com", "password");
    }

    @Test
    public void testLoginUserInvalidCredentials() {
        when(userRepository.findByEmailAndPassword("unknown@example.com", "invalid")).thenReturn(null);

        UserEntity authenticatedUser = authService.loginUser("unknown@example.com", "invalid");

        assertNull(authenticatedUser);

        verify(userRepository, times(1)).findByEmailAndPassword("unknown@example.com", "invalid");
    }

}

