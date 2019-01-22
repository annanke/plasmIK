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
		UserDto foundUser = userService.findUserByEmail(login);
		
		if (foundUser!=null && foundUser.getEmail()!=null && foundUser.getEmail().equals(login)) {
			isLoginConfirmed=true;
		}
		return isLoginConfirmed;
	}

	public boolean confirmUserEmailAndPassword(String email, String password) {
		boolean isConfirmed = false;
				
		Collection<User> usersList = connector.getSession().createCriteria(User.class).list();
		for (User user : usersList) {
			if (user.getEmail()!=null && user.getEmail().equals(email) && user.getPassword()!=null && user.getPassword().equals(password)) {
				isConfirmed = true;
			}
		}
		return isConfirmed;
	}
}
