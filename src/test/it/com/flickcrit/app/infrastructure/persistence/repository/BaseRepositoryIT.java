package com.flickcrit.app.infrastructure.persistence.repository;

import com.flickcrit.app.BaseSpringBootIT;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

abstract class BaseRepositoryIT extends BaseSpringBootIT {

    @Autowired
    protected JdbcTemplate jdbcTemplate;

    /**
     * Disables foreign key checks for the current session in the database.
     * This operation sets the PostgreSQL session replication role to 'replica',
     * allowing for unrestricted data manipulation without enforcing foreign key constraints.
     *
     * This method is typically used in integration tests where consistent data setup or teardown
     * is required without being obstructed by foreign key rules.
     */
    protected void disableForeignKeyChecks() {
        jdbcTemplate.execute("SET session_replication_role = 'replica';");
    }
}
