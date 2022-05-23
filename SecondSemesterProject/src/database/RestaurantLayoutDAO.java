package database;

import java.awt.Point;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;

import model.LayoutItem;
import model.RestaurantLayout;

public interface RestaurantLayoutDAO {
	
	List<RestaurantLayout> read() throws SQLException;

	RestaurantLayout read(String restaurantLayoutName) throws SQLException;
	
	void save(String name, int sizeX, int sizeY, HashMap<Point,LayoutItem> itemMap) throws SQLException;

	void update(RestaurantLayout restaurantLayout) throws SQLException;

	void delete(String restaurantLayoutName) throws SQLException;
	
}
