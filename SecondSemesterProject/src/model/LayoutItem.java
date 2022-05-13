package model;

public class LayoutItem {

	private String name;
	private String type;
	private int id;

	public LayoutItem(String name, String type) {
		setName(name);
		setType(type);
	}

	public String getType() {
		return type;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getName() {
		return name;
	}
	
	public void setId(int id) {
		this.id = id;
	}
	
	public int getId() {
		return id;
	}

}
