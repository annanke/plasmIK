package ikifp.plasmik.services;

import java.util.Collection;

import ikifp.plasmik.model.Project;
import ikifp.plasmik.persistence.DatabaseConnector;

public class ProjectService {
	 DatabaseConnector connector;
	 
	 public ProjectService() {
		 connector= DatabaseConnector.getInstance();
	 }

	public Collection<Project> getAllProjects() {
		return connector.getSession().createCriteria(Project.class).list();
	}
}
