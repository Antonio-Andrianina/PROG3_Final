package edu.hei.school.agricultural.repository;

import edu.hei.school.agricultural.entity.CollectivityLocalStat;
import edu.hei.school.agricultural.entity.CollectivityOverallStat;
import edu.hei.school.agricultural.entity.Member;
import edu.hei.school.agricultural.mapper.MemberMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.time.LocalDate;
import java.util.*;

@Repository
@RequiredArgsConstructor
public class StatisticsRepository {
    private final Connection connection;
    private final MemberMapper memberMapper;

    public Map<String, Double> getEarnedAmountByMember(String collectivityId, LocalDate from, LocalDate to) {
        Map<String, Double> result = new HashMap<>();
        String sql = """
            SELECT ct.member_debited_id, COALESCE(SUM(ct.amount), 0) as total
            FROM collectivity_transaction ct
            WHERE ct.collectivity_id = ?
              AND ct.creation_date BETWEEN ? AND ?
            GROUP BY ct.member_debited_id
            """;
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, collectivityId);
            ps.setDate(2, Date.valueOf(from));
            ps.setDate(3, Date.valueOf(to));
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                result.put(rs.getString("member_debited_id"), rs.getDouble("total"));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return result;
    }

    public Map<String, Double> getUnpaidAmountByMember(String collectivityId, LocalDate from, LocalDate to) {
        Map<String, Double> result = new HashMap<>();
        String sql = """
            SELECT m.id as member_id,
                   COALESCE(SUM(mf.amount), 0) as expected,
                   COALESCE(paid.total, 0) as paid
            FROM member m
            JOIN collectivity_member cm ON cm.member_id = m.id
            JOIN membership_fee mf ON mf.collectivity_id = cm.collectivity_id
            LEFT JOIN (
                SELECT ct.member_debited_id, COALESCE(SUM(ct.amount), 0) as total
                FROM collectivity_transaction ct
                WHERE ct.collectivity_id = ?
                  AND ct.creation_date BETWEEN ? AND ?
                GROUP BY ct.member_debited_id
            ) paid ON paid.member_debited_id = m.id
            WHERE cm.collectivity_id = ?
              AND mf.status = 'ACTIVE'
              AND mf.eligible_from <= ?
            GROUP BY m.id, paid.total
            """;
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, collectivityId);
            ps.setDate(2, Date.valueOf(from));
            ps.setDate(3, Date.valueOf(to));
            ps.setString(4, collectivityId);
            ps.setDate(5, Date.valueOf(to));
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                double expected = rs.getDouble("expected");
                double paid = rs.getDouble("paid");
                double unpaid = Math.max(0, expected - paid);
                result.put(rs.getString("member_id"), unpaid);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return result;
    }

    public List<CollectivityOverallStat> getOverallStats(LocalDate from, LocalDate to) {
        List<CollectivityOverallStat> results = new ArrayList<>();
        
        String sql = """
            SELECT c.id, c.name, c.number,
                   COUNT(DISTINCT cm.member_id) as total_members,
                   COUNT(DISTINCT CASE WHEN m.adhesion_date BETWEEN ? AND ? THEN m.id END) as new_members
            FROM collectivity c
            LEFT JOIN collectivity_member cm ON cm.collectivity_id = c.id
            LEFT JOIN member m ON m.id = cm.member_id
            GROUP BY c.id, c.name, c.number
            """;
        
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setDate(1, Date.valueOf(from));
            ps.setDate(2, Date.valueOf(to));
            ResultSet rs = ps.executeQuery();
            
            while (rs.next()) {
                String collectivityId = rs.getString("id");
                String name = rs.getString("name");
                Integer number = rs.getInt("number");
                int totalMembers = rs.getInt("total_members");
                int newMembers = rs.getInt("new_members");
                
                // Calculer le pourcentage de membres à jour
                double percentage = calculateUpToDatePercentage(collectivityId, to, totalMembers);
                
                Collectivity collectivity = Collectivity.builder()
                        .id(collectivityId)
                        .name(name)
                        .number(number)
                        .build();
                
                results.add(CollectivityOverallStat.builder()
                        .collectivity(collectivity)
                        .newMembersNumber(newMembers)
                        .overallMemberCurrentDuePercentage(percentage)
                        .build());
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return results;
    }
    
    private double calculateUpToDatePercentage(String collectivityId, LocalDate toDate, int totalMembers) {
        if (totalMembers == 0) return 0.0;
        
        String sql = """
            SELECT COUNT(DISTINCT m.id) as up_to_date_members
            FROM member m
            JOIN collectivity_member cm ON cm.member_id = m.id
            WHERE cm.collectivity_id = ?
              AND NOT EXISTS (
                  SELECT 1 FROM membership_fee mf
                  WHERE mf.collectivity_id = cm.collectivity_id
                    AND mf.status = 'ACTIVE'
                    AND mf.eligible_from <= ?
                    AND NOT EXISTS (
                        SELECT 1 FROM collectivity_transaction ct
                        WHERE ct.member_debited_id = m.id
                          AND ct.collectivity_id = cm.collectivity_id
                          AND ct.amount >= mf.amount
                    )
              )
            """;
        
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, collectivityId);
            ps.setDate(2, Date.valueOf(toDate));
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                int upToDate = rs.getInt("up_to_date_members");
                return (double) upToDate / totalMembers * 100;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return 0.0;
    }
}