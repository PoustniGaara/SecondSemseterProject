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

	RestaurantLayout getRestaurantLayout(String name);
	
	Map<Point,LayoutItem> getItemMap(String restaurantLayoutName);
	
	List<Table> getRestaurantLayoutTableList(int restaurantLayoutID);
	
	void saveRestaurantLayout(RestaurantLayout restaurantLayout) throws SQLException;

	void createRestaurantLayout(RestaurantLayout restaurantLayout);
	
	void createLayoutItems(RestaurantLayout restaurantLayout);
	
	void createTables(RestaurantLayout restaurantLayout);

	void update(RestaurantLayout restaurantLayout);

	void delete(RestaurantLayout restaurantLayout);

}
