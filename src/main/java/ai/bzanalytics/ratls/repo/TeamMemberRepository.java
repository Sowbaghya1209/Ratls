package ai.bzanalytics.ratls.repo;

import ai.bzanalytics.ratls.model.TeamMember;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TeamMemberRepository extends JpaRepository<TeamMember, Long> {

	boolean existsByName(String name);

}
