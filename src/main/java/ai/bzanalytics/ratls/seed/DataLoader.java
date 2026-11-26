package ai.bzanalytics.ratls.seed;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import ai.bzanalytics.ratls.model.*;
import ai.bzanalytics.ratls.repo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;

@Component
public class DataLoader {

	private final AdminRepository adminRepository;

	private final TeamMemberRepository teamRepository;

	private final BlogRepository blogRepository;

	private final TagRepository tagRepository;

	private final CategoryRepository categoryRepository;

	private final JdbcTemplate jdbcTemplate;

	private final BrochureRepository brochureRepository;

	private final IntroSliderRepository introSliderRepository;

	private final ProjectRepository projectRepository;

	private final ProjectCategoryRepository projectCategoryRepository;

	private final TestimonialRepository testimonialRepository;

	public DataLoader(AdminRepository adminRepository, TeamMemberRepository teamRepository,
			BlogRepository blogRepository, TagRepository tagRepository, CategoryRepository categoryRepository,
			BrochureRepository brochureRepository, IntroSliderRepository introSliderRepository,
			ProjectRepository projectRepository, ProjectCategoryRepository projectCategoryRepository,
			TestimonialRepository testimonialRepository, JdbcTemplate jdbcTemplate) {
		this.adminRepository = adminRepository;
		this.teamRepository = teamRepository;
		this.blogRepository = blogRepository;
		this.tagRepository = tagRepository;
		this.categoryRepository = categoryRepository;
		this.jdbcTemplate = jdbcTemplate;
		this.brochureRepository = brochureRepository;
		this.introSliderRepository = introSliderRepository;
		this.projectCategoryRepository = projectCategoryRepository;
		this.projectRepository = projectRepository;
		this.testimonialRepository = testimonialRepository;
	}

