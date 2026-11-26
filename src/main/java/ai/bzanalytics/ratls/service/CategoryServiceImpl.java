package ai.bzanalytics.ratls.service;

import ai.bzanalytics.ratls.dto.CategoryDTO;
import ai.bzanalytics.ratls.model.Category;
import ai.bzanalytics.ratls.repo.BlogRepository;
import ai.bzanalytics.ratls.repo.CategoryRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

	private final CategoryRepository categoryRepository;

	private final BlogRepository blogRepository;

	public List<CategoryDTO> getAllCategories() {
		List<Category> categories = categoryRepository.findAll();
		return categories.stream()
			.map(category -> new CategoryDTO(category.getId(), category.getName()))
			.collect(Collectors.toList());
	}

	public String deleteCategory(Long categoryId) {
		boolean isCategoryUsed = blogRepository.existsByCategoryId(categoryId);
		if (isCategoryUsed) {
			return "Category already used in existing blog";
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