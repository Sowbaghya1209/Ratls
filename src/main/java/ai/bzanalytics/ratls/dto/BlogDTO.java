package ai.bzanalytics.ratls.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BlogDTO {

	private Long id;

	private String title;

	private MultipartFile file;

	private MultipartFile videoFile;

	private String image;

	private String video;

	private String author;

	private String permlink;

	private CategoryDTO category;

	private String content;

	private LocalDateTime date;

	private List<String> tags;

	public BlogDTO(Long id, String title, String image, String video, String author, String permlink,
			CategoryDTO category, String content, LocalDateTime date, List<String> tags) {
		this.id = id;
		this.title = title;
		this.image = image;
		this.video = video;
		this.author = author;
		this.permlink = permlink;
		this.category = category;
		this.content = content;
		this.date = date;
		this.tags = tags;
	}

}
