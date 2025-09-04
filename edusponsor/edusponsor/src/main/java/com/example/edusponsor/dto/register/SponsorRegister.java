package com.example.edusponsor.dto.register;

import lombok.Data;

@Data
public class SponsorRegister {
    private String username;
    private String password;
    private String fullName;
    private String email;
    private String incomeProofBaseSF;
    private String location;
    private boolean approved;
}
