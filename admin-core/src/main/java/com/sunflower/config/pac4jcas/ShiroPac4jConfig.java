package com.sunflower.config.pac4jcas;

import com.sunflower.config.pac4jcas.cache.redis.RedisCacheManager;
import com.sunflower.config.pac4jcas.cache.redis.RedisSessionDAO;
import com.sunflower.config.pac4jcas.cache.redis.SessionRedisTemplate;
import com.sunflower.exceptions.BusinessException;
import io.buji.pac4j.filter.CallbackFilter;
import io.buji.pac4j.filter.LogoutFilter;
import io.buji.pac4j.filter.SecurityFilter;
import io.buji.pac4j.subject.Pac4jSubjectFactory;
import org.apache.shiro.cache.CacheManager;
import org.apache.shiro.codec.Base64;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.mgt.SubjectFactory;
import org.apache.shiro.realm.Realm;
import org.apache.shiro.session.mgt.SessionManager;
import org.apache.shiro.session.mgt.eis.RandomSessionIdGenerator;
import org.apache.shiro.session.mgt.eis.SessionDAO;
import org.apache.shiro.spring.LifecycleBeanPostProcessor;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.spring.web.config.AbstractShiroWebFilterConfiguration;
import org.apache.shiro.spring.web.config.DefaultShiroFilterChainDefinition;
import org.apache.shiro.spring.web.config.ShiroFilterChainDefinition;
import org.apache.shiro.web.mgt.CookieRememberMeManager;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.mgt.WebSecurityManager;
import org.apache.shiro.web.servlet.SimpleCookie;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.jasig.cas.client.session.SingleSignOutFilter;
import org.jasig.cas.client.session.SingleSignOutHttpSessionListener;
import org.pac4j.cas.client.CasClient;
import org.pac4j.cas.client.rest.CasRestFormClient;
import org.pac4j.cas.config.CasConfiguration;
import org.pac4j.core.client.Clients;
import org.pac4j.core.config.Config;
import org.pac4j.core.logout.handler.DefaultLogoutHandler;
import org.pac4j.core.profile.CommonProfile;
import org.pac4j.core.store.Store;
import org.pac4j.http.client.direct.ParameterClient;
import org.pac4j.jwt.config.encryption.SecretEncryptionConfiguration;
import org.pac4j.jwt.config.signature.SecretSignatureConfiguration;
import org.pac4j.jwt.credentials.authenticator.JwtAuthenticator;
import org.pac4j.jwt.profile.JwtGenerator;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletListenerRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.core.annotation.Order;
import org.springframework.core.env.Environment;
import org.springframework.core.env.PropertyResolver;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.DelegatingFilterProxy;

import javax.servlet.Filter;
import java.util.Map;

/**
 * @author sunflower
 */
@Configuration
public class ShiroPac4jConfig extends AbstractShiroWebFilterConfiguration {

	private String salt = "sunflower";

	public ShiroPac4jConfig() {
	}

	@Bean
	protected JwtGenerator<CommonProfile> jwtGenerator() {
		return new JwtGenerator<>(new SecretSignatureConfiguration(this.salt),
				new SecretEncryptionConfiguration(this.salt));
	}

	@Bean
	protected JwtAuthenticator jwtAuthenticator() {
		JwtAuthenticator jwtAuthenticator = new JwtAuthenticator();
		jwtAuthenticator
				.addSignatureConfiguration(new SecretSignatureConfiguration(this.salt));
		jwtAuthenticator
				.addEncryptionConfiguration(new SecretEncryptionConfiguration(this.salt));
		return jwtAuthenticator;
	}

	@Bean
	public CasConfiguration casConfiguration(Environment env,
			RedisTemplate<String, Object> redisTemplateObject,
			CasProperties casProperties) {

		CasConfiguration configuration = new CasConfiguration(
				casProperties.getCasServerUrlPrefix() + "/sunflower-cas/login");
		configuration
				.setPrefixUrl(casProperties.getCasServerUrlPrefix() + "/sunflower-cas/");
		Store<String, Object> redisStore = new RedisStore<>(redisTemplateObject,
				env.getActiveProfiles()[0] + "_pac4j_store_");
		configuration.setLogoutHandler(new DefaultLogoutHandler(redisStore));
		return configuration;
	}

	@Bean
	protected CasRestFormClient casRestFormClient(CasConfiguration casConfiguration) {
		CasRestFormClient casRestFormClient = new CasRestFormClient();
		casRestFormClient.setConfiguration(casConfiguration);
		casRestFormClient.setName("rest");
		return casRestFormClient;
	}

	@Bean
	public Realm pac4jRealm(CasProperties casProperties) {
		SunflowerPac4jRealms pac4jRealms = new SunflowerPac4jRealms();
		pac4jRealms.setUserInfoAndAuthorizationInfoUrl(
				casProperties.getUserInfoAndAuthorizationInfoUrl());
		pac4jRealms.setCachingEnabled(true);
		pac4jRealms.setAuthorizationCachingEnabled(true);
		pac4jRealms.setAuthenticationCacheName("authorizationcache");
		return pac4jRealms;
	}

