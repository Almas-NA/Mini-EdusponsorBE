package com.example.edusponsor.dto.register;

import lombok.Data;

@Data
public class InstitutionRegister {
    private String username;
    private String password;
    private String email;
    private String instituteName;
    private String instituteId;
    private String location;
}
