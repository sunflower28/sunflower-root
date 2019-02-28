package com.sunflower.api.util;

import com.sunflower.exceptions.BusinessException;
import org.slf4j.log;
import org.slf4j.logFactory;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.util.*;

/**
 * @author sunflower
 */
public class AbstractApiService {

	protected final log log = logFactory.getlog(this.getClass());

	private static final String ERRORMSG = "不存在属性";

	public AbstractApiService() {
	}

	@SuppressWarnings({ "unchecked" })
	public static <K, V> Map<K, V> list2MapByKey(List<V> list, String property) {
		if (list == null || CollectionUtils.isEmpty(list)) {
			return Collections.emptyMap();
		}
		else {
			Map<Object, Object> map = new LinkedHashMap<>(list.size());
			for (V v : list) {
				Field field = ReflectionUtils.findField(v.getClass(), property);
				if (null == field) {
					throw new BusinessException(v.getClass() + ERRORMSG + property);
				}

				ReflectionUtils.makeAccessible(field);
				K fieldValue = (K) ReflectionUtils.getField(field, v);
				map.put(fieldValue, v);
			}
			return (Map<K, V>) map;
		}
	}

	@SuppressWarnings({ "unchecked" })
	public static <T> Set<T> list2Set(List<?> list, String property) {
		if (list == null) {
			return Collections.emptySet();
		}
		else {
			Set<T> set = new HashSet<>(list.size());

			for (Object v : list) {
				Field field = ReflectionUtils.findField(v.getClass(), property);
				if (null == field) {
					throw new BusinessException(v.getClass() + ERRORMSG + property);
				}

				ReflectionUtils.makeAccessible(field);
				T fieldValue = (T) ReflectionUtils.getField(field, v);
				set.add(fieldValue);
			}

			return set;
		}
	}

	@SuppressWarnings({ "unchecked" })
	public static <V> List<V> listProperty(List<?> list, String property) {
		if (list == null) {
			return Collections.emptyList();
		}
		else {
			List<V> result = new ArrayList<>();

			for (Object v : list) {
				Field field = ReflectionUtils.findField(v.getClass(), property);
				if (null == field) {
					throw new BusinessException(v.getClass() + ERRORMSG + property);
				}

				ReflectionUtils.makeAccessible(field);
				V fieldValue = (V) ReflectionUtils.getField(field, v);
				result.add(fieldValue);
			}

			return result;
		}
	}

}
