package com.sunflower.http;

import com.sunflower.constants.IEnum;
import com.sunflower.constants.error.CommonEnum;
import com.sunflower.util.SunflowerCookieUtil;
import com.sunflower.util.SunflowerTokenUtil;
import lombok.extern.slf4j.Slf4j;
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

@Slf4j
public class RestTemplateUtil extends RestTemplate {

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
					.exchange(url, HttpMethod.GET, new HttpEntity<>(body, httpHeaders),
							responseTypeParameterizedTypeReference, uriVariables)
					.getBody();
		}
		catch (Exception e) {
			log.error(String.format("接口%s调用失败,参数为%s", url, body), e);

			try {
				ParameterizedType parameterizedType = (ParameterizedType) responseTypeParameterizedTypeReference
						.getType();
				Class<T> eee = (Class) parameterizedType.getRawType();
				result = eee.getConstructor(IEnum.class)
						.newInstance(CommonEnum.HTTP_FAIL_500);
			}
			catch (Exception e2) {
				log.error(e2.getMessage(), e2);
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
		catch (Exception e) {
			log.error(String.format("接口%s调用失败,参数为%s", url, body), e);

			try {
				ParameterizedType parameterizedType = (ParameterizedType) responseTypeParameterizedTypeReference
						.getType();
				Class<T> eee = (Class) parameterizedType.getRawType();
				result = eee.getConstructor(IEnum.class)
						.newInstance(CommonEnum.HTTP_FAIL_500);
			}
			catch (Exception e2) {
				log.error(e2.getMessage(), e2);
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
					new HttpEntity<>(body, httpHeaders), responseType, new Object[0])
					.getBody();
		}
		catch (Exception e) {
			log.error(String.format("接口%s调用失败,参数为%s", url, body), e);

			try {
				result = responseType.getConstructor(IEnum.class)
						.newInstance(CommonEnum.HTTP_FAIL_500);
			}
			catch (Exception e2) {
				log.error(e2.getMessage(), e2);
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
					.exchange(url, HttpMethod.POST, new HttpEntity<>(body, httpHeaders),
							responseTypeParameterizedTypeReference, new Object[0])
					.getBody();
		}
		catch (Exception e) {
			log.error(String.format("接口%s调用失败,参数为%s", url, body), e);

			try {
				ParameterizedType parameterizedType = (ParameterizedType) responseTypeParameterizedTypeReference
						.getType();
				Class<T> eee = (Class) parameterizedType.getRawType();
				result = eee.getConstructor(IEnum.class)
						.newInstance(CommonEnum.HTTP_FAIL_500);
			}
			catch (Exception e2) {
				log.error(e2.getMessage(), e2);
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
					new HttpEntity<>(body, httpHeaders), responseType, new Object[0])
					.getBody();
		}
		catch (Exception e) {
			log.error(String.format("接口%s调用失败,参数为%s", url, body), e);

			try {
				result = responseType.getConstructor(IEnum.class)
						.newInstance(CommonEnum.HTTP_FAIL_500);
			}
			catch (Exception e2) {
				log.error(e2.getMessage(), e2);
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
			List<String> cookieNameAndValues = new ArrayList<>(cookies.length);

			for (Cookie cookie : cookies) {
				cookieNameAndValues.add(cookie.getName() + "=" + cookie.getValue());
			}

			httpHeaders.add("Cookie", String.join("; ", cookieNameAndValues));
			httpHeaders.setContentType(MediaType.APPLICATION_JSON_UTF8);
			result = this
					.exchange(url, HttpMethod.POST, new HttpEntity<>(body, httpHeaders),
							responseTypeParameterizedTypeReference, new Object[0])
					.getBody();
		}
		catch (Exception e) {
			log.error(String.format("接口%s调用失败,参数为%s", url, body), e);

			try {
				ParameterizedType parameterizedType = (ParameterizedType) responseTypeParameterizedTypeReference
						.getType();
				Class<T> eee = (Class) parameterizedType.getRawType();
				result = eee.getConstructor(IEnum.class)
						.newInstance(CommonEnum.HTTP_FAIL_500);
			}
			catch (Exception e2) {
				log.error(e2.getMessage(), e2);
			}
		}

		return (T) result;
	}

}
