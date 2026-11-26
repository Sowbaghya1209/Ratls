package ai.bzanalytics.ratls.repo;

import ai.bzanalytics.ratls.model.ProjectCategory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProjectCategoryRepository extends JpaRepository<ProjectCategory, Long> {

	Optional<ProjectCategory> findByName(String name);

}