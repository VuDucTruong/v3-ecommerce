package shop.holy.v3.ecommerce.shared.config;

import lombok.RequiredArgsConstructor;
import org.flywaydb.core.Flyway;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;

@RequiredArgsConstructor
@Component
public class StartupAggregator implements ApplicationListener<ContextRefreshedEvent> {
    private final Flyway flyway;

    private final DataSource dataSource;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
//        try(Connection con = dataSource.getConnection();
//            Statement statement = con.createStatement()
//        ) {
//
//            statement.execute("DELETE FROM flyway_schema_history WHERE 1=1");
//
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }

        flyway.migrate();

    }
}
