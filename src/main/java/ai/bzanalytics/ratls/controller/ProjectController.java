package ai.bzanalytics.ratls.controller;

import ai.bzanalytics.ratls.dto.ProjectDTO;
import ai.bzanalytics.ratls.dto.SearchRequest;
import ai.bzanalytics.ratls.exceptions.NotFoundException;
import ai.bzanalytics.ratls.service.ProjectService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api/projects")
@RequiredArgsConstructor
@CrossOrigin
public class ProjectController {

	private final ProjectService projectService;

	@PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ProjectDTO createProject(@ModelAttribute ProjectDTO projectDTO) {
		try {
			return projectService.createProject(projectDTO);
		}
		catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
					"Error while creating project: " + e.getMessage());
		}
	}

	@GetMapping
	public List<ProjectDTO> getAllProjects() {
		try {
			List<ProjectDTO> projects = projectService.getAllProjects();
			if (projects.isEmpty()) {
				throw new NotFoundException("No projects found");
			}
			return projects;
		}
		catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
					"Error while retrieving projects: " + e.getMessage());
		}
	}

	@GetMapping("/{id}")
	public ProjectDTO getProjectById(@PathVariable Long id) {
		try {
			return projectService.getProjectById(id)
				.orElseThrow(() -> new NotFoundException("Project not found with Id: " + id));
		}
		catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND,
					"Project not found with Id: " + id + " - " + e.getMessage());
		}
	}

	@PutMapping("/{id}")
	public ProjectDTO updateProject(@PathVariable Long id, @ModelAttribute ProjectDTO updatedProjectDTO) {
		try {
			if (updatedProjectDTO == null) {
				throw new NotFoundException("No inputs passed for updating the project");
			}
			return projectService.updateProject(id, updatedProjectDTO);
		}
		catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND,
					"Error while updating project with Id: " + id + " - " + e.getMessage());
		}
	}

	@DeleteMapping("/{id}")
	public void deleteProject(@PathVariable Long id) {
		try {
			projectService.deleteProject(id);
		}
		catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND,
					"Error while deleting project with Id: " + id + " - " + e.getMessage());
		}
	}

	@GetMapping("/category/{id}")
	public List<ProjectDTO> getByCategoryId(@PathVariable Long id) {
		try {
			return projectService.getProjectByCategoryId(id);
		}
		catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND,
					"Error while retrieving projects for category with Id: " + id + " - " + e.getMessage());
		}
	}

	@GetMapping("/permlink/{permlink}")
	public ProjectDTO getBlogByPermlink(@PathVariable String permlink) {
		try {
			return projectService.getBlogByPermlink(permlink)
				.orElseThrow(() -> new RuntimeException("Projects not found with permalink: " + permlink));
		}
		catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Projects not found with permalink: " + permlink,
					e);
		}
	}

	@GetMapping("/search")
	public List<ProjectDTO> searchProjects(@RequestParam String query) {
		try {
			return projectService.searchProjects(query);
		}
		catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Projects not found: " + e.getMessage());
		}
	}


}
