package com.sunflower.framework.spring;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sunflower.framework.constants.error.CommonEnum;
import com.sunflower.framework.exceptions.BusinessException;
import com.sunflower.framework.token.TokenUtil;
import com.sunflower.framework.token.UserProfile;
import com.sunflower.framework.util.SunflowerTokenUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import java.lang.reflect.Method;

/**
 * @author sunflower
 */
public class UserProfileArgumentResolver implements HandlerMethodArgumentResolver {

    public static final Logger logger = LoggerFactory.getLogger(UserProfileArgumentResolver.class);

    @Override
    public boolean supportsParameter(MethodParameter methodParameter) {
        return UserProfile.class.isAssignableFrom(methodParameter.getParameterType());
    }

    @Override
    public Object resolveArgument(MethodParameter methodParameter, ModelAndViewContainer modelAndViewContainer, NativeWebRequest nativeWebRequest, WebDataBinderFactory webDataBinderFactory) throws Exception {
        String token = nativeWebRequest.getHeader("token");
        if (token != null && !token.trim().equals("") && token.split("\\.").length == 3) {
            Method method = methodParameter.getMethod();
            if (method == null) {
                throw new BusinessException(CommonEnum.LOGIN_TIMEOUT);
            } else {
                try {
                    String subject = TokenUtil.parseJWT(token).getSubject();
                    ObjectMapper objectMapper = new ObjectMapper();
                    Object readValue = objectMapper.readValue(subject, methodParameter.getParameterType());
                    if (null == readValue) {
                        throw new BusinessException(CommonEnum.LOGIN_TIMEOUT);
                    } else {
                        SunflowerTokenUtil.set(token);
                        /*if (readValue instanceof HouseHelperUserPrefile) {
                            HouseHelperUserPrefile userPrefile = (HouseHelperUserPrefile)readValue;
                            if (AnnotationUtils.findAnnotation(method, RequiredLogin.class) != null && !StringUtils.hasText(userPrefile.getPhone())) {
                                throw new BusinessException(CommonEnum.OPTNEEDPHONE);
                            }

                            String redisOpenId = this.customRedis.get("token_" + userPrefile.getLoginId());
                            if (!userPrefile.getOpenId().equals(redisOpenId)) {
                                throw new BusinessException(CommonEnum.LOGIN_FORCEOUT);
                            }
                        }*/

                        return readValue;
                    }
                } catch (Exception e) {
                    if (e instanceof BusinessException) {
                        throw e;
                    } else {
                        throw new BusinessException(CommonEnum.LOGIN_TIMEOUT);
                    }
                }
            }
        } else {
            logger.debug("请求头中未包含token");
            throw new BusinessException(CommonEnum.LOGIN_TIMEOUT);
        }
    }
}
