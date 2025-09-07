package com.example.edusponsor.repository;

import com.example.edusponsor.entity.Sponsorship;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface SponsorshipRepository extends MongoRepository<Sponsorship, String> {
    List<Sponsorship> findByInstitutionId(String institutionId);
    List<Sponsorship> findByStudentId(String studentId);
    List<Sponsorship> findBySponsorId(String sponsorId);
}