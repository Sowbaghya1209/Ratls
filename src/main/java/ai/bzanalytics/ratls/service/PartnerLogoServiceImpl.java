package ai.bzanalytics.ratls.service;

import ai.bzanalytics.ratls.configuration.S3Service;
import ai.bzanalytics.ratls.dto.PartnerLogoDTO;
import ai.bzanalytics.ratls.model.PartnerLogo;
import ai.bzanalytics.ratls.repo.PartnerLogoRepository;
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
public class PartnerLogoServiceImpl implements PartnerLogoService {

	private final PartnerLogoRepository partnerLogoRepository;

	private final S3Service s3Service;

	private PartnerLogoDTO convertToDTO(PartnerLogo partnerLogo) {
		return new PartnerLogoDTO(partnerLogo.getId(), partnerLogo.getLogoUrl(), null

		);
	}

	private PartnerLogo convertToEntity(PartnerLogoDTO partnerLogoDTO, String logoUrl) {
		PartnerLogo partnerLogo = new PartnerLogo();
		partnerLogo.setLogoUrl(logoUrl);
		return partnerLogoRepository.save(partnerLogo);
	}

	@Override
	public PartnerLogoDTO createPartnerLogo(PartnerLogoDTO partnerLogoDTO) {
		String logoUrl = null;

		try {
			if (partnerLogoDTO.getLogoFile() != null) {
				logoUrl = s3Service.uploadImage(partnerLogoDTO.getLogoFile());
			}
		}
		catch (IOException e) {
			throw new RuntimeException("Error uploading partner logo", e);
		}

		PartnerLogo partnerLogo = convertToEntity(partnerLogoDTO, logoUrl);
		partnerLogo.setDate(LocalDateTime.now());
		PartnerLogo savedPartnerLogo = partnerLogoRepository.save(partnerLogo);
		return convertToDTO(savedPartnerLogo);
	}

	@Override
	public List<PartnerLogoDTO> getAllPartnerLogos() {
		List<PartnerLogo> partnerLogos = partnerLogoRepository.findAll();
		return partnerLogos.stream().map(this::convertToDTO).collect(Collectors.toList());
	}

	@Override
	public Optional<PartnerLogoDTO> getPartnerLogoById(Long id) {
		PartnerLogo partnerLogo = partnerLogoRepository.findById(id)
			.orElseThrow(() -> new EntityNotFoundException("Partner logo not found with ID: " + id));
		return Optional.of(convertToDTO(partnerLogo));
	}

	@Override
	public void deletePartnerLogo(Long id) {
		if (partnerLogoRepository.existsById(id)) {
			partnerLogoRepository.deleteById(id);
		}
		else {
			throw new EntityNotFoundException("Partner logo not found with ID: " + id);
		}
	}

}
