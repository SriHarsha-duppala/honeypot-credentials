package com.honeypot.credentials.controller;

import com.honeypot.credentials.model.CredentialLog;
import com.honeypot.credentials.repository.CredentialLogRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/honeypot")
public class HoneypotController {

    private final CredentialLogRepository repo;

    public HoneypotController(CredentialLogRepository repo) {
        this.repo = repo;
    }

    @PostMapping("/login")
    public ResponseEntity<?> fakeLogin(@RequestBody Map<String, String> body, HttpServletRequest request) {
    	String username = body.getOrDefault("username", body.getOrDefault("user",""));
    	String password = body.getOrDefault("password", body.getOrDefault("pass",""));
        String ip = request.getRemoteAddr();
        System.out.println("Received body: " + body);

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
}
