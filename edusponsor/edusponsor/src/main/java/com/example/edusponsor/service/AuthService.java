package com.example.edusponsor.service;

import com.example.edusponsor.dto.common.ApiResponse;
import com.example.edusponsor.dto.login.LoginRequest;
import com.example.edusponsor.dto.login.LoginResponse;
import com.example.edusponsor.dto.register.InstitutionRegister;
import com.example.edusponsor.dto.register.SponsorRegister;
import com.example.edusponsor.entity.*;
import com.example.edusponsor.repository.*;
import com.example.edusponsor.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.*;
import org.springframework.security.authentication.*;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    private final InstitutionRepository institutionRepo;
    private final SponsorRepository sponsorRepo;
    private final UserRepository userRepo;

    public ResponseEntity<?> registerInstitution(InstitutionRegister req) {
        try{
            Institution institution = Institution.builder()
                    .username(req.getUsername())
                    .password(req.getPassword())
                    .email(req.getEmail())
                    .instituteName(req.getInstituteName())
                    .instituteId(req.getInstituteId())
                    .location(req.getLocation())
                    .approved(false)
                    .build();

            Institution institute=institutionRepo.findByUsername(req.getUsername())
                    .orElse(null);
            if (institute != null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(ApiResponse.warning(200, "User already exists"));
            }
            else{
                institution = institutionRepo.save(institution);
                return ResponseEntity.ok(ApiResponse.mapSuccess(institution, "Successfully registered, now login!"));
            }

        } catch (DataIntegrityViolationException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(ApiResponse.error(404, "Some error occurred.Try Later!"));

        } catch (Exception e) {
            // A generic catch block for any other unexpected errors
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error(404, "Some error occurred.Try Later!"));
        }
    }

    public ResponseEntity<?> registerSponsor(SponsorRegister req) {
        try{

            Sponsor sponsor = Sponsor.builder()
                    .username(req.getUsername())
                    .password(req.getPassword())
                    .fullName(req.getFullName())
                    .email(req.getEmail())
                    .incomeProofBaseSF(req.getIncomeProofBaseSF())
                    .location(req.getLocation())
                    .approved(false)
                    .build();
            Sponsor sponsorer = sponsorRepo.findByUsername(req.getUsername())
                    .orElse(null);
            if (sponsorer != null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(ApiResponse.warning(200, "User already exists"));
            }
            else{
                sponsor = sponsorRepo.save(sponsor);
                return ResponseEntity.ok(ApiResponse.mapSuccess(sponsor, "Successfully registered, now login!"));
            }

        } catch (DataIntegrityViolationException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(ApiResponse.error(404, "Some error occurred.Try Later!"));

        } catch (Exception e) {
            // A generic catch block for any other unexpected errors
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error(404, "Some error occurred.Try Later!"));
        }
    }

    public ResponseEntity<?> authenticateUser(LoginRequest loginRequest) {
        try {
            // Try authenticating manually
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequest.getUsername(),
                            loginRequest.getPassword()
                    )
            );

            // Fetch user after successful authentication
            User user = userRepo.findByUsername(loginRequest.getUsername())
                    .orElse(null);

            if (user == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(ApiResponse.error(404, "User not found"));
            }

            String token = jwtService.generateToken(user.getUsername(), user.getRole().name());
            LoginResponse loginResponse = new LoginResponse(token, user);

            return ResponseEntity.ok(ApiResponse.mapSuccess(loginResponse, "Successfully logged in!"));

        } catch (BadCredentialsException e) {
            // Wrong password or username
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(ApiResponse.error(401, "Invalid username or password"));

        } catch (UsernameNotFoundException e) {
            // User doesn't exist
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error(404, "User not found"));

        } catch (AuthenticationException e) {
            // Other authentication-related issues
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(ApiResponse.error(401, "Authentication failed"));
        }
    }

}
