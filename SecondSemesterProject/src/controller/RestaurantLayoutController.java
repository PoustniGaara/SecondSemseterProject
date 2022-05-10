package controller;

import java.awt.Point;
import java.util.HashMap;

import database.RestaurantLayoutConcreteDAO;
import model.LayoutItem;
import model.RestaurantLayout;

public class RestaurantLayoutController {
	RestaurantLayoutConcreteDAO restaurantLayoutDB;
	
	public RestaurantLayoutController() {
		restaurantLayoutDB = RestaurantLayoutConcreteDAO.getInstance();
	}
	
	public void createRestaurantLayout(RestaurantLayout restaurantLayout) {
		restaurantLayoutDB.createRestaurantLayout(restaurantLayout);
	}
	
	public void createLayoutObj(String name, int sizeX, int sizeY, HashMap<Point,LayoutItem> itemMap) {
		RestaurantLayout resLay = new RestaurantLayout();
		resLay.setName(name);
		resLay.setSizeX(sizeX);
		resLay.setSizeY(sizeY);
		resLay.setItemMap(itemMap);
	}

}
