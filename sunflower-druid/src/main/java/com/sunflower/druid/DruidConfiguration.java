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
		// datasource.setPasswordCallback(new DruidPassworkCallbackBrc());
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
		ServletRegistrationBean<StatViewServlet> servletRegistrationBean = new ServletRegistrationBean(
				new StatViewServlet(), new String[] { this.statViewServletMapping });
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
		FilterRegistrationBean<WebStatFilter> filterRegistrationBean = new FilterRegistrationBean(
				webStatFilter, new ServletRegistrationBean[0]);
		filterRegistrationBean.addUrlPatterns(new String[] { "/*" });
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
		if (o == this) {
			return true;
		}
		else if (!(o instanceof DruidConfiguration)) {
			return false;
		}
		else {
			DruidConfiguration other = (DruidConfiguration) o;
			if (!other.canEqual(this)) {
				return false;
			}
			else {
				label175: {
					Object this$url = this.getUrl();
					Object other$url = other.getUrl();
					if (this$url == null) {
						if (other$url == null) {
							break label175;
						}
					}
					else if (this$url.equals(other$url)) {
						break label175;
					}

					return false;
				}

				Object this$username = this.getUsername();
				Object other$username = other.getUsername();
				if (this$username == null) {
					if (other$username != null) {
						return false;
					}
				}
				else if (!this$username.equals(other$username)) {
					return false;
				}

				Object this$driverClassName = this.getDriverClassName();
				Object other$driverClassName = other.getDriverClassName();
				if (this$driverClassName == null) {
					if (other$driverClassName != null) {
						return false;
					}
				}
				else if (!this$driverClassName.equals(other$driverClassName)) {
					return false;
				}

				if (this.getInitialSize() != other.getInitialSize()) {
					return false;
				}
				else if (this.getMinIdle() != other.getMinIdle()) {
					return false;
				}
				else if (this.getMaxActive() != other.getMaxActive()) {
					return false;
				}
				else if (this.getMaxWait() != other.getMaxWait()) {
					return false;
				}
				else if (this.getTimeBetweenEvictionRunsMillis() != other
						.getTimeBetweenEvictionRunsMillis()) {
					return false;
				}
				else if (this.getMinEvictableIdleTimeMillis() != other
						.getMinEvictableIdleTimeMillis()) {
					return false;
				}
				else if (this.isTestWhileIdle() != other.isTestWhileIdle()) {
					return false;
				}
				else if (this.isTestOnBorrow() != other.isTestOnBorrow()) {
					return false;
				}
				else if (this.isTestOnReturn() != other.isTestOnReturn()) {
					return false;
				}
				else if (this.isPoolPreparedStatements() != other
						.isPoolPreparedStatements()) {
					return false;
				}
				else if (this.getMaxPoolPreparedStatementPerConnectionSize() != other
						.getMaxPoolPreparedStatementPerConnectionSize()) {
					return false;
				}
				else {
					Object this$filters = this.getFilters();
					Object other$filters = other.getFilters();
					if (this$filters == null) {
						if (other$filters != null) {
							return false;
						}
					}
					else if (!this$filters.equals(other$filters)) {
						return false;
					}

					Object this$connectionProperties = this.getConnectionProperties();
					Object other$connectionProperties = other.getConnectionProperties();
					if (this$connectionProperties == null) {
						if (other$connectionProperties != null) {
							return false;
						}
					}
					else if (!this$connectionProperties
							.equals(other$connectionProperties)) {
						return false;
					}

					label124: {
						Object this$webStatFilterExclusions = this
								.getWebStatFilterExclusions();
						Object other$webStatFilterExclusions = other
								.getWebStatFilterExclusions();
						if (this$webStatFilterExclusions == null) {
							if (other$webStatFilterExclusions == null) {
								break label124;
							}
						}
						else if (this$webStatFilterExclusions
								.equals(other$webStatFilterExclusions)) {
							break label124;
						}

						return false;
					}

					Object this$statViewServletMapping = this.getStatViewServletMapping();
					Object other$statViewServletMapping = other
							.getStatViewServletMapping();
					if (this$statViewServletMapping == null) {
						if (other$statViewServletMapping != null) {
							return false;
						}
					}
					else if (!this$statViewServletMapping
							.equals(other$statViewServletMapping)) {
						return false;
					}

					Object this$statViewServletLoginUsername = this
							.getStatViewServletLoginUsername();
					Object other$statViewServletLoginUsername = other
							.getStatViewServletLoginUsername();
					if (this$statViewServletLoginUsername == null) {
						if (other$statViewServletLoginUsername != null) {
							return false;
						}
					}
					else if (!this$statViewServletLoginUsername
							.equals(other$statViewServletLoginUsername)) {
						return false;
					}

					label103: {
						Object this$statViewServletLoginPassword = this
								.getStatViewServletLoginPassword();
						Object other$statViewServletLoginPassword = other
								.getStatViewServletLoginPassword();
						if (this$statViewServletLoginPassword == null) {
							if (other$statViewServletLoginPassword == null) {
								break label103;
							}
						}
						else if (this$statViewServletLoginPassword
								.equals(other$statViewServletLoginPassword)) {
							break label103;
						}

						return false;
					}

					Object this$statViewServletResetEnable = this
							.getStatViewServletResetEnable();
					Object other$statViewServletResetEnable = other
							.getStatViewServletResetEnable();
					if (this$statViewServletResetEnable == null) {
						if (other$statViewServletResetEnable != null) {
							return false;
						}
					}
					else if (!this$statViewServletResetEnable
							.equals(other$statViewServletResetEnable)) {
						return false;
					}

					return true;
				}
			}
		}
	}

	protected boolean canEqual(Object other) {
		return other instanceof DruidConfiguration;
	}

	@Override
	public int hashCode() {
		int result = 1;
		Object $url = this.getUrl();
		result = result * 59 + ($url == null ? 43 : $url.hashCode());
		Object $username = this.getUsername();
		result = result * 59 + ($username == null ? 43 : $username.hashCode());
		Object $driverClassName = this.getDriverClassName();
		result = result * 59
				+ ($driverClassName == null ? 43 : $driverClassName.hashCode());
		result = result * 59 + this.getInitialSize();
		result = result * 59 + this.getMinIdle();
		result = result * 59 + this.getMaxActive();
		result = result * 59 + this.getMaxWait();
		long $timeBetweenEvictionRunsMillis = this.getTimeBetweenEvictionRunsMillis();
		result = result * 59 + (int) ($timeBetweenEvictionRunsMillis >>> 32
				^ $timeBetweenEvictionRunsMillis);
		long $minEvictableIdleTimeMillis = this.getMinEvictableIdleTimeMillis();
		result = result * 59 + (int) ($minEvictableIdleTimeMillis >>> 32
				^ $minEvictableIdleTimeMillis);
		result = result * 59 + (this.isTestWhileIdle() ? 79 : 97);
		result = result * 59 + (this.isTestOnBorrow() ? 79 : 97);
		result = result * 59 + (this.isTestOnReturn() ? 79 : 97);
		result = result * 59 + (this.isPoolPreparedStatements() ? 79 : 97);
		result = result * 59 + this.getMaxPoolPreparedStatementPerConnectionSize();
		Object $filters = this.getFilters();
		result = result * 59 + ($filters == null ? 43 : $filters.hashCode());
		Object $connectionProperties = this.getConnectionProperties();
		result = result * 59
				+ ($connectionProperties == null ? 43 : $connectionProperties.hashCode());
		Object $webStatFilterExclusions = this.getWebStatFilterExclusions();
		result = result * 59 + ($webStatFilterExclusions == null ? 43
				: $webStatFilterExclusions.hashCode());
		Object $statViewServletMapping = this.getStatViewServletMapping();
		result = result * 59 + ($statViewServletMapping == null ? 43
				: $statViewServletMapping.hashCode());
		Object $statViewServletLoginUsername = this.getStatViewServletLoginUsername();
		result = result * 59 + ($statViewServletLoginUsername == null ? 43
				: $statViewServletLoginUsername.hashCode());
		Object $statViewServletLoginPassword = this.getStatViewServletLoginPassword();
		result = result * 59 + ($statViewServletLoginPassword == null ? 43
				: $statViewServletLoginPassword.hashCode());
		Object $statViewServletResetEnable = this.getStatViewServletResetEnable();
		result = result * 59 + ($statViewServletResetEnable == null ? 43
				: $statViewServletResetEnable.hashCode());
		return result;
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
