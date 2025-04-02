package shop.holy.v3.ecommerce.shared.config;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.deser.std.NumberDeserializers;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.util.ISO8601DateFormat;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JacksonConfig {
    @Bean
    public Jackson2ObjectMapperBuilderCustomizer jacksonCustomizer(){
        return JksObjMapperBuilder-> {
//            JksObjMapperBuilder.dateFormat(new ISO8601DateFormat());

            JksObjMapperBuilder.featuresToDisable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
            JksObjMapperBuilder.featuresToDisable(DeserializationFeature.ADJUST_DATES_TO_CONTEXT_TIME_ZONE);

            SimpleModule module = new SimpleModule();
            module.addDeserializer(Byte.class, new NumberDeserializers.ByteDeserializer(Byte.class, null));
            JksObjMapperBuilder.modules(module, new JavaTimeModule());
        };
    }
}
