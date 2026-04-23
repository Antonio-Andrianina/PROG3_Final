package com.collectivities.binome.controller;

import com.collectivities.binome.entity.Collectivity;
import com.collectivities.binome.entity.CreateCollectivity;
import com.collectivities.binome.entity.CreateMembershipFees;
import com.collectivities.binome.entity.FinancialAccount;
import com.collectivities.binome.entity.MembershipFees;
import com.collectivities.binome.entity.Transaction;
import com.collectivities.binome.exceptions.AppBadRequestException;
import com.collectivities.binome.service.CollectivityService;
import com.collectivities.binome.service.FinancialAccountService;
import com.collectivities.binome.service.MembershipFeesService;
import com.collectivities.binome.service.TransactionService;
import lombok.AllArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@AllArgsConstructor
public class CollectivityController {

    private final CollectivityService collectivityService;
    private final MembershipFeesService membershipFeesService;
    private final TransactionService transactionService;
    private final FinancialAccountService financialAccountService;

    @PostMapping("/collectivities")
    public ResponseEntity<?> saveAll(@RequestBody List<CreateCollectivity> toSave) {
        try {
            List<Collectivity> result = collectivityService.saveAll(toSave);
            return ResponseEntity.status(201)
                    .header("Content-Type", "application/json")
                    .body(result);
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

    @GetMapping("/collectivities/{id}")
    public ResponseEntity<?> getCollectivityById(@PathVariable String id) {
        try {
            Collectivity collectivity = collectivityService.findById(id);
            return ResponseEntity.ok()
                    .header("Content-Type", "application/json")
                    .body(collectivity);
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

    @PutMapping("/collectivities/{id}/identity")
    public ResponseEntity<?> assignIdentity(
            @PathVariable String id,
            @RequestBody AssignCollectivityIdentity body
    ) {
        try {
            Collectivity updated = collectivityService.assignIdentity(id, body);
            return ResponseEntity.ok()
                    .header("Content-Type", "application/json")
                    .body(updated);
        } catch (AppBadRequestException e) {
            String message = e.getMessage();
            if (message.contains("not found")) {
                return ResponseEntity.status(404)
                        .header("Content-Type", "application/json")
                        .body(new ErrorResponse(e.getMessage()));
            } else if (message.contains("already exists")) {
                return ResponseEntity.status(409)
                        .header("Content-Type", "application/json")
                        .body(new ErrorResponse(e.getMessage()));
            }
            return ResponseEntity.status(400)
                    .header("Content-Type", "application/json")
                    .body(new ErrorResponse(e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(500)
                    .header("Content-Type", "application/json")
                    .body(new ErrorResponse("Internal server error: " + e.getMessage()));
        }
    }

    @GetMapping("/collectivities/{id}/financialAccounts")
    public ResponseEntity<?> getFinancialAccounts(
            @PathVariable String id,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate at
    ) {
        try {
            List<FinancialAccount> accounts = financialAccountService.getAccountsWithBalance(id, at);
            return ResponseEntity.ok()
                    .header("Content-Type", "application/json")
                    .body(accounts);
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

    @GetMapping("/collectivities/{id}/membershipFees")
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

    @PostMapping("/collectivities/{id}/membershipFees")
    public ResponseEntity<?> createMembershipFees(
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

    @GetMapping("/collectivities/{id}/transactions")
    public ResponseEntity<?> getTransactions(
            @PathVariable String id,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate
    ) {
        try {
            List<Transaction> transactions = transactionService.getTransactions(id, startDate, endDate);
            return ResponseEntity.ok()
                    .header("Content-Type", "application/json")
                    .body(transactions);
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
}