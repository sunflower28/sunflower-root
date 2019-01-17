package com.sunflower.tx;

import org.apache.ibatis.mapping.SqlCommandType;

import java.util.EnumSet;
import java.util.Set;

/**
 * @author sunflower
 */
public final class TxServiceHelper {

	private static ThreadLocal<EnumSet<SqlCommandType>> transactionAttribute = new ThreadLocal<>();

	private TxServiceHelper() {
	}

	public static void add(SqlCommandType type) {
		EnumSet<SqlCommandType> set = transactionAttribute.get();
		if (set == null) {
			set = EnumSet.noneOf(SqlCommandType.class);
			transactionAttribute.set(set);
		}
		set.add(type);
	}

	public static void addAll(Set<SqlCommandType> sets) {
		EnumSet<SqlCommandType> set = transactionAttribute.get();
		if (set == null) {
			set = EnumSet.noneOf(SqlCommandType.class);
			transactionAttribute.set(set);
		}

		set.addAll(sets);
	}

	public static Set<SqlCommandType> get() {
		EnumSet<SqlCommandType> set = transactionAttribute.get();
		return set == null ? EnumSet.noneOf(SqlCommandType.class) : set;
	}

	public static void removeAll() {
		transactionAttribute.remove();
	}

}
