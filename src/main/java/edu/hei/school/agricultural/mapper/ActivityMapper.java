package edu.hei.school.agricultural.mapper;

import edu.hei.school.agricultural.entity.ActivityType;
import edu.hei.school.agricultural.entity.CollectivityActivity;
import edu.hei.school.agricultural.entity.MonthlyRecurrenceRule;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Component
public class ActivityMapper {

    public CollectivityActivity mapFromResultSet(ResultSet rs) throws SQLException {
        String weekOrdinalStr = rs.getString("recurrence_week_ordinal");
        String dayOfWeek = rs.getString("recurrence_day_of_week");

        MonthlyRecurrenceRule recurrenceRule = null;
        if (weekOrdinalStr != null && dayOfWeek != null) {
            recurrenceRule = MonthlyRecurrenceRule.builder()
                    .weekOrdinal(rs.getInt("recurrence_week_ordinal"))
                    .dayOfWeek(dayOfWeek)
                    .build();
        }

        java.sql.Date execDate = rs.getDate("executive_date");

        return CollectivityActivity.builder()
                .id(rs.getString("id"))
                .label(rs.getString("label"))
                .activityType(rs.getString("activity_type") == null
                        ? null
                        : ActivityType.valueOf(rs.getString("activity_type")))
                .recurrenceRule(recurrenceRule)
                .executiveDate(execDate == null ? null : execDate.toLocalDate())
                .memberOccupationConcerned(List.of())
                .build();
    }
}