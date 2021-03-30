package ikifp.plasmik.services;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import javax.management.Query;
import javax.swing.SortOrder;

import org.hibernate.HibernateException;
import org.hibernate.Session;

import org.hibernate.Transaction;

import ikifp.plasmik.helpers.CriteriaHelper;
import ikifp.plasmik.model.Construct;
import ikifp.plasmik.model.Project;
import ikifp.plasmik.model.User;
import ikifp.plasmik.persistence.DatabaseConnector;

public class ProjectService {
	 DatabaseConnector connector;
	 
	 public ProjectService() {
		 connector= DatabaseConnector.getInstance();
	 }

	public Collection<Project> getAllProjects(String sortByProperty, SortOrder selectedOrder) {
		Session newSession = connector.getNewSession();
		
		Collection<Object[]> queryResult = null; 
		Collection<Project> projectsList = new ArrayList<Project>();

		try {
			String sqlOrder = CriteriaHelper.getOrderCommand(selectedOrder);
			String hql = "SELECT P, count(C.id ) FROM Project P LEFT JOIN P.constructs C GROUP BY (P) ORDER BY P."+sortByProperty+" "+sqlOrder;
			queryResult = newSession.createQuery(hql).list();
			for (Object[] element : queryResult) {
				Project project = (Project) element[0];
				project.setNumberOfConstructs((Long) element[1]);
				projectsList.add(project);
			}		
			return projectsList;
			//return newSession.createCriteria(Project.class).list();
		}finally{
			newSession.close();
		}
	}

	public Collection<Project> getAllProjects(){
		return 	getAllProjects("projectName", SortOrder.ASCENDING);
	}
	
	public Project findProjectByName(String projectName) {
		Session newSession = connector.getNewSession();
		try {
			String hql ="FROM Project P WHERE P.projectName='"+projectName+"'";
			List<Project> foundProject = newSession.createQuery(hql).list();
			if (foundProject.size()>0) {
				return foundProject.get(0);
			}else {
				return null;
			}
		} 
		finally{
			newSession.close();
		}
	}

	public void addProject(Project newProject) {
		Session newSession = connector.getNewSession();
		Transaction transaction = null;
		
		try {
			transaction = newSession.beginTransaction();
			newSession.save(newProject);
			transaction.commit();
		} catch (Exception e){
			if (transaction != null) {
				transaction.rollback();
			}
			throw e;
		}finally{
			newSession.close();
		}
	}

	public void deleteProject(long projectId) {
		Session newSession = connector.getNewSession();
		Transaction transaction = null;
		try {
			Project project = (Project)newSession.get(Project.class, projectId);
			Set<Construct> constructs = project.getConstructs();
			ArrayList<Long> constructsIDs = new ArrayList<>();
			for (Construct construct : constructs) {
				constructsIDs.add(construct.getId());
			}
			
			transaction = newSession.beginTransaction();
			newSession.delete(project);
			transaction.commit();
			
			ConstructService constructService = new ConstructService();
			for (Long id : constructsIDs) {
				constructService.deleteConstructFiles(id);
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

	public Project findProjectById(long projectId) {
		Session newSession = connector.getNewSession();
		try {
			return (Project) newSession.get(Project.class, projectId);
		} finally {
			newSession.close();
		}
	}

	public String editProjectData(long projectId, String projectNewName, long ownerId) {
		Session newSession = connector.getNewSession();
		Transaction transaction = null; 
		
		try {
			UserService userService = new UserService();
			Project project = (Project) newSession.get(Project.class, projectId);
			if (project.getProjectName().equals(projectNewName)) {
				transaction = newSession.beginTransaction();
				project.setUser(userService.findUserById(ownerId));
				newSession.update(project);
				transaction.commit();
				return "project updated";
			} else {
				String hql = "FROM Project P WHERE P.projectName='" + projectNewName + "'";
				List<Project> foundProject = newSession.createQuery(hql).list();
				if (foundProject.size() > 0) {
					return "another project called " + projectNewName + " already exists";
				} else {
					transaction = newSession.beginTransaction();
					project.setProjectName(projectNewName);
					project.setUser(userService.findUserById(ownerId));
					newSession.update(project);
					transaction.commit();
					return "project updated";
				}
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
