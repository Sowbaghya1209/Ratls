package ai.bzanalytics.ratls.service;

import ai.bzanalytics.ratls.configuration.S3Service;
import ai.bzanalytics.ratls.dto.BlogDTO;
import ai.bzanalytics.ratls.dto.CategoryDTO;
import ai.bzanalytics.ratls.dto.TagDTO;
import ai.bzanalytics.ratls.model.Blog;
import ai.bzanalytics.ratls.model.Category;
import ai.bzanalytics.ratls.model.Tag;
import ai.bzanalytics.ratls.repo.BlogRepository;
import ai.bzanalytics.ratls.repo.CategoryRepository;
import ai.bzanalytics.ratls.repo.TagRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BlogServiceImpl implements BlogService {

	private final BlogRepository blogRepository;

	private final TagRepository tagRepository;

	private final CategoryRepository categoryRepository;

	private final S3Service s3Service;

	private BlogDTO convertToDTO(Blog blog) {

		List<TagDTO> tagDTOs = blog.getTags()
			.stream()
			.map(tag -> new TagDTO(tag.getId(), tag.getName()))
			.collect(Collectors.toList());
		CategoryDTO categoryDTO = null;
		if (blog.getCategory() != null) {
			categoryDTO = new CategoryDTO(blog.getCategory().getId(), blog.getCategory().getName());
		}

		return new BlogDTO(blog.getId(), blog.getTitle(), blog.getImage(), blog.getVideo(), blog.getAuthor(),
				blog.getPermlink(), categoryDTO, blog.getContent(), blog.getDate(),
				tagDTOs.stream().map(TagDTO::getName).collect(Collectors.toList()));
	}

	public Blog convertToEntity(BlogDTO blogDTO, String imageUrl, String videoUrl) {
		List<Tag> tags = blogDTO.getTags().stream().map(tagName -> {

			return tagRepository.findByName(tagName).orElseGet(() -> {
				Tag newTag = new Tag();

				newTag.setName(tagName);
				tagRepository.save(newTag);
				return newTag;
			});
		}).collect(Collectors.toList());
		Category category = categoryRepository.findByName(blogDTO.getCategory().getName()).orElseGet(() -> {
			Category newCategory = new Category(blogDTO.getCategory().getName());
			categoryRepository.save(newCategory);
			return newCategory;
		});

		Blog blog = new Blog();
		blog.setTitle(blogDTO.getTitle());
		blog.setImage(imageUrl);
		blog.setAuthor(blogDTO.getAuthor());
		blog.setContent(blogDTO.getContent());
		blog.setCategory(category);
		blog.setDate(blogDTO.getDate());
		blog.setTags(tags);
		blog.setPermlink(blogDTO.getPermlink());
		blog.setVideo(videoUrl);

		return blogRepository.save(blog);
	}

	public BlogDTO createBlog(BlogDTO blogDTO) {
		String videoUrl = null;

		String imageUrl = null;
		if (blogDTO.getPermlink() != null && blogRepository.existsByPermlink(blogDTO.getPermlink())) {
			throw new RuntimeException("Permlink already exists");
		}

		try {
			if (blogDTO.getFile() != null) {
				imageUrl = s3Service.uploadImage(blogDTO.getFile());
			}

			if (blogDTO.getVideoFile() != null) {
				videoUrl = s3Service.uploadVideo(blogDTO.getVideoFile());
			}
			Blog blog = convertToEntity(blogDTO, imageUrl, videoUrl);
			blog.setDate(LocalDateTime.now());
			Blog savedBlog = blogRepository.save(blog);
			return convertToDTO(savedBlog);
		}
		catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	public List<BlogDTO> getAllBlogs() {
		List<Blog> blogs = blogRepository.findAll();
		return blogs.stream().map(this::convertToDTO).collect(Collectors.toList());
	}

	public Optional<BlogDTO> getBlogById(Long id) {
		Blog blog = blogRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Blog not found"));
		return Optional.of(convertToDTO(blog));
	}

	public BlogDTO updateBlog(Long id, BlogDTO blogDTO) {

		Blog existingBlog = blogRepository.findById(id)
			.orElseThrow(() -> new EntityNotFoundException("Blog not found"));

		existingBlog.setTitle(blogDTO.getTitle());
		existingBlog.setAuthor(blogDTO.getAuthor());
		existingBlog.setContent(blogDTO.getContent());


		if (blogDTO.getPermlink() != null && existingBlog.getPermlink() != null
				&& !existingBlog.getPermlink().equals(blogDTO.getPermlink())) {

			Optional<Blog> existingPermlinkBlog = blogRepository.findByPermlink(blogDTO.getPermlink());
			if (existingPermlinkBlog.isPresent()) {
				throw new RuntimeException("Permalink already exists: " + blogDTO.getPermlink());
			}
		}
		existingBlog.setPermlink(blogDTO.getPermlink());

		try {
			if (blogDTO.getFile() != null) {
				String imageUrl = s3Service.uploadImage(blogDTO.getFile());
				existingBlog.setImage(imageUrl);
			}
		}
		catch (IOException e) {
			throw new RuntimeException("Failed to upload image", e);
		}

		try {
			if (blogDTO.getVideoFile() != null) {
				String videoUrl = s3Service.uploadVideo(blogDTO.getVideoFile());
				existingBlog.setVideo(videoUrl);
			}

		}
		catch (IOException e) {
			throw new RuntimeException("Failed to upload video", e);
		}

		Category category = categoryRepository.findByName(blogDTO.getCategory().getName()).orElseGet(() -> {
			Category newCategory = new Category(blogDTO.getCategory().getName());
			categoryRepository.save(newCategory);
			return newCategory;
		});
		existingBlog.setCategory(category);

		if (blogDTO.getTags() != null) {
			List<Tag> tags = blogDTO.getTags()
				.stream()
				.map(tagName -> tagRepository.findByName(tagName).orElseGet(() -> {
					Tag newTag = new Tag();
					newTag.setName(tagName);
					return tagRepository.save(newTag);
				}))
				.collect(Collectors.toList());
			existingBlog.setTags(tags);
		}

		Blog updatedBlog = blogRepository.save(existingBlog);

		return convertToDTO(updatedBlog);
	}

	// Delete a blog post
	public void deleteBlog(Long id) {

		if (blogRepository.existsById(id)) {
			blogRepository.deleteById(id);
		}
		else {
			throw new EntityNotFoundException(" Blogs not found");
		}
	}

	public List<BlogDTO> getBlogsByTagId(Long tagId) {
		List<Blog> blogs = blogRepository.findBlogsByTagId(tagId);
		if (blogs == null || blogs.isEmpty()) {
			throw new EntityNotFoundException(" No blogs found for category id: " + tagId);
		}
		return blogs.stream().map(this::convertToDTO).collect(Collectors.toList());
	}

	public List<BlogDTO> getBlogsByCategoryId(Long categoryId) {
		List<Blog> blogs = blogRepository.findBlogsByCategoryId(categoryId);

		if (blogs == null || blogs.isEmpty()) {
			throw new EntityNotFoundException(" No blogs found for category id: " + categoryId);
		}

		return blogs.stream().map(this::convertToDTO).collect(Collectors.toList());
	}

	public List<BlogDTO> searchBlogs(String query) {
		List<Blog> blogs = blogRepository.searchBlogsByTitle(query);

		if (blogs == null || blogs.isEmpty()) {
			throw new EntityNotFoundException(" No blogs found for input: " + query);
		}

		return blogs.stream().map(this::convertToDTO).collect(Collectors.toList());
	}

	public Optional<BlogDTO> getBlogByPermlink(String permlink) {
		Blog blog = blogRepository.findByPermlink(permlink)
			.orElseThrow(() -> new RuntimeException(" Blog not found with permalink: " + permlink));
		return Optional.of(convertToDTO(blog));
	}

}
