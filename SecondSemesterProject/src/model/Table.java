package model;

public class Table extends LayoutItem {

	private int capacity;
	private long id;

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

	public void setId(long id) {
		this.id = id;
	}

	public long getId() {
		return id;
	}

}
