package controller;

import java.awt.Point;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;

import database.RestaurantLayoutConcreteDAO;
import database.RestaurantLayoutDAO;
import model.LayoutItem;
import model.RestaurantLayout;

public class RestaurantLayoutController {
	RestaurantLayoutDAO restaurantLayoutDAO;
	
	public RestaurantLayoutController() {
		restaurantLayoutDAO = RestaurantLayoutConcreteDAO.getInstance();
	}
	
	public void save(String name, int sizeX, int sizeY, HashMap<Point,LayoutItem> itemMap)
			throws SQLException, Exception {
		if(restaurantLayoutDAO.read(name).getId() != 0)
		throw new Exception("There already exists restaurant layout with such a name");
		else restaurantLayoutDAO.save(name, sizeX, sizeY, itemMap);
	}
	
	public RestaurantLayout read(String name) throws SQLException {
		return restaurantLayoutDAO.read(name);
	}
	
	public void deleteRestaurantLayout(String restaurantLayoutName) throws SQLException {
		try {
			restaurantLayoutDAO.delete(restaurantLayoutName);
		} catch (SQLException e) {
			throw new SQLException("Error in deleting RestaurantLayout:"+ e.getMessage());
		}
	}
	
	public List<RestaurantLayout> read() throws SQLException{
		return restaurantLayoutDAO.read();
	}
	

}
