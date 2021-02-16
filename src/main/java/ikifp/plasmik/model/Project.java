package ikifp.plasmik.model;

import java.util.Set;

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
	private User user;

	@OneToMany(mappedBy="project", cascade=CascadeType.ALL)  
	private Set<Construct> constructs;
	
	@Transient
	private Long numberOfConstructs;
	
	public Long getNumberOfConstructs() {
		return numberOfConstructs;
	}

	public void setNumberOfConstructs(Long numberOfConstructs) {
		this.numberOfConstructs = numberOfConstructs;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getProjectName() {
		return projectName;
	}

	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}
	public Set<Construct> getConstructs() {
		return constructs;
	}

	public void setConstructs(Set<Construct> constructs) {
		this.constructs = constructs;
	}
}
