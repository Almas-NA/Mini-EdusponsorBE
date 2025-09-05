package com.example.edusponsor.controller;

import com.example.edusponsor.entity.*;
import com.example.edusponsor.service.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {

    private final AdminService adminService;

    @PostMapping("/get/info")
    public ResponseEntity<?> getAdmin(
            @RequestBody Map<String, Object> userId) {
        return adminService.getAdminDetail(userId);
    }

    @PostMapping("/update/info")
    public ResponseEntity<?> updateAdmin(
            @RequestBody Map<String, Object> adminDetail) {
        return adminService.updateAdminDetail(adminDetail);
    }

    @GetMapping("/dashboard/data")
    public ResponseEntity<?> dashboardInfo() {
        return adminService.dashboardData();
    }

    @GetMapping("/institutions")
    public ResponseEntity<?> getInstitutions() {
        return adminService.getInstitutions();
    }

    @GetMapping("/sponsors")
    public ResponseEntity<?> getSponsors() {
        return adminService.getSponsors();
    }

    @GetMapping("/pending/institutions")
    public ResponseEntity<?> getPendingInstitutions() {
        return adminService.getPendingInstitutions();
    }

    @GetMapping("/pending/sponsors")
    public ResponseEntity<?> getPendingSponsors() {
        return adminService.getPendingSponsors();
    }

    @PostMapping("/approve/institution")
    public ResponseEntity<?> approveInstitution(@RequestBody Map<String, Object> institute) {
        return adminService.approveInstitution(institute);
    }

    @PostMapping("/approve/sponsor")
    public ResponseEntity<?> approveSponsor(@RequestBody Map<String, Object> sponsor) {
        return adminService.approveSponsor(sponsor);
    }

    @PostMapping("/reject/institution")
    public ResponseEntity<?> rejectInstitution(@RequestBody Map<String, Object> institute) {
        return adminService.rejectInstitution(institute);
    }

    @PostMapping("/reject/sponsor")
    public ResponseEntity<?> rejectSponsor(@RequestBody Map<String, Object> sponsor) {
        return adminService.rejectSponsor(sponsor);
    }
}
