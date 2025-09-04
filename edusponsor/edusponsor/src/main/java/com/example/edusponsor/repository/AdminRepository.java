package com.example.edusponsor.repository;

import com.example.edusponsor.entity.Admin;
import com.example.edusponsor.entity.Institution;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface AdminRepository extends MongoRepository<Admin, String> {

    Optional<Admin> findByRefId(String refId);

}
