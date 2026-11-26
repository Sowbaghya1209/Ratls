package ai.bzanalytics.ratls;

import ai.bzanalytics.ratls.controller.TeamMemberController;
import ai.bzanalytics.ratls.dto.TeamMemberDTO;
import ai.bzanalytics.ratls.service.TeamMemberService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class TeamMemberControllerTest {

	private MockMvc mockMvc;

	@Mock
	private TeamMemberService teamMemberService;

	@InjectMocks
	private TeamMemberController teamMemberController;

	@BeforeEach
	public void setUp() {
		MockitoAnnotations.openMocks(this);
		mockMvc = MockMvcBuilders.standaloneSetup(teamMemberController).build();
	}

	@Test
	void testGetAllTeamMembers() throws Exception {
		TeamMemberDTO member1 = new TeamMemberDTO(1L, "John Doe", "Developer", "Bio of John", "image1.jpg", null);
		TeamMemberDTO member2 = new TeamMemberDTO(2L, "Jane Smith", "Manager", "Bio of Jane", "image2.jpg", null);
		when(teamMemberService.getAllTeamMembers()).thenReturn(List.of(member1, member2));

		mockMvc.perform(get("/api/team"))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$[0].name").value("John Doe"))
			.andExpect(jsonPath("$[1].name").value("Jane Smith"));
	}

	@Test
	void testGetTeamMemberById() throws Exception {
		TeamMemberDTO member = new TeamMemberDTO(1L, "John Doe", "Developer", "Bio of John", "image1.jpg", null);
		when(teamMemberService.getTeamMemberById(1L)).thenReturn(member);

		mockMvc.perform(get("/api/team/{id}", 1))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.name").value("John Doe"))
			.andExpect(jsonPath("$.role").value("Developer"));
	}

	@Test
	void testAddTeamMember() throws Exception {
		TeamMemberDTO newMember = new TeamMemberDTO(null, "New Member", "Tester", "Bio of New Member", "image3.jpg",
				null);
		TeamMemberDTO savedMember = new TeamMemberDTO(1L, "New Member", "Tester", "Bio of New Member", "image3.jpg",
				null);
		when(teamMemberService.addTeamMember(any(TeamMemberDTO.class))).thenReturn(savedMember);

		mockMvc.perform(multipart("/api/team").file("file", "dummy content".getBytes()) // Simulate
																						// file
																						// upload
			.param("name", "New Member")
			.param("role", "Tester")
			.param("bio", "Bio of New Member")
			.param("imageUrl", "image3.jpg"))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.id").value(1L))
			.andExpect(jsonPath("$.name").value("New Member"));
	}

	@Test
	void testUpdateTeamMember() throws Exception {
		TeamMemberDTO updatedMember = new TeamMemberDTO(1L, "Updated Member", "Updated Role", "Updated Bio",
				"updated_image.jpg", null);
		when(teamMemberService.updateTeamMember(anyLong(), any(TeamMemberDTO.class))).thenReturn(updatedMember);

		mockMvc.perform(multipart("/api/team/{id}", 1).file("file", "dummy content".getBytes()) // Simulate
																								// file
																								// upload
			.param("name", "Updated Member")
			.param("role", "Updated Role")
			.param("bio", "Updated Bio")
			.param("imageUrl", "updated_image.jpg"));
	}

	@Test
	void testDeleteTeamMember() throws Exception {
		mockMvc.perform(delete("/api/team/{id}", 1)).andExpect(status().isOk());

	}

}
