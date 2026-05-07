package edu.hei.school.agricultural.controller.mapper;

import edu.hei.school.agricultural.controller.dto.CollectivityActivityDto;
import edu.hei.school.agricultural.controller.dto.CreateCollectivityActivity;
import edu.hei.school.agricultural.entity.CollectivityActivity;
import org.springframework.stereotype.Component;

@Component
public class CollectivityActivityDtoMapper {
    
    public CollectivityActivityDto mapToDto(CollectivityActivity entity) {
        return CollectivityActivityDto.builder()
                .id(entity.getId())
                .label(entity.getLabel())
                .activityType(entity.getActivityType() == null 
                    ? null : CollectivityActivityDto.ActivityType.valueOf(entity.getActivityType().name()))
                .memberOccupationConcerned(entity.getMemberOccupationConcerned())
                .recurrenceRule(entity.getRecurrenceRule())
                .executiveDate(entity.getExecutiveDate())
                .build();
    }
    
    public CollectivityActivity mapToEntity(CreateCollectivityActivity createActivity, String collectivityId) {
        return CollectivityActivity.builder()
                .label(createActivity.getLabel())
                .activityType(createActivity.getActivityType() == null 
                    ? null : edu.hei.school.agricultural.entity.ActivityType.valueOf(createActivity.getActivityType().name()))
                .memberOccupationConcerned(createActivity.getMemberOccupationConcerned())
                .recurrenceRule(createActivity.getRecurrenceRule())
                .executiveDate(createActivity.getExecutiveDate())
                .collectivity(null) // Will be set by service
                .build();
    }
}
