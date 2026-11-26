package ai.bzanalytics.ratls.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "admins")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Admin {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;

	@Column(name = "username")
	private String userName;

	@Column(name = "password")

	private String password;

	public Admin(String userName, String password) {

		this.userName = userName;
		this.password = password;
	}

}
