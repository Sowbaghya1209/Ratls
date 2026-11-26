package ai.bzanalytics.ratls.repo;

import ai.bzanalytics.ratls.model.Blog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BlogRepository extends JpaRepository<Blog, Long> {

	@Query("SELECT b FROM Blog b JOIN b.tags t WHERE t.id = :tagId")
	List<Blog> findBlogsByTagId(@Param("tagId") Long tagId);

	@Query("SELECT b FROM Blog b WHERE b.category.id = :categoryId")
	List<Blog> findBlogsByCategoryId(Long categoryId);

	// Native query for full-text search on the title
	@Query(value = "SELECT * FROM blog b WHERE b.title LIKE %?1% ORDER BY b.title LIKE %?1% DESC", nativeQuery = true)
	List<Blog> searchBlogsByTitle(String query);

	boolean existsByCategoryId(Long categoryId);

	boolean existsByPermlink(String permlink);

	Optional<Blog> findByPermlink(String permlink);

}
