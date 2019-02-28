package com.sunflower.token;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.sunflower.exceptions.BusinessException;

import java.util.Date;

/**
 * @author sunflower
 */
public final class TokenUtil {

	public static final long DEFAULT_TIMEOUT = 2592000000L;
	private static final String SECRET = "com.sunflower.secret";
	private static final String ISSUER = "sunflower";

	/**
	 * 生成token
	 * @param subjects 加密信息
	 * @param ttlMillis 过期时间
	 * @return
	 * @throws Exception
	 */
	public static String createToken(String subjects, long ttlMillis) {
		try {
			Algorithm algorithm = Algorithm.HMAC256(SECRET);
			return  JWT.create()
					.withIssuer(ISSUER)
					.withExpiresAt(new Date(System.currentTimeMillis() - ttlMillis))
					.withSubject(subjects)
					.sign(algorithm);
		} catch (IllegalArgumentException e) {
			throw new BusinessException("生成token失败");
		}
	}

	/**
	 * 验证jwt，并返回数据
	 */
	public static DecodedJWT parseJWT(String token) {
		Algorithm algorithm;
		try {
			algorithm = Algorithm.HMAC256(SECRET);
			JWTVerifier verifier = JWT.require(algorithm).withIssuer(ISSUER).build();
			return verifier.verify(token);
		} catch (Exception e) {
			throw new BusinessException("鉴权失败");
		}
	}
}

