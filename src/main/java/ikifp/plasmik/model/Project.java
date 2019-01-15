package ikifp.plasmik.model;

import javax.persistence.*;

@Entity
@Table(name="projects")
public class Project {
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private long id;
	
	@Column
	private String projectName;
	
	@ManyToOne
	@JoinColumn(name="user_id")
	private User project;

}
