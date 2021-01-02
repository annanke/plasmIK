package ikifp.plasmik.services;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.hibernate.Transaction;
import org.hibernate.Session;

import ikifp.plasmik.AppSettings;
import ikifp.plasmik.model.Construct;
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
		Session newSession = connector.getNewSession();
		
		Collection<Object[]> queryResult = null;
		Collection<User> usersList = new ArrayList<User>();
		
		try {
			String hql = "SELECT U, count(P.id ) FROM User U LEFT JOIN U.projects P GROUP BY (U)";
			queryResult = newSession.createQuery(hql).list();
			for (Object[] element : queryResult) {
				User user = (User) element[0];
				user.setNumberOfProjects((Long) element[1]);
				usersList.add(user);
			}		
			return usersList;
			//return newSession.createCriteria(User.class).list();
		} finally {
			newSession.close();
		}
	}

	public void addUser(User user) {
		Session newSession = connector.getNewSession();
		Transaction transaction = null;
		
		try {
			transaction = newSession.beginTransaction();
			newSession.save(user);
			transaction.commit();
		} catch (Exception e){
			if (transaction != null) {
				transaction.rollback();
			}
			throw e;
		} finally {
			newSession.close();
		}
	}

	public UserDto findUserByEmail(String email) {
		Session newSession = connector.getNewSession();
		try {
			Collection<User> usersList = newSession.createCriteria(User.class).list();
			for (User user : usersList) {
				if (user.getEmail() != null && user.getEmail().equals(email)) {
					return new UserDto(user);
				}
			}
			return null;
		} finally {
			newSession.close();
		}
	}

	public String deleteUser(long id) {
		Session newSession = connector.getNewSession();
		Transaction transaction = null;
		try {
			User user = (User) newSession.get(User.class, id);
			if (user != null && !user.getEmail().equals(AppSettings.getDefaultUserEmail())) {
				transaction = newSession.beginTransaction();
				UserService userService = new UserService();
				UserDto userDto = userService.findUserByEmail(AppSettings.getDefaultUserEmail());
				User defaultUser = userService.findUserById(userDto.getId());
				
				String hql = "FROM Project P WHERE P.user=" + user.getId();
				List<Project> foundProjects = newSession.createQuery(hql).list();
				for (Project project : foundProjects) {
					project.setUser(defaultUser);
					newSession.update(project);
				}
				
				String constructsQuery = "FROM Construct C WHERE C.user=" + user.getId();
				List<Construct> foundConstructs = newSession.createQuery(constructsQuery).list();
				for (Construct construct : foundConstructs) {
					construct.setUser(defaultUser);
					newSession.update(construct);
				}
				
				newSession.delete(user);
				transaction.commit();
				return "user deleted";
			} else if (user != null && user.getEmail().equals(AppSettings.getDefaultUserEmail())) {
				return "user can not be deleted - it's admin of admins";
			} else {
				return "user does not exist!";
			} 
		} catch (Exception e){
			if (transaction != null) {
				transaction.rollback();
			}
			throw e;
		}finally{
			newSession.close();
		}
	}

	public UserDto findUserDtoById(long id) {
		Session newSession = connector.getNewSession();
		try {
			Collection<User> usersList = newSession.createCriteria(User.class).list();
			for (User user : usersList) {
				if (user.getId() == id) {
					return new UserDto(user);
				}
			}
			return null;
		} finally {
			newSession.close();
		}
	}
	
	public User findUserById(long id) {
		Session newSession = connector.getNewSession();
		try {
			Collection<User> usersList = newSession.createCriteria(User.class).list();
			for (User user : usersList) {
				if (user.getId() == id) {
					return user;
				}
			}
			return null;
		} finally {
			newSession.close();
		}
	}

	public String editUserData(long id, String name, String email, boolean role) {
		Session newSession = connector.getNewSession();
		Transaction transaction = null;
		
		try {
			User user = (User) newSession.get(User.class, id);
			if (user != null && user.getEmail().equals(email)) {
				transaction = newSession.beginTransaction();
				user.setName(name);
				user.setIsadmin(role);
				newSession.update(user);
				transaction.commit();
				return "user updated";
			} else if (user != null && !user.getEmail().equals(email)) {
				String hql = "FROM User U WHERE U.email='" + email + "'";
				List<User> foundUser = newSession.createQuery(hql).list();
				if (foundUser.size() == 0) {
					transaction = newSession.beginTransaction();
					user.setEmail(email);
					user.setName(name);
					user.setIsadmin(role);
					newSession.update(user);
					transaction.commit();
					return "user updated";
				} else {
					return "user with emial: '" + email + "' already exists";
				}
			} else {
				return "user does not exists";
			} 
		} catch (Exception e){
			if (transaction != null) {
				transaction.rollback();
			}
			throw e;
		}finally{
			newSession.close();
		}
	}
	

}
