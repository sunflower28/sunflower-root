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
		return (K) (this.keyPrefix.toString() + key.toString());
	}

	@Override
	public V get(K key) throws CacheException {
		return this.redisTemplate.opsForValue().get(this.getKey(key));
	}

	@Override
	public V put(K key, V value) throws CacheException {
		this.redisTemplate.opsForValue().set(this.getKey(key), value);
		return value;
	}

	@Override
	public V remove(K key) throws CacheException {
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
