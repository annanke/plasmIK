package ikifp.plasmik.services;

import java.util.Collection;

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

}
