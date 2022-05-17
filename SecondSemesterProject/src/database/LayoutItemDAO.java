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
	
	ArrayList<Long> createLayoutItems(RestaurantLayout restaurantLayout, Long restaurantLayoutID);
	

}
