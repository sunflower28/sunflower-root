package com.sunflower.util;

public class SunflowerTokenUtil {

	private static final ThreadLocal<String> tokenThreadLocal = new ThreadLocal();

	public SunflowerTokenUtil() {
	}

	public static void set(String token) {
		tokenThreadLocal.set(token);
	}

	public static void remove() {
		tokenThreadLocal.remove();
	}

	public static String get() {
		return tokenThreadLocal.get();
	}

}
