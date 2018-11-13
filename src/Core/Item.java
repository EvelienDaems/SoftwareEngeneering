package Core;

import be.ac.ua.ansymo.adbc.annotations.*;

import java.util.HashSet;

public class Item {
	private int m_iid;
	
	private String m_name;
	private String m_desc;
	private float m_price;
	private HashSet<String> m_category;
	
	/**
	 * Create new Item
	 * 
	 * @param name
	 * @param desc
	 * @param price
	 */
	@requires({"desc != null, name != null, desc != null, price>0", "name.length() > 0"})	// The price of an item must be non-zero positive.
	public Item(String name, String desc, float price) {
		m_name = name;
		m_desc = desc;
		m_price = price;
		m_iid = 1;
	}

	@ensures("$result != null")
	public int ID() {
		return m_iid;
	}

	@ensures({"$result != null, $result.length() > 0"})
	public String name() {
		return m_name;
	}

	@ensures({"$result != null"})
	public String desc() {
		return m_desc;
	}

	@ensures({"$result != null, $result > 0"})
	public float price() {
		return m_price;
	}

	@ensures({"$result != null"})
	public HashSet<String> category() {
		return m_category;
	}
}
