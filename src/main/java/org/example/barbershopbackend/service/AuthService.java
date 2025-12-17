package org.example.barbershopbackend.service;

import org.example.barbershopbackend.model.User;
import org.example.barbershopbackend.repository.UserRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final UserRepository userRepo;
    private final BCryptPasswordEncoder passwordEncoder;

    public AuthService(UserRepository userRepo,
                       BCryptPasswordEncoder passwordEncoder) {
        this.userRepo = userRepo;
        this.passwordEncoder = passwordEncoder;
    }

    public String signup(String name,
                         String phoneNumber,
                         String email,
                         String password) throws Exception {

        if (userRepo.findByEmail(email) != null) {
            throw new Exception("Email already exists");
        }

        String encodedPassword = passwordEncoder.encode(password);

        User user = new User(name, phoneNumber, email, encodedPassword);
        userRepo.save(user);

        return "Signup successful";
    }

    public String login(String email, String password) throws Exception {
        User user = userRepo.findByEmail(email);

        if (user == null) {
            throw new Exception("User not found");
        }

        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new Exception("Incorrect password");
        }

        return "Login successful";
    }
}
