package shop.holy.v3.ecommerce;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.flyway.FlywayAutoConfiguration;

@Slf4j
@SpringBootApplication(exclude = {FlywayAutoConfiguration.class})
public class V3EcommerceApplication {

    public static void main(String[] args) {
        SpringApplication.run(V3EcommerceApplication.class, args);
        log.info("Application started successfully.");
    }


}
