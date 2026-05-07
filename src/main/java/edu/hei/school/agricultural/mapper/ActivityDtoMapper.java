package edu.hei.school.agricultural.mapper;

import edu.hei.school.agricultural.controller.dto.ActivityMemberAttendanceDto;
import edu.hei.school.agricultural.controller.dto.ActivityType;
import edu.hei.school.agricultural.controller.dto.AttendanceStatus;
import edu.hei.school.agricultural.controller.dto.CollectivityActivityDto;
import edu.hei.school.agricultural.controller.dto.CreateActivityMemberAttendance;
import edu.hei.school.agricultural.controller.dto.CreateCollectivityActivity;
import edu.hei.school.agricultural.controller.dto.MemberDescription;
import edu.hei.school.agricultural.controller.dto.MonthlyRecurrenceRule;
import edu.hei.school.agricultural.controller.dto.MemberOccupation;
import edu.hei.school.agricultural.entity.ActivityMemberAttendance;
import edu.hei.school.agricultural.entity.CollectivityActivity;
import edu.hei.school.agricultural.exception.NotFoundException;
import edu.hei.school.agricultural.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class ActivityDtoMapper {
    private final MemberRepository memberRepository;

    public CollectivityActivity mapToEntity(CreateCollectivityActivity dto) {
        edu.hei.school.agricultural.entity.MonthlyRecurrenceRule recurrenceRule = null;
        if (dto.getRecurrenceRule() != null) {
            recurrenceRule = edu.hei.school.agricultural.entity.MonthlyRecurrenceRule.builder()
                    .weekOrdinal(dto.getRecurrenceRule().getWeekOrdinal())
                    .dayOfWeek(dto.getRecurrenceRule().getDayOfWeek())
                    .build();
        }

        List<edu.hei.school.agricultural.entity.MemberOccupation> occupations = null;
        if (dto.getMemberOccupationConcerned() != null) {
            occupations = dto.getMemberOccupationConcerned().stream()
                    .map(o -> edu.hei.school.agricultural.entity.MemberOccupation.valueOf(o.name()))
                    .toList();
        }

        return CollectivityActivity.builder()
                .label(dto.getLabel())
                .activityType(dto.getActivityType() == null
                        ? null
                        : edu.hei.school.agricultural.entity.ActivityType.valueOf(
                        dto.getActivityType().name()))
                .memberOccupationConcerned(occupations)
                .recurrenceRule(recurrenceRule)
                .executiveDate(dto.getExecutiveDate())
                .build();
    }

    public CollectivityActivityDto mapToDto(CollectivityActivity entity) {
        MonthlyRecurrenceRule recurrenceRuleDto = null;
        if (entity.getRecurrenceRule() != null) {
            recurrenceRuleDto = MonthlyRecurrenceRule.builder()
                    .weekOrdinal(entity.getRecurrenceRule().getWeekOrdinal())
                    .dayOfWeek(entity.getRecurrenceRule().getDayOfWeek())
                    .build();
        }

        List<MemberOccupation> occupationsDto = null;
        if (entity.getMemberOccupationConcerned() != null) {
            occupationsDto = entity.getMemberOccupationConcerned().stream()
                    .map(o -> MemberOccupation.valueOf(o.name()))
                    .toList();
        }

        return CollectivityActivityDto.builder()
                .id(entity.getId())
                .label(entity.getLabel())
                .activityType(entity.getActivityType() == null
                        ? null : ActivityType.valueOf(entity.getActivityType().name()))
                .memberOccupationConcerned(occupationsDto)
                .recurrenceRule(recurrenceRuleDto)
                .executiveDate(entity.getExecutiveDate())
                .build();
    }

    public ActivityMemberAttendance mapToEntity(CreateActivityMemberAttendance dto) {
        Member member = memberRepository.findById(dto.getMemberIdentifier())
                .orElseThrow(() -> new NotFoundException(
                        "Member.id=" + dto.getMemberIdentifier() + " not found"));
        return ActivityMemberAttendance.builder()
                .member(member)
                .attendanceStatus(dto.getAttendanceStatus() == null
                        ? edu.hei.school.agricultural.entity.AttendanceStatus.UNDEFINED
                        : edu.hei.school.agricultural.entity.AttendanceStatus.valueOf(
                        dto.getAttendanceStatus().name()))
                .build();
    }

    public ActivityMemberAttendanceDto mapToDto(ActivityMemberAttendance entity) {
        return ActivityMemberAttendanceDto.builder()
                .id(entity.getId())
                .memberDescription(MemberDescription.builder()
                        .id(entity.getMember().getId())
                        .firstName(entity.getMember().getFirstName())
                        .lastName(entity.getMember().getLastName())
                        .email(entity.getMember().getEmail())
                        .occupation(entity.getMember().getOccupation() == null
                                ? null : entity.getMember().getOccupation().name())
                        .build())
                .attendanceStatus(entity.getAttendanceStatus() == null
                        ? AttendanceStatus.UNDEFINED
                        : AttendanceStatus.valueOf(entity.getAttendanceStatus().name()))
                .build();
    }
}