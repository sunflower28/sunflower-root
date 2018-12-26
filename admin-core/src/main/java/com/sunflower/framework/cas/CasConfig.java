package com.sunflower.framework.cas;

import io.buji.pac4j.filter.LogoutFilter;
import org.jasig.cas.client.authentication.AuthenticationFilter;
import org.jasig.cas.client.session.SingleSignOutFilter;
import org.jasig.cas.client.session.SingleSignOutHttpSessionListener;
import org.jasig.cas.client.util.AssertionHolder;
import org.jasig.cas.client.util.AssertionThreadLocalFilter;
import org.jasig.cas.client.util.HttpServletRequestWrapperFilter;
import org.jasig.cas.client.validation.Cas30ProxyReceivingTicketValidationFilter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletListenerRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author hankui
 */
@Configuration
public class CasConfig {

	@Value("${cas.server.url.login}")
	public String casServerLoginUrl;

	@Value("${cas.server.url.prefix}")
	public String casServerUrlPrefix;

	@Value("${cas.client.name}")
	public String casClientName;

	/**
	 * 用于实现单点登出功能
	 */
	@Bean
	public ServletListenerRegistrationBean<SingleSignOutHttpSessionListener> singleSignOutHttpSessionListener() {
		ServletListenerRegistrationBean<SingleSignOutHttpSessionListener> listener = new ServletListenerRegistrationBean<>();
		listener.setEnabled(true);
		listener.setListener(new SingleSignOutHttpSessionListener());
		listener.setOrder(1);
		return listener;
	}

	/**
	 * 该过滤器用于实现单点登出功能，单点退出配置，一定要放在其他filter之前
	 */
	@Bean
	public FilterRegistrationBean<LogoutFilter> logOutFilter() {
		FilterRegistrationBean<LogoutFilter> filterRegistration = new FilterRegistrationBean<>();

		LogoutFilter logoutFilter = new LogoutFilter();
		filterRegistration.setFilter(logoutFilter);
		filterRegistration.setEnabled(true);
		filterRegistration.addUrlPatterns("/logout");
		filterRegistration.addInitParameter("casServerUrlPrefix", casServerUrlPrefix);
		filterRegistration.addInitParameter("serverName", casClientName);
		filterRegistration.addInitParameter("redirectAfterValidation", "true");
		filterRegistration.setOrder(2);
		return filterRegistration;
	}

	/**
	 * CAS Single Sign Out Filter 该过滤器用于实现单点登出功能，单点退出配置，一定要放在其他filter之前
	 */
	@Bean
	public FilterRegistrationBean<SingleSignOutFilter> singleSignOutFilter() {
		FilterRegistrationBean<SingleSignOutFilter> filterRegistration = new FilterRegistrationBean<SingleSignOutFilter>();
		filterRegistration.setFilter(new SingleSignOutFilter());
		filterRegistration.addUrlPatterns("/*");
		filterRegistration.addInitParameter("casServerUrlPrefix", casServerUrlPrefix);
		filterRegistration.addInitParameter("serverName", casClientName);
		filterRegistration.setOrder(3);
		return filterRegistration;
	}

	/**
	 * 该过滤器负责用户的认证工作
	 */
	@Bean
	public FilterRegistrationBean<AuthenticationFilter> authenticationFilter() {
		FilterRegistrationBean<AuthenticationFilter> filterRegistration = new FilterRegistrationBean<>();
		filterRegistration.setFilter(new AuthenticationFilter());
		filterRegistration.addUrlPatterns("/*");
		filterRegistration.addInitParameter("ignorePattern",
				"/error|/public*|/assets*|/ftl*");
		filterRegistration.addInitParameter("casServerLoginUrl", casServerLoginUrl);
		filterRegistration.addInitParameter("encoding", "UTF-8");
		filterRegistration.addInitParameter("serverName", casClientName);
		filterRegistration.addInitParameter("useSession", "true");
		filterRegistration.setOrder(4);
		return filterRegistration;
	}

	/**
	 * 该过滤器负责对Ticket的校验工作
	 */
	@Bean
	public FilterRegistrationBean<Cas30ProxyReceivingTicketValidationFilter> cas20ProxyReceivingTicketValidationFilter() {
		FilterRegistrationBean<Cas30ProxyReceivingTicketValidationFilter> filterRegistration = new FilterRegistrationBean<>();
		Cas30ProxyReceivingTicketValidationFilter filter = new Cas30ProxyReceivingTicketValidationFilter();
		filterRegistration.setFilter(filter);
		filterRegistration.addUrlPatterns("/*");
		filterRegistration.addInitParameter("encoding", "UTF-8");
		filterRegistration.addInitParameter("casServerUrlPrefix", casServerUrlPrefix);
		filterRegistration.addInitParameter("serverName", casClientName);
		filterRegistration.setOrder(5);
		return filterRegistration;
	}

	/**
	 * 该过滤器对HttpServletRequest请求包装， 可通过HttpServletRequest的getRemoteUser()方法获得登录用户的登录名
	 */
	@Bean
	public FilterRegistrationBean<HttpServletRequestWrapperFilter> httpServletRequestWrapperFilter() {
		FilterRegistrationBean<HttpServletRequestWrapperFilter> filterRegistration = new FilterRegistrationBean<>();
		filterRegistration.setFilter(new HttpServletRequestWrapperFilter());
		filterRegistration.setEnabled(true);
		filterRegistration.addUrlPatterns("/*");
		filterRegistration.setOrder(6);
		return filterRegistration;
	}

	/**
	 * 该过滤器使得可以通过org.jasig.cas.client.util.AssertionHolder来获取用户的登录名。
	 * 比如AssertionHolder.getAssertion().getPrincipal().getName()。
	 * 这个类把Assertion信息放在ThreadLocal变量中，这样应用程序不在web层也能够获取到当前登录信息
	 */
	@Bean
	public FilterRegistrationBean<AssertionThreadLocalFilter> assertionThreadLocalFilter() {
		FilterRegistrationBean<AssertionThreadLocalFilter> filterRegistration = new FilterRegistrationBean<>();
		filterRegistration.setFilter(new AssertionThreadLocalFilter());
		filterRegistration.setEnabled(true);
		filterRegistration.addUrlPatterns("/*");
		filterRegistration.setOrder(7);
		return filterRegistration;
	}

	public static class RemoteUserUtil {

		public static Boolean hasLogin() {
			return AssertionHolder.getAssertion() != null;
		}

		/**
		 * 获取单点登录用户id
		 * @return
		 */
		public static Long getRemoteUserId() {
			Object userId = AssertionHolder.getAssertion().getPrincipal().getAttributes()
					.get("id");
			return userId == null ? null : Long.parseLong(userId.toString());
		}

		/**
		 * 获取单点登录用户账户
		 * @return
		 */
		public static String getRemoteUserAccount() {
			return AssertionHolder.getAssertion().getPrincipal().getName();
		}

		/**
		 * 获取单点登录用户名称
		 * @return
		 */
		public static String getRemoteUserName() {
			Object userName = AssertionHolder.getAssertion().getPrincipal()
					.getAttributes().get("name");
			return userName == null ? null : userName.toString();
		}

	}

}
