package ai.bzanalytics.ratls.service;

import ai.bzanalytics.ratls.dto.ImageGalleryDTO;

import java.util.List;
import java.util.Optional;

public interface ImageGalleryService {

	public ImageGalleryDTO createImage(ImageGalleryDTO imageGalleryDTO);

	public List<ImageGalleryDTO> getAllImages();

	public Optional<ImageGalleryDTO> getImageById(Long id);

	public void deleteImage(Long id);

}
