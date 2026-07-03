package com.sthouts.backend.controller;

import com.sthouts.backend.dto.AuthResponseDto;
import com.sthouts.backend.dto.LoginRequestDto;
import com.sthouts.backend.dto.SignupRequestDto;
import com.sthouts.backend.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/signup")
    public ResponseEntity<?> signup(@RequestBody SignupRequestDto request) {
        try {
            AuthResponseDto response = authService.registerTenant(request);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, String> err = new HashMap<>();
            err.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(err);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequestDto request) {
        try {
            AuthResponseDto response = authService.login(request);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, String> err = new HashMap<>();
            err.put("error", e.getMessage());
            return ResponseEntity.status(401).body(err);
        }
    }

    @GetMapping("/check-subdomain")
    public ResponseEntity<Map<String, Boolean>> checkSubdomain(@RequestParam String subdomain) {
        boolean available = authService.checkSubdomainAvailable(subdomain);
        Map<String, Boolean> res = new HashMap<>();
        res.put("available", available);
        return ResponseEntity.ok(res);
    }
}
