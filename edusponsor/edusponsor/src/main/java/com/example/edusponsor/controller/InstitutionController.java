package com.example.edusponsor.controller;

import com.example.edusponsor.dto.StudentRequest;
import com.example.edusponsor.entity.Student;
import com.example.edusponsor.service.InstitutionService;
import com.example.edusponsor.service.SponsorshipService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/institution")
@RequiredArgsConstructor
public class InstitutionController {

    private final InstitutionService institutionService;
    private final SponsorshipService sponsorshipService;


    @PostMapping("/get/info")
    public ResponseEntity<?> getInstitution(
            @RequestBody Map<String, Object> userId) {
        return institutionService.getInstitutionDetail(userId);
    }

    @PostMapping("/update/info")
    public ResponseEntity<?> updateInstitution(
            @RequestBody Map<String, Object> institutionDetail) {
        return institutionService.updateInstitutionDetail(institutionDetail);
    }

    @PostMapping("/add/student")
    public ResponseEntity<?> addStudent(@RequestBody Map<String, Object> studentDetail) {
        return institutionService.addStudent(studentDetail);
    }

    @PostMapping("/get/students")
    public ResponseEntity<?> getStudents(@RequestBody Map<String, Object> institution) {
        return institutionService.getStudentsByInstitution(institution);
    }

    @PostMapping("/update/student")
    public ResponseEntity<?> updateStudents(@RequestBody Map<String, Object> student) {
        return institutionService.updateStudent(student);
    }

    @PostMapping("/delete/student")
    public ResponseEntity<?> deleteStudents(@RequestBody Map<String, Object> student) {
        return institutionService.deleteStudent(student);
    }

    @PostMapping("/create/sponsorship")
    public ResponseEntity<?> createSponsorship(@RequestBody Map<String, Object> sponsorship) {
        return sponsorshipService.createSponsorship(sponsorship);
    }

    @PostMapping("/check/sponsorship/exists")
    public ResponseEntity<?> checkSponsorshipExists(@RequestBody Map<String, Object> sponsorship) {
        return sponsorshipService.checkSponsorshipExists(sponsorship);
    }
}
