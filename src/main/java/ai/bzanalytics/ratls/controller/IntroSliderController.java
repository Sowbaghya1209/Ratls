package ai.bzanalytics.ratls.controller;

import ai.bzanalytics.ratls.dto.IntroSliderDTO;
import ai.bzanalytics.ratls.exceptions.NotFoundException;
import ai.bzanalytics.ratls.service.IntroSliderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api/intro-sliders")
@RequiredArgsConstructor
@CrossOrigin
public class IntroSliderController {

	private final IntroSliderService introSliderService;

	@PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)

	public IntroSliderDTO createIntroSlider(@ModelAttribute IntroSliderDTO introSliderDTO) {
		try {

			return introSliderService.createIntroSlider(introSliderDTO);
		}
		catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
					"Error while creating intro slider: " + e.getMessage());
		}
	}

	@GetMapping
	public List<IntroSliderDTO> getAllIntroSliders() {
		try {
			List<IntroSliderDTO> introSliders = introSliderService.getAllIntroSliders();
			if (introSliders.isEmpty()) {
				throw new NotFoundException("No intro sliders found");
			}
			return introSliders;
		}
		catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
					"Error while retrieving intro sliders: " + e.getMessage());
		}
	}

	@GetMapping("/{id}")
	public IntroSliderDTO getIntroSliderById(@PathVariable Long id) {
		try {
			return introSliderService.getIntroSliderById(id)
				.orElseThrow(() -> new NotFoundException("Intro slider not found with Id: " + id));
		}
		catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND,
					"Intro slider not found with Id: " + id + " - " + e.getMessage());
		}
	}

	@PutMapping("/{id}")
	public IntroSliderDTO updateIntroSlider(@PathVariable Long id, @ModelAttribute IntroSliderDTO introSliderDTO) {
		try {

			return introSliderService.updateIntroSlider(id, introSliderDTO);
		}
		catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND,
					"Error while updating intro slider with Id: " + id + " - " + e.getMessage());
		}
	}

	@DeleteMapping("/{id}")
	public void deleteIntroSlider(@PathVariable Long id) {
		try {
			introSliderService.deleteIntroSlider(id);
		}
		catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND,
					"Error while deleting intro slider with Id: " + id + " - " + e.getMessage());
		}
	}

}
