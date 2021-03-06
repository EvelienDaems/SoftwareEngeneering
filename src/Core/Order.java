package Core;

import java.util.HashMap;

import be.ac.ua.ansymo.adbc.annotations.*;

import Core.Clients.Client;

public class Order {
	private int m_oid;
	private static int m_next_id = 1;

	private Order_Status m_status = Order_Status.STARTED;
	
	private Client m_client;
	private HashMap<Item, Integer> m_contents;

	
	/**
	 * Create a new Order from a User's Cart
	 */
	@requires({"cart != null, client != null"})
	public Order(Cart cart, Client client) {
		m_oid = m_next_id;
		m_next_id++;
		
		m_client = client;
		m_contents = cart.contents();
	}

	@ensures({"$result != null"})
	public HashMap<Item, Integer> getItems() {
		return m_contents;
	}

	@ensures({"$result != null"})
	public int ID() {
		return m_oid;
	}

	@ensures({"$result != null"})
	public Client getClient() {
		return m_client;
	}

	@ensures({"$result != null"})
	public Order_Status getStatus() {
		return m_status;
	}

	@ensures({"$result != null"})
	public boolean requestCancel() {
		if (m_status != Order_Status.DELIVERED) {
			m_status = Order_Status.CANCELED;
			return true;
		} else {
			return false;
		}
	}
	
	
	
}
