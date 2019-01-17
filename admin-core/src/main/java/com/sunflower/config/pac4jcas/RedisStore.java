package com.sunflower.config.pac4jcas;

import org.pac4j.core.store.AbstractStore;
import org.springframework.data.redis.core.RedisTemplate;

public class RedisStore<K, V> extends AbstractStore<K, V> {

	private RedisTemplate<K, V> redisTemplate;

	private K keyPrefix;

	public RedisStore(RedisTemplate<K, V> redisTemplate, K keyPrefix) {
		if (redisTemplate == null) {
			throw new IllegalArgumentException("redisTemplate argument cannot be null.");
		}
		else if (keyPrefix == null) {
			throw new IllegalArgumentException("keyPrefix argument cannot be null.");
		}
		else {
			this.redisTemplate = redisTemplate;
			this.keyPrefix = keyPrefix;
		}
	}

	@SuppressWarnings({ "unchecked" })
	private K getKey(K key) {
		return (K) (String.valueOf(this.keyPrefix) + key);
	}

	@Override
	protected V internalGet(K key) {
		return this.redisTemplate.opsForValue().get(this.getKey(key));
	}

	@Override
	protected void internalSet(K key, V value) {
		this.redisTemplate.opsForValue().set(this.getKey(key), value);
	}

	@Override
	protected void internalRemove(K key) {
		this.redisTemplate.delete(this.getKey(key));
	}

}
