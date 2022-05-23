package controller;

import java.awt.Point;
import java.sql.SQLException;
import java.util.HashMap;

import database.RestaurantLayoutConcreteDAO;
import database.RestaurantLayoutDAO;
import model.LayoutItem;
import model.RestaurantLayout;

public class RestaurantLayoutController {
	RestaurantLayoutDAO restaurantLayoutDAO;
	
	public RestaurantLayoutController() {
		restaurantLayoutDAO = RestaurantLayoutConcreteDAO.getInstance();
	}
	
	public void createRestaurantLayout(RestaurantLayout restaurantLayout) throws SQLException {
		restaurantLayoutDAO.createRestaurantLayout(restaurantLayout);
	}
	
	public void saveRestaurantLayout(String name, int sizeX, int sizeY, HashMap<Point,LayoutItem> itemMap)
			throws SQLException, Exception {
		if(restaurantLayoutDAO.getRestaurantLayoutByName(name).getId() != 0)
		restaurantLayoutDAO.saveRestaurantLayout(name, sizeX, sizeY, itemMap);
		else throw new Exception("There already exists restaurant layout with such a name");
	}
	
	public RestaurantLayout getRestaurantLayoutByName(String name) throws SQLException {
		return restaurantLayoutDAO.getRestaurantLayoutByName(name);
	}
	
	public void deleteRestaurantLayout(String restaurantLayoutName) throws SQLException {
		restaurantLayoutDAO.delete(restaurantLayoutName);
	}
	

}
