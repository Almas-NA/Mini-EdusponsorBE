package com.example.edusponsor.dto;

import lombok.Data;

@Data
public class Sponsorship {
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
