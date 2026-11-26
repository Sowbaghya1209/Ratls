package ai.bzanalytics.ratls.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TeamMember {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String name;

	private String role;

	@Column(columnDefinition = "LONGTEXT")
	private String bio;

	private String imageUrl;

	public TeamMember(String name, String role, String bio, String imageUrl) {
		this.name = name;
		this.role = role;
		this.bio = bio;
		this.imageUrl = imageUrl;
	}

}
