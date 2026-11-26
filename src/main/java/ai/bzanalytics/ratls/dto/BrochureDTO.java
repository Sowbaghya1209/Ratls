package ai.bzanalytics.ratls.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BrochureDTO {

	private String url;

	private MultipartFile file;

	public BrochureDTO(String url) {
		this.url = url;
	}

}