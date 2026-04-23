package com.collectivities.binome.controller;

import com.collectivities.binome.entity.CreateCollectivity;
import com.collectivities.binome.exceptions.AppBadRequestException;
import com.collectivities.binome.service.CollectivityService;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RestController
@AllArgsConstructor
public class CollectivityController {

    private CollectivityService collectivityService;

    @PostMapping("/collectivities")
    public ResponseEntity<?> saveALl(
            @RequestBody List<CreateCollectivity> toSave
    ) {
        try {
            return ResponseEntity.status(201)
                    .header("Content-Type", "application/json")
                    .body(this.collectivityService.saveAll(toSave));
        } catch (Exception e){
            return ResponseEntity.status(500)
                    .header("Content-Type", "text/plain")
                    .body(this.collectivityService.saveAll(toSave));
        }
    }

        @PutMapping("/collectivities/{id}/identity")
        public <CollectivityIdentity> ResponseEntity<?> assignIdentity(
                @PathVariable String id,
                @RequestBody CollectivityIdentity body
        ){
            try {
                return ResponseEntity.ok()
                        .header("Content-Type", "application/json")
                        .body(collectivityService.assignIdentity(id, body));

            } catch (AppBadRequestException e){
                return ResponseEntity.status(400)
                        .header("Content-Type", "text/plain")
                        .body(e.getMessage());

            } catch (RuntimeException e){
                return ResponseEntity.status(404)
                        .header("Content-Type", "text/plain")
                        .body("Collectivity not found");
            }
        }
}
