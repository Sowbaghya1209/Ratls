package ai.bzanalytics.ratls.service;

import ai.bzanalytics.ratls.configuration.S3Service;
import ai.bzanalytics.ratls.dto.IntroSliderDTO;
import ai.bzanalytics.ratls.exceptions.NotFoundException;
import ai.bzanalytics.ratls.model.IntroSlider;
import ai.bzanalytics.ratls.repo.IntroSliderRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class IntroSliderServiceImpl implements IntroSliderService {

	private final IntroSliderRepository introSliderRepository;

	private final S3Service s3Service;

	private IntroSliderDTO toDTO(IntroSlider introSlider) {
		IntroSliderDTO introSliderDTO = new IntroSliderDTO();
		introSliderDTO.setId(introSlider.getId());
		introSliderDTO.setHeading(introSlider.getHeading());
		introSliderDTO.setImage1(introSlider.getImage1());
		introSliderDTO.setImage2(introSlider.getImage2());
		introSliderDTO.setHeadingDescription(introSlider.getHeadingDescription());
		introSliderDTO.setBackgroundImage(introSlider.getBackgroundImage());
		introSliderDTO.setBackgroundVideo(introSlider.getBackgroundVideo());
		introSliderDTO.setSelectBackground(introSlider.getSelectBackground());

		return introSliderDTO;
	}

	private IntroSlider toEntity(IntroSliderDTO introSliderDTO, String imageUrl1, String imageUrl2,
			String backgroundImageUrl, String backgroundVideoUrl) {
		IntroSlider introSlider = new IntroSlider();
		introSlider.setId(introSliderDTO.getId());
		introSlider.setHeading(introSliderDTO.getHeading());
		introSlider.setImage1(imageUrl1);
		introSlider.setImage2(imageUrl2);
		introSlider.setHeadingDescription(introSliderDTO.getHeadingDescription());
		introSlider.setBackgroundImage(backgroundImageUrl);
		introSlider.setBackgroundVideo(backgroundVideoUrl);
		introSlider.setSelectBackground(introSliderDTO.getSelectBackground());
		return introSlider;
	}

	public IntroSliderDTO createIntroSlider(IntroSliderDTO introSliderDTO) {
		try {
			String imageUrl1 = null;
			String imageUrl2 = null;
			String backgroundImageUrl = null;
			String backgroundVideoUrl = null;
			if (introSliderDTO.getFile1() != null) {
				imageUrl1 = s3Service.uploadImage(introSliderDTO.getFile1());
			}
			if (introSliderDTO.getFile2() != null) {
				imageUrl2 = s3Service.uploadImage(introSliderDTO.getFile2());
			}
			if (introSliderDTO.getBackgroundImageFile() != null) {
				backgroundImageUrl = s3Service.uploadImage(introSliderDTO.getBackgroundImageFile());
			}

			if (introSliderDTO.getBackgroundVideoFile() != null) {
				backgroundVideoUrl = s3Service.uploadVideo(introSliderDTO.getBackgroundVideoFile());
			}
			IntroSlider introSlider = toEntity(introSliderDTO, imageUrl1, imageUrl2, backgroundImageUrl,
					backgroundVideoUrl);
			IntroSlider savedIntroSlider = introSliderRepository.save(introSlider);
			return toDTO(savedIntroSlider);
		}
		catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	public List<IntroSliderDTO> getAllIntroSliders() {
		return introSliderRepository.findAll().stream().map(this::toDTO).collect(Collectors.toList());
	}

	public Optional<IntroSliderDTO> getIntroSliderById(Long id) {
		Optional<IntroSlider> introSlider = introSliderRepository.findById(id);
		return introSlider.map(this::toDTO);
	}

	public IntroSliderDTO updateIntroSlider(Long id, IntroSliderDTO updatedIntroSliderDTO) {
		try {
			IntroSlider existingIntroSlider = introSliderRepository.findById(id)
				.orElseThrow(() -> new NotFoundException("IntroSlider not found with ID: " + id));
			if (updatedIntroSliderDTO.getFile1() != null) {
				String imageUrl1 = s3Service.uploadImage(updatedIntroSliderDTO.getFile1());
				existingIntroSlider.setImage1(imageUrl1);
			}
			if (updatedIntroSliderDTO.getFile2() != null) {
				String imageUrl2 = s3Service.uploadImage(updatedIntroSliderDTO.getFile2());
				existingIntroSlider.setImage2(imageUrl2);
			}
			if (updatedIntroSliderDTO.getBackgroundImageFile() != null) {
				String backgroundImageUrl = s3Service.uploadImage(updatedIntroSliderDTO.getBackgroundImageFile());
				existingIntroSlider.setBackgroundImage(backgroundImageUrl);
			}

			if (updatedIntroSliderDTO.getBackgroundVideoFile() != null) {
				String backgroundVideoUrl = s3Service.uploadVideo(updatedIntroSliderDTO.getBackgroundVideoFile());
				existingIntroSlider.setBackgroundVideo(backgroundVideoUrl);
			}
			existingIntroSlider.setHeading(updatedIntroSliderDTO.getHeading());
			existingIntroSlider.setHeadingDescription(updatedIntroSliderDTO.getHeadingDescription());
			existingIntroSlider.setSelectBackground(updatedIntroSliderDTO.getSelectBackground());

			IntroSlider savedIntroSlider = introSliderRepository.save(existingIntroSlider);
			return toDTO(savedIntroSlider);
		}
		catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	public void deleteIntroSlider(Long id) {

		if (introSliderRepository.existsById(id)) {
			introSliderRepository.deleteById(id);
		}
		else {
			throw new EntityNotFoundException("IntroSlider not found");
		}
	}

}
