package ai.bzanalytics.ratls.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProjectCategoryDTO {

	private Long id;

	private String name;

	public ProjectCategoryDTO(String category) {
		this.name = category;
	}

}
