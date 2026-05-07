package edu.hei.school.agricultural.repository;

import edu.hei.school.agricultural.entity.Activity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class ActivityRepository {
    private final Connection connection;

    public List<Activity> saveAll(List<Activity> activities) {
        List<Activity> savedActivities = new ArrayList<>();
        
        try (PreparedStatement preparedStatement = connection.prepareStatement("""
                insert into activity (id, collectivity_id, name, description, activity_date, created_at, updated_at)
                values (?, ?, ?, ?, ?, ?, ?)
                on conflict (id) do update set name = excluded.name,
                                                 description = excluded.description,
                                                 activity_date = excluded.activity_date,
                                                 updated_at = excluded.updated_at
                """)) {
            
            for (Activity activity : activities) {
                preparedStatement.setString(1, activity.getId());
                preparedStatement.setString(2, activity.getCollectivityId());
                preparedStatement.setString(3, activity.getName());
                preparedStatement.setString(4, activity.getDescription());
                preparedStatement.setDate(5, activity.getActivityDate() != null ? 
                    java.sql.Date.valueOf(activity.getActivityDate()) : null);
                preparedStatement.setTimestamp(6, java.sql.Timestamp.valueOf(activity.getCreatedAt()));
                preparedStatement.setTimestamp(7, java.sql.Timestamp.valueOf(activity.getUpdatedAt()));
                preparedStatement.addBatch();
            }
            
            preparedStatement.executeBatch();
            
            // Return saved activities
            for (Activity activity : activities) {
                savedActivities.add(findById(activity.getId()).orElseThrow());
            }
            
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        
        return savedActivities;
    }

    public Optional<Activity> findById(String id) {
        try (PreparedStatement preparedStatement = connection.prepareStatement("""
                select id, collectivity_id, name, description, activity_date, created_at, updated_at
                from activity
                where id = ?
                """)) {
            
            preparedStatement.setString(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            
            if (resultSet.next()) {
                return Optional.of(mapFromResultSet(resultSet));
            }
            
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        
        return Optional.empty();
    }

    public List<Activity> findByCollectivityId(String collectivityId) {
        List<Activity> activities = new ArrayList<>();
        
        try (PreparedStatement preparedStatement = connection.prepareStatement("""
                select id, collectivity_id, name, description, activity_date, created_at, updated_at
                from activity
                where collectivity_id = ?
                order by activity_date desc
                """)) {
            
            preparedStatement.setString(1, collectivityId);
            ResultSet resultSet = preparedStatement.executeQuery();
            
            while (resultSet.next()) {
                activities.add(mapFromResultSet(resultSet));
            }
            
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        
        return activities;
    }

    private Activity mapFromResultSet(ResultSet resultSet) throws SQLException {
        Activity activity = new Activity();
        activity.setId(resultSet.getString("id"));
        activity.setCollectivityId(resultSet.getString("collectivity_id"));
        activity.setName(resultSet.getString("name"));
        activity.setDescription(resultSet.getString("description"));
        
        java.sql.Date activityDate = resultSet.getDate("activity_date");
        if (activityDate != null) {
            activity.setActivityDate(activityDate.toLocalDate());
        }
        
        activity.setCreatedAt(resultSet.getTimestamp("created_at").toLocalDateTime());
        activity.setUpdatedAt(resultSet.getTimestamp("updated_at").toLocalDateTime());
        
        return activity;
    }
}
