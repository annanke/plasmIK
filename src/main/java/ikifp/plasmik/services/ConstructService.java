package ikifp.plasmik.services;

import java.io.File;
import java.io.FilenameFilter;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collection;

import javax.swing.SortOrder;

import org.hibernate.Transaction;
import org.hibernate.criterion.Order;

import ikifp.plasmik.helpers.CriteriaHelper;
import ikifp.plasmik.model.Construct;
import ikifp.plasmik.model.Project;
import ikifp.plasmik.persistence.DatabaseConnector;

import org.hibernate.Criteria;
import org.hibernate.Session;

public class ConstructService {
	
	DatabaseConnector connector;
	String pathInProject = "src/main/resources/static/files/";
	
	public ConstructService() {
		connector = DatabaseConnector.getInstance();
	}

	public Collection<Construct> getAllConstructs(String sortByProperty, SortOrder selectedOrder) {
		Session newSession = connector.getNewSession();
		
		try {
			Order order = CriteriaHelper.sort(sortByProperty, selectedOrder);

			Criteria criteriaOrdered = newSession.createCriteria(Construct.class).addOrder(order);
			
			return criteriaOrdered.list();

			//return newSession.createCriteria(Construct.class).list(); // bez sortowania
		}finally{
			newSession.close();
		}
	}
	
	public Collection<Construct> getAllConstructsSortedByName() {
		Session newSession = connector.getNewSession();
		
		try {
			Criteria criteriaOrdered = newSession.createCriteria(Construct.class).addOrder(Order.asc("constructName"));
			return criteriaOrdered.list();

			//return newSession.createCriteria(Construct.class).list(); // bez sortowania
		}finally{
			newSession.close();
		}
	}

	public void addConstruct(Construct newConstruct) {
		Session newSession = connector.getNewSession();
		Transaction transaction = null;
		
		try {
			transaction = newSession.beginTransaction();
			newSession.save(newConstruct);
			transaction.commit();
		}catch(Exception e){
			if (transaction != null) {
				transaction.rollback();
			}
			throw e;
		}
		finally{
			newSession.close();
		}
		
	}
	
	public void updateConstruct(Construct construct) {
		Session newSession = connector.getNewSession();
		Transaction transaction = null;
		
		try {
			transaction = newSession.beginTransaction();
			newSession.update(construct);
			transaction.commit();
		}catch(Exception e){
			if (transaction != null) {
				transaction.rollback();
			}
			throw e;
		}
		finally{
			newSession.close();
		}
		
	}

	public void deleteConstruct(long constructId) {
		
		Session newSession = connector.getNewSession();
		Transaction transaction = null;
		
		try {
			Construct construct = (Construct)newSession.get(Construct.class, constructId);
			transaction = newSession.beginTransaction();
			newSession.delete(construct);
			transaction.commit();
			deleteConstructFiles(constructId);
		}catch(Exception e){
			if (transaction != null) {
				transaction.rollback();
			}
			throw e;
		}
		finally{
			newSession.close();
		}
	}
	public void deleteConstructFiles(long constructId) {
		//map = pathInProject+((Long)construct.getId()).toString()+"_map_"+construct.getConstructName()+".pdf";
		//sequenceFile = pathInProject+((Long)construct.getId()).toString()+"_seq_"+construct.getConstructName()+"_"+file.getOriginalFilename();
		File rootDir = new File(pathInProject);
		File[] filesList = rootDir.listFiles(new FilenameFilter() {
			
			@Override
			public boolean accept(File dir, String name) {
				return name.startsWith(constructId+"_");
			}
		});
		for (File element : filesList) {
				element.delete();
		}
	}
	
	public void deleteConstructFilesWithLambda(long constructId) {
		File rootDir = new File(pathInProject);
		File[] filesList = rootDir.listFiles( (dir, name) -> name.startsWith(constructId+"_") );
		for (File element : filesList) {
			element.delete();
		}
	}
	
	public Construct findConstructById(long constructId) {
		Session newSession = connector.getNewSession();
		
		try {
			return (Construct)newSession.get(Construct.class, constructId);
		}finally{
			newSession.close();
		}
	}

}
