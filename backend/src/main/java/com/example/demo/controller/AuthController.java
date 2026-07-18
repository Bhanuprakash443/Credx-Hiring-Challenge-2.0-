package com.example.demo.controller;

import com.example.demo.dto.RegisterRequest;
import com.example.demo.model.*;
import com.example.demo.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private StudentRepository studentRepository;
    @Autowired
    private CompanyRepository companyRepository;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody User loginRequest) {
        Optional<User> userOpt = userRepository.findByUsername(loginRequest.getUsername());
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            if (user.getPassword().equals(loginRequest.getPassword())) {
                return ResponseEntity.ok(user);
            }
        }
        return ResponseEntity.status(401).body("Invalid username or password");
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest req) {
        if (userRepository.findByUsername(req.getUsername()).isPresent()) {
            return ResponseEntity.badRequest().body("Username already exists");
        }

        Long associatedId = null;

        if (req.getRole() == UserRole.STUDENT) {
            Student s = new Student(
                req.getName(),
                req.getEmail(),
                req.getRollNumber(),
                req.getDepartment(),
                req.getCgpa(),
                req.getResumeText()
            );
            s = studentRepository.save(s);
            associatedId = s.getId();
        } else if (req.getRole() == UserRole.COMPANY) {
            Company c = new Company(
                req.getName(),
                req.getIndustry(),
                req.getWebsite(),
                req.getDescription(),
                req.getContactEmail()
            );
            c = companyRepository.save(c);
            associatedId = c.getId();
        }

        User u = new User(
            req.getUsername(),
            req.getPassword(),
            req.getEmail(),
            req.getRole(),
            associatedId
        );
        u = userRepository.save(u);

        return ResponseEntity.ok(u);
    }
}
