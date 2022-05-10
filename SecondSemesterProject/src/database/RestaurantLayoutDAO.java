package database;

import java.awt.Point;
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
	
	List<Table> getRestaurantLayoutItemsList(int restaurantLayoutID);
	
	void saveRestaurantLayout(RestaurantLayout restaurantLayout);

	void createRestaurantLayout(RestaurantLayout restaurantLayout);
	
	void createLayoutItems(List<LayoutItem> layoutItems);
	
	void createTables(List<Table> layoutItems);

	void update(RestaurantLayout restaurantLayout);

	void delete(RestaurantLayout restaurantLayout);

}
