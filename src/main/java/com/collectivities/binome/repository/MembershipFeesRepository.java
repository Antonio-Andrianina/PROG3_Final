package com.collectivities.binome.repository;

import com.collectivities.binome.entity.ActivityStatus;
import com.collectivities.binome.entity.CreateMembershipFees;
import com.collectivities.binome.entity.MembershipFees;
import com.collectivities.binome.exceptions.AppBadRequestException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class MembershipFeesRepository {

    private final Connection conn;

    public List<MembershipFees> getByCollectivityId(String collectivityId) {
        List<MembershipFees> fees = new ArrayList<>();
        String sql = "SELECT * FROM membership_fees WHERE collectivity_id = ?";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setObject(1, UUID.fromString(collectivityId));
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                fees.add(mapResultSetToMembershipFees(rs));
            }
        } catch (SQLException e) {
            throw new AppBadRequestException("Error finding membership fees: " + e.getMessage());
        }
        return fees;
    }

    public String save(CreateMembershipFees fee, String collectivityId) {
        String id = UUID.randomUUID().toString();
        String sql = """
            INSERT INTO membership_fees (id, collectivity_id, eligible_from, frequency, amount, label, status)
            VALUES (?, ?, ?, ?, ?, ?, ?)
        """;

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setObject(1, UUID.fromString(id));
            ps.setObject(2, UUID.fromString(collectivityId));
            ps.setDate(3, Date.valueOf(fee.getEligibleForm()));
            ps.setString(4, fee.getFrequency().name());
            ps.setDouble(5, fee.getAmount());
            ps.setString(6, fee.getLabel());
            ps.setString(7, ActivityStatus.ACTIVE.name());
            ps.executeUpdate();
            return id;
        } catch (SQLException e) {
            throw new AppBadRequestException("Error saving membership fee: " + e.getMessage());
        }
    }

    public MembershipFees getById(String id) {
        String sql = "SELECT * FROM membership_fees WHERE id = ?";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setObject(1, UUID.fromString(id));
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return mapResultSetToMembershipFees(rs);
            }
            throw new AppBadRequestException("Membership fee not found: " + id);
        } catch (SQLException e) {
            throw new AppBadRequestException("Error finding membership fee: " + e.getMessage());
        }
    }

    private MembershipFees mapResultSetToMembershipFees(ResultSet rs) throws SQLException {
        MembershipFees fee = new MembershipFees();
        fee.setId(rs.getString("id"));
        fee.setEligibleForm(rs.getDate("eligible_from").toLocalDate());
        fee.setFrequency(com.collectivities.binome.controller.Frequency.valueOf(rs.getString("frequency")));
        fee.setAmount(rs.getDouble("amount"));
        fee.setLabel(rs.getString("label"));
        fee.setStatus(ActivityStatus.valueOf(rs.getString("status")));
        return fee;
    }
}