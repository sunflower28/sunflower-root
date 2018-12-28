package com.sunflower.util;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

public final class Servlets {

	private Servlets() {
	}

	public static HttpServletRequest getRequest() {
		ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder
				.getRequestAttributes();
		return requestAttributes == null ? null : requestAttributes.getRequest();
	}

	public static String getHeader(String headerName) {
		HttpServletRequest request = getRequest();
		return null == request ? null : request.getHeader(headerName);
	}

	public static HttpServletResponse getResponse() {
		ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder
				.getRequestAttributes();
		return requestAttributes == null ? null : requestAttributes.getResponse();
	}

	public static String getIpAddr() {
		return getIpAddr(getRequest());
	}

	public static String getIpAddr(HttpServletRequest request) {
		if (null == request) {
			return null;
		}
		else {
			String ip = request.getHeader("X-Forwarded-For");
			if (ip != null && ip.trim().length() != 0
					&& !"unKnown".equalsIgnoreCase(ip)) {
				int index = ip.indexOf(44);
				return index != -1 ? ip.substring(0, index) : ip;
			}
			else {
				ip = request.getHeader("X-Real-IP");
				return ip != null && ip.trim().length() != 0
						&& !"unKnown".equalsIgnoreCase(ip) ? ip : request.getRemoteAddr();
			}
		}
	}

	public static boolean isInnerIpRequest(String ipAddress) {
		if (ipAddress.contains(":")) {
			return true;
		}
		else {
			boolean isInnerIp = false;
			long ipNum = getIpNum(ipAddress);
			long aBegin = getIpNum("10.0.0.0");
			long aEnd = getIpNum("10.255.255.255");
			long bBegin = getIpNum("172.16.0.0");
			long bEnd = getIpNum("172.31.255.255");
			long cBegin = getIpNum("192.168.0.0");
			long cEnd = getIpNum("192.168.255.255");
			isInnerIp = isInner(ipNum, aBegin, aEnd) || isInner(ipNum, bBegin, bEnd)
					|| isInner(ipNum, cBegin, cEnd) || ipAddress.equals("127.0.0.1");
			return isInnerIp;
		}
	}

	private static long getIpNum(String ipAddress) {
		String[] ip = ipAddress.split("\\.");
		long a = (long) Integer.parseInt(ip[0]);
		long b = (long) Integer.parseInt(ip[1]);
		long c = (long) Integer.parseInt(ip[2]);
		long d = (long) Integer.parseInt(ip[3]);
		return a * 256L * 256L * 256L + b * 256L * 256L + c * 256L + d;
	}

	private static boolean isInner(long userIp, long begin, long end) {
		return userIp >= begin && userIp <= end;
	}

}
