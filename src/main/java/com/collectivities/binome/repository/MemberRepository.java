package com.collectivities.binome.repository;

import com.collectivities.binome.entity.CreateMember;
import com.collectivities.binome.entity.Member;
import com.collectivities.binome.entity.MemberOccupation;
import com.collectivities.binome.exceptions.AppBadRequestException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.sql.Date;
import java.time.LocalDate;
import java.util.*;

@Slf4j
@Repository
@RequiredArgsConstructor
public class MemberRepository {

    private final Connection conn;

    public Optional<Member> findById(String id) {
        String sql = "SELECT * FROM member WHERE id = ?";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, id);
            ResultSet rs = ps.executeQuery();
            return rs.next() ? Optional.of(mapResultSetToMember(rs)) : Optional.empty();
        } catch (SQLException e) {
            throw new AppBadRequestException("Error finding member: " + e.getMessage());
        }
    }

    public Member save(CreateMember data) {
        String id = UUID.randomUUID().toString();
        String sql = """
            INSERT INTO member (id, first_name, last_name, birth_date, gender, address, 
             profession, phone_number, email, registration_date, occupation, collectivity_id)
            VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
        """;

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, id);
            ps.setString(2, data.getFirstName());
            ps.setString(3, data.getLastName());
            ps.setDate(4, toSqlDate(data.getBirthDate()));
            ps.setString(5, nullSafe(data.getGender()));
            ps.setString(6, data.getAddress());
            ps.setString(7, data.getProfession());
            ps.setInt(8, data.getPhoneNumber());
            ps.setString(9, data.getEmail());
            ps.setDate(10, Date.valueOf(LocalDate.now()));
            ps.setString(11, nullSafe(data.getOccupation()));
            ps.setString(12, data.getCollectivityIdentifier());
            ps.executeUpdate();

            Member member = mapCreateMemberToMember(data, id);
            saveReferees(id, data.getReferees());
            return member;
        } catch (SQLException e) {
            throw new AppBadRequestException("Error creating member: " + e.getMessage());
        }
    }

    public void attachMember(String memberId, String collectivityId, MemberOccupation occupation) {
        String sql = "UPDATE member SET collectivity_id = ?, occupation = ? WHERE id = ?";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, collectivityId);
            ps.setString(2, nullSafe(occupation));
            ps.setString(3, memberId);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new AppBadRequestException("Error attaching member: " + e.getMessage());
        }
    }

    private Member mapResultSetToMember(ResultSet rs) throws SQLException {
        Member member = new Member();
        member.setId(rs.getString("id"));
        member.setFirstName(rs.getString("first_name"));
        member.setLastName(rs.getString("last_name"));
        member.setBirthDate(toLocalDate(rs.getDate("birth_date")));
        member.setGender(parseGender(rs.getString("gender")));
        member.setAddress(rs.getString("address"));
        member.setProfession(rs.getString("profession"));
        member.setPhoneNumber(rs.getInt("phone_number"));
        member.setEmail(rs.getString("email"));
        member.setRegistrationDate(toLocalDate(rs.getDate("registration_date")));
        member.setOccupation(parseOccupation(rs.getString("occupation")));
        member.setReferees(loadReferees(member.getId()));
        return member;
    }

    private Member mapCreateMemberToMember(CreateMember data, String id) {
        Member member = new Member();
        member.setId(id);
        member.setFirstName(data.getFirstName());
        member.setLastName(data.getLastName());
        member.setBirthDate(data.getBirthDate());
        member.setGender(data.getGender());
        member.setAddress(data.getAddress());
        member.setProfession(data.getProfession());
        member.setPhoneNumber(data.getPhoneNumber());
        member.setEmail(data.getEmail());
        member.setRegistrationDate(LocalDate.now());
        member.setOccupation(data.getOccupation());
        member.setReferees(data.getReferees());
        return member;
    }

    private List<String> loadReferees(String memberId) {
        List<String> referees = new ArrayList<>();
        String sql = "SELECT referee_id FROM member_referee WHERE member_id = ?";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, memberId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) referees.add(rs.getString("referee_id"));
        } catch (SQLException e) {
            throw new AppBadRequestException("Error loading referees: " + e.getMessage());
        }
        return referees;
    }

    private void saveReferees(String memberId, List<String> refereeIds) {
        if (refereeIds == null || refereeIds.isEmpty()) return;
        String sql = "INSERT INTO member_referee (member_id, referee_id) VALUES (?, ?)";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            for (String refereeId : refereeIds) {
                ps.setString(1, memberId);
                ps.setString(2, refereeId);
                ps.addBatch();
            }
            ps.executeBatch();
        } catch (SQLException e) {
            throw new AppBadRequestException("Error saving referees: " + e.getMessage());
        }
    }

    private Date toSqlDate(LocalDate date) { return date != null ? Date.valueOf(date) : null; }
    private LocalDate toLocalDate(Date date) { return date != null ? date.toLocalDate() : null; }
    private String nullSafe(Enum<?> e) { return e != null ? e.name() : null; }
    private Gender parseGender(String s) { return s != null ? Gender.valueOf(s) : null; }
    private MemberOccupation parseOccupation(String s) { return s != null ? MemberOccupation.valueOf(s) : null; }
}
