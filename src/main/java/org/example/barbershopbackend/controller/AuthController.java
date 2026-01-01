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

@CrossOrigin(origins = "http://localhost:63342", allowCredentials = "true")
@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/signup")
    public ResponseEntity<String> signup(
            @RequestParam String name,
            @RequestParam String phoneNumber,
            @RequestParam String email,
            @RequestParam String password) {

        try {
            String msg = authService.signup(name, phoneNumber, email, password);
            return ResponseEntity.ok(msg);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
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

            Cookie cookie = new Cookie("jwt", token);
            cookie.setHttpOnly(true);
            cookie.setPath("/");
            cookie.setMaxAge(60 * 60); // 1 hour
            cookie.setSecure(false); // for localhost
            cookie.setDomain("localhost"); // optional
            response.addCookie(cookie);

            return ResponseEntity.ok(Map.of(
                    "userId", user.getUserId(),
                    "role", user.getRole().name()
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
