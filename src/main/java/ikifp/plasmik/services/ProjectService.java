package ikifp.plasmik.services;

import java.util.Collection;
import java.util.List;

import javax.management.Query;
import javax.websocket.Session;

import org.hibernate.Transaction;

import ikifp.plasmik.model.Project;
import ikifp.plasmik.model.User;
import ikifp.plasmik.persistence.DatabaseConnector;

public class ProjectService {
	 DatabaseConnector connector;
	 
	 public ProjectService() {
		 connector= DatabaseConnector.getInstance();
	 }

	public Collection<Project> getAllProjects() {
		return connector.getSession().createCriteria(Project.class).list();
	}

	public Project findProjectByName(String projectName) {
		String hql ="FROM Project P WHERE P.projectName='"+projectName+"'";
		List<Project> foundProject = connector.getSession().createQuery(hql).list();
		if (foundProject.size()>0) {
			return foundProject.get(0);
		}else {
			return null;
		}
	}

	public void addProject(Project newProject) {
		Transaction transaction = connector.getSession().beginTransaction();
		connector.getSession().save(newProject);
		transaction.commit();
		
	}

	public void deleteProject(long projectId) {
		Project project = (Project)connector.getSession().get(Project.class, projectId);
		Transaction transaction = connector.getSession().beginTransaction();
		connector.getSession().delete(project);
		transaction.commit();
	}

	public Project findProjectById(long projectId) {
		return (Project)connector.getSession().get(Project.class, projectId);
	}

	public String editProjectData(long projectId, String projectNewName, long ownerId) {
		UserService userService = new UserService();
		Project project = (Project)connector.getSession().get(Project.class, projectId);
		if (project.getProjectName().equals(projectNewName)) {
			Transaction transaction = connector.getSession().beginTransaction();
			project.setUser(userService.findUserById(ownerId));
			connector.getSession().update(project);
			transaction.commit();
			return "project updated";
		}else {
			String hql ="FROM Project P WHERE P.projectName='"+projectNewName+"'";
			List<Project> foundProject = connector.getSession().createQuery(hql).list();
			if (foundProject.size()>0) {
				return "another project called "+projectNewName+" already exists";
			}else {
				Transaction transaction = connector.getSession().beginTransaction();
				project.setProjectName(projectNewName);
				project.setUser(userService.findUserById(ownerId));
				connector.getSession().update(project);
				transaction.commit();
				return "project updated";
			}
		}
		
	}
}
