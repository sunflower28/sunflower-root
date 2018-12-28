package com.sunflower.config.pac4jcas;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.Objects;

@Configuration
@ConfigurationProperties(prefix = "cas")
public class CasProperties {

	private String casServerUrlPrefix;

	private String userInfoAndAuthorizationInfoUrl;

	private long globalSessionTimeout = 1800000L;

	private long sessionValidationInterval = 120000L;

	public CasProperties() {
	}

	public String getCasServerUrlPrefix() {
		return casServerUrlPrefix;
	}

	public void setCasServerUrlPrefix(String casServerUrlPrefix) {
		this.casServerUrlPrefix = casServerUrlPrefix;
	}

	public String getUserInfoAndAuthorizationInfoUrl() {
		return userInfoAndAuthorizationInfoUrl;
	}

	public void setUserInfoAndAuthorizationInfoUrl(
			String userInfoAndAuthorizationInfoUrl) {
		this.userInfoAndAuthorizationInfoUrl = userInfoAndAuthorizationInfoUrl;
	}

	public long getGlobalSessionTimeout() {
		return globalSessionTimeout;
	}

	public void setGlobalSessionTimeout(long globalSessionTimeout) {
		this.globalSessionTimeout = globalSessionTimeout;
	}

	public long getSessionValidationInterval() {
		return sessionValidationInterval;
	}

	public void setSessionValidationInterval(long sessionValidationInterval) {
		this.sessionValidationInterval = sessionValidationInterval;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (!(o instanceof CasProperties)) {
			return false;
		}
		CasProperties that = (CasProperties) o;
		return getGlobalSessionTimeout() == that.getGlobalSessionTimeout()
				&& getSessionValidationInterval() == that.getSessionValidationInterval()
				&& Objects.equals(getCasServerUrlPrefix(), that.getCasServerUrlPrefix())
				&& Objects.equals(getUserInfoAndAuthorizationInfoUrl(),
						that.getUserInfoAndAuthorizationInfoUrl());
	}

	@Override
	public int hashCode() {
		return Objects.hash(getCasServerUrlPrefix(), getUserInfoAndAuthorizationInfoUrl(),
				getGlobalSessionTimeout(), getSessionValidationInterval());
	}

	@Override
	public String toString() {
		return "CasProperties{" + "casServerUrlPrefix='" + casServerUrlPrefix + '\''
				+ ", userInfoAndAuthorizationInfoUrl='" + userInfoAndAuthorizationInfoUrl
				+ '\'' + ", globalSessionTimeout=" + globalSessionTimeout
				+ ", sessionValidationInterval=" + sessionValidationInterval + '}';
	}

}
