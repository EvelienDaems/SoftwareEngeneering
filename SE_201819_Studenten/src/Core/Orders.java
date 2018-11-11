package Core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import be.ac.ua.ansymo.adbc.annotations.*;

public class Orders {
	private List<Order> m_orders;
	private HashMap<Integer, Order> m_id_order;
	
	/**
	 * Create empty order history
	 */
	public Orders() {
		m_orders = new ArrayList<Order>();
		m_id_order = new HashMap<Integer, Order>();
	}
	
	@requires({"order != null"})
	public void addOrder(Order order) {
		m_orders.add(order);
		m_id_order.put(order.ID(), order);
	}

	@ensures({"$return != null"})
	public List<Order> getAllOrders() {
		return m_orders;
	}

	@requires({"oid != null"})
	@ensures({"$return != null"})
	public Order getOrder(int oid) {
		return m_id_order.get(oid);
	}

	@requires({"oid != null"})
	@ensures({"$return != null"})
	public Order_Status getOrderStatus(int oid) {
		return m_id_order.get(oid).getStatus();
	}

	@requires({"oid != null"})
	@ensures({"$return != null"})
	public boolean requestOrderCancel(int oid) {
		for (Order o : m_orders) {
			if (o.ID() == oid) {
				return o.requestCancel();
			}
		}
		return false;
	}
	
	
}
