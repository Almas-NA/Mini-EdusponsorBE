package com.example.edusponsor.controller;

import com.example.edusponsor.service.SponsorService;
import com.example.edusponsor.service.SponsorshipService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/sponsorship")
@RequiredArgsConstructor
public class SponsorshipController {

    private final SponsorshipService sponsorshipService;

    @PostMapping("/change/status")
    public ResponseEntity<?> getSponsor(
            @RequestBody Map<String, Object> sponsorship) {
        return sponsorshipService.updateSponsorshipDetail(sponsorship);
    }
}
