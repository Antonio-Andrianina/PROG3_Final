package com.collectivities.binome.controller;

import com.collectivities.binome.entity.CreateCollectivity;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
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
}
