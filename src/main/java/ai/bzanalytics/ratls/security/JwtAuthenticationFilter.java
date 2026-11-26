package ai.bzanalytics.ratls.security;

import ai.bzanalytics.ratls.repo.AdminRepository;
import ai.bzanalytics.ratls.service.CustomUserDetailsService;
import ai.bzanalytics.ratls.util.JwtUtil;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@Component

public class JwtAuthenticationFilter implements Filter {

	private final JwtUtil jwtUtil;

	private final AdminRepository repository;

	public JwtAuthenticationFilter(JwtUtil jwtUtil, AdminRepository repository) {
		this.jwtUtil = jwtUtil;
		this.repository = repository;
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		String token = extractToken((HttpServletRequest) request);

		if (token != null && jwtUtil.verifyJwtToken(token) != null) {
			String userName = jwtUtil.verifyJwtToken(token).getSubject();
			List<String> roles = Arrays.asList("USER", "ADMIN");
			var user = repository.findByUserName(userName).orElseThrow(() -> new RuntimeException("User not found"));
			CustomUserDetailsService customUserDetails = new CustomUserDetailsService(user.getId(), user.getUserName(),
					user.getPassword(), roles);

			var authentication = new UsernamePasswordAuthenticationToken(customUserDetails, null,
					customUserDetails.getAuthorities());
			SecurityContextHolder.getContext().setAuthentication(authentication);
		}

		chain.doFilter(request, response);
	}

	private String extractToken(HttpServletRequest request) {
		String bearerToken = request.getHeader("Authorization");

		if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
			return bearerToken.substring(7);
		}
		return null;
	}

}
