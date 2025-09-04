package com.example.edusponsor.entity;

import com.example.edusponsor.enums.UserRole;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {
    @Id
    private String id;

    private String username;
    private String password;
    private UserRole role;

    private String refId; // Institution/Sponsor/Student ID
}
