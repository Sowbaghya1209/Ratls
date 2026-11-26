package ai.bzanalytics.ratls.controller;

import ai.bzanalytics.ratls.dto.PartnerLogoDTO;
import ai.bzanalytics.ratls.service.PartnerLogoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api/partner-logo")
@RequiredArgsConstructor
@CrossOrigin
public class PartnerLogoController {

	private final PartnerLogoService partnerLogoService;

	@GetMapping("/{id}")
	public PartnerLogoDTO getPartnerLogoById(@PathVariable Long id) {
		try {
			return partnerLogoService.getPartnerLogoById(id)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
						"Partner logo not found with ID: " + id));
		}
		catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
					"Error while retrieving partner logo: " + e.getMessage());
		}
	}

	@GetMapping
	public List<PartnerLogoDTO> getAllPartnerLogos() {
		try {
			return partnerLogoService.getAllPartnerLogos();
		}
		catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
					"Error while retrieving all partner logos: " + e.getMessage());
		}
	}

	@PostMapping(consumes = "multipart/form-data")
	@ResponseStatus(HttpStatus.CREATED)
	public PartnerLogoDTO createPartnerLogo(@ModelAttribute PartnerLogoDTO partnerLogoDTO) {
		try {
			return partnerLogoService.createPartnerLogo(partnerLogoDTO);
		}
		catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
					"Error while creating partner logo: " + e.getMessage());
		}
	}

	@DeleteMapping("/{id}")
	public String deletePartnerLogo(@PathVariable Long id) {
		try {
			partnerLogoService.deletePartnerLogo(id);
			return "Partner logo deleted successfully";
		}
		catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
					"Error while deleting partner logo: " + e.getMessage());
		}
	}

}
