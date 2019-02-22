package com.sunflower.aop;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sunflower.config.pac4jcas.SunflowerCasProfile;
import com.sunflower.config.pac4jcas.SunflowerUserUtil;
import com.sunflower.constants.IEnum;
import com.sunflower.constants.error.CommonEnum;
import com.sunflower.util.Servlets;
import com.sunflower.util.SunflowerCookieUtil;
import io.buji.pac4j.subject.Pac4jPrincipal;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.apache.shiro.authz.annotation.RequiresGuest;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.apache.shiro.authz.annotation.RequiresUser;
import org.apache.shiro.subject.Subject;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.pac4j.core.profile.CommonProfile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.core.env.Environment;
import org.springframework.core.io.InputStreamSource;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.sql.DataSource;
import java.io.Serializable;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.Executor;

@Component
@Aspect
public class ControllerAspect {

	private static final Logger logger = LoggerFactory.getLogger(ControllerAspect.class);

	@Autowired
	private Environment env;

	@Autowired
	private ObjectMapper objectMapper;

	@Autowired
	private DataSource dataSource;

	@Autowired
	private Executor executor;

	public ControllerAspect() {
	}

	@Around("execution(public * com.sunflower..controller..*.*(..))")
	public Object doAround(ProceedingJoinPoint pjp) throws Throwable {
		long beginTime = System.currentTimeMillis();
		MethodSignature signature = (MethodSignature) pjp.getSignature();
		Method method = signature.getMethod();
		Class<?> returnType = signature.getReturnType();
		Class<?> controllerClass = method.getDeclaringClass();
		SunflowerCasProfile profile = getPrincipal();
		SunflowerUserUtil.set(profile);
		boolean prodEnv = Arrays.asList(this.env.getActiveProfiles()).contains("prod");
		if (!prodEnv) {
			StringBuilder sb = new StringBuilder();
			if (!AnnotatedElementUtils.hasAnnotation(controllerClass,
					RestController.class)) {
				sb.append(String.format("%n类%s上缺少%s注解", controllerClass.getName(),
						RestController.class.getName()));
			}

			if (method.getAnnotations().length == 0) {
				sb.append(String.format("%n类%s的方法%s上缺少注解", controllerClass.getName(),
						method.getName()));
			}

			if (AnnotationUtils.findAnnotation(method, ResponseBody.class) != null) {
				sb.append(String.format("%n类%s的方法%s上不建议加入%s注解", controllerClass.getName(),
						method.getName(), ResponseBody.class.getName()));
			}

			if (AnnotationUtils.findAnnotation(method, DeleteMapping.class) == null
					&& AnnotationUtils.findAnnotation(method, GetMapping.class) == null
					&& AnnotationUtils.findAnnotation(method, PatchMapping.class) == null
					&& AnnotationUtils.findAnnotation(method, PostMapping.class) == null
					&& AnnotationUtils.findAnnotation(method, PutMapping.class) == null) {
				sb.append(String.format("%n类%s的方法%s上建议加入的注解包括%s",
						controllerClass.getName(), method.getName(),
						String.join(",", DeleteMapping.class.getName(),
								GetMapping.class.getName(), PatchMapping.class.getName(),
								PostMapping.class.getName(),
								PutMapping.class.getName())));
			}

			if (AnnotationUtils.findAnnotation(method, ApiOperation.class) == null) {
				sb.append(String.format("%n类%s的方法%s上建议加入%s注解", controllerClass.getName(),
						method.getName(), ApiOperation.class.getName()));
			}

			if (!method.isAnnotationPresent(RequiresGuest.class)
					&& !method.isAnnotationPresent(RequiresPermissions.class)
					&& !method.isAnnotationPresent(RequiresRoles.class)
					&& !method.isAnnotationPresent(RequiresUser.class)
					&& !method.isAnnotationPresent(RequiresAuthentication.class)) {
				sb.append(String.format("%n缺少权限注解,类名:%s方法名:%s", controllerClass.getName(),
						method.getName()));
			}

			if (sb.length() > 0 && logger.isDebugEnabled()) {
				logger.debug("警告：{}", sb);
			}
		}

		HttpServletRequest request = Servlets.getRequest();
		SunflowerCookieUtil.set(request == null ? null : request.getCookies());
		String ip = Servlets.getIpAddr(request);
		Object result;
		if (prodEnv
				&& AnnotationUtils.findAnnotation(method, RequestControl.class) != null
				&& !"180.169.219.98".equals(ip)) {
			result = returnType.getConstructor(IEnum.class)
					.newInstance(CommonEnum.HTTP_FAIL_666);
		}
		else if (prodEnv
				&& AnnotationUtils.findAnnotation(method, InnerIpRequest.class) != null
				&& !Servlets.isInnerIpRequest(ip)) {
			result = returnType.getConstructor(IEnum.class)
					.newInstance(CommonEnum.HTTP_FAIL_666);
		}
		else {
			result = pjp.proceed();
		}

		BackCommonLogEntity logEntity = new BackCommonLogEntity();
		logEntity.setUrl(request == null ? null : request.getRequestURL().toString());
		logEntity.setIp(ip);
		logEntity.setMethod(request == null ? null : request.getMethod());
		logEntity.setClassAndMethod(
				method.getDeclaringClass().getName() + "." + method.getName());
		Object[] args = pjp.getArgs();
		List<Object> temp = new ArrayList<>();
		for (Object arg : args) {
			if (null != arg && !(arg instanceof InputStreamSource)
					&& arg instanceof Serializable) {
				temp.add(arg);
			}
		}

		logEntity.setParams(temp);
		if (AnnotatedElementUtils.hasAnnotation(controllerClass, RestController.class)) {
			logEntity.setResult((Serializable) result);
		}

		logEntity.setCreateDate(new Date());
		logEntity.setDay(logEntity.getCreateDate());
		logEntity.setCreateBy(profile == null ? null : profile.getUserId());
		long duration = System.currentTimeMillis() - beginTime;
		logEntity.setDuration(duration);

		logger.debug("耗时：{}", duration);

		this.executor.execute(() -> {
			long logStartTime = System.currentTimeMillis();
			Connection connection = null;
			PreparedStatement preparedStatement = null;

			try {
				connection = this.dataSource.getConnection();
				String sql = "insert into back_common_log_entity(id,url,method,ip,class_and_method,params,result,create_date,create_by,duration,day)values(?,?,?,?,?,?,?,?,?,?,?)";
				preparedStatement = connection.prepareStatement(sql);
				preparedStatement.setString(1,
						UUID.randomUUID().toString().replace("-", ""));
				preparedStatement.setString(2, logEntity.getUrl());
				preparedStatement.setString(3, logEntity.getMethod());
				preparedStatement.setString(4, logEntity.getIp());
				preparedStatement.setString(5, logEntity.getClassAndMethod());
				preparedStatement.setString(6,
						this.objectMapper.writeValueAsString(logEntity.getParams()));
				preparedStatement.setString(7,
						this.objectMapper.writeValueAsString(logEntity.getResult()));
				preparedStatement.setTimestamp(8,
						new Timestamp(logEntity.getCreateDate().getTime()));
				preparedStatement.setString(9, logEntity.getCreateBy());
				preparedStatement.setLong(10, logEntity.getDuration());
				preparedStatement.setDate(11,
						new java.sql.Date(logEntity.getDay().getTime()));
				preparedStatement.executeUpdate();
			}
			catch (Exception e) {
				logger.error(e.getMessage(), e);
			}
			finally {
				try {
					if (preparedStatement != null) {
						preparedStatement.close();
					}

					if (connection != null) {
						connection.close();
					}
				}
				catch (Exception e) {
					logger.error(e.getMessage(), e);
				}

				logger.debug("记录日志耗时 : {}", System.currentTimeMillis() - logStartTime);
			}

		});
		SunflowerUserUtil.remove();
		SunflowerCookieUtil.remove();
		return result;
	}

