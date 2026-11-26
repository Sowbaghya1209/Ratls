package ai.bzanalytics.ratls.service;

import ai.bzanalytics.ratls.dto.ProjectDTO;

import java.util.List;
import java.util.Optional;

public interface ProjectService {

	public ProjectDTO createProject(ProjectDTO projectDTO);

	public List<ProjectDTO> getAllProjects();

	public Optional<ProjectDTO> getProjectById(Long id);

	public ProjectDTO updateProject(Long id, ProjectDTO updatedProjectDTO);

	public void deleteProject(Long id);

	public List<ProjectDTO> getProjectByCategoryId(Long categoryId);

	public Optional<ProjectDTO> getBlogByPermlink(String permlink);

	public List<ProjectDTO> searchProjects(String query);

	public List<ProjectDTO> findAllProjectsOrderedByCustomOrder();

}
