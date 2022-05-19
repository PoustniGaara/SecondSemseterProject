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
	
	ArrayList<Table> getTableList(HashMap<Point,LayoutItem> itemMap, long restaurantLayoutID);

	void update(ArrayList<Table> tableList);

	void delete(ArrayList<Table> tableList);
	
	void createTables(HashMap<Point,LayoutItem> itemMap, long restaurantLayoutID);
	
	HashMap<Point, LayoutItem> getTableMap(long restaurantLayoutID);
	

}
