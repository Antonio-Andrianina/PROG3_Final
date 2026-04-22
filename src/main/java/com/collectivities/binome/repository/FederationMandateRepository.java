package com.collectivities.binome.repository;

import com.collectivities.binome.entity.FederationMandate;
import com.collectivities.binome.entity.FederationPosition;
import com.collectivities.binome.entity.Member;
import com.collectivities.binome.exceptions.AppBadRequestException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Repository
@RequiredArgsConstructor
public class FederationMandateRepository {

    private final Connection conn;
    private final MemberRepository memberRepository;

    public FederationMandate save(FederationMandate mandate) {
        if (mandate.getId() == null) {
            return insert(mandate);
        }
        return update(mandate);
    }

    private FederationMandate insert(FederationMandate mandate) {
        String sql = """
            INSERT INTO federation_mandate (member_id, position, start_date, end_date, is_active)
            VALUES (?, ?, ?, ?, ?)
            """;

        try (PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, mandate.getMember().getId());
            ps.setString(2, mandate.getPosition().name());
            ps.setDate(3, Date.valueOf(mandate.getStartDate()));
            ps.setDate(4, Date.valueOf(mandate.getEndDate()));
            ps.setBoolean(5, mandate.isActive());
            ps.executeUpdate();

            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                mandate.setId(rs.getLong(1));
            }
            return mandate;
        } catch (SQLException e) {
            throw new AppBadRequestException("Error saving federation mandate: " + e.getMessage());
        }
    }

    private FederationMandate update(FederationMandate mandate) {
        String sql = """
            UPDATE federation_mandate 
            SET member_id = ?, position = ?, start_date = ?, end_date = ?, is_active = ?
            WHERE id = ?
            """;

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, mandate.getMember().getId());
            ps.setString(2, mandate.getPosition().name());
            ps.setDate(3, Date.valueOf(mandate.getStartDate()));
            ps.setDate(4, Date.valueOf(mandate.getEndDate()));
            ps.setBoolean(5, mandate.isActive());
            ps.setLong(6, mandate.getId());
            ps.executeUpdate();
            return mandate;
        } catch (SQLException e) {
            throw new AppBadRequestException("Error updating federation mandate: " + e.getMessage());
        }
    }

    public Optional<FederationMandate> findById(Long id) {
        String sql = "SELECT * FROM federation_mandate WHERE id = ?";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, id);
            ResultSet rs = ps.executeQuery();
            return rs.next() ? Optional.of(mapResultSetToMandate(rs)) : Optional.empty();
        } catch (SQLException e) {
            throw new AppBadRequestException("Error finding mandate: " + e.getMessage());
        }
    }

    public List<FederationMandate> findByMemberId(String memberId) {
        List<FederationMandate> mandates = new ArrayList<>();
        String sql = "SELECT * FROM federation_mandate WHERE member_id = ? ORDER BY start_date DESC";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, memberId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                mandates.add(mapResultSetToMandate(rs));
            }
        } catch (SQLException e) {
            throw new AppBadRequestException("Error finding mandates by member: " + e.getMessage());
        }
        return mandates;
    }

    public Optional<FederationMandate> findCurrentByPosition(FederationPosition position) {
        String sql = """
            SELECT * FROM federation_mandate 
            WHERE position = ? AND is_active = true AND start_date <= ? AND end_date >= ?
            """;

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, position.name());
            ps.setDate(2, Date.valueOf(LocalDate.now()));
            ps.setDate(3, Date.valueOf(LocalDate.now()));
            ResultSet rs = ps.executeQuery();
            return rs.next() ? Optional.of(mapResultSetToMandate(rs)) : Optional.empty();
        } catch (SQLException e) {
            throw new AppBadRequestException("Error finding current mandate: " + e.getMessage());
        }
    }

    public List<FederationMandate> findAllCurrent() {
        List<FederationMandate> mandates = new ArrayList<>();
        String sql = """
            SELECT * FROM federation_mandate 
            WHERE is_active = true AND start_date <= ? AND end_date >= ?
            ORDER BY position
            """;

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setDate(1, Date.valueOf(LocalDate.now()));
            ps.setDate(2, Date.valueOf(LocalDate.now()));
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                mandates.add(mapResultSetToMandate(rs));
            }
        } catch (SQLException e) {
            throw new AppBadRequestException("Error finding current mandates: " + e.getMessage());
        }
        return mandates;
    }

    public List<FederationMandate> findAllByYear(int year) {
        List<FederationMandate> mandates = new ArrayList<>();
        String sql = """
            SELECT * FROM federation_mandate 
            WHERE YEAR(start_date) = ? OR YEAR(end_date) = ?
            ORDER BY start_date DESC
            """;

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, year);
            ps.setInt(2, year);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                mandates.add(mapResultSetToMandate(rs));
            }
        } catch (SQLException e) {
            throw new AppBadRequestException("Error finding mandates by year: " + e.getMessage());
        }
        return mandates;
    }

    public void endMandate(Long id) {
        String sql = "UPDATE federation_mandate SET is_active = false, end_date = ? WHERE id = ?";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setDate(1, Date.valueOf(LocalDate.now()));
            ps.setLong(2, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new AppBadRequestException("Error ending mandate: " + e.getMessage());
        }
    }

    public long countMandatesByMemberAndPosition(String memberId, FederationPosition position) {
        String sql = "SELECT COUNT(*) FROM federation_mandate WHERE member_id = ? AND position = ?";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, memberId);
            ps.setString(2, position.name());
            ResultSet rs = ps.executeQuery();
            return rs.next() ? rs.getLong(1) : 0;
        } catch (SQLException e) {
            throw new AppBadRequestException("Error counting mandates: " + e.getMessage());
        }
    }

    private FederationMandate mapResultSetToMandate(ResultSet rs) throws SQLException {
        Member member = memberRepository.findById(rs.getString("member_id"))
                .orElseThrow(() -> new AppBadRequestException("Member not found for mandate"));

        return FederationMandate.builder()
                .id(rs.getLong("id"))
                .member(member)
                .position(FederationPosition.valueOf(rs.getString("position")))
                .startDate(rs.getDate("start_date").toLocalDate())
                .endDate(rs.getDate("end_date").toLocalDate())
                .isActive(rs.getBoolean("is_active"))
                .build();
    }
}
