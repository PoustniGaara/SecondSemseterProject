package database;

import java.awt.Point;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

import model.LayoutItem;
import model.Table;

public interface TableDAO {

	ArrayList<Table> read() throws SQLException;

	Table read(int id) throws SQLException;

	ArrayList<Table> getTableList(HashMap<Point, LayoutItem> itemMap, long restaurantLayoutID) throws SQLException;

	void update(ArrayList<Table> tableList) throws SQLException;

	void delete(ArrayList<Table> tableList) throws SQLException;

	void createTables(HashMap<Point, LayoutItem> itemMap, long restaurantLayoutID) throws SQLException;

	HashMap<Point, LayoutItem> getTableMap(long restaurantLayoutID) throws SQLException;

}
