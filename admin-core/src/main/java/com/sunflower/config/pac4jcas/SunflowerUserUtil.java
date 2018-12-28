package com.sunflower.config.pac4jcas;

public class SunflowerUserUtil {

	private static final ThreadLocal<SunflowerCasProfile> TOKEN_THREAD_LOCAL = new ThreadLocal<>();

	public SunflowerUserUtil() {
	}

	public static void set(SunflowerCasProfile profile) {
		TOKEN_THREAD_LOCAL.set(profile);
	}

	public static void remove() {
		TOKEN_THREAD_LOCAL.remove();
	}

	public static SunflowerCasProfile get() {
		return TOKEN_THREAD_LOCAL.get();
	}

}
