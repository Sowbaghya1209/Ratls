package ai.bzanalytics.ratls;

import ai.bzanalytics.ratls.controller.TestimonialController;
import ai.bzanalytics.ratls.dto.TestimonialDTO;
import ai.bzanalytics.ratls.service.TestimonialService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Collections;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class TestimonialControllerTest {

	private MockMvc mockMvc;

	@InjectMocks
	private TestimonialController testimonialController;

	@Mock
	private TestimonialService testimonialService;

	private TestimonialDTO testimonialDTO;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
		mockMvc = MockMvcBuilders.standaloneSetup(testimonialController).build();
		testimonialDTO = new TestimonialDTO(1L, "Great service!", "John Doe", "CEO", "New York", "image.jpg", null);
	}

	@Test
	void testCreateTestimonial() throws Exception {
		when(testimonialService.createTestimonial(any(TestimonialDTO.class))).thenReturn(testimonialDTO);

		mockMvc.perform(multipart("/api/testimonials").file("file", "dummy content".getBytes()) // Simulating
																								// a
																								// file
																								// upload
			.param("quote", "Great service!")
			.param("name", "John Doe")
			.param("designation", "CEO")
			.param("location", "New York")
			.param("image", "image.jpg"))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.quote").value("Great service!"))
			.andExpect(jsonPath("$.name").value("John Doe"))
			.andExpect(jsonPath("$.designation").value("CEO"))
			.andExpect(jsonPath("$.location").value("New York"))
			.andExpect(jsonPath("$.image").value("image.jpg"));

		verify(testimonialService, times(1)).createTestimonial(any());
	}

	@Test
	void testGetAllTestimonials() throws Exception {
		when(testimonialService.getAllTestimonials()).thenReturn(Collections.singletonList(testimonialDTO));

		mockMvc.perform(get("/api/testimonials"))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$[0].quote").value("Great service!"))
			.andExpect(jsonPath("$[0].name").value("John Doe"))
			.andExpect(jsonPath("$[0].designation").value("CEO"))
			.andExpect(jsonPath("$[0].location").value("New York"))
			.andExpect(jsonPath("$[0].image").value("image.jpg"));

		verify(testimonialService, times(1)).getAllTestimonials();
	}

	@Test
	void testGetTestimonialById() throws Exception {
		when(testimonialService.getTestimonialById(anyLong())).thenReturn(Optional.of(testimonialDTO));

		mockMvc.perform(get("/api/testimonials/{id}", 1))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.quote").value("Great service!"))
			.andExpect(jsonPath("$.name").value("John Doe"))
			.andExpect(jsonPath("$.designation").value("CEO"))
			.andExpect(jsonPath("$.location").value("New York"))
			.andExpect(jsonPath("$.image").value("image.jpg"));

		verify(testimonialService, times(1)).getTestimonialById(anyLong());
	}

	@Test
	void testGetTestimonialById_NotFound() throws Exception {
		when(testimonialService.getTestimonialById(anyLong())).thenReturn(Optional.empty());

		mockMvc.perform(get("/api/testimonials/{id}", 999)).andExpect(status().isNotFound());

		verify(testimonialService, times(1)).getTestimonialById(anyLong());
	}

	@Test
	void testUpdateTestimonial() throws Exception {
		Long testimonialId = 1L;
		TestimonialDTO updatedTestimonialDTO = new TestimonialDTO(testimonialId, "Updated quote", "John Doe", "CEO",
				"New York", "updated_image.jpg", null);

		when(testimonialService.updateTestimonial(eq(testimonialId), any(TestimonialDTO.class)))
			.thenReturn(updatedTestimonialDTO);

		mockMvc
			.perform(put("/api/testimonials/{id}", testimonialId).param("quote", "Updated quote")
				.param("name", "John Doe")
				.param("designation", "CEO")
				.param("location", "New York")
				.param("image", "updated_image.jpg"))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.quote").value("Updated quote"))
			.andExpect(jsonPath("$.name").value("John Doe"))
			.andExpect(jsonPath("$.designation").value("CEO"))
			.andExpect(jsonPath("$.location").value("New York"))
			.andExpect(jsonPath("$.image").value("updated_image.jpg"));

		verify(testimonialService, times(1)).updateTestimonial(eq(testimonialId), any());
	}

	@Test
	void testDeleteTestimonial() throws Exception {
		Long testimonialId = 1L;
		doNothing().when(testimonialService).deleteTestimonial(testimonialId);

		mockMvc.perform(delete("/api/testimonials/{id}", testimonialId)).andExpect(status().isOk());

		verify(testimonialService, times(1)).deleteTestimonial(testimonialId);
	}

}
