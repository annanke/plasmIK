package ikifp.plasmik.model;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
@Table(name="users")
public class User {
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private long id;
	
	@Column
	@Size(max = 100)
	@NotNull
	private String name;
	
	@Column
	@Size(max = 100)
	@NotNull
	private String login;
	
	@Column
	@Size(max = 200)
	@NotNull
	private String email;
	
	@Column
	@NotNull
	private String password;
	
	@Column
	private boolean isadmin;

	//Gettery i settery:
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}


	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public boolean getIsadmin() {
		return isadmin;
	}

	public void setIsadmin(boolean isadmin) {
		this.isadmin = isadmin;
	}
	
}
