package ai.bzanalytics.ratls.repo;

import ai.bzanalytics.ratls.model.Admin;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AdminRepository extends JpaRepository<Admin, Long> {

	public Optional<Admin> findByUserName(String userName);

	public Admin findByPassword(String password);

	public Admin findByUserNameAndPassword(String userName, String password);

	public boolean existsByUserName(String userName);

}
