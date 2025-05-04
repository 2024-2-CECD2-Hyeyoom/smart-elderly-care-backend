package com.example.smart_elderly_care.web.controller;

import com.example.smart_elderly_care.service.JoinService;
import com.example.smart_elderly_care.web.dto.member.CaregiverSignupDTO;
import com.example.smart_elderly_care.web.dto.member.StaffSignupDTO;
import com.example.smart_elderly_care.web.dto.member.UserSignupDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/member/signup")
@RequiredArgsConstructor
public class MemberSignupController {

    private final JoinService joinService;

    @PostMapping("/user")
    public ResponseEntity<?> signupUser(@RequestBody UserSignupDTO dto) {
        joinService.signupUser(dto);
        return ResponseEntity.ok("User signup successful");
    }

    @PostMapping("/caregiver")
    public ResponseEntity<?> signupCaregiver(@RequestBody CaregiverSignupDTO dto) {
        joinService.signupCaregiver(dto);
        return ResponseEntity.ok("Caregiver signup successful");
    }

    @PostMapping("/staff")
    public ResponseEntity<?> signupStaff(@RequestBody StaffSignupDTO dto) {
        joinService.signupStaff(dto);
        return ResponseEntity.ok("Staff signup successful");
    }
}
