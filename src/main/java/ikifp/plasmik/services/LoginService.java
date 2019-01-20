package ikifp.plasmik.services;

import java.util.Collection;

import ikifp.plasmik.model.User;
import ikifp.plasmik.persistence.DatabaseConnector;
import ikifp.plasmik.persistence.dto.UserDto;

public class LoginService {
	
	DatabaseConnector connector;
	
	public LoginService() {
		 connector= DatabaseConnector.getInstance();
	
	}

	public boolean confirmLogin(String login) {
		boolean isLoginConfirmed = false;
		UserService userService = new UserService();
		UserDto foundUser = userService.findUserByLogin(login);
		
		if (foundUser!=null && foundUser.getLogin()!=null && foundUser.getLogin().equals(login)) {
			isLoginConfirmed=true;
		}
		return isLoginConfirmed;
	}

	public boolean confirmUserLoginAndPassword(String login, String password) {
		boolean isConfirmed = false;
				
		Collection<User> usersList = connector.getSession().createCriteria(User.class).list();
		for (User user : usersList) {
			if (user.getLogin()!=null && user.getLogin().equals(login) && user.getPassword()!=null && user.getPassword().equals(password)) {
				isConfirmed = true;
			}
		}
		return isConfirmed;
	}
}
