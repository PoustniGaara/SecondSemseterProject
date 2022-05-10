package database;

import java.awt.Point;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import model.LayoutItem;
import model.RestaurantLayout;
import model.Table;

public interface RestaurantLayoutDAO {
	
	ArrayList<RestaurantLayout> read();

	RestaurantLayout getRestaurantLayout(String name);
	
	HashMap<Point,LayoutItem> getItemMap(String name);
	
	void saveRestaurantLayout(RestaurantLayout restaurantLayout);

	void createRestaurantLayout(RestaurantLayout restaurantLayout);
	
	void createLayoutItems(List<LayoutItem> layoutItems);
	
	void createTables(List<Table> layoutItems);

	void update(RestaurantLayout restaurantLayout);

	void delete(RestaurantLayout restaurantLayout);

}
