package com.sunflower.framework.http;

import com.sunflower.framework.constants.IEnum;
import com.sunflower.framework.constants.error.CommonEnum;
import com.sunflower.framework.util.SunflowerCookieUtil;
import com.sunflower.framework.util.SunflowerTokenUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.Cookie;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class RestTemplateUtil extends RestTemplate {

	private static final Logger logger = LoggerFactory.getLogger(RestTemplateUtil.class);

	public RestTemplateUtil() {
	}

	public <T> T getJsonResult(String url, Object body,
			ParameterizedTypeReference<T> responseTypeParameterizedTypeReference,
			Map<String, ?> uriVariables) {
		Object result = null;

		try {
			HttpHeaders httpHeaders = new HttpHeaders();
			httpHeaders.setContentType(MediaType.APPLICATION_JSON);
			result = this
					.exchange(url, HttpMethod.GET, new HttpEntity(body, httpHeaders),
							responseTypeParameterizedTypeReference, uriVariables)
					.getBody();
		}
		catch (Exception var10) {
			logger.error(String.format("接口%s调用失败,参数为%s", url, body), var10);

			try {
				ParameterizedType parameterizedType = (ParameterizedType) responseTypeParameterizedTypeReference
						.getType();
				Class<T> eee = (Class) parameterizedType.getRawType();
				result = eee.getConstructor(IEnum.class)
						.newInstance(CommonEnum.HTTP_FAIL_500);
			}
			catch (Exception var9) {
				logger.error(var9.getMessage(), var9);
			}
		}

		return (T) result;
	}

	public <T> T postWithToken(String url, Object body,
			ParameterizedTypeReference<T> responseTypeParameterizedTypeReference) {
		Object result = null;

		try {
			HttpHeaders httpHeaders = new HttpHeaders();
			httpHeaders.add("token", SunflowerTokenUtil.get());
			httpHeaders.setContentType(MediaType.APPLICATION_JSON_UTF8);
			result = this
					.exchange(url, HttpMethod.POST, new HttpEntity(body, httpHeaders),
							responseTypeParameterizedTypeReference, new Object[0])
					.getBody();
		}
		catch (Exception var9) {
			logger.error(String.format("接口%s调用失败,参数为%s", url, body), var9);

			try {
				ParameterizedType parameterizedType = (ParameterizedType) responseTypeParameterizedTypeReference
						.getType();
				Class<T> eee = (Class) parameterizedType.getRawType();
				result = eee.getConstructor(IEnum.class)
						.newInstance(CommonEnum.HTTP_FAIL_500);
			}
			catch (Exception var8) {
				logger.error(var8.getMessage(), var8);
			}
		}

		return (T) result;
	}

	public <T> T postWithToken(String url, Object body, Class<T> responseType) {
		Object result = null;

		try {
			HttpHeaders httpHeaders = new HttpHeaders();
			httpHeaders.add("token", SunflowerTokenUtil.get());
			httpHeaders.setContentType(MediaType.APPLICATION_JSON_UTF8);
			result = this.exchange(url, HttpMethod.POST,
					new HttpEntity(body, httpHeaders), responseType, new Object[0])
					.getBody();
		}
		catch (Exception var8) {
			logger.error(String.format("接口%s调用失败,参数为%s", url, body), var8);

			try {
				result = responseType.getConstructor(IEnum.class)
						.newInstance(CommonEnum.HTTP_FAIL_500);
			}
			catch (Exception var7) {
				logger.error(var7.getMessage(), var7);
			}
		}

		return (T) result;
	}

	public <T> T postWithOutToken(String url, Object body,
			ParameterizedTypeReference<T> responseTypeParameterizedTypeReference) {
		Object result = null;

		try {
			HttpHeaders httpHeaders = new HttpHeaders();
			httpHeaders.setContentType(MediaType.APPLICATION_JSON_UTF8);
			result = this
					.exchange(url, HttpMethod.POST, new HttpEntity(body, httpHeaders),
							responseTypeParameterizedTypeReference, new Object[0])
					.getBody();
		}
		catch (Exception var9) {
			logger.error(String.format("接口%s调用失败,参数为%s", url, body), var9);

			try {
				ParameterizedType parameterizedType = (ParameterizedType) responseTypeParameterizedTypeReference
						.getType();
				Class<T> eee = (Class) parameterizedType.getRawType();
				result = eee.getConstructor(IEnum.class)
						.newInstance(CommonEnum.HTTP_FAIL_500);
			}
			catch (Exception var8) {
				logger.error(var8.getMessage(), var8);
			}
		}

		return (T) result;
	}

	public <T> T postWithOutToken(String url, Object body, Class<T> responseType) {
		Object result = null;

		try {
			HttpHeaders httpHeaders = new HttpHeaders();
			httpHeaders.setContentType(MediaType.APPLICATION_JSON_UTF8);
			result = this.exchange(url, HttpMethod.POST,
					new HttpEntity(body, httpHeaders), responseType, new Object[0])
					.getBody();
		}
		catch (Exception var8) {
			logger.error(String.format("接口%s调用失败,参数为%s", url, body), var8);

			try {
				result = responseType.getConstructor(IEnum.class)
						.newInstance(CommonEnum.HTTP_FAIL_500);
			}
			catch (Exception var7) {
				logger.error(var7.getMessage(), var7);
			}
		}

		return (T) result;
	}

	public <T> T postWithCookie(String url, Object body,
			ParameterizedTypeReference<T> responseTypeParameterizedTypeReference) {
		Object result = null;

		try {
			HttpHeaders httpHeaders = new HttpHeaders();
			Cookie[] cookies = SunflowerCookieUtil.get();
			List<String> cookieNameAndValues = new ArrayList(cookies.length);
			Cookie[] var8 = cookies;
			int var9 = cookies.length;

			for (int var10 = 0; var10 < var9; ++var10) {
				Cookie cookie = var8[var10];
				cookieNameAndValues.add(cookie.getName() + "=" + cookie.getValue());
			}

			httpHeaders.add("Cookie", String.join("; ", cookieNameAndValues));
			httpHeaders.setContentType(MediaType.APPLICATION_JSON_UTF8);
			result = this
					.exchange(url, HttpMethod.POST, new HttpEntity(body, httpHeaders),
							responseTypeParameterizedTypeReference, new Object[0])
					.getBody();
		}
		catch (Exception var13) {
			logger.error(String.format("接口%s调用失败,参数为%s", url, body), var13);

			try {
				ParameterizedType parameterizedType = (ParameterizedType) responseTypeParameterizedTypeReference
						.getType();
				Class<T> eee = (Class) parameterizedType.getRawType();
				result = eee.getConstructor(IEnum.class)
						.newInstance(CommonEnum.HTTP_FAIL_500);
			}
			catch (Exception var12) {
				logger.error(var12.getMessage(), var12);
			}
		}

		return (T) result;
	}

}
