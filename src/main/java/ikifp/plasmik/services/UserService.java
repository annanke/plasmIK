package ikifp.plasmik.services;

import java.util.Collection;
import java.util.List;

import org.hibernate.Transaction;

import ikifp.plasmik.AppSettings;
import ikifp.plasmik.model.Project;
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

	public String deleteUser(long id) {
		User user = (User)connector.getSession().get(User.class, id);
		if (user!=null && !user.getEmail().equals(AppSettings.getDefaultUserEmail())) {
			Transaction transaction = connector.getSession().beginTransaction();
			String hql ="FROM Project P WHERE P.user="+user.getId();
			List<Project> foundProjects = connector.getSession().createQuery(hql).list();
			for (Project project : foundProjects) {
				UserService userService = new UserService();
				UserDto userDto = userService.findUserByEmail(AppSettings.getDefaultUserEmail());
				project.setUser(userService.findUserById(userDto.getId()));
				connector.getSession().update(project);
			}
			connector.getSession().delete(user);
			transaction.commit();
			return "user deleted";
		}else if (user!=null && user.getEmail().equals(AppSettings.getDefaultUserEmail())) {
			return "user can not be deleted - it's admin of admins";
		}else {
			return "user does not exist!";
		}
	}

	public UserDto findUserDtoById(long id) {
		Collection<User> usersList = connector.getSession().createCriteria(User.class).list();
		for (User user : usersList) {
			if (user.getId()==id) {
				return new UserDto(user);
			}
		}
		return null;
	}
	
	public User findUserById(long id) {
		Collection<User> usersList = connector.getSession().createCriteria(User.class).list();
		for (User user : usersList) {
			if (user.getId()==id) {
				return user;
			}
		}
		return null;
	}

	public String editUserData(long id, String name, String email, boolean role) {
		User user = (User)connector.getSession().get(User.class, id);
		if (user!=null && user.getEmail().equals(email)) {
			Transaction transaction = connector.getSession().beginTransaction();
			user.setName(name);
			user.setIsadmin(role);
			connector.getSession().update(user);
			transaction.commit();
			return "user updated";
		} else if (user!=null && !user.getEmail().equals(email)){
			String hql ="FROM User U WHERE U.email='"+email+"'";
			List<User> foundUser = connector.getSession().createQuery(hql).list();
			if (foundUser.size()==0) {
				Transaction transaction = connector.getSession().beginTransaction();
				user.setEmail(email);
				user.setName(name);
				user.setIsadmin(role);
				connector.getSession().update(user);
				transaction.commit();
				return "user updated";
			}else {
				return "user with emial: '"+email+"' already exists";
			}
		}else {
			return "user does not exists";
		}
	}
	

}
