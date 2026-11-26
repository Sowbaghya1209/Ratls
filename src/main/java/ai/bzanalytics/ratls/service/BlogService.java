package ai.bzanalytics.ratls.service;

import ai.bzanalytics.ratls.dto.BlogDTO;

import java.util.List;
import java.util.Optional;

public interface BlogService {

	public BlogDTO createBlog(BlogDTO blogDTO);

	public List<BlogDTO> getAllBlogs();

	public Optional<BlogDTO> getBlogById(Long id);

	public BlogDTO updateBlog(Long id, BlogDTO blogDTO);

	public void deleteBlog(Long id);

	public List<BlogDTO> getBlogsByTagId(Long tagId);

	public List<BlogDTO> getBlogsByCategoryId(Long categoryId);

	public List<BlogDTO> searchBlogs(String query);

	public Optional<BlogDTO> getBlogByPermlink(String permlink);

}
