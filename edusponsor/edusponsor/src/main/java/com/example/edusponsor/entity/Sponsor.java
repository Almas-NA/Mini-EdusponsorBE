package com.example.edusponsor.entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Sponsor {
    @Id
    private String id;

    private String username;
    private String password;
    private String fullName;
    private String email;
    private String contactNumber;
    private String incomeProofBaseSF;
    private String location;
    private boolean approved;
}
