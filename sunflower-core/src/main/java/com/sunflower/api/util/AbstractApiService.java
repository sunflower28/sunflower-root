package com.sunflower.api.util;

import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.plugins.pagination.Pagination;
import com.sunflower.api.InputPageDto;
import com.sunflower.api.PageDto;
import com.sunflower.api.PageResultDto;
import com.sunflower.exceptions.BusinessException;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.util.*;

/**
 * @author sunflower
 */
public class AbstractApiService {

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
					throw new BusinessException(v.getClass() + "不存在的属性" + property);
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
					throw new BusinessException(v.getClass() + "不存在属性" + property);
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
					throw new BusinessException(v.getClass() + "不存在属性" + property);
				}

				ReflectionUtils.makeAccessible(field);
				V fieldValue = (V) ReflectionUtils.getField(field, v);
				result.add(fieldValue);
			}

			return result;
		}
	}

	public <T> Page<T> request2Page(InputPageDto requestDto) {
		return requestDto == null ? new Page()
				: new Page<>(requestDto.getPageNum(), requestDto.getPageSize());
	}

	public <K> PageResultDto<K> response2Page(Pagination page, List<K> list,
			InputPageDto request) {
		PageDto<K> rPage = new PageDto<>();
		rPage.setList(list);
		rPage.setPageCount(page == null ? 0L : page.getPages());
		rPage.setPageSize(page == null ? request.getPageSize() : page.getSize());
		rPage.setTotal(page == null ? 0L : page.getTotal());
		rPage.setPageNum(page == null ? request.getPageNum() : page.getCurrent());
		return PageResultDto.success(rPage);
	}

}
