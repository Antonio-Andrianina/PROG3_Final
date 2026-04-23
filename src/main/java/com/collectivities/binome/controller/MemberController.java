package com.collectivities.binome.controller;

import com.collectivities.binome.entity.CreateMember;
import com.collectivities.binome.exceptions.AppBadRequestException;
import com.collectivities.binome.service.MemberService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@AllArgsConstructor
@RestController
public class MemberController {

    private final MemberService memberService;

    @PostMapping("/members")
    public ResponseEntity<?> saveAll(@RequestBody List<CreateMember> toSave) {
        try {
            return ResponseEntity.status(201)
                    .header("Content-Type", "application/json")
                    .body(memberService.saveAll(toSave));
        } catch (AppBadRequestException e) {
            return ResponseEntity.status(400)
                    .header("Content-Type", "text/plain")
                    .body(e.getMessage());
        } catch (RuntimeException e) {
            return ResponseEntity.status(404)
                    .header("Content-Type", "text/plain")
                    .body(e.getMessage());
        }
    }

    @PostMapping("/members/{id}/payments")
    public ResponseEntity<?> createPayments(
            @PathVariable String id,
            @RequestBody List<CreateMemberPayment> payments) {
        try {
            return ResponseEntity.status(201)
                    .header("Content-Type", "application/json")
                    .body(memberService.createPayments(id, payments));
        } catch (AppBadRequestException e) {
            return ResponseEntity.status(400)
                    .header("Content-Type", "text/plain")
                    .body(e.getMessage());
        } catch (RuntimeException e) {
            return ResponseEntity.status(404)
                    .header("Content-Type", "text/plain")
                    .body("Member not found");
        }
    }
}