package ai.bzanalytics.ratls.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Project {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String title;

	private String permlink;

	@ManyToOne
	@JoinColumn(name = "category_id")
	private ProjectCategory category;

	@Column(columnDefinition = "LONGTEXT")
	private String description1;

	private String client;

	private String scopeOfWork;

	private String location;

	private LocalDate startDate;

	private LocalDate endDate;

	private String image1;

	private String image2;

	private String image3;

	@Column(columnDefinition = "LONGTEXT")
	private String description2;

	private boolean isOngoing;

	private Double orderNumber = 999.0;

}
