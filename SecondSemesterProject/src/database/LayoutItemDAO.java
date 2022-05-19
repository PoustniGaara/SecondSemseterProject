package database;

import java.awt.Point;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import model.LayoutItem;
import model.RestaurantLayout;

public interface LayoutItemDAO {
	
	Map<Point,LayoutItem> getLayoutItems(long restaurantLayoutID);
	
	void update(HashMap<Point,LayoutItem> itemMap, long restaurantLayoutID);

	void delete(ArrayList<LayoutItem> layoutItemList);
	
	void createLayoutItems(HashMap<Point,LayoutItem> itemMap, long restaurantLayoutID);
	

}
