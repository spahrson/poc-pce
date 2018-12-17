package client.core;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import client.core.util.Node;

/**
 * Category object
 * 
 * PoC - PCE
 * @author ramon.spahr@gmail.com
 *
 */
public class Category extends Node<Map<String, Document>> {

	private String label = null; // category
	
	// FIXME make sure, path delimiter can not be used within label name
	public static final String PATH_DELIMITER = ">";

	public Category(Map<String, Document> data, String label) {
		super(data);
		this.label = label;
	}

	public Category(String label, Document... documents) {
		super();
		this.label = label;
		for(Document doc : documents) {
			if(doc != null)
				this.getData().put(generateRandomIdentifier(), doc);
		}
	}

	public Category(Map<String, Document> data, Category parent, String label) {
		super(data, parent);
		this.label = label;
	}

	public Category(String label) {
		super();
		this.label = label;
	}
	
	public void setLabel(String label) {
		this.label = label;
	}

	public String getLabel() {
		return this.label;
	}

	public boolean equals(Category c) {
		if(this.label.equals(c.label) && ((this.getParent() == null && c.getParent() == null) || (this.getParent().equals(c.getParent()))))
			return true;
		else return false;
	}

	@Override
	public Category getParent() {
		return (Category) super.getParent();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Category> getChildren() {
		return (List<Category>) super.getChildren();
	}

	@Override
	public String toString() { 
		return this.label;
	} 	

	public String generateRandomIdentifier() {
		return UUID.randomUUID().toString().concat("LOCAL");
	}

	public String addDocument(String locator, Document document) {
		if(locator == null) locator = generateRandomIdentifier();
		this.getData().put(locator, document);
		return locator;
	}

	public String getPath() {
		StringBuilder sb = new StringBuilder();
		Category c = this;
		boolean first = true;
		do {
			if(first) {
				sb.insert(0, c.getLabel());
				first = false;
			} else
				sb.insert(0, c.getLabel().concat(PATH_DELIMITER));
		} while((c = (Category) c.getParent())!=null);
		return sb.toString();
	}
	
	@Override
	public Map<String, Document> getData() {
		if(super.getData()==null) super.setData(new HashMap<String, Document>()); 
		return super.getData();
	}

}
