package model;

public class Table extends LayoutItem {

	private int capacity;
	private int id;

	public Table(String name, String type, int capacity) {
		super(name, type);
		this.capacity = capacity;
	}

	public int getCapacity() {
		return capacity;
	}

	public void setCapacity(int capacity) {
		this.capacity = capacity;
	}
	
	public void setId(int id) {
		this.id = id;
	}
	
	public int getId() {
		return id;
	}

}
