package shop.holy.v3.ecommerce.shared.config;

import org.springframework.core.convert.converter.Converter;
import org.springframework.format.FormatterRegistry;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@Component
public class WebMVCConfig implements WebMvcConfigurer {

    public static class CustomDateConverter implements Converter<String, Date> {
        private final DateFormat dateFormat;

        public CustomDateConverter() {
            this.dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSX");
            this.dateFormat.setLenient(false);
        }

        @Override
        public Date convert(@NonNull String source) {
            try {
                return dateFormat.parse(source);
            } catch (ParseException e) {
                throw new IllegalArgumentException("Invalid date format. Please use yyyy-MM-dd'T'HH:mm:ss.SSSX");
            }
        }
    }


    @Override
    public void addFormatters(FormatterRegistry registry) {
        registry.addConverter(new CustomDateConverter());
    }

}
