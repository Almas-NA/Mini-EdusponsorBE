package com.example.edusponsor.service;

import com.example.edusponsor.dto.common.ApiResponse;
import com.example.edusponsor.entity.*;
import com.example.edusponsor.enums.UserRole;
import com.example.edusponsor.repository.*;
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
public class AdminService {

    private final InstitutionRepository institutionRepo;
    private final SponsorRepository sponsorRepo;
    private final UserRepository userRepo;
    private final PasswordEncoder passwordEncoder;
    private final AdminRepository adminRepository;



    // Get all institutions
    public ResponseEntity<?> getInstitutions() {
        List<Institution> institutions = institutionRepo.findAll();
        return ResponseEntity.ok(ApiResponse.listSuccess(institutions));
    }

    public ResponseEntity<?> getSponsors() {
        List<Sponsor> sponsor = sponsorRepo.findAll();
        return ResponseEntity.ok(ApiResponse.listSuccess(sponsor));
    }

    public ResponseEntity<?> getPendingInstitutions() {
        List<Institution> institutions = institutionRepo.findByApprovedFalse();
        return ResponseEntity.ok(ApiResponse.listSuccess(institutions));
    }


    public List<Sponsor> getPendingSponsors() {
        return sponsorRepo.findByApprovedFalse();
    }





    public ResponseEntity<?> getAdminDetail(Map<String, Object> userID) {

        try{
            String id = (String) userID.get("id");

            Optional<Admin> admin = adminRepository.findByRefId(id);

            if (admin.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(ApiResponse.error(404,"No admin information available"));
            }

            return ResponseEntity.ok(ApiResponse.mapSuccess(admin));

        } catch (DataIntegrityViolationException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(ApiResponse.error(404, "Some error occurred.Try Later!"));

        } catch (Exception e) {
            // A generic catch block for any other unexpected errors
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error(404, "Some error occurred.Try Later!"));
        }
    }


    public ResponseEntity<?> updateAdminDetail(Map<String, Object> adminDetail) {
        try {
            String id = (String) adminDetail.get("id");
            String firstName = (String) adminDetail.get("firstName");
            String secondName = (String) adminDetail.get("secondName");

            // Try to find existing admin
            Admin admin = adminRepository.findByRefId(id).orElse(null);

            if (admin == null) {
                // Create new admin if not found
                admin = new Admin();
                admin.setRefId(id);
            }

            // Update fields (applies to both new & existing)
            admin.setFirstName(firstName);
            admin.setSecondName(secondName);

            // Save (JPA will insert if new, update if existing)
            Admin savedAdmin = adminRepository.save(admin);

            return ResponseEntity.ok(ApiResponse.mapSuccess(savedAdmin, "successfully updated!"));

        } catch (DataIntegrityViolationException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(ApiResponse.error(409, "Data integrity violation"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error(500, "Some error occurred. Try later!"));
        }
    }


    public ResponseEntity<?> approveInstitution(Map<String, Object> institute) {

        String id = (String) institute.get("id");
        String username = (String) institute.get("username");
        String password = (String) institute.get("password");

        Institution institution = institutionRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Institution not found"));

        institution.setApproved(true);
        institutionRepo.save(institution);

        User user = User.builder()
                .id(id)
                .username(username)
                .password(passwordEncoder.encode(password))
                .role(UserRole.INSTITUTION)
                .build();

        userRepo.save(user);
        return ResponseEntity.ok(ApiResponse.mapSuccess(user,"Institution approved and user created!"));
    }

    public ResponseEntity<?> approveSponsor(String sponsorId, String username, String password) {
        Sponsor sponsor = sponsorRepo.findById(sponsorId)
                .orElseThrow(() -> new RuntimeException("Sponsor not found"));

        sponsor.setApproved(true);
        sponsorRepo.save(sponsor);

        User user = User.builder()
                .id(sponsorId)
                .username(username)
                .password(passwordEncoder.encode(password))
                .role(UserRole.SPONSOR)
                .build();

        userRepo.save(user);
        return ResponseEntity.ok("Sponsor approved and user created");
    }

    public ResponseEntity<?> rejectInstitution(Map<String, Object> institute) {

        String id = (String) institute.get("id");

        Institution institution = institutionRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Institution not found"));

        if(institution==null){
            return ResponseEntity.ok(ApiResponse.error(404,"Institution not found!"));
        }

        institutionRepo.deleteById(id);
        return ResponseEntity.ok(ApiResponse.mapSuccess(institution,"Institution rejected and removed!"));
    }

    public ResponseEntity<?> rejectSponsor(String sponsorId) {
        sponsorRepo.deleteById(sponsorId);
        return ResponseEntity.ok("Sponsor rejected and removed");
    }
}
