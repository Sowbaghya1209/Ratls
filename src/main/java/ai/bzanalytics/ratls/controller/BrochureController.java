package ai.bzanalytics.ratls.controller;

import ai.bzanalytics.ratls.dto.BrochureDTO;
import ai.bzanalytics.ratls.service.BrochureService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/brochure")
@RequiredArgsConstructor
@CrossOrigin
public class BrochureController {

	private final BrochureService brochureService;

	@GetMapping("/url")
	public BrochureDTO getBrochureUrl() {
		try {
			return brochureService.getBrochureUrl();
		}
		catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
					"Error while retrieving brochure URL: " + e.getMessage());
		}
	}

	@PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public String saveBrochureUrl(@ModelAttribute BrochureDTO brochureDTO) {
		try {
			brochureService.saveBrochureUrl(brochureDTO);
			return "URL saved successfully";
		}
		catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
					"Error while saving brochure URL: " + e.getMessage());
		}
	}

	@PutMapping("/update-brochure-url")
	public String updateBrochureUrl(@ModelAttribute BrochureDTO brochureDTO) {
		try {
			brochureService.updateBrochureUrl(brochureDTO);
			return "URL updated successfully";
		}
		catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
					"Error while updating brochure URL: " + e.getMessage());
		}
	}

}
