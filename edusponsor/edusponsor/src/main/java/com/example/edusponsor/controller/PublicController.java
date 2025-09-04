package com.example.edusponsor.controller;

import com.example.edusponsor.entity.Institution;
import com.example.edusponsor.entity.Sponsor;
import com.example.edusponsor.repository.InstitutionRepository;
import com.example.edusponsor.repository.SponsorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/public")
@RequiredArgsConstructor
public class PublicController {

    private final InstitutionRepository institutionRepo;
    private final SponsorRepository sponsorRepo;

    @GetMapping("/institutions")
    public ResponseEntity<List<Institution>> getAllInstitutions() {
        return ResponseEntity.ok(institutionRepo.findAll());
    }

    @GetMapping("/sponsors")
    public ResponseEntity<List<Sponsor>> getAllSponsors() {
        return ResponseEntity.ok(sponsorRepo.findAll());
    }
}
