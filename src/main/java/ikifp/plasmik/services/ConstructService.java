package ikifp.plasmik.services;

import java.util.Collection;

import org.hibernate.Transaction;

import ikifp.plasmik.model.Construct;
import ikifp.plasmik.model.Project;
import ikifp.plasmik.persistence.DatabaseConnector;
import org.hibernate.Session;

public class ConstructService {
	
	DatabaseConnector connector;
	
	public ConstructService() {
		connector = DatabaseConnector.getInstance();
	}

	public Collection<Construct> getAllConstructs() {
		Session newSession = connector.getNewSession();
		
		try {
			return newSession.createCriteria(Construct.class).list();
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

	public Construct findConstructById(long constructId) {
		Session newSession = connector.getNewSession();
		
		try {
			return (Construct)newSession.get(Construct.class, constructId);
		}finally{
			newSession.close();
		}
	}

}
