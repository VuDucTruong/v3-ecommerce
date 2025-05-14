package shop.holy.v3.ecommerce.shared.config;

import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.deser.std.NumberDeserializers;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.util.TimeZone;

@Configuration
public class JacksonConfig {
    // ISO_DATE_TIME formatter with optional fractional seconds
    private static final DateTimeFormatter ISO_DATE_TIME = new DateTimeFormatterBuilder()
            .parseCaseInsensitive()
            .append(DateTimeFormatter.ISO_LOCAL_DATE_TIME)
            .optionalStart()
            .appendOffsetId()
            .toFormatter();

    @Bean
    public Jackson2ObjectMapperBuilderCustomizer jacksonCustomizer() {
        return jacksonObjectMapperBuilder -> {
            // Set global date format to ISO 8601
            SimpleDateFormat dateFormat  = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSX");
            dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
            jacksonObjectMapperBuilder.dateFormat(dateFormat);

            jacksonObjectMapperBuilder.featuresToDisable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
//            jacksonObjectMapperBuilder.featuresToEnable(DeserializationFeature.ADJUST_DATES_TO_CONTEXT_TIME_ZONE);

            // Add any additional custom modules if needed
            SimpleModule module = new SimpleModule();
            module.addDeserializer(Byte.class, new NumberDeserializers.ByteDeserializer(Byte.class, null));
            jacksonObjectMapperBuilder.modules(module,
                    new JavaTimeModule()
                            .addDeserializer(java.time.LocalDateTime.class, new LocalDateTimeDeserializer(ISO_DATE_TIME))
                            .addSerializer(java.time.LocalDateTime.class, new LocalDateTimeSerializer(ISO_DATE_TIME)),
                    new Jdk8Module(),
                    new ParameterNamesModule()
            );
        };
    }
}