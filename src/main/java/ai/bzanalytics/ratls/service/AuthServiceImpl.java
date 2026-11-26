package ai.bzanalytics.ratls.service;

//import ai.bzanalytics.ratls.util.JwtUtil;

import ai.bzanalytics.ratls.dto.LoginDTO;
import ai.bzanalytics.ratls.exceptions.NotFoundException;
import ai.bzanalytics.ratls.repo.AdminRepository;
import ai.bzanalytics.ratls.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

	private final AdminRepository repository;

	String accessToken;

	public Object loginUser(LoginDTO loginDTO) {
		if (loginDTO.getUserName().equals("ratlscontracting123") && loginDTO.getPassword().equals("Ratls@123")) {
			accessToken = JwtUtil.generateJwtToken(loginDTO.getUserName(), Map.of());
			return Map.of("message", "Login successfully.", "accessToken", accessToken);
		}
		else {
			throw new NotFoundException("Provide valid Credentials: ");
		}
	}

}
