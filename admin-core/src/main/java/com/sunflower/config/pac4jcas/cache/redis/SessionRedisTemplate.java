package com.sunflower.config.pac4jcas.cache.redis;

import org.apache.shiro.session.Session;
import org.springframework.data.redis.connection.DefaultStringRedisConnection;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.JdkSerializationRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.lang.NonNull;

import javax.validation.constraints.NotNull;

public class SessionRedisTemplate extends RedisTemplate<String, Session> {

	public SessionRedisTemplate() {
		RedisSerializer<String> stringSerializer = new StringRedisSerializer();
		RedisSerializer<?> jdkSerializationRedisSerializer = new JdkSerializationRedisSerializer();
		this.setDefaultSerializer(jdkSerializationRedisSerializer);
		this.setKeySerializer(stringSerializer);
		this.setValueSerializer(jdkSerializationRedisSerializer);
		this.setHashKeySerializer(stringSerializer);
		this.setHashValueSerializer(jdkSerializationRedisSerializer);
	}

	public SessionRedisTemplate(RedisConnectionFactory connectionFactory) {
		this();
		this.setConnectionFactory(connectionFactory);
		this.afterPropertiesSet();
	}

	@Override
	@NonNull
	protected RedisConnection preProcessConnection(@NotNull RedisConnection connection,
			boolean existingConnection) {
		return new DefaultStringRedisConnection(connection);
	}

}
