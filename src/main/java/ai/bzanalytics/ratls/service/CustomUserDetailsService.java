
package ai.bzanalytics.ratls.service;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class CustomUserDetailsService implements UserDetails {

	private Long id;

	private String userName;

	private String password;

	private List<String> role;

	public CustomUserDetailsService(Long id, String userName, String password, List<String> role) {

		this.id = id;
		this.userName = userName;
		this.password = password;
		this.role = role;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		// Assign roles as granted authorities
		return role.stream()
			.map(roleName -> new SimpleGrantedAuthority("ROLE_" + roleName))
			.collect(Collectors.toList());
	}

	@Override
	public String getPassword() {
		return password;
	}

	@Override
	public String getUsername() {
		return userName;
	}

}
