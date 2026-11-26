package ai.bzanalytics.ratls.controller;

import ai.bzanalytics.ratls.dto.BlogDTO;
import ai.bzanalytics.ratls.dto.SearchRequest;
import ai.bzanalytics.ratls.exceptions.NotFoundException;
import ai.bzanalytics.ratls.service.BlogService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Log4j2
@RestController
@RequestMapping("/api/blogs")
@RequiredArgsConstructor
@CrossOrigin
public class BlogController {

	private final BlogService blogService;

	@PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	@ResponseStatus(HttpStatus.CREATED)
	public BlogDTO createBlog(@ModelAttribute BlogDTO blogDTO) {
		try {
			return blogService.createBlog(blogDTO);
		}
		catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Error while creating a blog: " + e.getMessage());
		}
	}

	@GetMapping
	public List<BlogDTO> getAllBlogs() {
		try {
			List<BlogDTO> blogs = blogService.getAllBlogs();
			if (blogs.isEmpty()) {
				throw new NotFoundException("No blogs found");
			}
			return blogs;
		}
		catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
					"Error while retrieving blogs: " + e.getMessage());
		}
	}

	@GetMapping("/{id}")
	public BlogDTO getBlogById(@PathVariable Long id) {
		try {
			return blogService.getBlogById(id).orElseThrow(() -> new NotFoundException("Blog not found"));
		}
		catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Blog not found with Id: " + id + e.getMessage());
		}
	}

	@PutMapping("/{id}")
	public BlogDTO updateBlog(@PathVariable Long id, @ModelAttribute BlogDTO blogDTO) {
		try {

			return blogService.updateBlog(id, blogDTO);
		}
		catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
					"Error while updating a blog: " + id + e.getMessage());
		}
	}

	@DeleteMapping("/{id}")
	public void deleteBlog(@PathVariable Long id) {
		try {
			blogService.deleteBlog(id);
		}
		catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Blog not found with Id: " + id + e.getMessage());
		}
	}

	@GetMapping("/tag/{id}")
	public List<BlogDTO> getByTagId(@PathVariable Long id) {
		try {
			return blogService.getBlogsByTagId(id);
		}
		catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND,
					"Blog not found with Id: " + id + " " + e.getMessage());
		}
	}

	@GetMapping("/category/{id}")
	public List<BlogDTO> getByCategoryId(@PathVariable Long id) {
		try {
			return blogService.getBlogsByCategoryId(id);
		}
		catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND,
					"Blog not found with Id: " + id + " " + e.getMessage());
		}
	}

	@GetMapping("/search")
	public List<BlogDTO> searchBlogs(@RequestParam String query) {
		try {
			return blogService.searchBlogs(query);

		}
		catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Blog not found " + e.getMessage());
		}
	}

	@GetMapping("/permlink/{permlink}")
	public BlogDTO getBlogByPermlink(@PathVariable String permlink) {
		try {
			return blogService.getBlogByPermlink(permlink)
				.orElseThrow(() -> new RuntimeException("Blog not found with permalink: " + permlink));
		}
		catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Blog not found with permalink: " + permlink, e);
		}
	}

}
