package ai.bzanalytics.ratls.controller;

import ai.bzanalytics.ratls.dto.CategoryDTO;
import ai.bzanalytics.ratls.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
@RequiredArgsConstructor
@CrossOrigin
public class CategoryController {

	private final CategoryService categoryService;

	@GetMapping
	public List<CategoryDTO> getAllCategories() {
		try {
			return categoryService.getAllCategories();
		}
		catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
					"Error while retrieving Categories: " + e.getMessage());
		}
	}

	@DeleteMapping("/{id}")
	public String deleteCategory(@PathVariable Long id) {
		try {
			return categoryService.deleteCategory(id);
		}
		catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
					"Error while deleting Categories: " + e.getMessage());
		}
	}

}
