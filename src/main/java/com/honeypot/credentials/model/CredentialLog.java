package com.honeypot.credentials.model;

import jakarta.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "credential_logs")
public class CredentialLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;
    private String password;
    private String ip;
    private Instant timestamp = Instant.now();

    public CredentialLog() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    public String getIp() { return ip; }
    public void setIp(String ip) { this.ip = ip; }
    public Instant getTimestamp() { return timestamp; }
    public void setTimestamp(Instant timestamp) { this.timestamp = timestamp; }
}
