package com.sunflower.framework.aop;

import com.sunflower.framework.api.AbstractResultDto;
import com.sunflower.framework.api.InputDto;
import com.sunflower.framework.constants.error.CommonEnum;
import com.sunflower.framework.exceptions.BusinessException;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import java.lang.reflect.Array;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.*;

@Component
@Aspect
public class ApiServiceValidAop {

	public static final Logger LOGGER = LoggerFactory.getLogger(ApiServiceValidAop.class);

	@Autowired
	private Validator validator;

	@Autowired
	private Environment env;

	public ApiServiceValidAop() {
	}

	@Around("@annotation(com.sunflower.framework.aop.ValidApi)")
	public Object paramCheck(ProceedingJoinPoint pjp) throws Throwable {
		MethodSignature signature = (MethodSignature) pjp.getSignature();
		Method targetMethod = signature.getMethod();
		Class<?> returnType = signature.getReturnType();

		// 验证返回类型
		if (!Arrays.asList(this.env.getActiveProfiles()).contains("prod")
				&& !AbstractResultDto.class.isAssignableFrom(returnType)) {
			LOGGER.debug(String.format("请修正%s.%s的返回值类型为%s的子类", pjp.getTarget().getClass(),
					targetMethod.getName(), AbstractResultDto.class));
		}

		Map<Integer, Object> errorMessages = new HashMap<>();
		// 验证注解
		Parameter[] parameters = targetMethod.getParameters();
		List<Integer> hasValidParamIndexList = new ArrayList<>();

		for (int i = 0; i < parameters.length; i++) {
			Parameter parameter = parameters[i];
			if (parameter.isAnnotationPresent(ValidParam.class)) {
				hasValidParamIndexList.add(i);
			}
		}

		// 验证入参
		Object[] args = pjp.getArgs();
		for (Integer argIndex : hasValidParamIndexList) {
			Object arg = args[argIndex];

			/////////////////////// 验证基本数据类型开始//////////////////
			if (isEmpty(arg)) {
				errorMessages.put(argIndex, "can not be empty");
			}
			else if (InputDto.class.isAssignableFrom(arg.getClass())) {
				Set<ConstraintViolation<Object>> constraintViolations = this.validator
						.validate(arg, new Class[0]);
				if (constraintViolations.isEmpty()) {
					Map<String, String> errorMessage = new HashMap<>(
							constraintViolations.size());
					constraintViolations.forEach(validation -> errorMessage.put(
							validation.getPropertyPath().toString(),
							validation.getMessage()));
					errorMessages.put(argIndex, errorMessage);
				}
			}
		}

		if (!errorMessages.isEmpty()) {
			BusinessException businessException = new BusinessException(
					CommonEnum.PARAM_VALID_FAIL);
			businessException.setData(errorMessages);
			throw businessException;
		}
		else {
			return pjp.proceed();
		}

	}

	private static boolean isEmpty(Object obj) throws IllegalArgumentException {
		if (obj == null) {
			return true;
		}
		else if (obj instanceof String) {
			return ((String) obj).trim().length() == 0;
		}
		else if (obj instanceof Collection) {
			return ((Collection) obj).isEmpty();
		}
		else if (obj.getClass().isArray()) {
			return Array.getLength(obj) == 0;
		}
		else {
			return obj instanceof Map ? ((Map) obj).isEmpty() : false;
		}
	}

}
