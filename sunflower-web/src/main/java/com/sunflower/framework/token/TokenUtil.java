package com.sunflower.framework.token;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.util.Assert;
import org.springframework.util.Base64Utils;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.util.Date;

public class TokenUtil {

	private static final String secretKey = "kkweos09dd23njslpe";

	public static final long defaultTimeOut = 2592000000L;

	public TokenUtil() {
	}

	public static SecretKey generalKey(String secretKey) {
		byte[] encodedKey = Base64Utils.decodeFromString(secretKey);
		SecretKey key = new SecretKeySpec(encodedKey, 0, encodedKey.length, "AES");
		return key;
	}

	public static String createJWT(String id, String subject, long ttlMillis) {
		Assert.isTrue(ttlMillis >= 10000L, "token 最小有效期为10秒");
		SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;
		SecretKey key = generalKey("kkweos09dd23njslpe");
		JwtBuilder builder = Jwts.builder()
				.setExpiration(new Date(System.currentTimeMillis() + ttlMillis)).setId(id)
				.setIssuedAt(new Date()).setSubject(subject)
				.signWith(signatureAlgorithm, key);
		return builder.compact();
	}

	public static Claims parseJWT(String jwt) throws Exception {
		SecretKey key = generalKey("kkweos09dd23njslpe");
		Claims claims = (Claims) Jwts.parser().setSigningKey(key).parseClaimsJws(jwt)
				.getBody();
		return claims;
	}

}
