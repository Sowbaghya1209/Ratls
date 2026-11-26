package ai.bzanalytics.ratls.service;

import ai.bzanalytics.ratls.configuration.S3Service;
import ai.bzanalytics.ratls.dto.ProjectCategoryDTO;
import ai.bzanalytics.ratls.dto.ProjectDTO;
import ai.bzanalytics.ratls.model.Project;
import ai.bzanalytics.ratls.model.ProjectCategory;
import ai.bzanalytics.ratls.repo.ProjectCategoryRepository;
import ai.bzanalytics.ratls.repo.ProjectRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProjectServiceImpl implements ProjectService {

	private final ProjectRepository projectRepository;

	private final ProjectCategoryRepository categoryRepository;

	private final S3Service s3Service;

	public ProjectDTO toDTO(Project project) {
		ProjectDTO dto = new ProjectDTO();
		dto.setId(project.getId());
		dto.setTitle(project.getTitle());
		ProjectCategoryDTO categoryDTO = null;
		if (project.getCategory() != null) {
			categoryDTO = new ProjectCategoryDTO(project.getCategory().getId(), project.getCategory().getName());
		}
		dto.setCategory(categoryDTO);
		dto.setOngoing(project.isOngoing());
		dto.setDescription1(project.getDescription1());
		dto.setDescription2(project.getDescription2());
		dto.setClient(project.getClient());
		dto.setScopeOfWork(project.getScopeOfWork());
		dto.setLocation(project.getLocation());
		dto.setStartDate(project.getStartDate());
		dto.setEndDate(project.getEndDate());
		if (project.getImage1() != null) {
			dto.setImage1(project.getImage1());
		}
		if (project.getImage2() != null) {
			dto.setImage2(project.getImage2());
		}
		if (project.getImage3() != null) {
			dto.setImage3(project.getImage3());
		}
		dto.setPermlink(project.getPermlink());
		dto.setOrderNumber(project.getOrderNumber());
		return dto;
	}

	public Project toEntity(ProjectDTO dto, String imageUrl1, String imageUrl2, String imageUrl3) {
		Project project = new Project();
		project.setTitle(dto.getTitle());
		ProjectCategory category = categoryRepository.findByName(dto.getCategory().getName()).orElseGet(() -> {
			ProjectCategory newCategory = new ProjectCategory(dto.getCategory().getName());
			categoryRepository.save(newCategory);
			return newCategory;
		});
		project.setCategory(category);
		project.setOngoing(dto.isOngoing());
		project.setDescription1(dto.getDescription1());
		project.setClient(dto.getClient());
		project.setScopeOfWork(dto.getScopeOfWork());
		project.setLocation(dto.getLocation());
		project.setStartDate(dto.getStartDate());
		project.setEndDate(dto.getEndDate());
		project.setDescription2(dto.getDescription2());
		project.setOrderNumber(dto.getOrderNumber() != null ? dto.getOrderNumber() : 999.0);
		if (imageUrl1 != null) {
			project.setImage1(imageUrl1);
		}
		if (imageUrl2 != null) {
			project.setImage2(imageUrl2);
		}
		if (imageUrl3 != null) {
			project.setImage3(imageUrl3);
		}
		project.setPermlink(dto.getPermlink());
		return project;
	}

	public ProjectDTO createProject(ProjectDTO projectDTO) {
		String imageUrl1 = null;

		String imageUrl2 = null;

		String imageUrl3 = null;
		try {
			if (projectDTO.getPermlink() != null && projectRepository.existsByPermlink(projectDTO.getPermlink())) {
				throw new RuntimeException("Permlink already exists");
			}

			if (projectDTO.getFile1() != null) {
				imageUrl1 = s3Service.uploadImage(projectDTO.getFile1());
			}

			if (projectDTO.getFile2() != null) {
				imageUrl2 = s3Service.uploadImage(projectDTO.getFile2());
			}

			if (projectDTO.getFile3() != null) {
				imageUrl3 = s3Service.uploadImage(projectDTO.getFile3());
			}
			Project project = toEntity(projectDTO, imageUrl1, imageUrl2, imageUrl3);
			Project savedProject = projectRepository.save(project);
			return toDTO(savedProject);
		}
		catch (IOException e) {
			throw new RuntimeException("Failed to upload image", e);
		}
	}

	public List<ProjectDTO> getAllProjects() {
		return projectRepository.findAllProjectsOrderedByCustomOrder()
			.stream()
			.map(this::toDTO)
			.collect(Collectors.toList());
	}

	public Optional<ProjectDTO> getProjectById(Long id) {
		Optional<Project> project = projectRepository.findById(id);
		return project.map(this::toDTO);
	}

	public ProjectDTO updateProject(Long id, ProjectDTO updatedProjectDTO) {
		try {

			Project existingProject = projectRepository.findById(id)
				.orElseThrow(() -> new EntityNotFoundException("Project not found"));

			if (updatedProjectDTO.getPermlink() != null && existingProject.getPermlink() != null

					&& !existingProject.getPermlink().equals(updatedProjectDTO.getPermlink())) {
				if (projectRepository.existsByPermlink(updatedProjectDTO.getPermlink())) {
					throw new RuntimeException("Permlink already exists");
				}
			}
			existingProject.setTitle(updatedProjectDTO.getTitle());
			existingProject.setOngoing(updatedProjectDTO.isOngoing());
			existingProject.setDescription1(updatedProjectDTO.getDescription1());
			existingProject.setDescription2(updatedProjectDTO.getDescription2());
			existingProject.setClient(updatedProjectDTO.getClient());
			existingProject.setScopeOfWork(updatedProjectDTO.getScopeOfWork());
			existingProject.setLocation(updatedProjectDTO.getLocation());
			existingProject.setStartDate(updatedProjectDTO.getStartDate());
			existingProject.setEndDate(updatedProjectDTO.getEndDate());
			existingProject.setPermlink(updatedProjectDTO.getPermlink());
			if (updatedProjectDTO.getOrderNumber() != null) {
				existingProject.setOrderNumber(updatedProjectDTO.getOrderNumber());
			}

			ProjectCategory category = categoryRepository.findByName(updatedProjectDTO.getCategory().getName())
				.orElseGet(() -> {
					ProjectCategory newCategory = new ProjectCategory(updatedProjectDTO.getCategory().getName());
					categoryRepository.save(newCategory);
					return newCategory;
				});
			existingProject.setCategory(category);

			if (updatedProjectDTO.getFile1() != null) {
				String imageUrl1 = s3Service.uploadImage(updatedProjectDTO.getFile1());
				existingProject.setImage1(imageUrl1);
			}

			if (updatedProjectDTO.getFile2() != null) {
				String imageUrl2 = s3Service.uploadImage(updatedProjectDTO.getFile2());
				existingProject.setImage2(imageUrl2);
			}

			if (updatedProjectDTO.getFile3() != null) {
				String imageUrl3 = s3Service.uploadImage(updatedProjectDTO.getFile3());
				existingProject.setImage3(imageUrl3);
			}

			Project updatedProject = projectRepository.save(existingProject);
			return toDTO(updatedProject);

		}
		catch (IOException e) {
			throw new RuntimeException("Failed to upload image", e);
		}
	}

	public void deleteProject(Long id) {

		if (projectRepository.existsById(id)) {
			projectRepository.deleteById(id);
		}
		else {
			throw new EntityNotFoundException("Project not found");
		}
	}

	public List<ProjectDTO> getProjectByCategoryId(Long categoryId) {
		List<Project> projects = projectRepository.findProjectByCategoryId(categoryId);

		if (projects == null || projects.isEmpty()) {
			throw new EntityNotFoundException("No related projects found for category id: " + categoryId);
		}

		return projects.stream().map(this::toDTO).collect(Collectors.toList());
	}

	public Optional<ProjectDTO> getBlogByPermlink(String permlink) {
		Project project = projectRepository.findByPermlink(permlink)
			.orElseThrow(() -> new RuntimeException("Project not found with permalink: " + permlink));
		return Optional.of(toDTO(project));
	}

	public List<ProjectDTO> searchProjects(String query) {
		List<Project> projects = projectRepository.searchProjectsByTitleOrCategory(query, query, query);
		if (projects == null || projects.isEmpty()) {
			throw new EntityNotFoundException("No projects found for the query: " + query);
		}
		return projects.stream().map(this::toDTO).collect(Collectors.toList());
	}

	public List<ProjectDTO> findAllProjectsOrderedByCustomOrder() {
		List<Project> orderedProjects = projectRepository.findAllProjectsOrderedByCustomOrder();
		return orderedProjects.stream().map(this::toDTO).collect(Collectors.toList());
	}

}