package ikifp.plasmik.services;

import java.util.Collection;

import ikifp.plasmik.model.User;
import ikifp.plasmik.persistence.DatabaseConnector;

public class LoginService {
	
	DatabaseConnector connector;
	
	public LoginService() {
		 connector= DatabaseConnector.getInstance();
	
	}

	public boolean confirmLogin(String login) {
		boolean isLoginConfirmed = false;
		UserService userService = new UserService();
		User foundUser = userService.findUserByLogin(login);
		
		if (foundUser!=null && foundUser.getLogin()!=null && foundUser.getLogin().equals(login)) {
			isLoginConfirmed=true;
		}
		return isLoginConfirmed;
	}

	public boolean confirmUserPassword(String login, String password) {
		boolean isPasswordConfirmed = false;
		UserService userService = new UserService();
		User foundUser = userService.findUserByLogin(login);
		if (foundUser!=null) {
			isPasswordConfirmed = foundUser.getPassword().equals(password);
		}
		return isPasswordConfirmed;
	}
}
