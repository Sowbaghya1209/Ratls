package ai.bzanalytics.ratls;

import ai.bzanalytics.ratls.controller.BrochureController;
import ai.bzanalytics.ratls.dto.BrochureDTO;
import ai.bzanalytics.ratls.service.BrochureService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class BrochureControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@Mock
	private BrochureService brochureService;

	@InjectMocks
	private BrochureController brochureController;

	@BeforeEach
	public void setUp() {
		MockitoAnnotations.openMocks(this);
		mockMvc = MockMvcBuilders.standaloneSetup(brochureController).build();
	}

	@Test
	public void testGetBrochureUrl() throws Exception {

		BrochureDTO brochureDTO = new BrochureDTO();
		brochureDTO.setUrl("https://example.com/brochure.pdf");

		when(brochureService.getBrochureUrl()).thenReturn(brochureDTO);

		mockMvc.perform(get("/api/brochure/url"))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.url").value("https://example.com/brochure.pdf"));

		verify(brochureService, times(1)).getBrochureUrl();
	}

	@Test
	public void testSaveBrochureUrl() throws Exception {

		MockMultipartFile mockFile = new MockMultipartFile("file", "dummy-file.pdf", "application/pdf",
				"dummy content".getBytes());

		BrochureDTO brochureDTO = new BrochureDTO();
		brochureDTO.setUrl("https://example.com/new-brochure.pdf");
		brochureDTO.setFile(mockFile); // Set the file here

		mockMvc.perform(multipart("/api/brochure").file(mockFile) // Simulate file upload
			.param("url", "https://example.com/new-brochure.pdf"))
			.andExpect(status().isOk())
			.andExpect(content().string("URL saved successfully"));
		verify(brochureService, times(1)).saveBrochureUrl(brochureDTO);
	}

}
