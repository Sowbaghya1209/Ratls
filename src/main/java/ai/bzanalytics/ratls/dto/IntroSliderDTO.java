package ai.bzanalytics.ratls.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class IntroSliderDTO {

	private Long id;

	private String heading;

	private String headingDescription;

	private String image1;

	private String image2;

	private MultipartFile file1;

	private MultipartFile file2;

	private String backgroundImage;

	private String backgroundVideo;

	private String selectBackground;

	private MultipartFile backgroundImageFile;

	private MultipartFile backgroundVideoFile;

}
