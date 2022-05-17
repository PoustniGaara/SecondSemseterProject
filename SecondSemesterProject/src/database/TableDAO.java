package database;

import java.awt.Point;
import java.util.ArrayList;
import java.util.HashMap;

import model.LayoutItem;
import model.RestaurantLayout;
import model.Table;

public interface TableDAO {

	ArrayList<Table> read();
	
	ArrayList<Table> getReservationTables(int reservationId);

	Table read(int id);
	
	ArrayList<Long> getTableListById(ArrayList<Long> idList);

	void create(Table table);

	void update(Table table);

	void delete(ArrayList<Table> tableList);
	
	void createTables(RestaurantLayout restaurantLayout,ArrayList<Long> idList);
	
	HashMap<Point, LayoutItem> getTableMap(long restaurantLayoutID);
	

}
