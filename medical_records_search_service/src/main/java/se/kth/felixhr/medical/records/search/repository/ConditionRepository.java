package se.kth.felixhr.medical.records.search.repository;

import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import se.kth.felixhr.medical.records.search.model.ConditionDTO;
import io.agroal.api.AgroalDataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@ApplicationScoped
public class ConditionRepository {

    @Inject
    AgroalDataSource dataSource;

    public Uni<List<ConditionDTO>> getPatientsByCondition(String conditionName) {
        return Uni.createFrom().completionStage(() -> CompletableFuture.supplyAsync(() -> {
            List<ConditionDTO> conditions = new ArrayList<>();
            String sql = """
                SELECT c.condition_id, c.condition_name, c.condition_info, c.condition_date, 
                       p.patient_id, p.first_name, p.last_name 
                FROM t_condition c
                JOIN t_patient p ON c.patient_id = p.patient_id
                WHERE c.condition_name LIKE ?
            """;

            try (Connection conn = dataSource.getConnection();
                 PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, "%" + conditionName + "%");  // 🔹 Allows partial search
                ResultSet rs = stmt.executeQuery();

                while (rs.next()) {
                    conditions.add(new ConditionDTO(
                            rs.getLong("condition_id"),
                            rs.getLong("patient_id"),
                            rs.getString("condition_name"),
                            rs.getString("condition_info"),
                            rs.getDate("condition_date"),
                            rs.getString("first_name"),
                            rs.getString("last_name")
                    ));
                }
            } catch (Exception e) {
                throw new RuntimeException("Error fetching conditions", e);
            }
            return conditions;
        }));
    }

}
