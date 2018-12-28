package com.sunflower.config.pac4jcas;

import io.buji.pac4j.realm.Pac4jRealm;
import io.buji.pac4j.subject.Pac4jPrincipal;
import io.buji.pac4j.token.Pac4jToken;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UnknownAccountException;
import org.pac4j.core.profile.CommonProfile;

import java.util.List;

/**
 * 授权需要继承 AuthorizingRealm 类，并实现 doGetAuthenticationInfo 方法
 *
 * @author Administrator
 *
 */
public class SunflowerPac4jRealms extends Pac4jRealm {

	private String userInfoAndAuthorizationInfoUrl;

	public String getUserInfoAndAuthorizationInfoUrl() {
		return userInfoAndAuthorizationInfoUrl;
	}

	public void setUserInfoAndAuthorizationInfoUrl(
			String userInfoAndAuthorizationInfoUrl) {
		this.userInfoAndAuthorizationInfoUrl = userInfoAndAuthorizationInfoUrl;
	}

	public SunflowerPac4jRealms() {
	}

	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(
			AuthenticationToken authenticationToken) throws AuthenticationException {
		System.out.println("ShiroRealms 开始认证！");

		// 1. 把 AuthenticationToken 转换为 Pac4jToken
		Pac4jToken token = (Pac4jToken) authenticationToken;

		// 2. 从 UsernamePasswordToken 中来获取 username
		List<CommonProfile> profiles = token.getProfiles();
		for (CommonProfile profile : profiles) {
			profile.addAttribute("userId", "1");
		}

		Pac4jPrincipal principal = new Pac4jPrincipal(profiles,
				this.getPrincipalNameAttribute());

		// 3. 调用数据库的方法, 从数据库中查询 username 对应的用户记录
		System.out.println("从数据库中获取 loginName: " + principal.getName() + " 所对应的用户信息.");

		// 4. 若用户不存在, 则可以抛出 UnknownAccountException 异常
		if ("unknown".equals(principal.getName())) {
			throw new UnknownAccountException("用户不存在!");
		}

		// 5. 根据用户信息的情况, 决定是否需要抛出其他的 AuthenticationException 异常.
		if ("monster".equals(principal.getName())) {
			throw new LockedAccountException("用户被锁定");
		}

		// 根据用户的情况, 来构建 AuthenticationInfo 对象并返回. 通常使用的实现类为:

		return new SimpleAuthenticationInfo(principal, profiles.hashCode(),
				this.getName());
	}

}
