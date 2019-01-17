package com.sunflower.spring;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sunflower.constants.error.CommonEnum;
import com.sunflower.exceptions.BusinessException;
import com.sunflower.token.TokenUtil;
import com.sunflower.token.UserProfile;
import com.sunflower.util.SunflowerTokenUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import javax.annotation.Nullable;
import java.lang.reflect.Method;

/**
 * @author sunflower 自定义参数解析--token--用于登陆--前后端交互
 */
public class UserProfileArgumentResolver implements HandlerMethodArgumentResolver {

	private static final Logger logger = LoggerFactory
			.getLogger(UserProfileArgumentResolver.class);

	public UserProfileArgumentResolver() {
		// userProfileArgumentResolver
	}

	@Override
	public boolean supportsParameter(@Nullable MethodParameter methodParameter) {
		if (methodParameter == null) {
			return false;
		}
		return UserProfile.class.isAssignableFrom(methodParameter.getParameterType());
	}

	@Override
	public Object resolveArgument(@Nullable MethodParameter methodParameter,
			ModelAndViewContainer modelAndViewContainer,
			@Nullable NativeWebRequest nativeWebRequest,
			WebDataBinderFactory webDataBinderFactory) {
		if (methodParameter == null || nativeWebRequest == null) {
			throw new BusinessException("ERROR", "参数不合法");
		}

		String token = nativeWebRequest.getHeader("token");
		boolean tokenIsTrue = token != null && !"".equals(token.trim())
				&& token.split("\\.").length == 3;
		if (!tokenIsTrue) {
			logger.debug("请求头中未包含token");
			throw new BusinessException(CommonEnum.LOGIN_TIMEOUT);
		}

		Method method = methodParameter.getMethod();
		if (method == null) {
			throw new BusinessException(CommonEnum.LOGIN_TIMEOUT);
		}
		try {
			String subject = TokenUtil.parseJWT(token).getSubject();
			ObjectMapper objectMapper = new ObjectMapper();
			Object readValue = objectMapper.readValue(subject,
					methodParameter.getParameterType());
			if (null == readValue) {
				throw new BusinessException(CommonEnum.LOGIN_TIMEOUT);
			}

			// 放入ThreadLocal存储
			SunflowerTokenUtil.set(token);
			return readValue;

		}
		catch (BusinessException e) {
			throw e;
		}
		catch (Exception e) {
			throw new BusinessException(CommonEnum.LOGIN_TIMEOUT);
		}
	}

}
