package com.sunflower.druid;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.support.http.StatViewServlet;
import com.alibaba.druid.support.http.WebStatFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @author hankui
 */
@Configuration
@ConfigurationProperties(prefix = "spring.datasource-druid")
@ConditionalOnClass({ DruidDataSource.class })
public class DruidConfiguration {

	private static final Logger logger = LoggerFactory
			.getLogger(DruidConfiguration.class);

	private String url;

	private String username;

	private String password;

	private String driverClassName;

	private int initialSize = 0;

	private int minIdle = 0;

	private int maxActive = 8;

	private int maxWait = -1;

	private long timeBetweenEvictionRunsMillis = 60000L;

	private long minEvictableIdleTimeMillis = 1800000L;

	private boolean testWhileIdle = true;

	private boolean testOnBorrow = false;

	private boolean testOnReturn = false;

	private boolean poolPreparedStatements = false;

	private int maxPoolPreparedStatementPerConnectionSize = 10;

	private String filters;

	private String connectionProperties;

	private String webStatFilterExclusions;

	private String statViewServletMapping;

	private String statViewServletLoginUsername;

	private String statViewServletLoginPassword;

	private String statViewServletResetEnable;

	@Bean(destroyMethod = "close")
	@Primary
	public DataSource myDruidDataSource() {
		DruidDataSource datasource = new DruidDataSource();
		datasource.setUrl(this.url);
		datasource.setUsername(this.username);
		datasource.setPassword(this.password);
		datasource.setDriverClassName(this.driverClassName);
		datasource.setInitialSize(this.initialSize);
		datasource.setMinIdle(this.minIdle);
		datasource.setMaxActive(this.maxActive);
		datasource.setMaxWait((long) this.maxWait);
		datasource.setTimeBetweenEvictionRunsMillis(this.timeBetweenEvictionRunsMillis);
		datasource.setMinEvictableIdleTimeMillis(this.minEvictableIdleTimeMillis);
		datasource.setTestWhileIdle(this.testWhileIdle);
		datasource.setTestOnBorrow(this.testOnBorrow);
		datasource.setTestOnReturn(this.testOnReturn);
		datasource.setPoolPreparedStatements(this.poolPreparedStatements);
		datasource.setMaxPoolPreparedStatementPerConnectionSize(
				this.maxPoolPreparedStatementPerConnectionSize);
		List<String> connectionInitSqls = new ArrayList<>();
		connectionInitSqls.add("set names utf8mb4");
		datasource.setConnectionInitSqls(connectionInitSqls);

		try {
			datasource.setFilters(this.filters);
		}
		catch (SQLException e) {
			logger.error("druid configuration initialization filter: ", e);
		}

		datasource.setConnectionProperties(this.connectionProperties);
		return datasource;
	}

	@Bean
	public ServletRegistrationBean<StatViewServlet> druidServlet() {
		logger.debug("init Druid Servlet Configuration ");
		ServletRegistrationBean<StatViewServlet> servletRegistrationBean = new ServletRegistrationBean<>(
				new StatViewServlet(), this.statViewServletMapping);
		servletRegistrationBean.addInitParameter("loginUsername",
				this.statViewServletLoginUsername);
		servletRegistrationBean.addInitParameter("loginPassword",
				this.statViewServletLoginPassword);
		servletRegistrationBean.addInitParameter("resetEnable",
				this.statViewServletResetEnable);
		return servletRegistrationBean;
	}

	@Bean
	public FilterRegistrationBean<WebStatFilter> filterRegistrationBean() {
		WebStatFilter webStatFilter = new WebStatFilter();
		webStatFilter.setSessionStatEnable(false);
		FilterRegistrationBean<WebStatFilter> filterRegistrationBean = new FilterRegistrationBean<>(
				webStatFilter);
		filterRegistrationBean.addUrlPatterns("/*");
		filterRegistrationBean.addInitParameter("exclusions",
				this.webStatFilterExclusions);
		return filterRegistrationBean;
	}

	public DruidConfiguration() {
	}

	public String getUrl() {
		return this.url;
	}

