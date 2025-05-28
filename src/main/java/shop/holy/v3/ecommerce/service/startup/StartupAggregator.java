package shop.holy.v3.ecommerce.service.startup;

import lombok.RequiredArgsConstructor;
import org.flywaydb.core.Flyway;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import java.util.Optional;

@RequiredArgsConstructor
@Component
public class StartupAggregator implements ApplicationListener<ContextRefreshedEvent> {

    private final Optional<Flyway> flyway;


//    @Value("${app.flyway-refresh}")
//    private boolean APP_FLYWAY_REFRESH;
//
//    private final JdbcTemplate jdbcTemplate;
//    private final TransactionTemplate transactionTemplate;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
//        if(APP_FLYWAY_REFRESH){
//            assert jdbcTemplate.getDataSource() != null;
//            TransactionTemplate txTemplate = new TransactionTemplate(
//                    new DataSourceTransactionManager(jdbcTemplate.getDataSource())
//            );
//            txTemplate.executeWithoutResult(status -> {
//                String sql = """
//                    DELETE FROM flyway_schema_history;
//                    """;
//                jdbcTemplate.execute(sql);
//            });
//        }

        flyway.ifPresent(Flyway::migrate);
    }
}
