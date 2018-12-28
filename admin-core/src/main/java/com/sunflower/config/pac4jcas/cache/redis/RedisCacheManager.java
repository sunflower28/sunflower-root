package com.sunflower.config.pac4jcas.cache.redis;

import org.apache.shiro.cache.AbstractCacheManager;
import org.apache.shiro.cache.Cache;
import org.springframework.data.redis.core.RedisTemplate;

public class RedisCacheManager<K, V> extends AbstractCacheManager {

	private RedisTemplate<K, V> redisTemplate;

	private K shiroCacheKeyPrefix;

	public RedisCacheManager() {
	}

	public void setRedisTemplate(RedisTemplate<K, V> redisTemplate) {
		this.redisTemplate = redisTemplate;
	}

	public void setShiroCacheKeyPrefix(K shiroCacheKeyPrefix) {
		this.shiroCacheKeyPrefix = shiroCacheKeyPrefix;
	}

	@Override
	protected Cache createCache(String name) {
		return new RedisCache(this.redisTemplate, this.shiroCacheKeyPrefix);
	}

}
