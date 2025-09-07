package com.example.edusponsor.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Sponsorship {
    @Id
    private String id;

    private String institutionId;
    private String studentId;
    private String yearOneFee;
    private String yearTwoFee;
    private String yearThreeFee;
    private String yearFourFee;
    private String priority;
    private String yearOneFeeStatus;
    private String yearTwoFeeStatus;
    private String yearThreeFeeStatus;
    private String yearFourFeeStatus;
    private String sponsorId;
}
