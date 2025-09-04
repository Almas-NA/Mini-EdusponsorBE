package com.example.edusponsor.service;

import com.example.edusponsor.dto.StudentRequest;
import com.example.edusponsor.entity.Student;
import com.example.edusponsor.entity.User;
import com.example.edusponsor.enums.UserRole;
import com.example.edusponsor.repository.StudentRepository;
import com.example.edusponsor.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class InstitutionService {

    private final StudentRepository studentRepo;
    private final UserRepository userRepo;
    private final PasswordEncoder passwordEncoder;

    public ResponseEntity<?> addStudent(StudentRequest request) {
        // Save student details
        Student student = Student.builder()
                .name(request.getName())
                .email(request.getEmail())
                .institutionId(request.getInstitutionId())
                .build();
        student = studentRepo.save(student);

        // Create user credentials
        User user = User.builder()
                .id(student.getId())
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(UserRole.STUDENT)
                .build();
        userRepo.save(user);

        return ResponseEntity.ok("Student added and login created");
    }

    public List<Student> getStudentsByInstitution(String institutionId) {
        return studentRepo.findByInstitutionId(institutionId);
    }

}
