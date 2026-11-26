package ai.bzanalytics.ratls.service;

import ai.bzanalytics.ratls.configuration.S3Service;
import ai.bzanalytics.ratls.dto.TestimonialDTO;
import ai.bzanalytics.ratls.model.Testimonial;
import ai.bzanalytics.ratls.repo.TestimonialRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TestimonialServiceImpl implements TestimonialService {

	private final TestimonialRepository testimonialRepository;

	private final S3Service s3Service;

	private TestimonialDTO toDTO(Testimonial testimonial) {
		TestimonialDTO testimonialDTO = new TestimonialDTO();
		testimonialDTO.setId(testimonial.getId());
		testimonialDTO.setQuote(testimonial.getQuote());
		testimonialDTO.setName(testimonial.getName());
		testimonialDTO.setDesignation(testimonial.getDesignation());
		testimonialDTO.setLocation(testimonial.getLocation());
		testimonialDTO.setImage(testimonial.getImage());
		return testimonialDTO;
	}

	private Testimonial toEntity(TestimonialDTO testimonialDTO, String imageUrl) {
		Testimonial testimonial = new Testimonial();
		testimonial.setId(testimonialDTO.getId());
		testimonial.setQuote(testimonialDTO.getQuote());
		testimonial.setName(testimonialDTO.getName());
		testimonial.setDesignation(testimonialDTO.getDesignation());
		testimonial.setLocation(testimonialDTO.getLocation());
		testimonial.setImage(imageUrl);
		return testimonial;
	}

	// Create a new testimonial
	public TestimonialDTO createTestimonial(TestimonialDTO testimonialDTO) {
		try {
			String imageUrl = null;
			if (testimonialDTO.getFile() != null) {
				imageUrl = s3Service.uploadImage(testimonialDTO.getFile());
			}
			Testimonial testimonial = toEntity(testimonialDTO, imageUrl);
			Testimonial savedTestimonial = testimonialRepository.save(testimonial);
			return toDTO(savedTestimonial);
		}
		catch (IOException e) {
			throw new RuntimeException("Failed to upload image", e);
		}
	}

	// Get all testimonials
	public List<TestimonialDTO> getAllTestimonials() {
		return testimonialRepository.findAll().stream().map(this::toDTO).collect(Collectors.toList());
	}

	// Get testimonial by ID
	public Optional<TestimonialDTO> getTestimonialById(Long id) {
		Optional<Testimonial> testimonial = testimonialRepository.findById(id);
		return testimonial.map(this::toDTO);
	}

	// Update a testimonial
	public TestimonialDTO updateTestimonial(Long id, TestimonialDTO updatedTestimonialDTO) {
		try {
			Optional<Testimonial> existingTestimonial = testimonialRepository.findById(id);
			if (existingTestimonial.isPresent()) {
				Testimonial existing = existingTestimonial.get();

				existing.setQuote(updatedTestimonialDTO.getQuote());
				existing.setName(updatedTestimonialDTO.getName());
				existing.setDesignation(updatedTestimonialDTO.getDesignation());
				existing.setLocation(updatedTestimonialDTO.getLocation());

				if (updatedTestimonialDTO.getFile() != null) {

					String imageUrl = s3Service.uploadImage(updatedTestimonialDTO.getFile());
					existing.setImage(imageUrl);
				}
				Testimonial savedTestimonial = testimonialRepository.save(existing);
				return toDTO(savedTestimonial);
			}
			else {
				throw new EntityNotFoundException("Testimonial not found");
			}
		}
		catch (IOException e) {
			throw new RuntimeException("Failed to upload image", e);
		}
	}

	// Delete a testimonial
	public void deleteTestimonial(Long id) {

		if (testimonialRepository.existsById(id)) {
			testimonialRepository.deleteById(id);
		}
		else {
			throw new EntityNotFoundException("Testimonial not found");
		}
	}

}
