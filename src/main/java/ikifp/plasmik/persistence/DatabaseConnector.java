package ikifp.plasmik.persistence;

import org.hibernate.Session;

public class DatabaseConnector {
	
	protected static DatabaseConnector instance = null;
	private Session session;
	
	private DatabaseConnector() {
		session = HibernateUtil.getSessionFactory().openSession(); 
	}
	
	public Session getSession() {
		return session;
	}
	
	public Session getNewSession() {
		return HibernateUtil.getSessionFactory().openSession();
	}
	
	public static DatabaseConnector getInstance() {
		if (instance==null) {
			instance = new DatabaseConnector();
		}
		return instance;
	}
	
	public void teardown() {
		session.close();
		HibernateUtil.shutdown();
		instance=null;
	}
}
