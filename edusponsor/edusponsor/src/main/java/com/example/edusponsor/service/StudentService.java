package com.example.edusponsor.service;

import com.example.edusponsor.dto.common.ApiResponse;
import com.example.edusponsor.entity.Institution;
import com.example.edusponsor.entity.Student;
import com.example.edusponsor.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;



@Service
@RequiredArgsConstructor
public class StudentService {

    private final StudentRepository studentRepo;

    public ResponseEntity<?> getStudentDetail(Map<String, Object> userID) {
        try {
            String id = (String) userID.get("id");

            Optional<Student> student = studentRepo.findById(id);

            if (student.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(ApiResponse.error(404, "No Student information available"));
            }

            return ResponseEntity.ok(ApiResponse.mapSuccess(student.get()));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error(500, "Some error occurred. Try Later!"));
        }
    }

    public ResponseEntity<?> updateStudentDetailInfo(Map<String, Object> studentDetail) {
        try {
            String id = (String) studentDetail.get("id");
            Student student = studentRepo.findById(id)
                    .orElseThrow(() -> new RuntimeException("Student not found"));

            student.setFirstName((String) studentDetail.get("firstName"));
            student.setSecondName((String) studentDetail.get("secondName"));
            student.setYear((String) studentDetail.get("year"));
            student.setStudentEmail((String) studentDetail.get("studentEmail"));
            student.setStudentContact((String) studentDetail.get("studentContact"));

            Student updatedStudent = studentRepo.save(student);

            return ResponseEntity.ok(
                    ApiResponse.mapSuccess(updatedStudent, "Student updated successfully")
            );
        } catch (DataIntegrityViolationException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(ApiResponse.error(409, "Data integrity violation"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error(500, "Some error occurred. Try later!"));
        }
    }
}
