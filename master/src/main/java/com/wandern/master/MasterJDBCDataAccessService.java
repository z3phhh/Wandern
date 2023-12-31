package com.wandern.master;

import com.wandern.master.DTO.projection.ServiceDetailsProjection;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository("jdbcServiceDetails")
@RequiredArgsConstructor
public class MasterJDBCDataAccessService {

    private final JdbcTemplate jdbcTemplate;
    private final MasterRowMapper masterRowMapper;

    public List<ServiceDetailsProjection> selectAllServiceDetails() {
        var sql = """
                SELECT
                    r.deployment_id, r.deployment_unit, r.system, r.service_url,
                    r.context_path, r.port, r.ip, r.status, 
                    m.system_load, m.jvm_cpu_load, m.used_memory_mb, m.free_memory_mb, m.total_threads 
                FROM registered_service r 
                LEFT JOIN metrics_service m ON r.id = m.registered_service_id
                LIMIT 1000
                """;

        return jdbcTemplate.query(sql, masterRowMapper);
    }

    public Optional<ServiceDetailsProjection> selectServiceDetailsById(Long id) {
        var sql = """
                SELECT
                    r.deployment_id, r.deployment_unit, r.system, r.service_url,
                    r.context_path, r.port, r.ip, r.status, 
                    m.system_load, m.jvm_cpu_load, m.used_memory_mb, m.free_memory_mb, m.total_threads 
                FROM registered_service r 
                LEFT JOIN metrics_service m ON r.id = m.registered_service_id
                WHERE r.id = ?
                """;

        return jdbcTemplate.query(sql, masterRowMapper, id)
                .stream()
                .findFirst();
    }
}
