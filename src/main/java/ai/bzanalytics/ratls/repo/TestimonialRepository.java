package ai.bzanalytics.ratls.repo;

import ai.bzanalytics.ratls.model.Testimonial;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TestimonialRepository extends JpaRepository<Testimonial, Long> {

}