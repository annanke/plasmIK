package ikifp.plasmik.services;

import java.util.Collection;

import org.hibernate.Transaction;

import ikifp.plasmik.model.Construct;
import ikifp.plasmik.model.Project;
import ikifp.plasmik.persistence.DatabaseConnector;

public class ConstructService {
	
	DatabaseConnector connector;
	
	public ConstructService() {
		connector = DatabaseConnector.getInstance();
	}

	public Collection<Construct> getAllConstructs() {
		return connector.getSession().createCriteria(Construct.class).list();
	}

	public void addConstruct(Construct newConstruct) {
		Transaction transaction = connector.getSession().beginTransaction();
		connector.getSession().save(newConstruct);
		transaction.commit();
		
	}

	public void deleteConstruct(long constructId) {
		Construct construct = (Construct)connector.getSession().get(Construct.class, constructId);
		Transaction transaction = connector.getSession().beginTransaction();
		connector.getSession().delete(construct);
		transaction.commit();
		
	}

	public Construct findConstructById(long constructId) {
		Construct construct = (Construct)connector.getSession().get(Construct.class, constructId);
		return construct;
	}

}
