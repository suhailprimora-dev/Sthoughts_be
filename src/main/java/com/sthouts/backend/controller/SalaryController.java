package com.sthouts.backend.controller;

import com.sthouts.backend.dto.PayrollRecordDto;
import com.sthouts.backend.dto.SalaryStructureDto;
import com.sthouts.backend.service.SalaryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class SalaryController {

    private final SalaryService salaryService;

    @GetMapping("/salary-structure")
    public ResponseEntity<List<SalaryStructureDto>> getAllSalaryStructures() {
        return ResponseEntity.ok(salaryService.getAllSalaryStructures());
    }

    @GetMapping("/salary-structure/{staffId}")
    public ResponseEntity<SalaryStructureDto> getSalaryStructureByStaffId(@PathVariable Long staffId) {
        return ResponseEntity.ok(salaryService.getSalaryStructureByStaffId(staffId));
    }

    @PostMapping("/salary-structure")
    public ResponseEntity<SalaryStructureDto> saveOrUpdateSalaryStructure(@RequestBody SalaryStructureDto dto) {
        return ResponseEntity.ok(salaryService.saveOrUpdateSalaryStructure(dto));
    }

    @PutMapping("/salary-structure")
    public ResponseEntity<SalaryStructureDto> updateSalaryStructure(@RequestBody SalaryStructureDto dto) {
        return ResponseEntity.ok(salaryService.saveOrUpdateSalaryStructure(dto));
    }

    @GetMapping("/payroll")
    public ResponseEntity<List<PayrollRecordDto>> getPayrollRecords(
            @RequestParam(required = false) String month,
            @RequestParam(required = false) String staffId) {
        return ResponseEntity.ok(salaryService.getPayrollRecords(month, staffId));
    }

    @PostMapping("/payroll")
    public ResponseEntity<PayrollRecordDto> submitOrUpdatePayroll(@RequestBody PayrollRecordDto dto) {
        return ResponseEntity.ok(salaryService.submitOrUpdatePayroll(dto));
    }

    @PutMapping("/payroll/{id}/status")
    public ResponseEntity<PayrollRecordDto> updatePayrollStatus(@PathVariable Long id, @RequestParam String status) {
        return ResponseEntity.ok(salaryService.updateStatus(id, status));
    }

    @DeleteMapping("/payroll/{id}")
    public ResponseEntity<Void> deletePayrollRecord(@PathVariable Long id) {
        salaryService.deletePayrollRecord(id);
        return ResponseEntity.ok().build();
    }
}
