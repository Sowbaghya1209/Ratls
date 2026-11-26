package ai.bzanalytics.ratls.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ImageGalleryDTO {

	private Long id;

	private MultipartFile imageFile;

	private String imageUrl;

}