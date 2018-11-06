package com.sunflower.framework.tx;

import org.apache.ibatis.mapping.SqlCommandType;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class TxServiceHelper {

	private static ThreadLocal<Set<SqlCommandType>> transactionAttribute = new ThreadLocal();

	public TxServiceHelper() {
	}

	public static void add(SqlCommandType type) {
		Set<SqlCommandType> list = (Set) transactionAttribute.get();
		if (list == null) {
			list = new HashSet();
			transactionAttribute.set(list);
		}

		((Set) list).add(type);
	}

	public static void addAll(Set<SqlCommandType> sets) {
		Set<SqlCommandType> list = (Set) transactionAttribute.get();
		if (list == null) {
			list = new HashSet();
			transactionAttribute.set(list);
		}

		((Set) list).addAll(sets);
	}

	public static Set<SqlCommandType> get() {
		Set<SqlCommandType> list = (Set) transactionAttribute.get();
		return list == null ? Collections.emptySet() : list;
	}

	public static void removeAll() {
		transactionAttribute.remove();
	}

}
