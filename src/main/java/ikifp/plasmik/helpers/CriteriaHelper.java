package ikifp.plasmik.helpers;

import javax.swing.SortOrder;

import org.hibernate.criterion.Order;

public class CriteriaHelper {
	
	public static Order sort(String sortByProperty, SortOrder selectedOrder) {
		Order order = null;
		
		switch (selectedOrder) {
		case ASCENDING :
			order = Order.asc(sortByProperty);
			break;

		case DESCENDING :
			order = Order.desc(sortByProperty);
			break;
		default:
			order = Order.asc(sortByProperty);
			break;
		} 
		return order;
	}
	
	public static String getOrderCommand(SortOrder selectedOrder) {
		String sqlOrder = null;
		
		switch (selectedOrder) {
		case ASCENDING :
			sqlOrder = "asc";
			break;

		case DESCENDING :
			sqlOrder = "desc";
			break;
		default:
			sqlOrder = "asc";
			break;
		}  
		return sqlOrder;
	}
}
