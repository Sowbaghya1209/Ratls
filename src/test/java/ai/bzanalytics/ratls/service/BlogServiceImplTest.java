package ai.bzanalytics.ratls.service;
import ai.bzanalytics.ratls.configuration.S3Service;
import ai.bzanalytics.ratls.dto.BlogDTO;
import ai.bzanalytics.ratls.dto.CategoryDTO;
import ai.bzanalytics.ratls.model.Blog;
import ai.bzanalytics.ratls.model.Category;
import ai.bzanalytics.ratls.model.Tag;
import ai.bzanalytics.ratls.repo.BlogRepository;
import ai.bzanalytics.ratls.repo.CategoryRepository;
import ai.bzanalytics.ratls.repo.TagRepository;
import ai.bzanalytics.ratls.service.BlogServiceImpl;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class BlogServiceImplTest {

    @InjectMocks
    private BlogServiceImpl blogService;

    @Mock
    private BlogRepository blogRepository;

    @Mock
    private TagRepository tagRepository;

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private S3Service s3Service;

    private BlogDTO blogDTO;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        blogDTO = new BlogDTO();
        blogDTO.setTitle("Test Blog");
        blogDTO.setAuthor("Author");
        blogDTO.setContent("This is a test content.");
        blogDTO.setPermlink("test-blog");
        blogDTO.setCategory(new CategoryDTO(1L, "Technology"));
        blogDTO.setTags(List.of("Java", "Spring"));
    }

    @Test
    void testCreateBlog_Success() throws Exception {
        when(blogRepository.existsByPermlink(anyString())).thenReturn(false);
        when(s3Service.uploadImage(any())).thenReturn("mockedImageUrl");
        when(s3Service.uploadVideo(any())).thenReturn("mockedVideoUrl");

        Category mockCategory = new Category("Technology");
        when(categoryRepository.findByName(anyString())).thenReturn(Optional.of(mockCategory));

        Tag mockTag = new Tag("Java");
        when(tagRepository.findByName(anyString())).thenReturn(Optional.of(mockTag));

        BlogDTO blogDTO = new BlogDTO();
        blogDTO.setTitle("Test Blog");
        blogDTO.setAuthor("Author");
        blogDTO.setContent("Blog Content");
        blogDTO.setPermlink("test-blog");
        blogDTO.setCategory(new CategoryDTO(1L, "Technology"));
        blogDTO.setTags(List.of("Java"));

        Blog mockBlog = new Blog();
        mockBlog.setTitle(blogDTO.getTitle());
        mockBlog.setAuthor(blogDTO.getAuthor());
        mockBlog.setContent(blogDTO.getContent());
        mockBlog.setPermlink(blogDTO.getPermlink());
        mockBlog.setCategory(mockCategory);
        mockBlog.setTags(List.of(mockTag));

        when(blogRepository.save(any(Blog.class))).thenReturn(mockBlog);
        BlogDTO result = blogService.createBlog(blogDTO);
        assertNotNull(result);
        assertEquals("Test Blog", result.getTitle());
        assertEquals("Author", result.getAuthor());

    }


    @Test
    void testCreateBlog_PermlinkAlreadyExists() {
        when(blogRepository.existsByPermlink(anyString())).thenReturn(true);
        RuntimeException exception = assertThrows(RuntimeException.class, () -> blogService.createBlog(blogDTO));
        assertEquals("Permlink already exists", exception.getMessage());
    }

    @Test
    void testGetAllBlogs() {
        // Arrange
        Blog blog = new Blog();
        blog.setTitle("Test Blog");

        // Initialize the tags list to avoid NullPointerException
        Tag tag = new Tag();
        tag.setName("Test Tag");
        blog.setTags(List.of(tag)); // Make sure the tags are not null

        // Mock the repository method
        when(blogRepository.findAll()).thenReturn(List.of(blog));

        // Act
        List<BlogDTO> result = blogService.getAllBlogs();

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Test Blog", result.get(0).getTitle());
        assertNotNull(result.get(0).getTags());
        assertEquals(1, result.get(0).getTags().size());
        assertEquals("Test Tag", result.get(0).getTags().get(0));
    }
    @Test
    void testGetBlogById_Success() {
        Blog mockBlog = new Blog();
        mockBlog.setTitle("Test Blog");
        mockBlog.setTags(List.of(new Tag("Java")));
        when(blogRepository.findById(1L)).thenReturn(Optional.of(mockBlog));
        Optional<BlogDTO> result = blogService.getBlogById(1L);
        assertTrue(result.isPresent());
        assertEquals("Test Blog", result.get().getTitle());
        assertEquals(1, result.get().getTags().size());
        assertEquals("Java", result.get().getTags().get(0));
    }


    @Test
    void testGetBlogById_NotFound() {
        when(blogRepository.findById(anyLong())).thenReturn(Optional.empty());
        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> blogService.getBlogById(1L));
        assertEquals("Blog not found", exception.getMessage());
    }

    @Test
    void testUpdateBlog_Success() throws Exception {
        Blog existingBlog = new Blog();
        existingBlog.setId(1L);
        existingBlog.setTitle("Old Title");
        when(blogRepository.findById(1L)).thenReturn(Optional.of(existingBlog));

        when(s3Service.uploadImage(any())).thenReturn("newImageUrl");
        when(s3Service.uploadVideo(any())).thenReturn("newVideoUrl");

        Category mockCategory = new Category("Technology");
        when(categoryRepository.findByName(anyString())).thenReturn(Optional.of(mockCategory));

        Tag mockTag = new Tag("Java");
        when(tagRepository.findByName(anyString())).thenReturn(Optional.of(mockTag));

        existingBlog.setTitle(blogDTO.getTitle());
        existingBlog.setContent(blogDTO.getContent());
        existingBlog.setTags(List.of(mockTag));
        existingBlog.setCategory(mockCategory);
        existingBlog.setPermlink(blogDTO.getPermlink());

        when(blogRepository.save(any(Blog.class))).thenReturn(existingBlog);
        BlogDTO result = blogService.updateBlog(1L, blogDTO);
        assertNotNull(result);
        assertEquals("Test Blog", result.getTitle());
        assertEquals("Java", result.getTags().get(0));
    }

    @Test
    void testDeleteBlog_Success() {
        when(blogRepository.existsById(1L)).thenReturn(true);
        blogService.deleteBlog(1L);
        verify(blogRepository, times(1)).deleteById(1L);
    }

    @Test
    void testDeleteBlog_NotFound() {
        when(blogRepository.existsById(1L)).thenReturn(false);
        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> blogService.deleteBlog(1L));
        assertEquals(" Blogs not found", exception.getMessage());
    }

    @Test
    void testSearchBlogs_Success() {
        Blog mockBlog = new Blog();
        mockBlog.setTitle("Test Blog");
        mockBlog.setTags(List.of(new Tag("Java")));
        when(blogRepository.searchBlogsByTitle(anyString())).thenReturn(List.of(mockBlog));
        List<BlogDTO> result = blogService.searchBlogs("Test");
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Test Blog", result.get(0).getTitle());
        assertEquals(1, result.get(0).getTags().size());
        assertEquals("Java", result.get(0).getTags().get(0));
    }


    @Test
    void testSearchBlogs_NotFound() {
        when(blogRepository.searchBlogsByTitle(anyString())).thenReturn(List.of());
        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> blogService.searchBlogs("Test"));
        assertEquals(" No blogs found for input: Test", exception.getMessage());
    }
}
