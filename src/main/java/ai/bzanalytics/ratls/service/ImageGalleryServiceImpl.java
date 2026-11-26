package ai.bzanalytics.ratls.service;

import ai.bzanalytics.ratls.dto.ImageGalleryDTO;
import ai.bzanalytics.ratls.model.ImageGallery;
import ai.bzanalytics.ratls.repo.ImageGalleryRepository;
import ai.bzanalytics.ratls.configuration.S3Service;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ImageGalleryServiceImpl implements ImageGalleryService {

	private final ImageGalleryRepository imageGalleryRepository;

	private final S3Service s3Service;

	private ImageGalleryDTO convertToDTO(ImageGallery imageGallery) {
		return new ImageGalleryDTO(imageGallery.getId(), null, imageGallery.getImageUrl());
	}

	private ImageGallery convertToEntity(ImageGalleryDTO imageGalleryDTO, String imageUrl) {
		ImageGallery imageGallery = new ImageGallery();
		imageGallery.setImageUrl(imageUrl);
		return imageGalleryRepository.save(imageGallery);
	}

	@Override
	public ImageGalleryDTO createImage(ImageGalleryDTO imageGalleryDTO) {
		String imageUrl = null;

		try {
			if (imageGalleryDTO.getImageFile() != null) {
				imageUrl = s3Service.uploadImage(imageGalleryDTO.getImageFile());
			}
		}
		catch (IOException e) {
			throw new RuntimeException("Error uploading image", e);
		}

		ImageGallery imageGallery = convertToEntity(imageGalleryDTO, imageUrl);
		imageGalleryRepository.save(imageGallery);
		return convertToDTO(imageGallery);
	}

	@Override
	public List<ImageGalleryDTO> getAllImages() {
		List<ImageGallery> imageGalleries = imageGalleryRepository.findAll();
		return imageGalleries.stream().map(this::convertToDTO).collect(Collectors.toList());
	}

	@Override
	public Optional<ImageGalleryDTO> getImageById(Long id) {
		ImageGallery imageGallery = imageGalleryRepository.findById(id)
			.orElseThrow(() -> new EntityNotFoundException("Image not found"));
		return Optional.of(convertToDTO(imageGallery));
	}

	@Override
	public void deleteImage(Long id) {
		if (imageGalleryRepository.existsById(id)) {
			imageGalleryRepository.deleteById(id);
		}
		else {
			throw new EntityNotFoundException("Image not found");
		}
	}

}
