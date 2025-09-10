package com.example.edusponsor.controller;
import com.example.edusponsor.service.StudentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/student")
@RequiredArgsConstructor
public class StudentController {
    private final StudentService studentService;

    // Get logged-in user profile
    @PostMapping("/get/info")
    public ResponseEntity<?> getStudent(
            @RequestBody Map<String, Object> userId) {
        return studentService.getStudentDetail(userId);
    }
    // Get logged-in user profile
    @PostMapping("/update/info")
    public ResponseEntity<?> updateStudent(
            @RequestBody Map<String, Object> userId) {
        return studentService.updateStudentDetailInfo(userId);
    }
}
