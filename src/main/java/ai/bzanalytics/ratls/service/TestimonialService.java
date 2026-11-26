package ai.bzanalytics.ratls.service;

import ai.bzanalytics.ratls.dto.TestimonialDTO;

import java.util.List;
import java.util.Optional;

public interface TestimonialService {

	public TestimonialDTO createTestimonial(TestimonialDTO testimonialDTO);

	public List<TestimonialDTO> getAllTestimonials();

	public Optional<TestimonialDTO> getTestimonialById(Long id);

	public TestimonialDTO updateTestimonial(Long id, TestimonialDTO updatedTestimonialDTO);

	public void deleteTestimonial(Long id);

}
