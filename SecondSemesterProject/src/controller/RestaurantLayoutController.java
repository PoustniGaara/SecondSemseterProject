package controller;

import java.awt.Point;
import java.util.HashMap;

import model.LayoutItem;
import model.RestaurantLayout;

public class RestaurantLayoutController {
	
	public RestaurantLayoutController() {
	}
	
	public void createLayout(String name, int sizeX, int sizeY, HashMap<Point,LayoutItem> itemMap) {
		RestaurantLayout resLay = new RestaurantLayout();
		resLay.setName(name);
		resLay.setSizeX(sizeX);
		resLay.setSizeY(sizeY);
		resLay.setItemMap(itemMap);
	}

}