	@Bean
	public CasClient casClient(PropertyResolver resolver,
			CasConfiguration casConfiguration, CasProperties casProperties) {
		CasClient casClient = new CasClient();
		casClient.setConfiguration(casConfiguration);
		String applicationName = resolver.getProperty("spring.application.name");
		if (StringUtils.isEmpty(applicationName)) {
			throw new BusinessException("请配置项目名称");
		}
		casClient.setCallbackUrl(casProperties.getCasServerUrlPrefix() + "/"
				+ applicationName.split("-")[0] + "/callback");
		casClient.setName(resolver.getProperty("spring.application.name"));
		return casClient;
	}

	@Bean
	protected Clients clients(CasClient casClient, CasRestFormClient casRestFormClient) {
		Clients clients = new Clients();
		ParameterClient parameterClient = new ParameterClient("token",
				this.jwtAuthenticator());
		parameterClient.setSupportGetRequest(true);
		parameterClient.setName("jwt");
		clients.setClients(casClient, casRestFormClient, parameterClient);
		return clients;
	}

	@Bean
	public Config config(Clients clients) {
		Config config = new Config();
		config.setClients(clients);
		return config;
	}

	@Bean(name = "subjectFactory")
	public Pac4jSubjectFactory subjectFactory() {
		return new Pac4jSubjectFactory();
	}

	@Bean
	public ServletListenerRegistrationBean<SingleSignOutHttpSessionListener> singleSignOutHttpSessionListener() {
		return new ServletListenerRegistrationBean<>(
				new SingleSignOutHttpSessionListener());
	}

	@Bean
	@Order(-2147483648)
	public FilterRegistrationBean<SingleSignOutFilter> singleSignOutFilter(
			CasProperties casProperties) {
		FilterRegistrationBean<SingleSignOutFilter> bean = new FilterRegistrationBean<>();
		SingleSignOutFilter singleSignOutFilter = new SingleSignOutFilter();
		singleSignOutFilter.setCasServerUrlPrefix(
				casProperties.getCasServerUrlPrefix() + "/sunflower-cas/");
		singleSignOutFilter.setIgnoreInitConfiguration(true);
		bean.setFilter(singleSignOutFilter);
		bean.addUrlPatterns("/*");
		return bean;
	}

	@Bean
	public DefaultWebSecurityManager securityManager(Realm pac4jRealm,
			SessionManager sessionManager, SubjectFactory subjectFactory,
			CacheManager cacheManager) {
		DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
		// 设置realm
		securityManager.setRealm(pac4jRealm);

		securityManager.setSubjectFactory(subjectFactory());

		// 自定义缓存实现 使用redis
		securityManager.setCacheManager(cacheManager);

		// 自定义session管理 使用redis
		securityManager.setSessionManager(sessionManager);
		// 注入记住我管理器;
		securityManager.setRememberMeManager(rememberMeManager());
		return securityManager;
	}

	@Bean
	public CacheManager cacheManager(RedisTemplate<String, Object> redisTemplateObject,
			Environment env) {
		RedisCacheManager<String, Object> cacheManager = new RedisCacheManager<>();
		cacheManager.setRedisTemplate(redisTemplateObject);
		cacheManager.setShiroCacheKeyPrefix(env.getActiveProfiles()[0] + "_shiro_cache_");
		return cacheManager;
	}

	@Bean
	public SessionManager sessionManager(SessionDAO sessionDAO,
			CasProperties casProperties) {
		DefaultWebSessionManager sessionManager = new DefaultWebSessionManager();
		sessionManager.setSessionDAO(sessionDAO);
		sessionManager.setGlobalSessionTimeout(casProperties.getGlobalSessionTimeout());
		sessionManager.setSessionValidationInterval(
				casProperties.getSessionValidationInterval());
		sessionManager.setSessionValidationSchedulerEnabled(true);
		sessionManager.setSessionIdCookie(new SimpleCookie("sunflower-sessionId"));
		sessionManager.setSessionIdCookieEnabled(true);
		return sessionManager;
	}

	@Bean
	public SessionDAO sessionDAO(SessionRedisTemplate sessionRedisTemplate,
			Environment env) {
		RedisSessionDAO sessionDAO = new RedisSessionDAO();
		sessionDAO.setRedisTemplate(sessionRedisTemplate);
		sessionDAO.setSessionKeyPrefix(
				env.getActiveProfiles()[0] + "_sunflower_shiro_session_");
		sessionDAO.setSessionIdGenerator(new RandomSessionIdGenerator());
		return sessionDAO;
	}

	@Bean
	public FilterRegistrationBean<DelegatingFilterProxy> shiroFilter() {
		FilterRegistrationBean<DelegatingFilterProxy> filterRegistrationBean = new FilterRegistrationBean<>();
		DelegatingFilterProxy delegateFilter = new DelegatingFilterProxy(
				"shiroFilterFactoryBean");
		delegateFilter.setTargetFilterLifecycle(true);
		filterRegistrationBean.setFilter(delegateFilter);
		filterRegistrationBean.addUrlPatterns("/*");
		return filterRegistrationBean;
	}

