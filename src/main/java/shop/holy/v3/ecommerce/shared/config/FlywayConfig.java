package shop.holy.v3.ecommerce.shared.config;

import lombok.RequiredArgsConstructor;
import org.flywaydb.core.Flyway;
import org.springframework.boot.autoconfigure.flyway.FlywayProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
@EnableConfigurationProperties(FlywayProperties.class)
public class FlywayConfig {

    private final FlywayProperties props;

    @Bean
    public Flyway manualFlyway() {
        return Flyway.configure()
                .dataSource(props.getUrl(), props.getUser(), props.getPassword())
                .driver(props.getDriverClassName())
                .defaultSchema(props.getDefaultSchema())
                .baselineOnMigrate(props.isBaselineOnMigrate())
                .baselineVersion(props.getBaselineVersion())
                .locations(props.getLocations().getFirst())
                .sqlMigrationPrefix(props.getSqlMigrationPrefix())
                .sqlMigrationSuffixes(props.getSqlMigrationSuffixes().getFirst())
                .outOfOrder(props.isOutOfOrder())
                .load();
    }

}