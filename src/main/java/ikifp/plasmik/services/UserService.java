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

	public UserDto findUserByEmail(String email) {
		Collection<User> usersList = connector.getSession().createCriteria(User.class).list();
		for (User user : usersList) {
			if (user.getEmail()!=null && user.getEmail().equals(email)) {
				return new UserDto(user);
			}
		}
		return null;
	}

	public void deleteUser(long id) {
		User user = (User)connector.getSession().get(User.class, id);
		if (user!=null) {
			Transaction transaction = connector.getSession().beginTransaction();
			
			connector.getSession().delete(user);
			transaction.commit();
		}
	}

	public UserDto findUserById(long id) {
		Collection<User> usersList = connector.getSession().createCriteria(User.class).list();
		for (User user : usersList) {
			if (user.getId()==id) {
				return new UserDto(user);
			}
		}
		return null;
	}

	public void editUserData(long id, String name, String email, boolean role) {
		User user = (User)connector.getSession().get(User.class, id);
		if (user!=null) {
			Transaction transaction = connector.getSession().beginTransaction();
			user.setEmail(email);
			user.setName(name);
			user.setIsadmin(role);
			connector.getSession().update(user);
			transaction.commit();
		}		
	}
	

}
