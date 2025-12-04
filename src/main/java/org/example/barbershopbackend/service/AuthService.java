package org.example.barbershopbackend.service;

import org.example.barbershopbackend.model.User;
import org.example.barbershopbackend.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final UserRepository userRepo;

    public AuthService(UserRepository userRepo) {
        this.userRepo = userRepo;
    }

    public String signup(String email, String password) throws Exception {
        if (userRepo.findByEmail(email) != null) {
            throw new Exception("Email already exists");
        }

        User user = new User(email, password);
        userRepo.save(user);

        return "Signup successful";
    }

    public String login(String email, String password) throws Exception {
        User user = userRepo.findByEmail(email);

        if (user == null) {
            throw new Exception("User not found");
        }

        if (!user.getPassword().equals(password)) {
            throw new Exception("Incorrect password");
        }

        return "Login successful";
    }
}
