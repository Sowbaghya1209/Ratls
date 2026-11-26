package ai.bzanalytics.ratls;

import ai.bzanalytics.ratls.controller.IntroSliderController;
import ai.bzanalytics.ratls.dto.IntroSliderDTO;
import ai.bzanalytics.ratls.service.IntroSliderService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Collections;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class IntroSliderControllerTest {

	private MockMvc mockMvc;

	@InjectMocks
	private IntroSliderController introSliderController;

	@Mock
	private IntroSliderService introSliderService;

	private IntroSliderDTO introSliderDTO;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
		mockMvc = MockMvcBuilders.standaloneSetup(introSliderController).build();

		// Using the default constructor and setting fields
		introSliderDTO = new IntroSliderDTO();
		introSliderDTO.setId(1L);
		introSliderDTO.setHeading("New Slider");
		introSliderDTO.setHeadingDescription("This is a test description");
		introSliderDTO.setImage1("image1.jpg");
		introSliderDTO.setImage2("image2.jpg");
		introSliderDTO.setBackgroundImage(null); // Can be null for testing
		introSliderDTO.setBackgroundVideo(null); // Can be null for testing
		introSliderDTO.setSelectBackground(null); // Can be null for testing
	}

	@Test
	void testGetAllIntroSliders() throws Exception {
		when(introSliderService.getAllIntroSliders()).thenReturn(Collections.singletonList(introSliderDTO));

		mockMvc.perform(get("/api/intro-sliders"))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$[0].heading").value("New Slider"))
			.andExpect(jsonPath("$[0].headingDescription").value("This is a test description"))
			.andExpect(jsonPath("$[0].image1").value("image1.jpg"))
			.andExpect(jsonPath("$[0].image2").value("image2.jpg"));

		verify(introSliderService, times(1)).getAllIntroSliders();
	}

	@Test
	void testGetIntroSliderById() throws Exception {
		when(introSliderService.getIntroSliderById(anyLong())).thenReturn(Optional.of(introSliderDTO));

		mockMvc.perform(get("/api/intro-sliders/{id}", 1))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.heading").value("New Slider"))
			.andExpect(jsonPath("$.headingDescription").value("This is a test description"))
			.andExpect(jsonPath("$.image1").value("image1.jpg"))
			.andExpect(jsonPath("$.image2").value("image2.jpg"));

		verify(introSliderService, times(1)).getIntroSliderById(anyLong());
	}

	@Test
	void testGetIntroSliderById_NotFound() throws Exception {
		when(introSliderService.getIntroSliderById(anyLong())).thenReturn(Optional.empty());

		mockMvc.perform(get("/api/intro-sliders/{id}", 999)).andExpect(status().isNotFound());

		verify(introSliderService, times(1)).getIntroSliderById(anyLong());
	}

	@Test
	void testDeleteIntroSlider() throws Exception {
		Long sliderId = 1L;
		doNothing().when(introSliderService).deleteIntroSlider(sliderId);

		mockMvc.perform(delete("/api/intro-sliders/{id}", sliderId)).andExpect(status().isOk());

		verify(introSliderService, times(1)).deleteIntroSlider(sliderId);
	}

}
