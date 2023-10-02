CREATE TABLE registered_service (
                                    id BIGSERIAL PRIMARY KEY,
                                    deployment_id VARCHAR NOT NULL,
                                    status VARCHAR NOT NULL,
                                    system VARCHAR NOT NULL,
                                    deployment_unit VARCHAR NOT NULL,
                                    service_url VARCHAR NOT NULL,
                                    context_path VARCHAR NOT NULL,
                                    port INT NOT NULL,
                                    ip VARCHAR NOT NULL,
                                    registration_time TIMESTAMP WITH TIME ZONE NOT NULL
);

CREATE TABLE metrics_service (
                                 id BIGSERIAL PRIMARY KEY,
                                 registered_service_id BIGINT,
                                 timestamp TIMESTAMP(0) WITH TIME ZONE NOT NULL,
                                 system_load DOUBLE PRECISION NOT NULL,
                                 jvm_cpu_load DOUBLE PRECISION NOT NULL,
                                 used_memory_mb BIGINT NOT NULL,
                                 free_memory_mb BIGINT NOT NULL,
                                 total_threads INT NOT NULL
);

ALTER TABLE registered_service
    ADD CONSTRAINT unique_deployment_id
        UNIQUE (deployment_id);

ALTER TABLE metrics_service
    ADD CONSTRAINT fk_metrics_service_registered_service
        FOREIGN KEY (registered_service_id)
            REFERENCES registered_service(id);
