package ikifp.plasmik.persistence.dto;

import ikifp.plasmik.model.User;

public class UserDto {
	
	private long id;
	
	private String name;
	
/*	private String login;*/
	
	private String email;
	
	private boolean isadmin;
	
	public UserDto(User user) {
		this.setId(user.getId());
		this.setEmail(user.getEmail());
		this.setName(user.getName());
		//this.setLogin(user.getLogin());
		this.setIsadmin(user.getIsadmin());
	}

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

/*	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}*/

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public boolean getIsadmin() {
		return isadmin;
	}

	public void setIsadmin(boolean isadmin) {
		this.isadmin = isadmin;
	}
}
