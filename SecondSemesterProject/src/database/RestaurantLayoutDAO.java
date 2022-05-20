package database;

import java.awt.Point;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;

import model.LayoutItem;
import model.RestaurantLayout;

public interface RestaurantLayoutDAO {
	
	List<RestaurantLayout> read() throws SQLException;

	RestaurantLayout getRestaurantLayoutByName(String restaurantLayoutName) throws SQLException;
	
	void saveRestaurantLayout(String name, int sizeX, int sizeY, HashMap<Point,LayoutItem> itemMap) throws SQLException;

	Long createRestaurantLayout(RestaurantLayout restaurantLayout) throws SQLException;
	
	void update(RestaurantLayout restaurantLayout) throws SQLException;

	void delete(String restaurantLayoutName) throws SQLException;
	
}
