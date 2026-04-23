package com.collectivities.binome.repository;

import com.collectivities.binome.entity.Collectivity;
import com.collectivities.binome.entity.CreateCollectivity;
import com.collectivities.binome.exceptions.AppBadRequestException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class CollectivityRepository {

    private final Connection conn;

    public Collectivity createCollectivity(CreateCollectivity data) {
        String id = UUID.randomUUID().toString();
        String query = """
                INSERT INTO collectivity (id, location, name, specialty, unique_number, unique_name, federation_approval, creation_date)
                VALUES (?, ?, ?, ?, ?, ?, ?, CURRENT_DATE)
                """;

        try (PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setString(1, id);
            ps.setString(2, data.getLocation());
            ps.setString(3, "Collectivity of " + data.getLocation());
            ps.setString(4, "GENERAL_AGRICULTURE");
            ps.setString(5, null);
            ps.setString(6, null);
            ps.setBoolean(7, data.getFederationApproval() != null && data.getFederationApproval());
            ps.executeUpdate();

            Collectivity collectivity = new Collectivity();
            collectivity.setId(id);
            collectivity.setLocation(data.getLocation());
            return collectivity;
        } catch (SQLException e) {
            throw new AppBadRequestException("Error creating collectivity: " + e.getMessage());
        }
    }

    public Optional<Collectivity> findById(UUID id) {
        String sql = "SELECT * FROM collectivity WHERE id = ?";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setObject(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return Optional.of(mapResultSetToCollectivity(rs));
            }
            return Optional.empty();
        } catch (SQLException e) {
            throw new AppBadRequestException("Error finding collectivity: " + e.getMessage());
        }
    }

    public Optional<Collectivity> findById(String id) {
        String sql = "SELECT * FROM collectivity WHERE id = ?";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return Optional.of(mapResultSetToCollectivity(rs));
            }
            return Optional.empty();
        } catch (SQLException e) {
            throw new AppBadRequestException("Error finding collectivity: " + e.getMessage());
        }
    }

    public boolean existsById(UUID id) {
        String sql = "SELECT 1 FROM collectivity WHERE id = ?";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setObject(1, id);
            ResultSet rs = ps.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            throw new AppBadRequestException("Error checking collectivity existence: " + e.getMessage());
        }
    }

    public boolean existsById(String id) {
        String sql = "SELECT 1 FROM collectivity WHERE id = ?";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, id);
            ResultSet rs = ps.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            throw new AppBadRequestException("Error checking collectivity existence: " + e.getMessage());
        }
    }

    public Collectivity assignIdentity(UUID id, String number, String name) {
        String sql = "UPDATE collectivity SET unique_number = ?, unique_name = ? WHERE id = ?";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, number);
            ps.setString(2, name);
            ps.setObject(3, id);
            ps.executeUpdate();
            return findById(id).orElseThrow(() -> new AppBadRequestException("Collectivity not found after update"));
        } catch (SQLException e) {
            throw new AppBadRequestException("Error assigning identity: " + e.getMessage());
        }
    }

    public Collectivity assignIdentity(String id, String number, String name) {
        String sql = "UPDATE collectivity SET unique_number = ?, unique_name = ? WHERE id = ?";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, number);
            ps.setString(2, name);
            ps.setString(3, id);
            ps.executeUpdate();
            return findById(id).orElseThrow(() -> new AppBadRequestException("Collectivity not found after update"));
        } catch (SQLException e) {
            throw new AppBadRequestException("Error assigning identity: " + e.getMessage());
        }
    }

    public Optional<Collectivity> findByUniqueNumber(String uniqueNumber) {
        String sql = "SELECT * FROM collectivity WHERE unique_number = ?";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, uniqueNumber);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return Optional.of(mapResultSetToCollectivity(rs));
            }
            return Optional.empty();
        } catch (SQLException e) {
            throw new AppBadRequestException("Error finding collectivity by unique number: " + e.getMessage());
        }
    }

    public Optional<Collectivity> findByUniqueName(String uniqueName) {
        String sql = "SELECT * FROM collectivity WHERE unique_name = ?";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, uniqueName);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return Optional.of(mapResultSetToCollectivity(rs));
            }
            return Optional.empty();
        } catch (SQLException e) {
            throw new AppBadRequestException("Error finding collectivity by unique name: " + e.getMessage());
        }
    }

    public boolean existsByUniqueNumber(String uniqueNumber) {
        String sql = "SELECT 1 FROM collectivity WHERE unique_number = ?";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, uniqueNumber);
            ResultSet rs = ps.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            throw new AppBadRequestException("Error checking unique number: " + e.getMessage());
        }
    }

    public boolean existsByUniqueName(String uniqueName) {
        String sql = "SELECT 1 FROM collectivity WHERE unique_name = ?";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, uniqueName);
            ResultSet rs = ps.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            throw new AppBadRequestException("Error checking unique name: " + e.getMessage());
        }
    }

    public List<Collectivity> findAll() {
        List<Collectivity> collectivities = new ArrayList<>();
        String sql = "SELECT * FROM collectivity ORDER BY creation_date DESC";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                collectivities.add(mapResultSetToCollectivity(rs));
            }
        } catch (SQLException e) {
            throw new AppBadRequestException("Error finding all collectivities: " + e.getMessage());
        }
        return collectivities;
    }

    public void deleteById(UUID id) {
        String sql = "DELETE FROM collectivity WHERE id = ?";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setObject(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new AppBadRequestException("Error deleting collectivity: " + e.getMessage());
        }
    }

    public void deleteById(String id) {
        String sql = "DELETE FROM collectivity WHERE id = ?";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new AppBadRequestException("Error deleting collectivity: " + e.getMessage());
        }
    }

    public long count() {
        String sql = "SELECT COUNT(*) FROM collectivity";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getLong(1);
            }
            return 0;
        } catch (SQLException e) {
            throw new AppBadRequestException("Error counting collectivities: " + e.getMessage());
        }
    }

    private Collectivity mapResultSetToCollectivity(ResultSet rs) throws SQLException {
        Collectivity collectivity = new Collectivity();
        collectivity.setId(rs.getString("id"));
        collectivity.setUniqueNumber(rs.getString("unique_number"));
        collectivity.setUniqueName(rs.getString("unique_name"));
        collectivity.setLocation(rs.getString("location"));
        return collectivity;
    }
}