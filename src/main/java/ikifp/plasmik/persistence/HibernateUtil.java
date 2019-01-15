package ikifp.plasmik.persistence;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.AnnotationConfiguration;

public class HibernateUtil {
	
	private static SessionFactory sessionFactory;
	
	static {
		try{
			
			AnnotationConfiguration ac = new AnnotationConfiguration().configure();
			if (System.getenv("JDBC_DATABASE_URL")!=null) {
				ac.setProperty("hibernate.connection.url", System.getenv("JDBC_DATABASE_URL"));
			}
			sessionFactory = ac.buildSessionFactory();
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}

	public static SessionFactory getSessionFactory() {
		return sessionFactory;
	}

	public static void shutdown() {
		sessionFactory.close();
		
	}

}
