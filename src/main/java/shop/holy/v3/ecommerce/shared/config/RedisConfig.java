package shop.holy.v3.ecommerce.shared.config;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.CacheManager;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.*;
import shop.holy.v3.ecommerce.shared.constant.CacheKeys;

@Configuration
@Profile({"cacheredis", "cachemem"})
public class RedisConfig {

    @Value("${app.cache.test.enable}")
    private boolean testEnabled;

    private final ObjectMapper objectMapper;

    public RedisConfig() {
        objectMapper = new ObjectMapper();
        objectMapper.configure(SerializationFeature.INDENT_OUTPUT, false);
//        PolymorphicTypeValidator ptv = BasicPolymorphicTypeValidator.builder()
//                .allowIfSubType("com.yourapp.model") // restrict to your packages
//                .build();
        /// INCLUDE TYPE NAME WHEN SERIALIZE
//        objectMapper.activateDefaultTyping(objectMapper.getPolymorphicTypeValidator(), ObjectMapper.DefaultTyping.NON_FINAL);
        objectMapper.configure(SerializationFeature.INDENT_OUTPUT, false);
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }


    @Bean
    @Profile("cacheredis")
    public RedisSerializer<Object> redisSerializer() {
        if (testEnabled) {
            return new Jackson2JsonRedisSerializer<Object>(objectMapper.getTypeFactory().constructType(Object.class));
        }
        return new JdkSerializationRedisSerializer();
    }

    @Bean
    @Profile("cacheredis")
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory factory, RedisSerializer<Object> defaultSerializer) {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(factory);

        template.setKeySerializer(new StringRedisSerializer());
        template.setValueSerializer(defaultSerializer);
        template.setHashKeySerializer(new StringRedisSerializer());
        template.setHashValueSerializer(defaultSerializer);
        template.setDefaultSerializer(defaultSerializer);
        template.afterPropertiesSet();
        return template;
    }


    @Bean
    @Profile("cacheredis")
    public CacheManager redisCacheManager(RedisConnectionFactory factory, RedisSerializer<Object> defaultSerializer) {
        RedisCacheConfiguration config = RedisCacheConfiguration.defaultCacheConfig()
                .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(defaultSerializer));
        return RedisCacheManager.builder(factory).cacheDefaults(config).build();
    }

    @Bean
    @Profile("cachemem")
    public CacheManager memoryCacheManager() {
        return new ConcurrentMapCacheManager(CacheKeys.values());
    }

}
