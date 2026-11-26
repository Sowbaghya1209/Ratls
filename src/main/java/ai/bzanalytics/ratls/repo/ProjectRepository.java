package ai.bzanalytics.ratls.repo;

import ai.bzanalytics.ratls.model.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ProjectRepository extends JpaRepository<Project, Long> {

	@Query("SELECT b FROM Project b WHERE b.category.id = :categoryId")
	List<Project> findProjectByCategoryId(Long categoryId);

	boolean existsByCategoryId(Long categoryId);

	boolean existsByPermlink(String permlink);

	Optional<Project> findByPermlink(String permlink);

	@Query(value = "SELECT p.* FROM project p " +
			"LEFT JOIN project_category c ON p.category_id = c.id " +
			"WHERE (p.title LIKE %?1% " +
			"OR c.name LIKE %?2% " +
			"OR p.location LIKE %?3%) " +
			"ORDER BY p.title LIKE %?1% DESC, c.name LIKE %?2% DESC", nativeQuery = true)
	List<Project> searchProjectsByTitleOrCategory(String titleQuery, String categoryQuery, String locationQuery);



	@Query("SELECT p FROM Project p ORDER BY p.orderNumber ASC")
	List<Project> findAllProjectsOrderedByCustomOrder();

}
