package ai.bzanalytics.ratls.controller;

import ai.bzanalytics.ratls.dto.TestimonialDTO;
import ai.bzanalytics.ratls.exceptions.NotFoundException;
import ai.bzanalytics.ratls.service.TestimonialService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api/testimonials")
@RequiredArgsConstructor
@CrossOrigin
public class TestimonialController {

	private final TestimonialService testimonialService;

	@PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public TestimonialDTO createTestimonial(@ModelAttribute TestimonialDTO testimonialDTO) {
		try {
			return testimonialService.createTestimonial(testimonialDTO);
		}
		catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
					"Error while creating testimonial: " + e.getMessage());
		}
	}

	@GetMapping
	public List<TestimonialDTO> getAllTestimonials() {
		try {
			List<TestimonialDTO> testimonials = testimonialService.getAllTestimonials();
			if (testimonials.isEmpty()) {
				throw new NotFoundException("No testimonials found");
			}
			return testimonials;
		}
		catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
					"Error while retrieving testimonials: " + e.getMessage());
		}
	}

	@GetMapping("/{id}")
	public TestimonialDTO getTestimonialById(@PathVariable Long id) {
		try {
			return testimonialService.getTestimonialById(id)
				.orElseThrow(() -> new NotFoundException("Testimonial not found with Id: " + id));
		}
		catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND,
					"Testimonial not found with Id: " + id + " - " + e.getMessage());
		}
	}

	@PutMapping("/{id}")
	public TestimonialDTO updateTestimonial(@PathVariable Long id,
			@ModelAttribute TestimonialDTO updatedTestimonialDTO) {
		try {
			if (updatedTestimonialDTO == null) {
				throw new NotFoundException("No inputs passed for updating testimonial");
			}
			return testimonialService.updateTestimonial(id, updatedTestimonialDTO);
		}
		catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND,
					"Error while updating testimonial with Id: " + id + " - " + e.getMessage());
		}
	}

	@DeleteMapping("/{id}")
	public void deleteTestimonial(@PathVariable Long id) {
		try {
			testimonialService.deleteTestimonial(id);
		}
		catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND,
					"Error while deleting testimonial with Id: " + id + " - " + e.getMessage());
		}
	}

}
