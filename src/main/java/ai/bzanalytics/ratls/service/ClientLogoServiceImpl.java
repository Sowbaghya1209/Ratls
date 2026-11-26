package ai.bzanalytics.ratls.service;

import ai.bzanalytics.ratls.configuration.S3Service;
import ai.bzanalytics.ratls.dto.ClientLogoDTO;
import ai.bzanalytics.ratls.model.ClientLogo;
import ai.bzanalytics.ratls.repo.ClientLogoRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ClientLogoServiceImpl implements ClientLogoService {

	private final ClientLogoRepository clientLogoRepository;

	private final S3Service s3Service;

	private ClientLogoDTO convertToDTO(ClientLogo clientLogo) {
		return new ClientLogoDTO(clientLogo.getId(), null, clientLogo.getLogoUrl());
	}

	private ClientLogo convertToEntity(ClientLogoDTO clientLogoDTO, String logoUrl) {
		ClientLogo clientLogo = new ClientLogo();
		clientLogo.setLogoUrl(logoUrl);
		return clientLogoRepository.save(clientLogo);
	}

	@Override
	public ClientLogoDTO createClientLogo(ClientLogoDTO clientLogoDTO) {
		String logoUrl = null;

		try {
			if (clientLogoDTO.getLogoFile() != null) {
				logoUrl = s3Service.uploadImage(clientLogoDTO.getLogoFile());
			}
		}
		catch (IOException e) {
			throw new RuntimeException("Error uploading logo", e);
		}

		ClientLogo clientLogo = convertToEntity(clientLogoDTO, logoUrl);
		clientLogo.setDate(LocalDateTime.now());
		ClientLogo savedLogo = clientLogoRepository.save(clientLogo);

		return convertToDTO(savedLogo);
	}

	@Override
	public List<ClientLogoDTO> getAllClientLogos() {
		List<ClientLogo> clientLogos = clientLogoRepository.findAll();
		return clientLogos.stream().map(this::convertToDTO).collect(Collectors.toList());
	}

	@Override
	public Optional<ClientLogoDTO> getClientLogoById(Long id) {
		ClientLogo clientLogo = clientLogoRepository.findById(id)
			.orElseThrow(() -> new EntityNotFoundException("Client logo not found"));
		return Optional.of(convertToDTO(clientLogo));
	}

	@Override
	public void deleteClientLogo(Long id) {
		if (clientLogoRepository.existsById(id)) {
			clientLogoRepository.deleteById(id);
		}
		else {
			throw new EntityNotFoundException("Client logo not found");
		}
	}

}
