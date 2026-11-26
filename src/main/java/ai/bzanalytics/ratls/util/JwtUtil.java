
package ai.bzanalytics.ratls.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class JwtUtil {

	private static final String SECRET_KEY = "your-secret-key";

	private static final long ACCESS_TOKEN_EXPIRATION = 15 * 60 * 1000;

	private static final long REFRESH_TOKEN_EXPIRATION = 7 * 24 * 60 * 60 * 1000;

	public static String generateJwtToken(String subject, Map<String, String> claims) {
		Algorithm algorithm = Algorithm.HMAC256(SECRET_KEY);

		var jwtBuilder = JWT.create()
			.withSubject(subject)
			.withIssuedAt(new Date())
			.withExpiresAt(new Date(System.currentTimeMillis() + ACCESS_TOKEN_EXPIRATION));

		claims.forEach(jwtBuilder::withClaim);
		return jwtBuilder.sign(algorithm);
	}

	public static String generateRefreshToken(String subject) {
		Algorithm algorithm = Algorithm.HMAC256(SECRET_KEY);
		return JWT.create()
			.withSubject(subject)
			.withIssuedAt(new Date())
			.withExpiresAt(new Date(System.currentTimeMillis() + REFRESH_TOKEN_EXPIRATION))
			.sign(algorithm);
	}

	public static DecodedJWT verifyJwtToken(String token) {
		try {
			Algorithm algorithm = Algorithm.HMAC256(SECRET_KEY);
			JWTVerifier verifier = JWT.require(algorithm).build();
			return verifier.verify(token);
		}
		catch (TokenExpiredException e) {
			throw new RuntimeException("Token has expired");
		}
		catch (JWTVerificationException e) {
			throw new RuntimeException("Invalid token");
		}
	}

	public static String refreshJwtToken(String refreshToken) {
		try {
			DecodedJWT decodedJWT = JWT.decode(refreshToken);
			String subject = decodedJWT.getSubject();
			return generateJwtToken(subject, Map.of());
		}
		catch (JWTDecodeException e) {
			throw new RuntimeException("Invalid refresh token");
		}
	}

	// Method to extract claims from the JWT
	public static Map<String, String> extractClaims(String token) {
		try {
			// Decode the JWT token
			DecodedJWT decodedJWT = JWT.decode(token);

			// Create a map to store the claims as String
			Map<String, String> claimsMap = new HashMap<>();

			// Loop through the claims and extract them as String
			decodedJWT.getClaims().forEach((key, claim) -> {
				claimsMap.put(key, claim.asString()); // Convert each claim to a String
			});

			return claimsMap;
		}
		catch (JWTDecodeException e) {
			throw new RuntimeException("Invalid token, unable to extract claims.", e);
		}
	}

}
