package com.example.edusponsor.service;

import com.example.edusponsor.dto.StudentRequest;
import com.example.edusponsor.dto.common.ApiResponse;
import com.example.edusponsor.entity.*;
import com.example.edusponsor.enums.UserRole;
import com.example.edusponsor.repository.InstitutionRepository;
import com.example.edusponsor.repository.SponsorshipRepository;
import com.example.edusponsor.repository.StudentRepository;
import com.example.edusponsor.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class InstitutionService {

    private final StudentRepository studentRepo;
    private final UserRepository userRepo;
    private final PasswordEncoder passwordEncoder;
    private final InstitutionRepository institutionRepository;
    private final SponsorshipRepository sponsorshipRepo;

    public ResponseEntity<?> getInstitutionDetail(Map<String, Object> userID) {
        try {
            String id = (String) userID.get("id");

            Optional<Institution> institution = institutionRepository.findById(id);

            if (institution.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(ApiResponse.error(404, "No institution information available"));
            }

            return ResponseEntity.ok(ApiResponse.mapSuccess(institution.get()));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error(500, "Some error occurred. Try Later!"));
        }
    }


    public ResponseEntity<?> updateInstitutionDetail(Map<String, Object> instituteDetail) {
        try {
            String id = (String) instituteDetail.get("id");
            String instituteName = (String) instituteDetail.get("instituteName");
            String email = (String) instituteDetail.get("email");
            String instituteId = (String) instituteDetail.get("instituteId");

            // Try to find existing admin
            Institution institution = institutionRepository.findById(id).orElse(null);

            if (institution == null) {
                // Create new admin if not found
                institution = new Institution();
                institution.setId(id);
            }

            // Update fields (applies to both new & existing)
            institution.setInstituteName(instituteName);
            institution.setEmail(email);
            institution.setInstituteId(instituteId);

            // Save (JPA will insert if new, update if existing)
            Institution savedInstitution = institutionRepository.save(institution);

            return ResponseEntity.ok(ApiResponse.mapSuccess(savedInstitution, "successfully updated!"));

        } catch (DataIntegrityViolationException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(ApiResponse.error(409, "Data integrity violation"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error(500, "Some error occurred. Try later!"));
        }
    }

    public ResponseEntity<?> addStudent(Map<String, Object> studentDetail) {
        try {
            String username = (String) studentDetail.get("username");

            if (userRepo.existsByUsername(username)) {
                return ResponseEntity.badRequest()
                        .body(ApiResponse.error(400, "Username already exists"));
            }

            // 2. Build Student entity
            Student student = Student.builder()
                    .instituteId((String) studentDetail.get("instituteId"))
                    .firstName((String) studentDetail.get("firstName"))
                    .secondName((String) studentDetail.get("secondName"))
                    .department((String) studentDetail.get("department"))
                    .year((String) studentDetail.get("year"))
                    .studentEmail((String) studentDetail.get("studentEmail"))
                    .studentContact((String) studentDetail.get("studentContact"))
                    .username(username)
                    .password((String) studentDetail.get("password")) // ⚠ hash this later
                    .parentName((String) studentDetail.get("parentName"))
                    .parentEmail((String) studentDetail.get("parentEmail"))
                    .parentContact((String) studentDetail.get("parentContact"))
                    .relation((String) studentDetail.get("relation"))
                    .annualIncome((String) studentDetail.get("annualIncome"))
                    .incomeProofBaseSF((String) studentDetail.get("incomeProof"))
                    .build();

            // 3. Save student
            Student savedStudent = studentRepo.save(student);

            // 4. Create User entity (refId = studentId)
            User user = User.builder()
                    .username(username)
                    .password(passwordEncoder.encode((String) studentDetail.get("password")))
                    .role(UserRole.STUDENT)
                    .refId(savedStudent.getId())
                    .build();

            userRepo.save(user);

            // 5. Return success response
            return ResponseEntity.ok(ApiResponse.mapSuccess(
                    savedStudent,
                    "Student registered successfully"
            ));

        } catch (Exception e) {
            // 6. Handle unexpected exceptions
            return ResponseEntity.internalServerError()
                    .body(ApiResponse.error(500, "Failed to register student", e.getMessage()));
        }
    }

    public ResponseEntity<?>  getStudentsByInstitution(Map<String, Object> institutionId) {
        String institutionID = (String) institutionId.get("id");
        List<Student> students= studentRepo.findByInstituteId(institutionID);
        return ResponseEntity.ok(ApiResponse.listSuccess(students));
    }

    public ResponseEntity<?> updateStudent(Map<String, Object> studentDetail) {
        try {
            String id = (String) studentDetail.get("id");
            Student student = studentRepo.findById(id)
                    .orElseThrow(() -> new RuntimeException("Student not found"));

            student.setYear((String) studentDetail.get("year"));
            student.setStudentEmail((String) studentDetail.get("studentEmail"));
            student.setStudentContact((String) studentDetail.get("studentContact"));
            student.setParentEmail((String) studentDetail.get("parentEmail"));
            student.setParentContact((String) studentDetail.get("parentContact"));
            student.setAnnualIncome((String) studentDetail.get("annualIncome"));

            Student updatedStudent = studentRepo.save(student);

            return ResponseEntity.ok(
                    ApiResponse.mapSuccess(updatedStudent, "Student updated successfully")
            );

        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error(404, e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error(500, "Error updating student"));
        }
    }

    public ResponseEntity<?> deleteStudent(Map<String, Object> studentDetail) {
        try {
            String studentId = (String) studentDetail.get("id");

            // 1. Check Student exists
            if (!studentRepo.existsById(studentId)) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(ApiResponse.error(404, "Student not found"));
            }

            // 2. Delete Student
            studentRepo.deleteById(studentId);

            // 3. Delete associated User (if exists)
            userRepo.findAll().stream()
                    .filter(u -> studentId.equals(u.getRefId()))
                    .findFirst()
                    .ifPresent(user -> userRepo.deleteById(user.getId()));

            // 4. Delete Sponsorships (if exists)
            if (!sponsorshipRepo.findByStudentId(studentId).isEmpty()) {
                sponsorshipRepo.findByStudentId(studentId)
                        .forEach(s -> sponsorshipRepo.deleteById(s.getId()));
            }

            return ResponseEntity.ok(
                    ApiResponse.mapSuccess(null, "Student and related data deleted successfully")
            );

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error(500, "Error deleting student", e.getMessage()));
        }
    }

}
