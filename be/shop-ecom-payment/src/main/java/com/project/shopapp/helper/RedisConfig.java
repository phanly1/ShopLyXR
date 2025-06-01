package com.project.shopapp.helper;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.time.Duration;

@Configuration
@EnableCaching
public class RedisConfig {
    @Value("${spring.data.redis.host}")
    private String host;
    @Value("${spring.data.redis.port}")
    private int port;

    @Bean
    public RedisConnectionFactory redisConnectionFactory() {
        RedisStandaloneConfiguration configuration = new RedisStandaloneConfiguration(host, port);
        return new LettuceConnectionFactory(configuration);
    }


    @Bean
    public RedisTemplate<String, Object> redisTemplate() {
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(redisConnectionFactory());
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(RedisSerializer.json());
        return redisTemplate;
    }

    @Bean
    CacheManager cacheManager(RedisTemplate<String, Object> redisTemplate) {
        RedisCacheConfiguration cacheConfig = RedisCacheConfiguration.defaultCacheConfig()
                .serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(new StringRedisSerializer()))
                .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(redisTemplate.getValueSerializer()))
                .entryTtl(Duration.ofMinutes(10));

        return RedisCacheManager.RedisCacheManagerBuilder
                .fromConnectionFactory(redisTemplate.getConnectionFactory())
                .cacheDefaults(cacheConfig)
                .build();
    }
}

//
//    @Autowired
//    private RedisTemplate<String, String> redisTemplate;
//
//    private final ObjectMapper objectMapper = new ObjectMapper();
//
//    // Lưu giá trị vào Redis
//    public <T> void setValue(String key, T value) throws Exception {
//        String jsonValue = objectMapper.writeValueAsString(value); // Chuyển đổi thành JSON
//        redisTemplate.opsForValue().set(key, jsonValue, 10, TimeUnit.MINUTES);
//    }
//
//    // Lấy giá trị từ Redis
//    public <T> T getValue(String key, Class<T> clazz) throws Exception {
//        String jsonValue = redisTemplate.opsForValue().get(key);
//        if (jsonValue != null) {
//            return objectMapper.readValue(jsonValue, clazz); // Chuyển đổi lại thành đối tượng
//        }
//        return null;
//    }


