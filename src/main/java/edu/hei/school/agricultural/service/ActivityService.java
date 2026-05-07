package edu.hei.school.agricultural.service;

import edu.hei.school.agricultural.entity.ActivityMemberAttendance;
import edu.hei.school.agricultural.entity.CollectivityActivity;
import edu.hei.school.agricultural.exception.BadRequestException;
import edu.hei.school.agricultural.exception.NotFoundException;
import edu.hei.school.agricultural.repository.AttendanceRepository;
import edu.hei.school.agricultural.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

import static java.util.UUID.randomUUID;

@Service
@RequiredArgsConstructor
public class ActivityService {
    private final ActivityRepository activityRepository;
    private final AttendanceRepository attendanceRepository;
    private final CollectivityRepository collectivityRepository;
    private final MemberRepository memberRepository;

    public List<CollectivityActivity> createActivities(String collectivityId,
                                                        List<CollectivityActivity> activities) {
        var collectivity = collectivityRepository.findById(collectivityId)
                .orElseThrow(() -> new NotFoundException(
                        "Collectivity.id=" + collectivityId + " not found"));

        for (CollectivityActivity activity : activities) {
            if (activity.getRecurrenceRule() != null && activity.getExecutiveDate() != null) {
                throw new BadRequestException(
                        "Cannot provide both recurrenceRule and executiveDate at the same time");
            }
            activity.setId(randomUUID().toString());
            activity.setCollectivity(collectivity);
        }
        return activityRepository.saveAll(activities);
    }

    public List<CollectivityActivity> getActivitiesByCollectivity(String collectivityId) {
        collectivityRepository.findById(collectivityId)
                .orElseThrow(() -> new NotFoundException(
                        "Collectivity.id=" + collectivityId + " not found"));
        return activityRepository.findAllByCollectivityId(collectivityId);
    }

    public List<ActivityMemberAttendance> createAttendances(String collectivityId,
                                                             String activityId,
                                                             List<ActivityMemberAttendance> attendances) {
        collectivityRepository.findById(collectivityId)
                .orElseThrow(() -> new NotFoundException(
                        "Collectivity.id=" + collectivityId + " not found"));

        var activity = activityRepository.findById(activityId)
                .orElseThrow(() -> new NotFoundException(
                        "Activity.id=" + activityId + " not found"));

        for (ActivityMemberAttendance att : attendances) {
            if (attendanceRepository.isAlreadyConfirmed(activityId, att.getMember().getId())) {
                throw new BadRequestException(
                        "Member.id=" + att.getMember().getId()
                                + " attendance already confirmed for activity.id=" + activityId);
            }
            att.setId(randomUUID().toString());
            att.setActivity(activity);
        }
        return attendanceRepository.saveAll(attendances);
    }

    public List<ActivityMemberAttendance> getAttendances(String collectivityId,
                                                          String activityId) {
        collectivityRepository.findById(collectivityId)
                .orElseThrow(() -> new NotFoundException(
                        "Collectivity.id=" + collectivityId + " not found"));
        activityRepository.findById(activityId)
                .orElseThrow(() -> new NotFoundException(
                        "Activity.id=" + activityId + " not found"));
        return attendanceRepository.findAllByActivityId(activityId);
    }
}