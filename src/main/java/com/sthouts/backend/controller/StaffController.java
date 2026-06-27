package com.sthouts.backend.controller;

import com.sthouts.backend.dto.StaffDto;
import com.sthouts.backend.service.StaffService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/staff")
@RequiredArgsConstructor
public class StaffController {

    private final StaffService staffService;

    @GetMapping
    public ResponseEntity<List<StaffDto>> getAllActiveStaff() {
        return ResponseEntity.ok(staffService.getAllActiveStaff());
    }

    @PostMapping
    public ResponseEntity<StaffDto> addStaff(@RequestBody StaffDto dto) {
        return ResponseEntity.ok(staffService.addStaff(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<StaffDto> updateStaff(@PathVariable Long id, @RequestBody StaffDto dto) {
        return ResponseEntity.ok(staffService.updateStaff(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteStaff(@PathVariable Long id) {
        staffService.deleteStaff(id);
        return ResponseEntity.ok().build();
    }
}
