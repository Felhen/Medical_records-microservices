package se.kth.felixhr.medical.records.search.repository;

import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import io.agroal.api.AgroalDataSource;
import se.kth.felixhr.medical.records.search.model.EncounterDTO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@ApplicationScoped
public class EncounterRepository {

    @Inject
    AgroalDataSource dataSource;

    public Uni<List<EncounterDTO>> getEncountersByDoctorAndDate(String doctorName, Date date) {
        return Uni.createFrom().completionStage(() -> CompletableFuture.supplyAsync(() -> {
            List<EncounterDTO> encounters = new ArrayList<>();

            String sql = """
            SELECT e.encounter_id, e.patient_id, e.doctor_id, e.encounter_date, e.encounter_info, 
                   p.first_name AS patient_first_name, p.last_name AS patient_last_name
            FROM t_encounter e
            JOIN t_patient p ON e.patient_id = p.patient_id
            JOIN t_user u ON e.doctor_id = u.user_id
            WHERE u.username = ? AND e.encounter_date = ?
        """;

            try (Connection conn = dataSource.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, doctorName);
                stmt.setDate(2, new java.sql.Date(date.getTime()));
                ResultSet rs = stmt.executeQuery();

                while (rs.next()) {
                    encounters.add(new EncounterDTO(
                            rs.getLong("encounter_id"),
                            rs.getLong("patient_id"),
                            rs.getLong("doctor_id"),
                            rs.getDate("encounter_date"),
                            rs.getString("encounter_info"),
                            rs.getString("patient_first_name") + " " + rs.getString("patient_last_name") // ✅ Patient Full Name
                    ));
                }
            } catch (Exception e) {
                throw new RuntimeException("Error fetching encounters", e);
            }
            return encounters;
        }));
    }

}

