package ai.bzanalytics.ratls.service;

import ai.bzanalytics.ratls.dto.LoginDTO;

public interface AuthService {

	public Object loginUser(LoginDTO login);

}
