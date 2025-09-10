package com.example.edusponsor.entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Student {
    @Id
    private String id;

    private String instituteId;
    private String firstName;
    private String secondName;
    private String department;
    private String year;
    private String studentEmail;
    private String studentContact;
    private String username;
    private String password;
    private String parentName;
    private String parentEmail;
    private String parentContact;
    private String relation;
    private String annualIncome;
    private String incomeProofBaseSF;
}
