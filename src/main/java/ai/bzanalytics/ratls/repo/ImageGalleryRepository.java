package ai.bzanalytics.ratls.repo;

import ai.bzanalytics.ratls.model.ClientLogo;
import ai.bzanalytics.ratls.model.ImageGallery;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ImageGalleryRepository extends JpaRepository<ImageGallery, Long> {

}
