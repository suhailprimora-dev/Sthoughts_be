package com.sthouts.backend.controller;

import com.sthouts.backend.dto.AttendanceDto;
import com.sthouts.backend.service.AttendanceService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/attendance")
@RequiredArgsConstructor
public class AttendanceController {

    private final AttendanceService attendanceService;

    @GetMapping
    public ResponseEntity<List<AttendanceDto>> getAllAttendance() {
        return ResponseEntity.ok(attendanceService.getAllAttendance());
    }

    @PostMapping("/submit")
    public ResponseEntity<List<AttendanceDto>> submitAttendance(@RequestBody List<AttendanceDto> dtoList) {
        return ResponseEntity.ok(attendanceService.submitAttendance(dtoList));
    }
}
