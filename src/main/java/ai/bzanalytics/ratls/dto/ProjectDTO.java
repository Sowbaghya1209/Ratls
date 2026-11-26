package ai.bzanalytics.ratls.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProjectDTO {

	private Long id;

	private String title;

	private ProjectCategoryDTO category;

	private String description1;

	private String client;

	private String scopeOfWork;

	private String location;

	private LocalDate startDate;

	private LocalDate endDate;

	private String permlink;

	private String image1;

	private String image2;

	private String image3;

	private String description2;

	private MultipartFile file1;

	private MultipartFile file2;

	private MultipartFile file3;

	private boolean isOngoing;

	private Double orderNumber;

}
