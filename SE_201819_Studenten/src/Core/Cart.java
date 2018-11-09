package Core;

import java.util.HashMap;
import be.ac.ua.ansymo.adbc.annotations.*;

public class Cart {
	private int m_cid;
	
	private HashMap<Item, Integer> m_contents;
	
	/**
	 * Create an empty cart (for a new session)
	 */
	public Cart() {
		m_contents = new HashMap<Item, Integer>();
	}
	
	
	/**
	 * Add an item with quantity to the cart
	 * 
	 * @param item
	 * @param quantity
	 */
    @requires({"quantity>=0", "item!=null"})                       // A positive amount of items may be added to a cart.
    @ensures("$old($this.contents().get(item))+quantity==$this.contents().get(item)")
    public void addItem(Item item, int quantity) {
		if (m_contents.containsKey(item)) {
			m_contents.put(item, m_contents.get(item) + quantity);
		} else {
			m_contents.put(item, quantity);
		}
	}
	
	
	/**
	 * Remove a quantity of an item from the Cart
	 * 
	 * @param item
	 * @param quantity
	 */
	@requires({"$old($this.contents().containsKey(item)==true", "item!=null", "quantity>=0"})
    @ensures("$this.contents().get(item)==$old($this.contents().get(item))-quantity")
	public void removeItem(Item item, int quantity) {
		if (m_contents.containsKey(item)) {
			if (m_contents.get(item) >= quantity) {
				m_contents.put(item, m_contents.get(item) - quantity);
			}
		}
	}
	
	
	public HashMap<Item, Integer> contents() {
		return m_contents;
	}


	@requires({"$this.contents()!=null", "$this!=null"})
	@ensures({"$result>=0", "$result!=null"})
	public float getCost() {
	    float result = 0;
	    for(HashMap.Entry<Item, Integer> entry : m_contents.entrySet()) {
            Item item = entry.getKey();
            float amount = (float) entry.getValue();

            result += amount * item.price();
        }
        return result;
    }
}
