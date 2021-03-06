package com.sunflower.member;

import com.sunflower.token.UserProfile;
import springfox.documentation.annotations.ApiIgnore;

@ApiIgnore
public class MemberUserProfile implements UserProfile {

	private static final long serialVersionUID = 1L;

	private String loginId;

	private String name;

	private String accountType;

	public MemberUserProfile() {
	}

	public MemberUserProfile(String loginId, String name, String accountType) {
		this.loginId = loginId;
		this.name = name;
		this.accountType = accountType;
	}

	@Override
	public String getLoginId() {
		return this.loginId;
	}

	@Override
	public String getName() {
		return this.name;
	}

	public String getAccountType() {
		return accountType;
	}

	public void setAccountType(String accountType) {
		this.accountType = accountType;
	}

}
