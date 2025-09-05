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


    // Get Dashboard Info
    public ResponseEntity<?> dashboardData() {
        try {
            List<Map<String, Object>> dashboardData = List.of(
                    Map.of(
                            "displayName", "Institutions",
                            "approved", institutionRepo.findByApprovedTrue().size(),
                            "pending", institutionRepo.findByApprovedFalse().size(),
                            "total", institutionRepo.count()
                    ),
                    Map.of(
                            "displayName", "Sponsors",
                            "approved", sponsorRepo.findByApprovedTrue().size(),
                            "pending", sponsorRepo.findByApprovedFalse().size(),
                            "total", sponsorRepo.count()
                    )
            );

            return ResponseEntity.ok(ApiResponse.listSuccess(dashboardData));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error(500, "Some error occurred. Try later!"));
        }
    }

    // Get all institutions
    public ResponseEntity<?> getInstitutions() {
        List<Institution> institutions = institutionRepo.findByApprovedTrue();
        return ResponseEntity.ok(ApiResponse.listSuccess(institutions));
    }

    public ResponseEntity<?> getSponsors() {
        List<Sponsor> sponsor = sponsorRepo.findByApprovedTrue();
        return ResponseEntity.ok(ApiResponse.listSuccess(sponsor));
    }

    public ResponseEntity<?> getPendingInstitutions() {
        List<Institution> institutions = institutionRepo.findByApprovedFalse();
        return ResponseEntity.ok(ApiResponse.listSuccess(institutions));
    }


    public ResponseEntity<?> getPendingSponsors() {
        List<Sponsor> sponsors = sponsorRepo.findByApprovedFalse();
        return ResponseEntity.ok(ApiResponse.listSuccess(sponsors));
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

    public ResponseEntity<?> approveSponsor(Map<String, Object> sponsor) {

        String id = (String) sponsor.get("id");
        String username = (String) sponsor.get("username");
        String password = (String) sponsor.get("password");

        Sponsor sponsor1 = sponsorRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Sponsor not found"));

        sponsor1.setApproved(true);
        sponsorRepo.save(sponsor1);

        User user = User.builder()
                .id(id)
                .username(username)
                .password(passwordEncoder.encode(password))
                .role(UserRole.SPONSOR)
                .build();

        userRepo.save(user);
        return ResponseEntity.ok(ApiResponse.mapSuccess(user,"Sponsor approved and user created!"));
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

    public ResponseEntity<?> rejectSponsor(Map<String, Object> sponsor) {

        String id = (String) sponsor.get("id");


        Sponsor sponsor1 = sponsorRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Sponsor not found"));

        if(sponsor1==null){
            return ResponseEntity.ok(ApiResponse.error(404,"Sponsor not found!"));
        }

        sponsorRepo.deleteById(id);
        return ResponseEntity.ok(ApiResponse.mapSuccess(sponsor1,"Institution rejected and removed!"));
    }
}
