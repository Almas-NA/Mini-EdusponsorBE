package com.example.edusponsor.service;

import com.example.edusponsor.dto.common.ApiResponse;
import com.example.edusponsor.entity.Sponsorship;
import com.example.edusponsor.entity.Student;
import com.example.edusponsor.entity.User;
import com.example.edusponsor.enums.UserRole;
import com.example.edusponsor.repository.SponsorshipRepository;
import lombok.RequiredArgsConstructor;
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

}
