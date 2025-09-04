package com.example.edusponsor.dto;

import lombok.Data;

@Data
public class StudentRequest {
    private String name;
    private String email;
    private String institutionId;
    private String username;
    private String password;
}
