package ai.bzanalytics.ratls.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class IntroSlider implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String heading;

	@Column(columnDefinition = "LONGTEXT")
	private String headingDescription;

	private String image1;

	private String image2;

	private String backgroundImage;

	private String backgroundVideo;

	private String selectBackground;

}
