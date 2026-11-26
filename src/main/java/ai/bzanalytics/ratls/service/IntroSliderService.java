package ai.bzanalytics.ratls.service;

import ai.bzanalytics.ratls.dto.IntroSliderDTO;

import java.util.List;
import java.util.Optional;

public interface IntroSliderService {

	public IntroSliderDTO createIntroSlider(IntroSliderDTO introSliderDTO);

	public List<IntroSliderDTO> getAllIntroSliders();

	public Optional<IntroSliderDTO> getIntroSliderById(Long id);

	public IntroSliderDTO updateIntroSlider(Long id, IntroSliderDTO updatedIntroSliderDTO);

	public void deleteIntroSlider(Long id);

}
