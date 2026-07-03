package com.sthouts.backend.controller;

import com.sthouts.backend.dto.ContactRequestDto;
import com.sthouts.backend.model.ContactMessage;
import com.sthouts.backend.repository.ContactMessageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/contact")
@RequiredArgsConstructor
public class ContactController {

    private final ContactMessageRepository contactMessageRepository;

    @PostMapping
    public ResponseEntity<Map<String, Object>> submitContact(@RequestBody ContactRequestDto request) {
        ContactMessage msg = ContactMessage.builder()
                .name(request.getName())
                .email(request.getEmail())
                .company(request.getCompany())
                .phone(request.getPhone())
                .message(request.getMessage())
                .build();

        ContactMessage saved = contactMessageRepository.save(msg);

        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("referenceId", "REF-" + saved.getId() + "-" + System.currentTimeMillis() % 1000);
        response.put("message", "Thank you for contacting Thoughtit Enterprise team. Our cloud POS advisors will reach out within 2 hours.");
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<List<ContactMessage>> getAllMessages() {
        return ResponseEntity.ok(contactMessageRepository.findAllByOrderByCreatedAtDesc());
    }
}
