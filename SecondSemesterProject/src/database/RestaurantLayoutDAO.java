package database;

import java.awt.Point;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import model.LayoutItem;
import model.RestaurantLayout;
import model.Table;

public interface RestaurantLayoutDAO {
	
	List<RestaurantLayout> read();

	RestaurantLayout getRestaurantLayoutByName(String restaurantLayoutName);
	
	Map<Point,LayoutItem> getLayoutItems(int restaurantLayoutID);
	
	Map<Integer, Integer> getTablesCapacity(int restaurantLayoutID);
	
	List<Table> getRestaurantLayoutTableList(int restaurantLayoutID);
	
	Integer getRestaurantLayoutID(String restaurantLayoutName);
	
	void saveRestaurantLayout(RestaurantLayout restaurantLayout) throws SQLException;

	void createRestaurantLayout(RestaurantLayout restaurantLayout);
	
	void createLayoutItems(RestaurantLayout restaurantLayout);
	
	void createTables(RestaurantLayout restaurantLayout);

	void update(RestaurantLayout restaurantLayout);

	void delete(RestaurantLayout restaurantLayout);
	
	Map<Point, LayoutItem> getUpdatedItemMapByTableID(RestaurantLayout restaurantLayout);
	
	

}