	@PostConstruct
	public void init() {
		createFullTextIndex();
		Admin post1 = new Admin("admin", "admin123");
		Admin post2 = new Admin("admin1", "admin123");
		if (adminRepository.count() == 0) {
			adminRepository.save(post1);
			adminRepository.save(post2);
		}
		if (teamRepository.count() == 0) {
			// Add sample team members
			teamRepository.save(new TeamMember("John Doe", "CEO", "John is the CEO with over 20 years of experience.",
					"https://example.com/john.jpg"));
			teamRepository.save(new TeamMember("Jane Smith", "CTO",
					"Jane leads the technical team with a background in software engineering.",
					"https://example.com/jane.jpg"));
			teamRepository.save(new TeamMember("Alice Johnson", "COO",
					"Alice is responsible for the daily operations and strategy of the company.",
					"https://example.com/alice.jpg"));
			teamRepository.save(new TeamMember("Bob Brown", "CMO",
					"Bob handles all marketing strategies and branding for the company.",
					"https://example.com/bob.jpg"));

			System.out.println("Database seeded with initial team members.");
		}
		if (blogRepository.count() == 0) {
			// Seed the database with initial blog data
			Tag tag1 = tagRepository.findByName("Spring Boot").orElseGet(() -> {
				Tag newTag = new Tag();
				newTag.setName("Spring Boot");
				tagRepository.save(newTag);
				return newTag;
			});

			Tag tag2 = tagRepository.findByName("Java").orElseGet(() -> {
				Tag newTag = new Tag();
				newTag.setName("Java");
				tagRepository.save(newTag);
				return newTag;
			});

			// Seed Categories
			Category category1 = categoryRepository.findByName("Technology").orElseGet(() -> {
				Category newCategory = new Category("Technology");
				categoryRepository.save(newCategory);
				return newCategory;
			});

			Category category2 = categoryRepository.findByName("Programming").orElseGet(() -> {
				Category newCategory = new Category("Programming");
				categoryRepository.save(newCategory);
				return newCategory;
			});

			// Step 2: Associate tags with the blog
			List<Tag> tags = List.of(tag1, tag2);

			blogRepository.save(new Blog("The Power of Spring Boot", "www.example.com", "spring_boot_image.png",
					"John Doe", LocalDateTime.now(),
					"Spring Boot makes it easy to create stand-alone, production-grade Spring-based applications.",
					category1, tags, "https:url"));

			blogRepository.save(new Blog("Exploring JPA and Hibernate", "www.example1.com", "jpa_hibernate_image.png",
					"Jane Smith", LocalDateTime.now().minusDays(1),
					"JPA and Hibernate are essential tools for building database-driven applications in Java.",
					category1, tags, "https:url1"));

			blogRepository.save(new Blog("A Guide to REST APIs", "www.example2.com", "rest_api_image.png", "James Lee",
					LocalDateTime.now().minusDays(2),
					"REST APIs are an integral part of modern web development, and this guide will walk you through them.",
					category2, tags, "https:url"));

			System.out.println("Database seeded with initial blog data.");
		}

		/*
		 * if (brochureRepository.count() == 0) { Brochure brochure1 = new Brochure(null,
		 * "https://example.com/brochure1.pdf"); Brochure brochure2 = new Brochure(null,
		 * "https://example.com/brochure2.pdf"); Brochure brochure3 = new Brochure(null,
		 * "https://example.com/brochure3.pdf");
		 * brochureRepository.saveAll(Arrays.asList(brochure1, brochure2, brochure3));
		 * System.out.println("Brochures have been seeded."); } else {
		 * System.out.println("Brochures are already seeded."); }
		 */

		if (introSliderRepository.count() == 0) {
			IntroSlider slider1 = new IntroSlider(null, "Welcome to Our Website", "Explore our services and products.",
					"image1.jpg", "image2.jpg", "background1.jpg", // backgroundImage
					"backgroundVideo1.mp4", // backgroundVideo
					"image" // selectBackground (could be 'image' or 'video')
			);

			IntroSlider slider2 = new IntroSlider(null, "Our Vision", "We are committed to delivering the best.",
					"image3.jpg", "image4.jpg", "background2.jpg", // backgroundImage
					"backgroundVideo2.mp4", // backgroundVideo
					"image" // selectBackground
			);

			IntroSlider slider3 = new IntroSlider(null, "Innovative Solutions", "Let us help you innovate and grow.",
					"image5.jpg", "image6.jpg", "background3.jpg", // backgroundImage
					"backgroundVideo3.mp4", // backgroundVideo
					"video" // selectBackground
			);

			introSliderRepository.saveAll(Arrays.asList(slider1, slider2, slider3));

			System.out.println("IntroSliders have been seeded.");
		}
		else {
			System.out.println("IntroSliders are already seeded.");
		}
		if (projectRepository.count() == 0) {
			ProjectCategory projectCategory1 = projectCategoryRepository.findByName("Web Development")
				.orElseGet(() -> projectCategoryRepository.save(new ProjectCategory("Web Development")));
			ProjectCategory projectCategory2 = projectCategoryRepository.findByName("Mobile App Development")
				.orElseGet(() -> projectCategoryRepository.save(new ProjectCategory("Mobile App Development")));
			Project project1 = new Project(null, "Project 1", "project-1", projectCategory1, "Description 1",
					"Client 1", "Scope 1", "Location 1", LocalDate.of(2023, 1, 1), LocalDate.of(2023, 12, 31),
					"image1.jpg", "image2.jpg", "image3.jpg", "Description 2", false, 1.0);

			Project project2 = new Project(null, "Project 2", "project-2", projectCategory2, "Description 1",
					"Client 2", "Scope 2", "Location 2", LocalDate.of(2023, 3, 1), LocalDate.of(2023, 9, 30),
					"image4.jpg", "image5.jpg", "image6.jpg", "Description 2", true, 2.0);

			projectRepository.saveAll(Arrays.asList(project1, project2));

			System.out.println("Projects have been seeded.");
		}
		else {
			System.out.println("Projects are already seeded.");
		}

		if (testimonialRepository.count() == 0) {

			Testimonial testimonial1 = new Testimonial(null,
					"This product helped our business grow exponentially. Highly recommend!", "John Doe", "CEO",
					"New York, USA", "path/to/image1.jpg");
			Testimonial testimonial2 = new Testimonial(null,
					"Amazing customer service and outstanding quality. Truly a game-changer!", "Jane Smith", "CTO",
					"San Francisco, USA", "path/to/image2.jpg");
			Testimonial testimonial3 = new Testimonial(null,
					"We saw results within the first month of using their service. Top-notch!", "Emily Johnson",
					"Marketing Manager", "London, UK", "path/to/image3.jpg");

			testimonialRepository.saveAll(java.util.Arrays.asList(testimonial1, testimonial2, testimonial3));

			System.out.println("Testimonials have been seeded.");
		}
		else {
			System.out.println("Testimonials are already seeded.");
		}

	}

