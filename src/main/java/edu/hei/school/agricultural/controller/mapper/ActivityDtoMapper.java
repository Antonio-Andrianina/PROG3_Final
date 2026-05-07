package edu.hei.school.agricultural.controller.mapper;

import edu.hei.school.agricultural.controller.dto.Activity as DtoActivity;
import edu.hei.school.agricultural.controller.dto.CreateActivity;
import edu.hei.school.agricultural.entity.Activity as EntityActivity;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class ActivityDtoMapper {
    
    public DtoActivity mapToDto(EntityActivity activity) {
        return new DtoActivity(
            activity.getId(),
            activity.getName(),
            activity.getDescription(),
            activity.getActivityDate(),
            activity.getCreatedAt(),
            activity.getUpdatedAt()
        );
    }
    
    public EntityActivity mapToEntity(CreateActivity createActivity, String collectivityId) {
        EntityActivity activity = new EntityActivity();
        activity.setName(createActivity.getName());
        activity.setDescription(createActivity.getDescription());
        activity.setActivityDate(createActivity.getActivityDate());
        activity.setCollectivityId(collectivityId);
        activity.setCreatedAt(LocalDateTime.now());
        activity.setUpdatedAt(LocalDateTime.now());
        return activity;
    }
}
