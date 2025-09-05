package com.example.edusponsor.repository;

import com.example.edusponsor.entity.Institution;
import com.example.edusponsor.entity.Sponsor;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface SponsorRepository extends MongoRepository<Sponsor, String> {

    // Finds an institution by its username.
    Optional<Sponsor> findByUsername(String username);

    Optional<Sponsor> findById(String id);

    List<Sponsor> findByApprovedFalse();

    List<Sponsor> findByApprovedTrue();
}
