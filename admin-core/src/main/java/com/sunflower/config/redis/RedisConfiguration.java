package com.sunflower.config.redis;

import com.sunflower.config.pac4jcas.cache.redis.SessionRedisTemplate;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
@ConditionalOnClass({ RedisTemplate.class })
public class RedisConfiguration {

	@Bean
	@Primary
	public RedisTemplate<String, ?> redisTemplate(
			RedisConnectionFactory redisConnectionFactory) {
		if (redisConnectionFactory instanceof JedisConnectionFactory) {
			JedisConnectionFactory temp = (JedisConnectionFactory) redisConnectionFactory;
			if (!temp.getUsePool()) {
				throw new RuntimeException("请配置redis pool");
			}
		}

		RedisTemplate<String, ?> template = new RedisTemplate<>();
		RedisSerializer<String> stringSerializer = new StringRedisSerializer();
		template.setKeySerializer(stringSerializer);
		template.setHashKeySerializer(stringSerializer);
		template.setDefaultSerializer(new GenericJackson2JsonRedisSerializer());
		template.setConnectionFactory(redisConnectionFactory);
		template.setEnableTransactionSupport(true);
		return template;
	}

	@Bean
	@ConditionalOnMissingBean
	public SessionRedisTemplate sessionRedisTemplate(
			RedisConnectionFactory redisConnectionFactory) {
		SessionRedisTemplate template = new SessionRedisTemplate();
		template.setConnectionFactory(redisConnectionFactory);
		return template;
	}

}