	public String getUsername() {
		return this.username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getDriverClassName() {
		return this.driverClassName;
	}

	public int getInitialSize() {
		return this.initialSize;
	}

	public int getMinIdle() {
		return this.minIdle;
	}

	public int getMaxActive() {
		return this.maxActive;
	}

	public int getMaxWait() {
		return this.maxWait;
	}

	public long getTimeBetweenEvictionRunsMillis() {
		return this.timeBetweenEvictionRunsMillis;
	}

	public long getMinEvictableIdleTimeMillis() {
		return this.minEvictableIdleTimeMillis;
	}

	public boolean isTestWhileIdle() {
		return this.testWhileIdle;
	}

	public boolean isTestOnBorrow() {
		return this.testOnBorrow;
	}

	public boolean isTestOnReturn() {
		return this.testOnReturn;
	}

	public boolean isPoolPreparedStatements() {
		return this.poolPreparedStatements;
	}

	public int getMaxPoolPreparedStatementPerConnectionSize() {
		return this.maxPoolPreparedStatementPerConnectionSize;
	}

	public String getFilters() {
		return this.filters;
	}

	public String getConnectionProperties() {
		return this.connectionProperties;
	}

	public String getWebStatFilterExclusions() {
		return this.webStatFilterExclusions;
	}

	public String getStatViewServletMapping() {
		return this.statViewServletMapping;
	}

	public String getStatViewServletLoginUsername() {
		return this.statViewServletLoginUsername;
	}

	public String getStatViewServletLoginPassword() {
		return this.statViewServletLoginPassword;
	}

	public String getStatViewServletResetEnable() {
		return this.statViewServletResetEnable;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public void setDriverClassName(String driverClassName) {
		this.driverClassName = driverClassName;
	}

	public void setInitialSize(int initialSize) {
		this.initialSize = initialSize;
	}

	public void setMinIdle(int minIdle) {
		this.minIdle = minIdle;
	}

	public void setMaxActive(int maxActive) {
		this.maxActive = maxActive;
	}

	public void setMaxWait(int maxWait) {
		this.maxWait = maxWait;
	}

	public void setTimeBetweenEvictionRunsMillis(long timeBetweenEvictionRunsMillis) {
		this.timeBetweenEvictionRunsMillis = timeBetweenEvictionRunsMillis;
	}

	public void setMinEvictableIdleTimeMillis(long minEvictableIdleTimeMillis) {
		this.minEvictableIdleTimeMillis = minEvictableIdleTimeMillis;
	}

	public void setTestWhileIdle(boolean testWhileIdle) {
		this.testWhileIdle = testWhileIdle;
	}

	public void setTestOnBorrow(boolean testOnBorrow) {
		this.testOnBorrow = testOnBorrow;
	}

	public void setTestOnReturn(boolean testOnReturn) {
		this.testOnReturn = testOnReturn;
	}

	public void setPoolPreparedStatements(boolean poolPreparedStatements) {
		this.poolPreparedStatements = poolPreparedStatements;
	}

	public void setMaxPoolPreparedStatementPerConnectionSize(
			int maxPoolPreparedStatementPerConnectionSize) {
		this.maxPoolPreparedStatementPerConnectionSize = maxPoolPreparedStatementPerConnectionSize;
	}

	public void setFilters(String filters) {
		this.filters = filters;
	}

	public void setConnectionProperties(String connectionProperties) {
		this.connectionProperties = connectionProperties;
	}

	public void setWebStatFilterExclusions(String webStatFilterExclusions) {
		this.webStatFilterExclusions = webStatFilterExclusions;
	}

	public void setStatViewServletMapping(String statViewServletMapping) {
		this.statViewServletMapping = statViewServletMapping;
	}

	public void setStatViewServletLoginUsername(String statViewServletLoginUsername) {
		this.statViewServletLoginUsername = statViewServletLoginUsername;
	}

	public void setStatViewServletLoginPassword(String statViewServletLoginPassword) {
		this.statViewServletLoginPassword = statViewServletLoginPassword;
	}

	public void setStatViewServletResetEnable(String statViewServletResetEnable) {
		this.statViewServletResetEnable = statViewServletResetEnable;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (!(o instanceof DruidConfiguration)) {
			return false;
		}
		DruidConfiguration that = (DruidConfiguration) o;
		return getInitialSize() == that.getInitialSize()
				&& getMinIdle() == that.getMinIdle()
				&& getMaxActive() == that.getMaxActive()
				&& getMaxWait() == that.getMaxWait()
				&& getTimeBetweenEvictionRunsMillis() == that
						.getTimeBetweenEvictionRunsMillis()
				&& getMinEvictableIdleTimeMillis() == that.getMinEvictableIdleTimeMillis()
				&& isTestWhileIdle() == that.isTestWhileIdle()
				&& isTestOnBorrow() == that.isTestOnBorrow()
				&& isTestOnReturn() == that.isTestOnReturn()
				&& isPoolPreparedStatements() == that.isPoolPreparedStatements()
				&& getMaxPoolPreparedStatementPerConnectionSize() == that
						.getMaxPoolPreparedStatementPerConnectionSize()
				&& Objects.equals(getUrl(), that.getUrl())
				&& Objects.equals(getUsername(), that.getUsername())
				&& Objects.equals(getPassword(), that.getPassword())
				&& Objects.equals(getDriverClassName(), that.getDriverClassName())
				&& Objects.equals(getFilters(), that.getFilters())
				&& Objects.equals(getConnectionProperties(),
						that.getConnectionProperties())
				&& Objects.equals(getWebStatFilterExclusions(),
						that.getWebStatFilterExclusions())
				&& Objects.equals(getStatViewServletMapping(),
						that.getStatViewServletMapping())
				&& Objects.equals(getStatViewServletLoginUsername(),
						that.getStatViewServletLoginUsername())
				&& Objects.equals(getStatViewServletLoginPassword(),
						that.getStatViewServletLoginPassword())
				&& Objects.equals(getStatViewServletResetEnable(),
						that.getStatViewServletResetEnable());
	}

	@Override
	public int hashCode() {
		return Objects.hash(getUrl(), getUsername(), getPassword(), getDriverClassName(),
				getInitialSize(), getMinIdle(), getMaxActive(), getMaxWait(),
				getTimeBetweenEvictionRunsMillis(), getMinEvictableIdleTimeMillis(),
				isTestWhileIdle(), isTestOnBorrow(), isTestOnReturn(),
				isPoolPreparedStatements(),
				getMaxPoolPreparedStatementPerConnectionSize(), getFilters(),
				getConnectionProperties(), getWebStatFilterExclusions(),
				getStatViewServletMapping(), getStatViewServletLoginUsername(),
				getStatViewServletLoginPassword(), getStatViewServletResetEnable());
	}

	protected boolean canEqual(Object other) {
		return other instanceof DruidConfiguration;
	}

	@Override
	public String toString() {
		return "DruidConfiguration(url=" + this.getUrl() + ", username="
				+ this.getUsername() + ", driverClassName=" + this.getDriverClassName()
				+ ", initialSize=" + this.getInitialSize() + ", minIdle="
				+ this.getMinIdle() + ", maxActive=" + this.getMaxActive() + ", maxWait="
				+ this.getMaxWait() + ", timeBetweenEvictionRunsMillis="
				+ this.getTimeBetweenEvictionRunsMillis()
				+ ", minEvictableIdleTimeMillis=" + this.getMinEvictableIdleTimeMillis()
				+ ", testWhileIdle=" + this.isTestWhileIdle() + ", testOnBorrow="
				+ this.isTestOnBorrow() + ", testOnReturn=" + this.isTestOnReturn()
				+ ", poolPreparedStatements=" + this.isPoolPreparedStatements()
				+ ", maxPoolPreparedStatementPerConnectionSize="
				+ this.getMaxPoolPreparedStatementPerConnectionSize() + ", filters="
				+ this.getFilters() + ", connectionProperties="
				+ this.getConnectionProperties() + ", webStatFilterExclusions="
				+ this.getWebStatFilterExclusions() + ", statViewServletMapping="
				+ this.getStatViewServletMapping() + ", statViewServletLoginUsername="
				+ this.getStatViewServletLoginUsername()
				+ ", statViewServletLoginPassword="
				+ this.getStatViewServletLoginPassword() + ", statViewServletResetEnable="
				+ this.getStatViewServletResetEnable() + ")";
	}

}
