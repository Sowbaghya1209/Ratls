package ai.bzanalytics.ratls;

import ai.bzanalytics.ratls.controller.BlogController;
import ai.bzanalytics.ratls.dto.BlogDTO;
import ai.bzanalytics.ratls.dto.SearchRequest;
import ai.bzanalytics.ratls.service.BlogService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class BlogControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@Mock
	private BlogService blogService;

	@InjectMocks
	private BlogController blogController;

	@BeforeEach
	public void setUp() {
		MockitoAnnotations.openMocks(this);
		mockMvc = MockMvcBuilders.standaloneSetup(blogController).build();
	}

	@Test
	public void testCreateBlog() throws Exception {
		BlogDTO blogDTO = new BlogDTO();
		blogDTO.setTitle("Test Blog");
		blogDTO.setContent("Test Content");

		when(blogService.createBlog(any(BlogDTO.class))).thenReturn(blogDTO);

		mockMvc
			.perform(multipart("/api/blogs").file("file", "test-file".getBytes())
				.param("title", "Test Blog")
				.param("content", "Test Content"))
			.andExpect(status().isCreated())
			.andExpect(jsonPath("$.title").value("Test Blog"))
			.andExpect(jsonPath("$.content").value("Test Content"));

		verify(blogService, times(1)).createBlog(any(BlogDTO.class));
	}

	@Test
	public void testGetAllBlogs() throws Exception {
		BlogDTO blogDTO1 = new BlogDTO();
		blogDTO1.setTitle("Test Blog 1");
		blogDTO1.setContent("Test Content 1");

		BlogDTO blogDTO2 = new BlogDTO();
		blogDTO2.setTitle("Test Blog 2");
		blogDTO2.setContent("Test Content 2");

		when(blogService.getAllBlogs()).thenReturn(Arrays.asList(blogDTO1, blogDTO2));

		mockMvc.perform(get("/api/blogs"))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$[0].title").value("Test Blog 1"))
			.andExpect(jsonPath("$[1].title").value("Test Blog 2"));

		verify(blogService, times(1)).getAllBlogs();
	}

	@Test
	public void testGetBlogById() throws Exception {
		BlogDTO blogDTO = new BlogDTO();
		blogDTO.setTitle("Test Blog");
		blogDTO.setContent("Test Content");

		when(blogService.getBlogById(1L)).thenReturn(Optional.of(blogDTO));

		mockMvc.perform(get("/api/blogs/{id}", 1L))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.title").value("Test Blog"))
			.andExpect(jsonPath("$.content").value("Test Content"));

		verify(blogService, times(1)).getBlogById(1L);
	}

	@Test
	public void testUpdateBlog() throws Exception {
		BlogDTO blogDTO = new BlogDTO();
		blogDTO.setId(1L);
		blogDTO.setTitle("Updated Blog");
		blogDTO.setContent("Updated Content");

		when(blogService.updateBlog(1L, blogDTO)).thenReturn(blogDTO);

		mockMvc
			.perform(put("/api/blogs/{id}", 1L).contentType(MediaType.MULTIPART_FORM_DATA_VALUE)
				.param("title", "Updated Blog")
				.param("content", "Updated Content"))
			.andExpect(status().isOk());

		verify(blogService, times(1)).updateBlog(1L, blogDTO);
	}

	@Test
	public void testDeleteBlog() throws Exception {
		doNothing().when(blogService).deleteBlog(1L);

		mockMvc.perform(delete("/api/blogs/{id}", 1L));

		verify(blogService, times(1)).deleteBlog(1L);
	}


	@Test
	public void testGetBlogByPermlink() throws Exception {
		BlogDTO blogDTO = new BlogDTO();
		blogDTO.setTitle("Test Blog");
		blogDTO.setContent("Test Content");

		when(blogService.getBlogByPermlink("test-permlink")).thenReturn(Optional.of(blogDTO));

		mockMvc.perform(get("/api/blogs/permlink/{permlink}", "test-permlink"))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.title").value("Test Blog"))
			.andExpect(jsonPath("$.content").value("Test Content"));

		verify(blogService, times(1)).getBlogByPermlink("test-permlink");
	}

}
