package com.honeypot.credentials.controller;

import com.honeypot.credentials.model.CredentialLog;
import com.honeypot.credentials.repository.CredentialLogRepository;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.*;

@RestController
@RequestMapping("/honeypot")
public class HoneypotController {

    private final CredentialLogRepository repo;

    // In-memory demo users for real login
    private final List<Map<String, String>> demoUsers = new ArrayList<>();

    public HoneypotController(CredentialLogRepository repo) {
        this.repo = repo;
    }

    @PostConstruct
    public void initDemoUsers() {
        // Add your 5 demo users
        demoUsers.add(Map.of(
                "username", "shreya@example.com",
                "password", "shreya@123",
                "email", "shreya@example.com",
                "company", "ITsolution",
                "name", "SHREYA",
                "role", "Engineer",
                "phone", "+91 90000 00001"
        ));
        demoUsers.add(Map.of(
                "username", "chandra@example.com",
                "password", "chandra@123",
                "email", "chandrasekhar@101.com",
                "company", "ITsolution",
                "name", "CHANDRA SEKHAR",
                "role", "Manager",
                "phone", "+91 90000 00002"
        ));
        demoUsers.add(Map.of(
                "username", "harika@example.com",
                "password", "harika@123",
                "email", "harika@example.com",
                "company", "ITsolution",
                "name", "HARIKA",
                "role", "DevOps",
                "phone", "+91 90000 00003"
        ));
        demoUsers.add(Map.of(
                "username", "hanuma@example.com",
                "password", "hanuma@123",
                "email", "hanuma@example.com",
                "company", "ITsolution",
                "name", " HANUMA",
                "role", "Analyst",
                "phone", "+91 90000 00004"
        ));
        demoUsers.add(Map.of(
                "username", "sriharsha@example.com",
                "password", "sriharsha@123",
                "email", "sriharsha@example.com",
                "company", "ITsolution",
                "name", "SRIHARSHA",
                "role", "Designer",
                "phone", "+91 90000 00005"
        ));
    }

    @PostMapping("/login")
    public ResponseEntity<?> fakeLogin(@RequestBody Map<String, String> body, HttpServletRequest request) {
        String username = body.getOrDefault("username", body.getOrDefault("user", ""));
        String password = body.getOrDefault("password", body.getOrDefault("pass", ""));
        String ip = request.getRemoteAddr();
        System.out.println("Received body: " + body);

        // First, check if user matches any real demo user
        Optional<Map<String, String>> matchedUser = demoUsers.stream()
                .filter(u -> u.get("username").equals(username) && u.get("password").equals(password))
                .findFirst();

        if (matchedUser.isPresent()) {
            // Return user details safely (without password)
            Map<String, String> user = matchedUser.get();
            Map<String, Object> response = new HashMap<>();
            response.put("id", demoUsers.indexOf(user) + 1);
            response.put("username", user.get("username"));
            response.put("name", user.get("name"));
            response.put("email", user.get("email"));
            response.put("company", user.get("company"));
            response.put("role", user.get("role"));
            response.put("phone", user.get("phone"));
            response.put("message", "Login successful (real user)");
            return ResponseEntity.ok(response);
        }

        // Otherwise, log as honeypot
        CredentialLog log = new CredentialLog();
        log.setUsername(username);
        log.setPassword(password);
        log.setIp(ip);
        log.setTimestamp(Instant.now());
        repo.save(log);

        return ResponseEntity.status(401).body(Map.of(
                "error", "Invalid credentials",
                "requestId", log.getId()
        ));
    }

    @GetMapping("/credentials")
    public List<CredentialLog> getAll() {
        return repo.findAll();
    }
    @GetMapping("/contact")
    public Map<String, String> getContactInfo() {
        // You can later update these values dynamically
        Map<String, String> contact = new HashMap<>();
        contact.put("phone", "+91 9000040005"); // your current phone
        contact.put("email", "info@company.com"); // your current email
        return contact;
    }

}
