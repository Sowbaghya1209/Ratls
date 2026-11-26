package ai.bzanalytics.ratls.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TestimonialDTO {

	private Long id;

	private String quote;

	private String name;

	private String designation;

	private String location;

	private String image;

	private MultipartFile file;

}
