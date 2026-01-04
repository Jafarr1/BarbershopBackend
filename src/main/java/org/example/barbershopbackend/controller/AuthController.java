package org.example.barbershopbackend.controller;

import io.jsonwebtoken.Claims;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.example.barbershopbackend.model.User;
import org.example.barbershopbackend.security.JwtUtil;
import org.example.barbershopbackend.service.AuthService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/signup")
    public ResponseEntity<?> signup(@RequestBody Map<String, String> body) {
        try {
            String name = body.get("name");
            String phoneNumber = body.get("phoneNumber");
            String email = body.get("email");
            String password = body.get("password");

            if (name == null || phoneNumber == null || email == null || password == null) {
                return ResponseEntity.badRequest().body("All fields are required");
            }

            authService.signup(name, phoneNumber, email, password);

            return ResponseEntity.ok(Map.of("message", "Signup successful"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("message", e.getMessage()));
        }
    }



    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> body, HttpServletResponse response) {

        try {
            String email = body.get("email");
            String password = body.get("password");

            if (email == null || password == null) {
                return ResponseEntity.badRequest().body("Email and password required");
            }

            User user = authService.loginAndGetUser(email, password);
            String token = JwtUtil.generateToken(
                    user.getUserId(),
                    user.getRole().name(),
                    user.getEmail()
            );

            response.addHeader(
                    "Set-Cookie",
                    "jwt=" + token + "; Path=/; HttpOnly; Secure; SameSite=None"
            );


            return ResponseEntity.ok(Map.of(
                    "userId", user.getUserId(),
                    "role", user.getRole().name(),
                    "email", user.getEmail()
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }



        @GetMapping("/me")
        public Map<String, Object> me(@CookieValue(name = "jwt", required = false) String token) {
            if (token == null) {
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
            }

            Claims claims = JwtUtil.validateToken(token).getBody();

            Map<String, Object> userInfo = new HashMap<>();
            userInfo.put("userId", claims.get("userId", Long.class));
            userInfo.put("role", claims.get("role", String.class));
            userInfo.put("email", claims.get("email", String.class));
            return userInfo;
        }
    }
