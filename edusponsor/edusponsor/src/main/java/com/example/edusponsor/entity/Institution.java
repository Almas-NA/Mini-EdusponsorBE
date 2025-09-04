package com.example.edusponsor.entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Institution {
    @Id
    private String id;

    private String username;
    private String email;
    private String password;
    private String instituteName;
    private String instituteId;
    private String location;
    private boolean approved;
}
