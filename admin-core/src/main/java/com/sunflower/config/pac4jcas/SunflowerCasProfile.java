package com.sunflower.config.pac4jcas;

import org.pac4j.cas.profile.CasProfile;

public class SunflowerCasProfile extends CasProfile {

	private static final long serialVersionUID = 7975326552623142122L;

	private String userId;

	private String userName;

	public SunflowerCasProfile() {
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

}
