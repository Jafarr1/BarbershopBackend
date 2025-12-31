package org.example.barbershopbackend;

import org.example.barbershopbackend.controller.AuthController;
import org.example.barbershopbackend.model.User;
import org.example.barbershopbackend.service.AuthService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class AuthControllerTest {

    @Mock
    private AuthService authService;

    @InjectMocks
    private AuthController authController;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(authController).build();
    }

    @Test
    void loginSuccess() throws Exception {
        User user = new User("Alice", "12345678", "alice@example.com", "hashedPass");
        when(authService.loginAndGetUser("alice@example.com", "pass123")).thenReturn(user);

        mockMvc.perform(post("/auth/login")
                        .param("email", "alice@example.com")
                        .param("password", "pass123")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Alice"))
                .andExpect(jsonPath("$.email").value("alice@example.com"));

        verify(authService, times(1)).loginAndGetUser("alice@example.com", "pass123");
    }
}
