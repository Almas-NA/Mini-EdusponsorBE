package com.example.edusponsor.controller;

import com.example.edusponsor.service.SponsorService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/sponsor")
@RequiredArgsConstructor
public class SponsorController {

    private final SponsorService sponsorService;


    @PostMapping("/get/info")
    public ResponseEntity<?> getSponsor(
            @RequestBody Map<String, Object> userId) {
        return sponsorService.getSponsorDetail(userId);
    }

    @PostMapping("/update/info")
    public ResponseEntity<?> updateSponsor(
            @RequestBody Map<String, Object> userId) {
        return sponsorService.updateSponsorDetail(userId);
    }

    @PostMapping("/get/institution/sponsorships")
    public ResponseEntity<?> getInstSponsorships(
            @RequestBody Map<String, Object> instId) {
        return sponsorService.getInstSponsorships(instId);
    }

    @PostMapping("/get/sponsor/sponsorships")
    public ResponseEntity<?> getSponsorSponsorships(
            @RequestBody Map<String, Object> sponsorId) {
        return sponsorService.getSponsorSponsorships(sponsorId);
    }
}
