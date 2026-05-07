package edu.hei.school.agricultural.controller;

import edu.hei.school.agricultural.controller.dto.Activity;
import edu.hei.school.agricultural.controller.dto.CreateActivity;
import edu.hei.school.agricultural.controller.dto.CreateActivityAttendance;
import edu.hei.school.agricultural.controller.dto.ActivityAttendance as DtoActivityAttendance;
import edu.hei.school.agricultural.controller.mapper.ActivityDtoMapper;
import edu.hei.school.agricultural.controller.mapper.ActivityAttendanceDtoMapper;
import edu.hei.school.agricultural.exception.BadRequestException;
import edu.hei.school.agricultural.exception.NotFoundException;
import edu.hei.school.agricultural.service.CollectivityService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.HttpStatus.*;

@RestController
@RequiredArgsConstructor
public class ActivityController {
    private final ActivityDtoMapper activityDtoMapper;
    private final ActivityAttendanceDtoMapper activityAttendanceDtoMapper;
    private final CollectivityService collectivityService;

    @PostMapping("/collectivities/{id}/activities")
    public ResponseEntity<?> createActivities(
            @PathVariable String id,
            @RequestBody List<CreateActivity> createActivities) {
        try {
            if (createActivities == null || createActivities.isEmpty()) {
                return ResponseEntity.status(BAD_REQUEST)
                        .body("Activities list cannot be null or empty");
            }
            return ResponseEntity.status(CREATED)
                    .body(collectivityService.createActivities(id, createActivities).stream()
                            .map(activityDtoMapper::mapToDto)
                            .toList());
        } catch (NotFoundException e) {
            return ResponseEntity.status(NOT_FOUND)
                    .body(e.getMessage());
        } catch (BadRequestException e) {
            return ResponseEntity.status(BAD_REQUEST)
                    .body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(e.getMessage());
        }
    }

    @GetMapping("/collectivities/{id}/activities")
    public ResponseEntity<?> getActivities(@PathVariable String id) {
        try {
            return ResponseEntity.status(OK)
                    .body(collectivityService.getActivities(id).stream()
                            .map(activityDtoMapper::mapToDto)
                            .toList());
        } catch (NotFoundException e) {
            return ResponseEntity.status(NOT_FOUND)
                    .body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(e.getMessage());
        }
    }

    @PostMapping("/collectivities/{id}/activities/{activityId}/attendance")
    public ResponseEntity<?> createActivityAttendance(
            @PathVariable String id,
            @PathVariable String activityId,
            @RequestBody CreateActivityAttendance createAttendance,
            @RequestHeader(value = "X-User-Id", required = false) String recordedBy) {
        try {
            if (createAttendance == null) {
                return ResponseEntity.status(BAD_REQUEST)
                        .body("Activity attendance cannot be null");
            }
            if ((createAttendance.getPresentMemberIds() == null || createAttendance.getPresentMemberIds().isEmpty()) &&
                (createAttendance.getAbsentMemberIds() == null || createAttendance.getAbsentMemberIds().isEmpty())) {
                return ResponseEntity.status(BAD_REQUEST)
                        .body("At least one present or absent member must be provided");
            }
            // Use recordedBy from header or fallback to system
            String finalRecordedBy = recordedBy != null ? recordedBy : "system";
            
            return ResponseEntity.status(CREATED)
                    .body(collectivityService.createActivityAttendance(id, activityId, createAttendance, finalRecordedBy).stream()
                            .map(activityAttendanceDtoMapper::mapToDto)
                            .toList());
        } catch (NotFoundException e) {
            return ResponseEntity.status(NOT_FOUND)
                    .body(e.getMessage());
        } catch (BadRequestException e) {
            return ResponseEntity.status(BAD_REQUEST)
                    .body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(e.getMessage());
        }
    }

    @GetMapping("/collectivities/{id}/activities/{activityId}/attendance")
    public ResponseEntity<?> getActivityAttendance(
            @PathVariable String id,
            @PathVariable String activityId) {
        try {
            return ResponseEntity.status(OK)
                    .body(collectivityService.getActivityAttendance(id, activityId).stream()
                            .map(activityAttendanceDtoMapper::mapToDto)
                            .toList());
        } catch (NotFoundException e) {
            return ResponseEntity.status(NOT_FOUND)
                    .body(e.getMessage());
        } catch (BadRequestException e) {
            return ResponseEntity.status(BAD_REQUEST)
                    .body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(e.getMessage());
        }
    }
}
