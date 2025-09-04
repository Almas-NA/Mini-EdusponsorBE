package com.example.edusponsor.repository;

import com.example.edusponsor.entity.Institution;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface InstitutionRepository extends MongoRepository<Institution, String> {

    // Finds an institution by its username.
    Optional<Institution> findByUsername(String username);

    Optional<Institution> findById(String id);

    List<Institution> findByApprovedFalse();
}