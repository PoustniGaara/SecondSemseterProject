package database;

import java.awt.Point;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import model.LayoutItem;

public interface LayoutItemDAO {
	
	Map<Point,LayoutItem> getLayoutItems(long restaurantLayoutID) throws SQLException;
	
	void update(HashMap<Point,LayoutItem> itemMap, long restaurantLayoutID) throws SQLException;

	void delete(ArrayList<LayoutItem> layoutItemList) throws SQLException;
	
	void createLayoutItems(HashMap<Point,LayoutItem> itemMap, long restaurantLayoutID) throws SQLException;
	

}
