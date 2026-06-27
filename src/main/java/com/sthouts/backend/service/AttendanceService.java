package com.sthouts.backend.service;

import com.sthouts.backend.dto.AttendanceDto;
import com.sthouts.backend.model.Attendance;
import com.sthouts.backend.repository.AttendanceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AttendanceService {

    private final AttendanceRepository attendanceRepository;

    public List<AttendanceDto> getAllAttendance() {
        return attendanceRepository.findAll().stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public List<AttendanceDto> submitAttendance(List<AttendanceDto> dtoList) {
        for (AttendanceDto dto : dtoList) {
            Long parsedStaffId;
            try {
                parsedStaffId = Long.parseLong(dto.getStaffId());
            } catch (NumberFormatException e) {
                continue; // Skip invalid IDs like "s1", "s2" from mock data
            }
            Optional<Attendance> existing = attendanceRepository.findByStaffIdAndDate(parsedStaffId, dto.getDate());
            if (existing.isPresent()) {
                Attendance attendance = existing.get();
                attendance.setStatus(dto.getStatus());
                attendanceRepository.save(attendance);
            } else {
                Attendance newAttendance = Attendance.builder()
                        .staffId(parsedStaffId)
                        .date(dto.getDate())
                        .status(dto.getStatus())
                        .build();
                attendanceRepository.save(newAttendance);
            }
        }
        return getAllAttendance(); // Return all records to refresh the client
    }

    private AttendanceDto mapToDto(Attendance attendance) {
        return AttendanceDto.builder()
                .id(attendance.getId())
                .staffId(String.valueOf(attendance.getStaffId()))
                .date(attendance.getDate())
                .status(attendance.getStatus())
                .build();
    }
}
