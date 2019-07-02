package com.hrong.db2kafka.redis;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.JdkSerializationRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

/**
 * @Author hrong
 * @ClassName RedisConfif
 * @Description
 * @Date 2019/6/30 15:17
 **/
@Slf4j
@Configuration
public class RedisConfig {

	@Bean
	public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory redisConnectionFactory) {
		RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
		redisTemplate.setConnectionFactory(redisConnectionFactory);
		redisTemplate.setKeySerializer(new StringRedisSerializer());
		log.info("设置redis缓存key序列化方式为：StringRedisSerializer,value序列化方式为：JdkSerializationRedisSerializer");
		redisTemplate.setValueSerializer(new JdkSerializationRedisSerializer());
		return redisTemplate;
	}

	@Bean
	public RedisTemplate<String, String> stringLongRedisTemplate(RedisConnectionFactory redisConnectionFactory) {
		RedisTemplate<String, String> redisTemplate = new RedisTemplate<>();
		redisTemplate.setConnectionFactory(redisConnectionFactory);
		redisTemplate.setKeySerializer(new StringRedisSerializer());
		log.info("设置redis缓存key序列化方式为：StringRedisSerializer,value序列化方式为：StringRedisSerializer");
		redisTemplate.setValueSerializer(new StringRedisSerializer());
		return redisTemplate;
	}


}
