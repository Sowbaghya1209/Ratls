package ai.bzanalytics.ratls.controller;

import ai.bzanalytics.ratls.dto.ImageGalleryDTO;
import ai.bzanalytics.ratls.service.ImageGalleryServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api/image-gallery")
@RequiredArgsConstructor
@CrossOrigin
public class ImageGalleryController {

	private final ImageGalleryServiceImpl imageGalleryService;

	@GetMapping("/{id}")
	public ImageGalleryDTO getImageById(@PathVariable Long id) {
		try {
			return imageGalleryService.getImageById(id)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Image not found with ID: " + id));
		}
		catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
					"Error while retrieving image: " + e.getMessage());
		}
	}

	@GetMapping
	public List<ImageGalleryDTO> getAllImages() {
		try {
			return imageGalleryService.getAllImages();
		}
		catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
					"Error while retrieving all images: " + e.getMessage());
		}
	}

	@PostMapping(consumes = "multipart/form-data")
	@ResponseStatus(HttpStatus.CREATED)
	public ImageGalleryDTO createImage(@ModelAttribute ImageGalleryDTO imageGalleryDTO) {
		try {
			return imageGalleryService.createImage(imageGalleryDTO);
		}
		catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Error while creating image: " + e.getMessage());
		}
	}

	@DeleteMapping("/{id}")
	public String deleteImage(@PathVariable Long id) {
		try {
			imageGalleryService.deleteImage(id);
			return "Image deleted successfully";
		}
		catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Error while deleting image: " + e.getMessage());
		}
	}

}
