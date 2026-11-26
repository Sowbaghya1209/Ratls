package ai.bzanalytics.ratls.service;

import ai.bzanalytics.ratls.dto.PartnerLogoDTO;

import java.util.List;
import java.util.Optional;

public interface PartnerLogoService {

	public PartnerLogoDTO createPartnerLogo(PartnerLogoDTO partnerLogoDTO);

	public List<PartnerLogoDTO> getAllPartnerLogos();

	public Optional<PartnerLogoDTO> getPartnerLogoById(Long id);

	public void deletePartnerLogo(Long id);

}
