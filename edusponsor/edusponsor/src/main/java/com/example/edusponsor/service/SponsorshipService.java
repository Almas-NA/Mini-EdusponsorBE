package com.example.edusponsor.service;

import com.example.edusponsor.dto.common.ApiResponse;
import com.example.edusponsor.entity.Sponsorship;
import com.example.edusponsor.repository.SponsorshipRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class SponsorshipService {

    private final SponsorshipRepository sponsorshipRepo;

    public ResponseEntity<?> createSponsorship(Map<String, Object> sponsorship) {
        try {
            String studentId = (String) sponsorship.get("studentId");

            // check if already exists
            if (!sponsorshipRepo.findByStudentId(studentId).isEmpty()) {
                return ResponseEntity.badRequest()
                        .body(ApiResponse.error(400, "Sponsorship already exists for this student"));
            }

            Sponsorship newSponsorship = Sponsorship.builder()
                    .institutionId((String) sponsorship.get("institutionId"))
                    .studentId(studentId)
                    .yearOneFee((String) sponsorship.get("yearOneFee"))
                    .yearTwoFee((String) sponsorship.get("yearTwoFee"))
                    .yearThreeFee((String) sponsorship.get("yearThreeFee"))
                    .yearFourFee((String) sponsorship.get("yearFourFee"))
                    .priority((String) sponsorship.get("priority"))
                    .sponsorId((String) sponsorship.get("sponsorId"))
                    .build();

            return ResponseEntity.ok(
                    ApiResponse.mapSuccess(sponsorshipRepo.save(newSponsorship), "Sponsorship created")
            );

        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body(ApiResponse.error(500, "Error creating sponsorship"));
        }
    }

    public ResponseEntity<?> checkSponsorshipExists(Map<String, Object> sponsorship) {
        try {
            String studentId = (String) sponsorship.get("studentId");

            // check if sponsorship exists
            var existingSponsorships = sponsorshipRepo.findByStudentId(studentId);

            if (!existingSponsorships.isEmpty()) {
                return ResponseEntity.ok(
                        ApiResponse.listSuccess(existingSponsorships, "Sponsorship already added for this student")
                );
            } else {
                return ResponseEntity.ok(
                        ApiResponse.error(400,"No sponsorship exists for this student")
                );
            }

        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body(ApiResponse.error(500, "Error checking sponsorship", e.getMessage()));
        }
    }

    public ResponseEntity<?> updateSponsorshipDetail(Map<String, Object> sponsorshipData) {
        try {
            String id = (String) sponsorshipData.get("id");

            Sponsorship sponsorship = sponsorshipRepo.findById(id).orElse(null);
            if (sponsorship == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(ApiResponse.error(404, "Sponsorship not found!"));
            }

            // Remove "id" from the map to only process fields to update
            sponsorshipData.remove("id");

            if (sponsorshipData.isEmpty()) {
                return ResponseEntity.badRequest()
                        .body(ApiResponse.error(400, "No fields provided to update!"));
            }

            for (Map.Entry<String, Object> entry : sponsorshipData.entrySet()) {
                String key = entry.getKey();
                Object value = entry.getValue();

                switch (key) {
                    case "yearOneFeeStatus":
                        sponsorship.setYearOneFeeStatus((String) value);
                        break;
                    case "yearTwoFeeStatus":
                        sponsorship.setYearTwoFeeStatus((String) value);
                        break;
                    case "yearThreeFeeStatus":
                        sponsorship.setYearThreeFeeStatus((String) value);
                        break;
                    case "yearFourFeeStatus":
                        sponsorship.setYearFourFeeStatus((String) value);
                        break;
                    case "sponsorId":
                        sponsorship.setSponsorId((String) value);
                        break;
                    default:
                        return ResponseEntity.badRequest()
                                .body(ApiResponse.error(400, "Invalid field: " + key));
                }
            }

            Sponsorship updated = sponsorshipRepo.save(sponsorship);

            return ResponseEntity.ok(ApiResponse.mapSuccess(updated, "Sponsorship updated successfully!"));

        } catch (DataIntegrityViolationException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(ApiResponse.error(409, "Data integrity violation"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error(500, "Some error occurred. Try later!"));
        }
    }

}
