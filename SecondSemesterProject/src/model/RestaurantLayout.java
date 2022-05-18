package model;

import java.awt.Point;
import java.util.ArrayList;
import java.util.HashMap;

public class RestaurantLayout {
	
	private String name;
	private int sizeX, sizeY;
	private HashMap<Point, LayoutItem> itemMap;
	private long id;
	
	public RestaurantLayout(String name, int sizeX, int sizeY, HashMap<Point,LayoutItem> itemMap) {
		setName(name);
		setSizeX(sizeX);
		setSizeY(sizeY);
		setItemMap(itemMap);
	}
	
	public ArrayList<LayoutItem> getLayoutItems(){
		ArrayList<LayoutItem> listOfLayoutItems = new ArrayList<>();
		for(LayoutItem l : itemMap.values()) {
			listOfLayoutItems.add(l);
		}
		return listOfLayoutItems;
	}
	
	public ArrayList<Table> getTableList(){
		ArrayList<Table> listOfTables = new ArrayList<>();
		for(LayoutItem l : itemMap.values()) {
			if(l instanceof Table) {
				listOfTables.add((Table) l);
			}
		}
		return listOfTables;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	public int getSizeX() {
		return sizeX;
	}
	public void setSizeX(int sizeX) {
		this.sizeX = sizeX;
	}
	public int getSizeY() {
		return sizeY;
	}
	public void setSizeY(int sizeY) {
		this.sizeY = sizeY;
	}
	public HashMap<Point, LayoutItem> getItemMap() {
		return itemMap;
	}
	public void setItemMap(HashMap<Point, LayoutItem> itemMap) {
		this.itemMap = itemMap;
	}
	
	public void setId(long id) {
		this.id = id;
	}
	
	public long getId() {
		return id;
	}

}
