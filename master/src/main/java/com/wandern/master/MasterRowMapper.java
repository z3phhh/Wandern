package com.wandern.master;

import com.wandern.clients.ServiceStatus;
import com.wandern.master.DTO.projection.MetricsProjection;
import com.wandern.master.DTO.projection.ServiceDetailsProjection;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class MasterRowMapper implements RowMapper<ServiceDetailsProjection> {

    @Override
    public ServiceDetailsProjection mapRow(ResultSet rs, int rowNum) throws SQLException {
        var metrics = new MetricsProjection(
            rs.getDouble("system_load"),
            rs.getDouble("jvm_cpu_load"),
            rs.getLong("used_memory_mb"),
            rs.getLong("free_memory_mb"),
            rs.getInt("total_threads")
        );

        return new ServiceDetailsProjection(
            rs.getString("deployment_id"),
            rs.getString("deployment_unit"),
            rs.getString("system"),
            rs.getString("service_url"),
            rs.getString("context_path"),
            rs.getInt("port"),
            rs.getString("ip"),
            ServiceStatus.valueOf(rs.getString("status")),
            metrics
        );
    }
}
