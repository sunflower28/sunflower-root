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
import javax.validation.constraints.NotNull;
import java.lang.reflect.Method;

/**
 * @author sunflower 自定义参数解析--token--用于登陆--前后端交互
 */
public class UserProfileArgumentResolver implements HandlerMethodArgumentResolver {

	public static final Logger logger = LoggerFactory
			.getLogger(UserProfileArgumentResolver.class);

	public UserProfileArgumentResolver() {
		// fgfg
	}

	@Override
	public boolean supportsParameter(@Nullable MethodParameter methodParameter) {
		return UserProfile.class.isAssignableFrom(methodParameter.getParameterType());
	}

	@Override
	public Object resolveArgument(@Nullable MethodParameter methodParameter,
			ModelAndViewContainer modelAndViewContainer,
			@Nullable NativeWebRequest nativeWebRequest,
			WebDataBinderFactory webDataBinderFactory) throws Exception {
		String token = nativeWebRequest.getHeader("token");
		if (token != null && !"".equals(token.trim()) && token.split("\\.").length == 3) {
			Method method = methodParameter.getMethod();
			if (method == null) {
				throw new BusinessException(CommonEnum.LOGIN_TIMEOUT);
			}
			else {
				try {
					String subject = TokenUtil.parseJWT(token).getSubject();
					ObjectMapper objectMapper = new ObjectMapper();
					Object readValue = objectMapper.readValue(subject,
							methodParameter.getParameterType());
					if (null == readValue) {
						throw new BusinessException(CommonEnum.LOGIN_TIMEOUT);
					}
					else {
						SunflowerTokenUtil.set(token);
						/*
						 * if (readValue instanceof HouseHelperUserPrefile) {
						 * HouseHelperUserPrefile userPrefile =
						 * (HouseHelperUserPrefile)readValue; if
						 * (AnnotationUtils.findAnnotation(method, RequiredLogin.class) !=
						 * null && !StringUtils.hasText(userPrefile.getPhone())) { throw
						 * new BusinessException(CommonEnum.OPTNEEDPHONE); }
						 *
						 * String redisOpenId = this.customRedis.get("token_" +
						 * userPrefile.getLoginId()); if
						 * (!userPrefile.getOpenId().equals(redisOpenId)) { throw new
						 * BusinessException(CommonEnum.LOGIN_FORCEOUT); } }
						 */

						return readValue;
					}
				}
				catch (Exception e) {
					if (e instanceof BusinessException) {
						throw e;
					}
					else {
						throw new BusinessException(CommonEnum.LOGIN_TIMEOUT);
					}
				}
			}
		}
		else {
			logger.debug("请求头中未包含token");
			throw new BusinessException(CommonEnum.LOGIN_TIMEOUT);
		}
	}

}
