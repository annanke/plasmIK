package ikifp.plasmik.services;

import java.util.Collection;

import org.hibernate.Transaction;

import ikifp.plasmik.model.User;
import ikifp.plasmik.persistence.DatabaseConnector;

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

	public User findUserByLogin(String login) {
		Collection<User> usersList = connector.getSession().createCriteria(User.class).list();
		User foundUser = null;
		for (User user : usersList) {
			if (user.getLogin()!=null && user.getLogin().equals(login)) {
				foundUser=user;
			}
		}
		return foundUser;
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
