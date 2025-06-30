package shop.holy.v3.ecommerce.shared.config;

import org.springframework.core.convert.converter.Converter;
import org.springframework.format.FormatterRegistry;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import shop.holy.v3.ecommerce.shared.constant.BizErrors;

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
                throw BizErrors.INVALID_DATE_FORMAT.exception();
            }
        }
    }


    @Override
    public void addFormatters(FormatterRegistry registry) {
        registry.addConverter(new CustomDateConverter());
    }


    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
//                .allowedOriginPatterns("*")
                .allowedOrigins("http://192.168.1.15:3000")
                .allowedMethods("*")
                .allowedHeaders("*")
                .allowCredentials(true); // or true with exact origins
    }

}
