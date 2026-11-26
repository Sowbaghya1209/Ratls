package ai.bzanalytics.ratls.controller;

import ai.bzanalytics.ratls.dto.LoginDTO;
import ai.bzanalytics.ratls.exceptions.NotFoundException;
import ai.bzanalytics.ratls.service.AuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@Log4j2
@RestController
@RequestMapping("/api/auth/login")
@RequiredArgsConstructor
@CrossOrigin
public class AuthController {

	private final AuthService authService;

	@PostMapping
	public Object loginUser(@RequestBody LoginDTO login) {
		try {
			if (login != null) {
				return authService.loginUser(login);
			}
			else {
				throw new NotFoundException("Provide valid Credentials");
			}
		}
		catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Error while login: " + e.getMessage());
		}
	}

}
