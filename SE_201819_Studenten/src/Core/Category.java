package Core;

import java.util.ArrayList;
import be.ac.ua.ansymo.adbc.annotations.*;


public class Category {
	
	private ArrayList<String> m_categories;

	@ensures("$this.getCategories().length()==10")
	public Category() {
		m_categories = new ArrayList<String>();
		String[] basic_categories = {"Phone", "Tablet", "Computer", "Image", "Sound", "Home & Living", "Kitchen", "Travel", "Fashion", "Sport"};
		for (String cat : basic_categories) {
			m_categories.add(cat);
		}
	}
	
	public ArrayList<String> getCategories() {
		return m_categories;
	}

	@requires({"category.length()!=0", "!$this.getCategories().contains(category)"})
	@ensures({"$this.getCategories().contains(category)==true", "$old($this.getCategories()).length()+1==$this.getCategories().length()"})
	public void addCategory(String category) {
		if (!m_categories.contains(category))
			m_categories.add(category);
	}
}
