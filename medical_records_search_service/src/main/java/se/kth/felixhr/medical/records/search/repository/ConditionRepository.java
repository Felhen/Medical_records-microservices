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

    public Uni<List<ConditionDTO>> getConditionsByName(String conditionName) {
        return Uni.createFrom().completionStage(() -> CompletableFuture.supplyAsync(() -> {
            List<ConditionDTO> conditions = new ArrayList<>();
            String sql = "SELECT * FROM t_condition WHERE condition_name = ?";

            try (Connection conn = dataSource.getConnection();
                 PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, conditionName);
                ResultSet rs = stmt.executeQuery();

                while (rs.next()) {
                    conditions.add(new ConditionDTO(
                            rs.getLong("condition_id"),
                            rs.getLong("patient_id"),
                            rs.getString("condition_name"),
                            rs.getString("condition_info"),
                            rs.getDate("condition_date")
                    ));
                }
            } catch (Exception e) {
                throw new RuntimeException("Error fetching conditions", e);
            }
            return conditions;
        }));
    }
}
