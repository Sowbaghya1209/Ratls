package ai.bzanalytics.ratls.service;

import ai.bzanalytics.ratls.dto.ProjectCategoryDTO;
import ai.bzanalytics.ratls.repo.ProjectCategoryRepository;
import ai.bzanalytics.ratls.repo.ProjectRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProjectCategoryServiceImpl implements ProjectCategoryService {

	private final ProjectCategoryRepository categoryRepository;

	private final ProjectRepository projectRepository;

	public List<ProjectCategoryDTO> getAllCategories() {
		return categoryRepository.findAll()
			.stream()
			.map(category -> new ProjectCategoryDTO(category.getId(), category.getName()))
			.collect(Collectors.toList());
	}

	public String deleteCategory(Long categoryId) {

		boolean isCategoryUsed = projectRepository.existsByCategoryId(categoryId);
		if (isCategoryUsed) {
			return "Category already used in existing project";

		}
		if (categoryRepository.existsById(categoryId)) {
			categoryRepository.deleteById(categoryId);
			return "Category deleted successfully";
		}
		else {
			throw new EntityNotFoundException("Category not found");
		}

	}

}