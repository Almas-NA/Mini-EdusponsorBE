package com.example.edusponsor.controller;

import com.example.edusponsor.dto.StudentRequest;
import com.example.edusponsor.entity.Student;
import com.example.edusponsor.service.InstitutionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/institution")
@RequiredArgsConstructor
public class InstitutionController {

    private final InstitutionService institutionService;

    @PostMapping("/add-student")
    public ResponseEntity<?> addStudent(@RequestBody StudentRequest request) {
        return institutionService.addStudent(request);
    }

    @GetMapping("/students/{institutionId}")
    public ResponseEntity<List<Student>> getStudents(@PathVariable String institutionId) {
        return ResponseEntity.ok(institutionService.getStudentsByInstitution(institutionId));
    }
}
