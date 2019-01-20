package ikifp.plasmik.services;

import java.util.Collection;

import org.hibernate.Transaction;

import ikifp.plasmik.model.User;
import ikifp.plasmik.persistence.DatabaseConnector;
import ikifp.plasmik.persistence.dto.UserDto;

public class UserService {
	
	DatabaseConnector connector;
	
	public UserService() {
		connector = DatabaseConnector.getInstance();
	}

	public Collection<User> getAll() {
		 return connector.getSession().createCriteria(User.class).list();
	}

	public void addUser(User user) {
		Transaction transaction = connector.getSession().beginTransaction();
		connector.getSession().save(user);
		transaction.commit();
	}

	public UserDto findUserByLogin(String login) {
		Collection<User> usersList = connector.getSession().createCriteria(User.class).list();
		for (User user : usersList) {
			if (user.getLogin()!=null && user.getLogin().equals(login)) {
				return new UserDto(user);
			}
		}
		return null;
	}
	
	public User findUserByEmail(String email) {
		Collection<User> usersList = connector.getSession().createCriteria(User.class).list();
		User foundUser = null;
		for (User user : usersList) {
			if (user.getEmail().equals(email)) {
				foundUser=user;
			}
		}
		return foundUser;
	}
	
	

}
