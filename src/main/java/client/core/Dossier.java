package client.core;

/**
 * Dossier object
 * 
 * PoC - PCE
 * @author ramon.spahr@gmail.com
 *
 */
public class Dossier {

	public static String ROOT_LABEL_NAME = "ROOT"; // root name with full access

	private Category root;
	
	public Dossier() {
		root = new Category(ROOT_LABEL_NAME);
		intialize();
	}
	
	private void intialize() {
		Category geheimeDaten = new Category("Geheime Daten");
		Category sensibleDaten = new Category("Sensible Daten");
		Category vertraulicheDaten = new Category("Verrauliche Daten");
		Category nuetzlicheDaten = new Category("Nützliche Daten");
		Category demographischeDaten = new Category("Demographische Daten");

		this.getRoot().addChild(geheimeDaten);
		geheimeDaten.addChild(sensibleDaten);
		sensibleDaten.addChild(vertraulicheDaten);
		vertraulicheDaten.addChild(nuetzlicheDaten);
		nuetzlicheDaten.addChild(demographischeDaten);
	}
	
	public Category getRoot() {
		return this.root;
	}	

	public Category getCategory(String path) {
		if(path.equals(ROOT_LABEL_NAME)) return this.getRoot();
		if(path.startsWith(Dossier.ROOT_LABEL_NAME)) path = path.substring(path.indexOf(Dossier.ROOT_LABEL_NAME)+Dossier.ROOT_LABEL_NAME.length()+1);
		String[] tokens = path.split(Category.PATH_DELIMITER);
		Category c = this.getRoot();
		for(String t : tokens) {
			c = find(c, t);
			if(c==null) return null;
		}
		return c;
	}
	
	private Category find(Category c, String label) {
		for(Category cc : c.getChildren()) {
			if(cc.getLabel().equals(label)) return cc;
		}
		Category cN = new Category(label);
		c.addChild(cN);
		return cN;
	}
	
	public Category findDeep(Category c, String label) {
		if(c.getLabel().equals(label)) {
			return c;
		} else {
			Category ret = null;
			for(Category cc : c.getChildren()) {
				ret = findDeep(cc, label);
				if(ret!=null) return ret;
			}
		}
		return null;
	}

}