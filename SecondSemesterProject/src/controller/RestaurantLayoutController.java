package controller;

import java.awt.Point;
import java.sql.SQLException;
import java.util.HashMap;

import database.RestaurantLayoutConcreteDAO;
import database.RestaurantLayoutDAO;
import database.TableDAO;
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
	
	public void saveRestaurantLayout(String name, int sizeX, int sizeY, HashMap<Point,LayoutItem> itemMap) throws SQLException {
		restaurantLayoutDAO.saveRestaurantLayout(name, sizeX, sizeY, itemMap);
	}
	
	public RestaurantLayout getRestaurantLayoutByName(String name) throws SQLException {
		return restaurantLayoutDAO.getRestaurantLayoutByName(name);
	}
	
	public void deleteRestaurantLayout(String restaurantLayoutName) throws SQLException {
		restaurantLayoutDAO.delete(restaurantLayoutName);
	}
	

}
