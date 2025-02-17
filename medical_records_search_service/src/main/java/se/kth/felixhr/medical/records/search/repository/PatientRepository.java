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
    public Uni<List<PatientDTO>> getPatientsByName(String firstName, String lastName) {
        return Uni.createFrom().completionStage(() -> CompletableFuture.supplyAsync(() -> {
            List<PatientDTO> patients = new ArrayList<>();
            String sql = "SELECT * FROM t_patient WHERE first_name = ? AND last_name = ?";

            try (Connection conn = dataSource.getConnection();
                 PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, firstName);
                stmt.setString(2, lastName);
                ResultSet rs = stmt.executeQuery();

                while (rs.next()) {
                    patients.add(new PatientDTO(
                            rs.getLong("patient_id"),
                            rs.getString("first_name"),
                            rs.getString("last_name"),
                            rs.getDate("birth_date"), // ✅ Store as Date, not String
                            rs.getInt("person_number")
                    ));
                }
            } catch (Exception e) {
                throw new RuntimeException("Error fetching patients", e);
            }
            return patients;
        }));
    }

    // Find patients assigned to a doctor based on encounters
    public Uni<List<PatientDTO>> getPatientsByDoctorEncounters(Long doctorId) {
        return Uni.createFrom().completionStage(() -> CompletableFuture.supplyAsync(() -> {
            List<PatientDTO> patients = new ArrayList<>();
            String sql = """
                SELECT * FROM t_patient 
                WHERE patient_id IN (SELECT DISTINCT patient_id FROM t_encounter WHERE doctor_id = ?)
            """;

            try (Connection conn = dataSource.getConnection();
                 PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setLong(1, doctorId);
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
                throw new RuntimeException("Error fetching patients by doctor", e);
            }
            return patients;
        }));
    }
}
