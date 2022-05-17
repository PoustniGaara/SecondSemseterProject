package database;

import java.awt.Point;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import model.LayoutItem;
import model.RestaurantLayout;

public interface LayoutItemDAO {
	
	Map<Point,LayoutItem> getLayoutItems(long restaurantLayoutID);
	
	void update(RestaurantLayout restaurantLayout);

	void delete(ArrayList<LayoutItem> layoutItemList);
	
	HashMap<Point,Integer> createLayoutItems(RestaurantLayout restaurantLayout, Long restaurantLayoutID);
	

}
