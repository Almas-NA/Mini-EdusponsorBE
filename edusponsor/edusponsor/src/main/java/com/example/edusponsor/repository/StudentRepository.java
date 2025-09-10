package com.example.edusponsor.repository;

import com.example.edusponsor.entity.Sponsor;
import com.example.edusponsor.entity.Student;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface StudentRepository extends MongoRepository<Student, String> {
    List<Student> findByInstituteId(String instituteId);

    Optional<Student> findById(String id);
}
