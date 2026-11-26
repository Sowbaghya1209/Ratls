package ai.bzanalytics.ratls.service;

import ai.bzanalytics.ratls.dto.CategoryDTO;

import java.util.List;

public interface CategoryService {

	public List<CategoryDTO> getAllCategories();

	public String deleteCategory(Long categoryId);

}
