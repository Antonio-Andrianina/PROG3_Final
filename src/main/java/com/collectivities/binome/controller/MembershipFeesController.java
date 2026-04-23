package com.collectivities.binome.controller;

import com.collectivities.binome.entity.CreateMembershipFees;
import com.collectivities.binome.entity.MembershipFees;
import com.collectivities.binome.exceptions.AppBadRequestException;
import com.collectivities.binome.service.MembershipFeesService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/collectivities")
public class MembershipFeesController {

    private final MembershipFeesService membershipFeesService;

    @GetMapping("/{id}/membershipFees")
    public ResponseEntity<?> getMembershipFees(@PathVariable String id) {
        try {
            List<MembershipFees> fees = membershipFeesService.getFees(id);
            return ResponseEntity.ok()
                    .header("Content-Type", "application/json")
                    .body(fees);
        } catch (AppBadRequestException e) {
            return ResponseEntity.status(404)
                    .header("Content-Type", "application/json")
                    .body(new ErrorResponse(e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(500)
                    .header("Content-Type", "application/json")
                    .body(new ErrorResponse("Internal server error: " + e.getMessage()));
        }
    }

    @PostMapping("/{id}/membershipFees")
    public ResponseEntity<?> createFees(
            @PathVariable String id,
            @RequestBody List<CreateMembershipFees> fees
    ) {
        try {
            List<MembershipFees> created = membershipFeesService.createFees(id, fees);
            return ResponseEntity.status(201)
                    .header("Content-Type", "application/json")
                    .body(created);
        } catch (AppBadRequestException e) {
            return ResponseEntity.status(400)
                    .header("Content-Type", "application/json")
                    .body(new ErrorResponse(e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(500)
                    .header("Content-Type", "application/json")
                    .body(new ErrorResponse("Internal server error: " + e.getMessage()));
        }
    }
}