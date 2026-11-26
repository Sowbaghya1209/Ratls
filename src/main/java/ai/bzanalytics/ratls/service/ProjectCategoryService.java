package ai.bzanalytics.ratls.service;

import ai.bzanalytics.ratls.dto.ProjectCategoryDTO;

import java.util.List;

public interface ProjectCategoryService {

	public List<ProjectCategoryDTO> getAllCategories();

	public String deleteCategory(Long categoryId);

}
