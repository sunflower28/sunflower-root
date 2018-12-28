package com.sunflower.tx;

import org.apache.ibatis.mapping.SqlCommandType;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * @author sunflower
 */
public class TxServiceHelper {

	private static ThreadLocal<Set<SqlCommandType>> transactionAttribute = new ThreadLocal<>();

	public TxServiceHelper() {
	}

	public static void add(SqlCommandType type) {
		Set<SqlCommandType> set = transactionAttribute.get();
		if (set == null) {
			set = new HashSet<>();
			transactionAttribute.set(set);
		}

		set.add(type);
	}

	public static void addAll(Set<SqlCommandType> sets) {
		Set<SqlCommandType> set = transactionAttribute.get();
		if (set == null) {
			set = new HashSet<>();
			transactionAttribute.set(set);
		}

		set.addAll(sets);
	}

	public static Set<SqlCommandType> get() {
		Set set = transactionAttribute.get();
		return set == null ? Collections.emptySet() : set;
	}

	public static void removeAll() {
		transactionAttribute.remove();
	}

}
