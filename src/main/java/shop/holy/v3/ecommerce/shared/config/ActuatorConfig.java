package shop.holy.v3.ecommerce.shared.config;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.actuate.web.exchanges.HttpExchangeRepository;
import org.springframework.boot.actuate.web.exchanges.InMemoryHttpExchangeRepository;
import org.springframework.boot.actuate.web.exchanges.Include;
import org.springframework.boot.actuate.web.exchanges.servlet.HttpExchangesFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Set;

@Configuration
public class ActuatorConfig {
    private final Set<Include> includes = Include.defaultIncludes();

    private static class TraceRequestFilter extends HttpExchangesFilter {

        public TraceRequestFilter(HttpExchangeRepository repository, Set<Include> includes) {
            super(repository, includes);
        }

        @Override
        protected boolean shouldNotFilter(HttpServletRequest request) {
            return request.getServletPath().contains("actuator")
                    || request.getServletPath().contains("swagger") || request.getServletPath().contains("api-docs");
        }
    }

    @Bean
    public HttpExchangesFilter httpExchangesFilter() {
        return new TraceRequestFilter(new InMemoryHttpExchangeRepository(), includes);
    }
}
