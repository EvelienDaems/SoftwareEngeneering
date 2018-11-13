package Core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import be.ac.ua.ansymo.adbc.annotations.*;


public class Catalog {
	private HashMap<Integer, Item> m_all_items;
	
	/**
	 * Create new, empty Catalog
	 */
	public Catalog() {
		m_all_items = new HashMap<Integer, Item>();
	}
	
	
	/**
	 * Load existing catalog from file
	 * 
	 * @param filename
	 */
	@requires({"filename != null, filename.length()!=0"})
	public Catalog(String filename) {
		
	}
	
	
	/**
	 * Store the current catalog to file
	 * 
	 * @param filename
	 */
	@requires("filename != null, filename.length()!=0")
	public void store(String filename) {
		
	}
	
	
	/**
	 * Voeg item toe aan de catalogus
	 * 
	 * @param item
	 */
	@requires({"item!=null", "$this.findMatch(item) == null"})
	@ensures("$this.getNumberOfItems() == $old($this.getNumberOfItems()) + 1")
	public void addItem(Item item) {
		m_all_items.put(item.ID(), item);
	}

	@ensures({"$result != null"})
	public HashMap<Integer, Item> getAllItems() {return m_all_items;}

	@ensures({"$result>=0"})
	public int getNumberOfItems() {
		return m_all_items.size();
	}

	@requires({"id != null, $this.getAllItems().containsKey(id)", "id>0"})
	@ensures({"$result!=null", "$result.ID()==id"})
	public Item getItemByID(int id) {
		return m_all_items.get(id);
	}

	@ensures({"$result != null"})
	public Set<Integer> getAllIDs() {
		return m_all_items.keySet();
	}

	@requires({"category!=null"})
	@ensures({"$result != null"})
	public ArrayList<Item> filterCategories(HashSet<String> categories) {
		ArrayList<Item> filtered_items = new ArrayList<Item>();
		
		for (Integer iid : m_all_items.keySet()) {
			HashSet<String> check = new HashSet<String>(categories);
			check.retainAll(m_all_items.get(iid).category());
			if (check.size() > 0)
				filtered_items.add(m_all_items.get(iid));
		}
		
		return filtered_items;
	}

	@requires({"item!=null"})
	public Item findMatch(Item item){
		for(Item saved_item:m_all_items.values()){
			if(item.name().equals(saved_item.name()) && item.desc().equals(saved_item.desc()) && item.price() == saved_item.price()){
				return saved_item;
			}
		}
		return null;
	}
}