	private void createFullTextIndex() {
		// Queries to check if indexes already exist
		String checkProjectIndexSql = "SHOW INDEX FROM project WHERE Key_name = 'title'";
		String checkProjectCategoryIndexSql = "SHOW INDEX FROM project_category WHERE Key_name = 'name'";
		String checkProjectLocationIndexSql = "SHOW INDEX FROM project WHERE Key_name = 'location'";
		String checkBlogIndexSql = "SHOW INDEX FROM blog WHERE Key_name = 'title'";  // Add for blog

		// Execute the check queries using JdbcTemplate
		List<Map<String, Object>> projectIndexResult = jdbcTemplate.queryForList(checkProjectIndexSql);
		List<Map<String, Object>> projectCategoryIndexResult = jdbcTemplate.queryForList(checkProjectCategoryIndexSql);
		List<Map<String, Object>> projectLocationIndexResult = jdbcTemplate.queryForList(checkProjectLocationIndexSql);
		List<Map<String, Object>> blogIndexResult = jdbcTemplate.queryForList(checkBlogIndexSql);  // Add for blog

		// If the 'title' index doesn't exist in 'project' table, create it
		if (projectIndexResult.isEmpty()) {
			String createProjectIndexSql = "ALTER TABLE project ADD FULLTEXT(title)";
			jdbcTemplate.execute(createProjectIndexSql);
			System.out.println("FULLTEXT index created on 'title' column in 'project' table.");
		} else {
			System.out.println("FULLTEXT index already exists on 'title' column in 'project' table.");
		}

		// If the 'name' index doesn't exist in 'project_category' table, create it
		if (projectCategoryIndexResult.isEmpty()) {
			String createProjectCategoryIndexSql = "ALTER TABLE project_category ADD FULLTEXT(name)";
			jdbcTemplate.execute(createProjectCategoryIndexSql);
			System.out.println("FULLTEXT index created on 'name' column in 'project_category' table.");
		} else {
			System.out.println("FULLTEXT index already exists on 'name' column in 'project_category' table.");
		}

		// If the 'location' index doesn't exist in 'project' table, create it
		if (projectLocationIndexResult.isEmpty()) {
			String createProjectLocationIndexSql = "ALTER TABLE project ADD FULLTEXT(location)";
			jdbcTemplate.execute(createProjectLocationIndexSql);
			System.out.println("FULLTEXT index created on 'location' column in 'project' table.");
		} else {
			System.out.println("FULLTEXT index already exists on 'location' column in 'project' table.");
		}

		// If the 'title' index doesn't exist in 'blog' table, create it
		if (blogIndexResult.isEmpty()) {
			String createBlogIndexSql = "ALTER TABLE blog ADD FULLTEXT(title)";
			jdbcTemplate.execute(createBlogIndexSql);
			System.out.println("FULLTEXT index created on 'title' column in 'blog' table.");
		} else {
			System.out.println("FULLTEXT index already exists on 'title' column in 'blog' table.");
		}
	}

}