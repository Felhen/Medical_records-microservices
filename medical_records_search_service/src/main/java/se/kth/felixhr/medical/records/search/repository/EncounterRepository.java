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

    public Uni<List<EncounterDTO>> getEncountersByDoctor(Long doctorId) {
        return Uni.createFrom().completionStage(() -> CompletableFuture.supplyAsync(() -> {
            List<EncounterDTO> encounters = new ArrayList<>();
            String sql = "SELECT * FROM t_encounter WHERE doctor_id = ?";

            try (Connection conn = dataSource.getConnection();
                 PreparedStatement stmt = conn.prepareStatement(sql)) {
                 stmt.setLong(1, doctorId);
                 ResultSet rs = stmt.executeQuery();

                 while (rs.next()) {
                     encounters.add(new EncounterDTO(
                             rs.getLong("encounter_id"),
                             rs.getLong("patient_id"),
                             rs.getLong("doctor_id"),
                             rs.getDate("encounter_date"),
                             rs.getString("encounter_info")
                     ));
                 }
            } catch (Exception e) {
                throw new RuntimeException("Error fetching encounters by doctor", e);
            }
            return encounters;
        }));
    }

    public Uni<List<EncounterDTO>> getEncountersByDoctorAndDate(Long doctorId, Date date) {
        return Uni.createFrom().completionStage(() -> CompletableFuture.supplyAsync(() -> {
            List<EncounterDTO> encounters = new ArrayList<>();
            String sql = "SELECT * FROM t_encounter WHERE doctor_id = ? AND encounter_date = ?";

            try (Connection conn = dataSource.getConnection();
                 PreparedStatement stmt = conn.prepareStatement(sql)) {
                 stmt.setLong(1, doctorId);
                 stmt.setDate(2, new java.sql.Date(date.getTime()));
                 ResultSet rs = stmt.executeQuery();

                 while (rs.next()) {
                     encounters.add(new EncounterDTO(
                             rs.getLong("encounter_id"),
                             rs.getLong("patient_id"),
                             rs.getLong("doctor_id"),
                             rs.getDate("encounter_date"),
                             rs.getString("encounter_info")
                     ));
                 }
            } catch (Exception e) {
                throw new RuntimeException("Error fetching encounters", e);
            }
            return encounters;
        }));
    }
}