	/**
	 * ShiroFilterFactoryBean 处理拦截资源文件问题。 注意：单独一个ShiroFilterFactoryBean配置是或报错的，因为在
	 * 初始化ShiroFilterFactoryBean的时候需要注入：SecurityManager
	 *
	 * Filter Chain定义说明 1、一个URL可以配置多个Filter，使用逗号分隔 2、当设置多个过滤器时，全部验证通过，才视为通过
	 * 3、部分过滤器可指定参数，如perms，roles
	 *
	 */
	@Bean({ "shiroFilterFactoryBean" })
	protected ShiroFilterFactoryBean shiroFilterFactoryBean(
			PropertyResolver propertyResolver, CasProperties casProperties,
			WebSecurityManager securityManager,
			ShiroFilterChainDefinition shiroFilterChainDefinition, Config config) {
		ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
		shiroFilterFactoryBean.setSecurityManager(securityManager);
		shiroFilterFactoryBean
				.setSuccessUrl(casProperties.getCasServerUrlPrefix() + "/index.html");
		shiroFilterFactoryBean.setFilterChainDefinitionMap(
				shiroFilterChainDefinition.getFilterChainMap());
		Map<String, Filter> filters = shiroFilterFactoryBean.getFilters();
		SecurityFilter securityFilter = new SecurityFilter();
		securityFilter.setConfig(config);
		securityFilter.setClients(
				propertyResolver.getProperty("spring.application.name") + ",rest,jwt");
		filters.put(SecurityFilter.class.getSimpleName(), securityFilter);
		LogoutFilter logoutFilter = new LogoutFilter();
		logoutFilter.setConfig(config);
		logoutFilter.setCentralLogout(true);
		logoutFilter.setLocalLogout(true);
		logoutFilter.setDefaultUrl(
				casProperties.getCasServerUrlPrefix() + "/sunflower-cas/logout?service="
						+ casProperties.getCasServerUrlPrefix());
		filters.put("logout", logoutFilter);
		CallbackFilter callbackFilter = new CallbackFilter();
		callbackFilter.setConfig(config);
		callbackFilter.setMultiProfile(false);
		filters.put("callbackFilter", callbackFilter);
		return shiroFilterFactoryBean;
	}

	@Bean
	public ShiroFilterChainDefinition shiroFilterChainDefinition() {
		DefaultShiroFilterChainDefinition definition = new DefaultShiroFilterChainDefinition();
		definition.addPathDefinition("/logout", "logout");
		definition.addPathDefinition("/callback", "callbackFilter");
		definition.addPathDefinition("/druid/**", SecurityFilter.class.getSimpleName());
		definition.addPathDefinition("/a/**", SecurityFilter.class.getSimpleName());
		definition.addPathDefinition("/swagger**", "anon");
		definition.addPathDefinition("/**", "anon");
		return definition;
	}

	@Bean
	@ConditionalOnMissingBean
	@DependsOn({ "lifecycleBeanPostProcessor" })
	public DefaultAdvisorAutoProxyCreator defaultAdvisorAutoProxyCreator() {
		DefaultAdvisorAutoProxyCreator daap = new DefaultAdvisorAutoProxyCreator();
		// 强制使用cglib，防止重复代理和可能引起代理出错的问题
		daap.setProxyTargetClass(true);
		return daap;
	}

	@Bean
	public static LifecycleBeanPostProcessor lifecycleBeanPostProcessor() {
		return new LifecycleBeanPostProcessor();
	}

	@Bean
	public AuthorizationAttributeSourceAdvisor getAuthorizationAttributeSourceAdvisor(
			SecurityManager securityManager) {
		AuthorizationAttributeSourceAdvisor advisor = new AuthorizationAttributeSourceAdvisor();
		advisor.setSecurityManager(securityManager);
		return advisor;
	}

	/**
	 * cookie管理对象;记住我功能
	 * @return
	 */
	public CookieRememberMeManager rememberMeManager() {
		CookieRememberMeManager cookieRememberMeManager = new CookieRememberMeManager();
		cookieRememberMeManager.setCookie(rememberMeCookie());
		// rememberMe cookie加密的密钥 建议每个项目都不一样 默认AES算法 密钥长度(128 256 512 位)
		cookieRememberMeManager.setCipherKey(Base64.decode("3AvVhmFLUs0KTA3Kprsdag=="));
		return cookieRememberMeManager;
	}

	/**
	 * cookie对象;
	 * @return
	 */
	public SimpleCookie rememberMeCookie() {
		// 这个参数是cookie的名称，对应前端的checkbox的name = rememberMe
		SimpleCookie simpleCookie = new SimpleCookie("rememberMe");
		// <!-- 记住我cookie生效时间30天 ,单位秒;-->
		simpleCookie.setMaxAge(2592000);
		return simpleCookie;
	}

}