	/**
	 * 获取登陆信息
	 * @return SunflowerCasProfile
	 */
	private static SunflowerCasProfile getPrincipal() {
		SunflowerCasProfile sunflowerCasProfile = null;

		try {
			Subject subject = SecurityUtils.getSubject();
			Pac4jPrincipal pac4jPrincipal = (Pac4jPrincipal) subject.getPrincipal();
			if (pac4jPrincipal != null) {
				for (CommonProfile commonProfile : pac4jPrincipal.getProfiles()) {
					sunflowerCasProfile = new SunflowerCasProfile();
					sunflowerCasProfile.setId(commonProfile.getId());
					sunflowerCasProfile.setClientName(commonProfile.getClientName());
					sunflowerCasProfile.setLinkedId(commonProfile.getLinkedId());
					sunflowerCasProfile.setRemembered(commonProfile.isRemembered());
					sunflowerCasProfile.addPermissions(commonProfile.getPermissions());
					sunflowerCasProfile.addRoles(commonProfile.getRoles());
					Map<String, Object> attributes = commonProfile.getAttributes();
					sunflowerCasProfile.addAttributes(attributes);
					sunflowerCasProfile.setUserId((String) attributes.get("userId"));
					sunflowerCasProfile.setUserName(commonProfile.getDisplayName());
				}
			}
		}
		catch (Exception e) {
			logger.error(e.getMessage(), e);
		}

		return sunflowerCasProfile;
	}

}
