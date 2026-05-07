package edu.hei.school.agricultural.controller;

import edu.hei.school.agricultural.controller.dto.CollectivityActivityDto;
import edu.hei.school.agricultural.controller.dto.CreateActivityMemberAttendance;
import edu.hei.school.agricultural.controller.dto.CreateCollectivityActivity;
import edu.hei.school.agricultural.controller.mapper.CollectivityActivityDtoMapper;
import edu.hei.school.agricultural.controller.mapper.ActivityAttendanceDtoMapper;
import edu.hei.school.agricultural.exception.BadRequestException;
import edu.hei.school.agricultural.exception.NotFoundException;
import edu.hei.school.agricultural.service.ActivityService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.OK;

@RestController
@RequiredArgsConstructor
public class ActivityController {
    private final ActivityService activityService;
    private final CollectivityActivityDtoMapper collectivityActivityDtoMapper;
    private final ActivityAttendanceDtoMapper activityAttendanceDtoMapper;

    @PostMapping("/collectivities/{id}/activities")
    public ResponseEntity<?> createActivities(
            @PathVariable String id,
            @Valid @RequestBody List<CreateCollectivityActivity> createActivities) {
        try {
            var entities = createActivities.stream()
                    .map(createActivity -> collectivityActivityDtoMapper.mapToEntity(createActivity, id))
                    .toList();
            return ResponseEntity.status(OK)
                    .body(activityService.createActivities(id, entities).stream()
                            .map(collectivityActivityDtoMapper::mapToDto)
                            .toList());
        } catch (BadRequestException e) {
            return ResponseEntity.status(BAD_REQUEST).body(e.getMessage());
        } catch (NotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @GetMapping("/collectivities/{id}/activities")
    public ResponseEntity<?> getActivities(@PathVariable String id) {
        try {
            return ResponseEntity.status(OK)
                    .body(activityService.getActivitiesByCollectivity(id).stream()
                            .map(collectivityActivityDtoMapper::mapToDto)
                            .toList());
        } catch (NotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @PostMapping("/collectivities/{id}/activities/{activityId}/attendance")
    public ResponseEntity<?> createAttendance(
            @PathVariable String id,
            @PathVariable String activityId,
            @Valid @RequestBody List<CreateActivityMemberAttendance> createAttendances) {
        try {
            var entities = createAttendances.stream()
                    .map(createAttendance -> activityAttendanceDtoMapper.mapToEntity(createAttendance, activityId, "system"))
                    .toList();
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(activityService.createAttendances(id, activityId, entities));
        } catch (BadRequestException e) {
            return ResponseEntity.status(BAD_REQUEST).body(e.getMessage());
        } catch (NotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @GetMapping("/collectivities/{id}/activities/{activityId}/attendance")
    public ResponseEntity<?> getAttendance(
            @PathVariable String id,
            @PathVariable String activityId) {
        try {
            return ResponseEntity.status(OK)
                    .body(activityService.getAttendances(id, activityId));
        } catch (NotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
}