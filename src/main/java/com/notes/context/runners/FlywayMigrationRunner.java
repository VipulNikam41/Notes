package com.notes.context.runners;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.flywaydb.core.Flyway;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class FlywayMigrationRunner implements ApplicationRunner {
    private final Flyway flyway;

    @Override
    public void run(ApplicationArguments args) {
        log.info("{}, Running flyway migration", this.getClass().getSimpleName());
        flyway.migrate();
        log.info("{}, Flyway migration completed", this.getClass().getSimpleName());
    }
}
