package ai.bzanalytics.ratls;

import ai.bzanalytics.ratls.controller.ProjectController;
import ai.bzanalytics.ratls.dto.ProjectDTO;
import ai.bzanalytics.ratls.exceptions.NotFoundException;
import ai.bzanalytics.ratls.service.ProjectService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class ProjectControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@Mock
	private ProjectService projectService;

	@InjectMocks
	private ProjectController projectController;

	private ProjectDTO projectDTO;

	@BeforeEach
	public void setUp() {
		MockitoAnnotations.openMocks(this);
		mockMvc = MockMvcBuilders.standaloneSetup(projectController).build();
		projectDTO = new ProjectDTO();
		projectDTO.setId(1L);
		projectDTO.setTitle("Test Project");
		projectDTO.setDescription1("Test Description");
		projectDTO.setClient("Test Client");
		projectDTO.setScopeOfWork("Test Scope");
		projectDTO.setLocation("Test Location");
		projectDTO.setStartDate(LocalDate.now());
		projectDTO.setEndDate(LocalDate.now().plusMonths(1));
		projectDTO.setPermlink("test-project-permlink");
		projectDTO.setImage1("image1.jpg");
		projectDTO.setImage2("image2.jpg");
		projectDTO.setImage3("image3.jpg");
		projectDTO.setDescription2("Test Description 2");
	}

	@Test
	public void testCreateProject() throws Exception {
		MockMultipartFile mockFile1 = new MockMultipartFile("file1", "image1.jpg", "image/jpeg",
				"dummy content".getBytes());
		MockMultipartFile mockFile2 = new MockMultipartFile("file2", "image2.jpg", "image/jpeg",
				"dummy content".getBytes());
		MockMultipartFile mockFile3 = new MockMultipartFile("file3", "image3.jpg", "image/jpeg",
				"dummy content".getBytes());

		when(projectService.createProject(any(ProjectDTO.class))).thenReturn(projectDTO);

		mockMvc
			.perform(multipart("/api/projects").file(mockFile1)
				.file(mockFile2)
				.file(mockFile3)
				.param("title", "Test Project")
				.param("description1", "Test Description")
				.param("client", "Test Client")
				.param("scopeOfWork", "Test Scope")
				.param("location", "Test Location")
				.param("startDate", LocalDate.now().toString())
				.param("endDate", LocalDate.now().plusMonths(1).toString())
				.param("permlink", "test-project-permlink")
				.param("description2", "Test Description 2"))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.title").value("Test Project"))
			.andExpect(jsonPath("$.client").value("Test Client"))
			.andExpect(jsonPath("$.description1").value("Test Description"))
			.andExpect(jsonPath("$.scopeOfWork").value("Test Scope"))
			.andExpect(jsonPath("$.location").value("Test Location"))
			.andExpect(jsonPath("$.permlink").value("test-project-permlink"))
			.andExpect(jsonPath("$.image1").value("image1.jpg"))
			.andExpect(jsonPath("$.image2").value("image2.jpg"))
			.andExpect(jsonPath("$.image3").value("image3.jpg"));

		verify(projectService, times(1)).createProject(any(ProjectDTO.class));
	}

	@Test
	public void testGetAllProjects() throws Exception {
		when(projectService.getAllProjects()).thenReturn(Arrays.asList(projectDTO));

		mockMvc.perform(get("/api/projects"))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$[0].title").value("Test Project"))
			.andExpect(jsonPath("$[0].client").value("Test Client"))
			.andExpect(jsonPath("$[0].description1").value("Test Description"));

		verify(projectService, times(1)).getAllProjects();
	}

	@Test
	public void testGetProjectById() throws Exception {
		when(projectService.getProjectById(1L)).thenReturn(Optional.of(projectDTO));

		mockMvc.perform(get("/api/projects/{id}", 1L))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.title").value("Test Project"))
			.andExpect(jsonPath("$.client").value("Test Client"))
			.andExpect(jsonPath("$.description1").value("Test Description"));

		verify(projectService, times(1)).getProjectById(1L);
	}

	@Test
	public void testGetProjectById_NotFound() throws Exception {
		when(projectService.getProjectById(1L)).thenReturn(Optional.empty());

		mockMvc.perform(get("/api/projects/{id}", 1L)).andExpect(status().isNotFound());

		verify(projectService, times(1)).getProjectById(1L);
	}

	@Test
	public void testUpdateProject() throws Exception {
		ProjectDTO updatedProject = new ProjectDTO();
		updatedProject.setId(1L);
		updatedProject.setTitle("Updated Project");
		updatedProject.setDescription1("Updated Description");
		updatedProject.setClient("Updated Client");
		updatedProject.setScopeOfWork("Updated Scope");
		updatedProject.setLocation("Updated Location");

		MockMultipartFile mockFile1 = new MockMultipartFile("file1", "image1.jpg", "image/jpeg",
				"updated content".getBytes());
		MockMultipartFile mockFile2 = new MockMultipartFile("file2", "image2.jpg", "image/jpeg",
				"updated content".getBytes());

		when(projectService.updateProject(1L, updatedProject)).thenReturn(updatedProject);

		ResultActions resultActions = mockMvc.perform(put("/api/projects/{id}", 1L)

			.param("title", "Updated Project")
			.param("description1", "Updated Description")
			.param("client", "Updated Client")
			.param("scopeOfWork", "Updated Scope")
			.param("location", "Updated Location"))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.title").value("Updated Project"))
			.andExpect(jsonPath("$.client").value("Updated Client"))
			.andExpect(jsonPath("$.description1").value("Updated Description"))
			.andExpect(jsonPath("$.scopeOfWork").value("Updated Scope"))
			.andExpect(jsonPath("$.location").value("Updated Location"));

		verify(projectService, times(1)).updateProject(1L, updatedProject);
	}

	@Test
	public void testDeleteProject() throws Exception {
		doNothing().when(projectService).deleteProject(1L);

		mockMvc.perform(delete("/api/projects/{id}", 1L));

		verify(projectService, times(1)).deleteProject(1L);
	}

	@Test
	public void testDeleteProject_NotFound() throws Exception {
		doThrow(new NotFoundException("Error while deleting project with Id: 1")).when(projectService)
			.deleteProject(1L);

		mockMvc.perform(delete("/api/projects/{id}", 1L)).andExpect(status().isNotFound());

		verify(projectService, times(1)).deleteProject(1L);
	}

	@Test
	public void testGetByCategoryId() throws Exception {
		when(projectService.getProjectByCategoryId(1L)).thenReturn(Arrays.asList(projectDTO));

		mockMvc.perform(get("/api/projects/category/{id}", 1L))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$[0].title").value("Test Project"));

		verify(projectService, times(1)).getProjectByCategoryId(1L);
	}

	@Test
	public void testGetByPermlink() throws Exception {
		when(projectService.getBlogByPermlink("test-permlink")).thenReturn(Optional.of(projectDTO));

		mockMvc.perform(get("/api/projects/permlink/{permlink}", "test-permlink"))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.title").value("Test Project"));

		verify(projectService, times(1)).getBlogByPermlink("test-permlink");
	}

	@Test
	public void testGetByPermlink_NotFound() throws Exception {
		when(projectService.getBlogByPermlink("invalid-permlink")).thenReturn(Optional.empty());

		mockMvc.perform(get("/api/projects/permlink/{permlink}", "invalid-permlink")).andExpect(status().isNotFound());

		verify(projectService, times(1)).getBlogByPermlink("invalid-permlink");
	}

}
