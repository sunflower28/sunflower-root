package com.sunflower.config.pac4jcas.cache.redis;

import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheException;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.Collection;
import java.util.Set;

public class RedisCache<K, V> implements Cache<K, V> {

	private RedisTemplate<K, V> redisTemplate;

	private K keyPrefix;

	public RedisCache(RedisTemplate<K, V> redisTemplate, K keyPrefix) {
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

	@SuppressWarnings("unchecked")
	private K getKey(K key) {
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append(this.keyPrefix).append(key);
		return (K) (stringBuilder);
	}

	@Override
	public V get(K key) {
		return this.redisTemplate.opsForValue().get(this.getKey(key));
	}

	@Override
	public V put(K key, V value) {
		this.redisTemplate.opsForValue().set(this.getKey(key), value);
		return value;
	}

	@Override
	public V remove(K key) {
		V previous = this.get(key);
		if (previous != null) {
			this.redisTemplate.delete(this.getKey(key));
		}

		return previous;
	}

	@Override
	public void clear() {
		throw new CacheException("not support");
	}

	@Override
	public int size() {
		throw new CacheException("not support");
	}

	@Override
	public Set<K> keys() {
		throw new CacheException("not support");
	}

	@Override
	public Collection<V> values() {
		throw new CacheException("not support");
	}

}
