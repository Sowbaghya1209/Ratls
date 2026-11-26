package ai.bzanalytics.ratls.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Blog {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String title;

	private String permlink;

	private String image;

	private String author;

	private LocalDateTime date;

	@Column(columnDefinition = "LONGTEXT")
	private String content;

	private String video;

	@ManyToOne
	@JoinColumn(name = "category_id")
	private Category category;

	@ManyToMany
	@JoinTable(name = "blog_tags", joinColumns = @JoinColumn(name = "blog_id"),
			inverseJoinColumns = @JoinColumn(name = "tag_id"))
	private List<Tag> tags;

	public Blog(String title, String permlink, String image, String author, LocalDateTime date, String content,
			Category category, List<Tag> tags, String video) {
		this.title = title;
		this.permlink = permlink;
		this.image = image;
		this.author = author;
		this.date = date;
		this.content = content;
		this.category = category;
		this.tags = tags;
		this.video = video;
	}

}
