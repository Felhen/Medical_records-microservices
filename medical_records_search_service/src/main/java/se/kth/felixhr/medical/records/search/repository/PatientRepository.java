package se.kth.felixhr.medical.records.search.repository;

import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import io.agroal.api.AgroalDataSource;
import se.kth.felixhr.medical.records.search.model.PatientDTO;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@ApplicationScoped
public class PatientRepository {

    @Inject
    AgroalDataSource dataSource;

    // Find patients by first & last name
    public Uni<List<PatientDTO>> getPatientsByName(String name) {
        return Uni.createFrom().completionStage(() -> CompletableFuture.supplyAsync(() -> {
            List<PatientDTO> patients = new ArrayList<>();
            System.out.println(name);
            if (name == null || name.trim().length() < 2) {
                return patients; // Return empty list if input is too short
            }

            String[] parts = name.trim().split("\\s+");
            boolean isFullName = parts.length > 1;
            String sql = isFullName
                    ? "SELECT * FROM t_patient WHERE first_name LIKE ? AND last_name LIKE ?"
                    : "SELECT * FROM t_patient WHERE first_name LIKE ? OR last_name LIKE ?";

            try (Connection conn = dataSource.getConnection();
                 PreparedStatement stmt = conn.prepareStatement(sql)) {
                if (isFullName) {
                    stmt.setString(1, parts[0] + "%");
                    stmt.setString(2, parts[1] + "%");
                } else {
                    stmt.setString(1, "%" + name + "%");
                    stmt.setString(2, "%" + name + "%");
                }

                ResultSet rs = stmt.executeQuery();
                while (rs.next()) {
                    patients.add(new PatientDTO(
                            rs.getLong("patient_id"),
                            rs.getString("first_name"),
                            rs.getString("last_name"),
                            rs.getDate("birth_date"),
                            rs.getInt("person_number")
                    ));
                }
            } catch (Exception e) {
                throw new RuntimeException("Error fetching patients by name", e);
            }
            return patients;
        }));
    }

    // Find patients assigned to a doctor based on encounters
    public Uni<List<PatientDTO>> getPatientsByDoctorName(String doctorUsername) {
        return Uni.createFrom().completionStage(() -> CompletableFuture.supplyAsync(() -> {
            List<PatientDTO> patients = new ArrayList<>();

            if (doctorUsername == null || doctorUsername.trim().isEmpty()) {
                return patients; // Prevent empty searches
            }

            String sql = """
                SELECT DISTINCT p.* 
                FROM t_patient p
                JOIN t_encounter e ON p.patient_id = e.patient_id
                JOIN t_user u ON e.doctor_id = u.user_id
                WHERE u.username LIKE ?
            """;

            try (Connection conn = dataSource.getConnection();
                 PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, "%" + doctorUsername + "%"); // Supports partial searches
                ResultSet rs = stmt.executeQuery();

                while (rs.next()) {
                    patients.add(new PatientDTO(
                            rs.getLong("patient_id"),
                            rs.getString("first_name"),
                            rs.getString("last_name"),
                            rs.getDate("birth_date"),
                            rs.getInt("person_number")
                    ));
                }
            } catch (Exception e) {
                throw new RuntimeException("Error fetching patients by doctor username", e);
            }
            return patients;
        }));
    }
}
