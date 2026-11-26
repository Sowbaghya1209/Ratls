package ai.bzanalytics.ratls.service;

import ai.bzanalytics.ratls.dto.ClientLogoDTO;

import java.util.List;
import java.util.Optional;

public interface ClientLogoService {

	public ClientLogoDTO createClientLogo(ClientLogoDTO clientLogoDTO);

	public List<ClientLogoDTO> getAllClientLogos();

	public Optional<ClientLogoDTO> getClientLogoById(Long id);

	public void deleteClientLogo(Long id);

}
