package model;

import java.awt.Point;
import java.util.HashMap;

public class RestaurantLayout {
	
	private String name;
	private int sizeX, sizeY;
	private HashMap<Point, LayoutItem> itemMap;
	public String getName() {
		return name;
	}
	
	public RestaurantLayout() {
		
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

}
