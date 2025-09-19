package com.example.edusponsor.service;

import com.example.edusponsor.dto.common.ApiResponse;
import com.example.edusponsor.entity.Institution;
import com.example.edusponsor.entity.Sponsor;
import com.example.edusponsor.entity.Sponsorship;
import com.example.edusponsor.repository.SponsorRepository;
import com.example.edusponsor.repository.SponsorshipRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SponsorService {


    private final SponsorRepository sponsorRepository;
    private final SponsorshipRepository sponsorshipRepository;


    public ResponseEntity<?> getSponsorDetail(Map<String, Object> userID) {
        try {
            String id = (String) userID.get("id");

            Optional<Sponsor> sponsor = sponsorRepository.findById(id);

            if (sponsor.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(ApiResponse.error(404, "No institution information available"));
            }

            return ResponseEntity.ok(ApiResponse.mapSuccess(sponsor.get()));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error(500, "Some error occurred. Try Later!"));
        }
    }

    public ResponseEntity<?> updateSponsorDetail(Map<String, Object> sponsorDetail) {
        try {
            String id = (String) sponsorDetail.get("id");
            String fullName = (String) sponsorDetail.get("fullName");
            String email = (String) sponsorDetail.get("email");
            String contactNumber = (String) sponsorDetail.get("contactNumber");

            // Try to find existing admin
            Sponsor sponsor = sponsorRepository.findById(id).orElse(null);

            if (sponsor == null) {
                // Create new admin if not found
                sponsor = new Sponsor();
                sponsor.setId(id);
            }

            // Update fields (applies to both new & existing)
            sponsor.setFullName(fullName);
            sponsor.setEmail(email);
            sponsor.setContactNumber(contactNumber);

            // Save (JPA will insert if new, update if existing)
            Sponsor savedSponsor = sponsorRepository.save(sponsor);

            return ResponseEntity.ok(ApiResponse.mapSuccess(savedSponsor, "successfully updated!"));

        } catch (DataIntegrityViolationException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(ApiResponse.error(409, "Data integrity violation"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error(500, "Some error occurred. Try later!"));
        }
    }

    public ResponseEntity<?> getInstSponsorships(Map<String, Object> request) {
        try {
            String institutionId = (String) request.get("institutionId");

            // fetch sponsorships by institutionId
            List<Sponsorship> existingSponsorships = sponsorshipRepository.findByInstitutionId(institutionId);

            if (!existingSponsorships.isEmpty()) {
                return ResponseEntity.ok(
                        ApiResponse.listSuccess(existingSponsorships, "Sponsorships found for this institution")
                );
            } else {
                return ResponseEntity.ok(
                        ApiResponse.error(404, "No sponsorships exist for this institution")
                );
            }

        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body(ApiResponse.error(500, "Error checking sponsorships", e.getMessage()));
        }
    }

    public ResponseEntity<?> getSponsorSponsorships(Map<String, Object> request) {
        try {
            String sponsorId = (String) request.get("sponsorId");

            // fetch sponsorships by institutionId
            List<Sponsorship> existingSponsorships = sponsorshipRepository.findBySponsorId(sponsorId);

            if (!existingSponsorships.isEmpty()) {
                return ResponseEntity.ok(
                        ApiResponse.listSuccess(existingSponsorships, "Sponsorships found for this institution")
                );
            } else {
                return ResponseEntity.ok(
                        ApiResponse.error(404, "No sponsorships exist for this institution")
                );
            }

        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body(ApiResponse.error(500, "Error checking sponsorships", e.getMessage()));
        }
    }
}
