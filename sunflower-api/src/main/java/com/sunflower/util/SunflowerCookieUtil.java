package com.sunflower.util;

import javax.servlet.http.Cookie;

public class SunflowerCookieUtil {

	private static final ThreadLocal<Cookie[]> cookieThreadLocal = new ThreadLocal();

	public SunflowerCookieUtil() {
	}

	public static void set(Cookie[] cookies) {
		cookieThreadLocal.set(cookies);
	}

	public static void remove() {
		cookieThreadLocal.remove();
	}

	public static Cookie[] get() {
		return (Cookie[]) cookieThreadLocal.get();
	}

}