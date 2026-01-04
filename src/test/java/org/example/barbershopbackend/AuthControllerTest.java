package org.example.barbershopbackend;

import org.example.barbershopbackend.controller.AuthController;
import org.example.barbershopbackend.model.Role;
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
        User user = mock(User.class);
        when(user.getUserId()).thenReturn(1L);
        when(user.getEmail()).thenReturn("alice@example.com");
        when(user.getRole()).thenReturn(Role.USER);

        when(authService.loginAndGetUser("alice@example.com", "pass123")).thenReturn(user);

        String jsonBody = "{\"email\":\"alice@example.com\",\"password\":\"pass123\"}";

        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonBody))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userId").value(1))
                .andExpect(jsonPath("$.role").value("USER"));

        verify(authService, times(1)).loginAndGetUser("alice@example.com", "pass123");
    }

}
