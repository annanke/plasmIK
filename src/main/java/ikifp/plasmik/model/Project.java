package ikifp.plasmik.model;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
@Table(name="projects")
public class Project {
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private long id;
	
	@Column(unique=true)
	@Size(max = 100)
	@NotNull
	private String projectName;
	
	@ManyToOne
	@JoinColumn(name="user_id")
	private User project;

}
