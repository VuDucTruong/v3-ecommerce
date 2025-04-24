package shop.holy.v3.ecommerce.shared.config;

import lombok.RequiredArgsConstructor;
import org.flywaydb.core.Flyway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.util.Optional;

@RequiredArgsConstructor
@Component
public class StartupAggregator implements ApplicationListener<ContextRefreshedEvent> {
    private final Optional<Flyway> flyway;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        flyway.ifPresent(Flyway::migrate);
    }
}
