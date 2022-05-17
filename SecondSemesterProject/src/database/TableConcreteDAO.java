package database;

import java.awt.Point;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import model.LayoutItem;
import model.RestaurantLayout;
import model.Table;

public class TableConcreteDAO implements TableDAO {

	private static TableConcreteDAO instance = new TableConcreteDAO();

	private TableConcreteDAO() {
	}

	public static TableConcreteDAO getInstance() {
		if (instance == null) {
			instance = new TableConcreteDAO();
		}
		return instance;
	}
	
	@Override
	public void createTables(RestaurantLayout restaurantLayout, ArrayList<Long> idList) {
		HashMap<Point,LayoutItem> itemMap = (HashMap<Point, LayoutItem>) restaurantLayout.getItemMap();
		Connection con = DBConnection.getInstance().getDBcon();
	    try (
	    	 PreparedStatement ps = con.prepareStatement(
	    			"insert into dbo.Tables(layoutItemID,capacity) values(?,?)");) {
	    	for(Point point: idMap.keySet()) {
	    		Table table = (Table) itemMap.get(point);
	    		ps.setInt(4, idMap.get(point));
				ps.setInt(5, table.getCapacity());
	    		ps.addBatch();
	    	}
	    	ps.executeBatch();
	    } catch (SQLException ex) {
	            System.out.println(ex.getMessage());
	        }	
	}

	public ArrayList<Table> getReservationTables(int reservationid) {
		Connection con = DBConnection.getInstance().getDBcon();
		ArrayList<Table> tables = new ArrayList<Table>();

		try {
			Statement tablesStatement = con.createStatement();
			ResultSet tablesResultSet = tablesStatement
					.executeQuery("SELECT * FROM [Tables], ReservedTables, Reservations "
							+ "WHERE [Tables].layoutItemID = ReservedTables.layoutItemId AND ReservedTables.reservationID = "
							+ reservationid);
			while (tablesResultSet.next()) {
				String name = tablesResultSet.getString("name");
				String type = tablesResultSet.getString("type");
				int capacity = tablesResultSet.getInt("capacity");

				Table table = new Table(name, type, capacity);
				tables.add(table);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DBConnection.closeConnection();
		}
		return tables;
	}

	public ArrayList<Table> read() {
		Connection con = DBConnection.getInstance().getDBcon();
		ArrayList<Table> tables = new ArrayList<Table>();

		try {
			Statement tablesStatement = con.createStatement();
			ResultSet tablesResultSet = tablesStatement.executeQuery("SELECT * FROM Tables");
			while (tablesResultSet.next()) {
				String name = tablesResultSet.getString("name");
				String type = tablesResultSet.getString("type");
				int capacity = tablesResultSet.getInt("capacity");

				Table table = new Table(name, type, capacity);
				tables.add(table);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DBConnection.closeConnection();
		}
		return tables;
	}

	public Table read(int id) {
		Connection con = DBConnection.getInstance().getDBcon();

		try {
			Statement tablesStatement = con.createStatement();
			ResultSet tablesResultSet = tablesStatement.executeQuery("SELECT * FROM [Tables] WHERE tableID = " + id);
			while (tablesResultSet.next()) {
				String name = tablesResultSet.getString("name");
				String type = tablesResultSet.getString("type");
				int capacity = tablesResultSet.getInt("capacity");

				Table table = new Table(name, type, capacity);
				return table;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DBConnection.closeConnection();
		}
		return null;
	}

	@Override
	public void create(Table table) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void update(Table table) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void delete(ArrayList<Table> tableList) {
		Connection con = DBConnection.getInstance().getDBcon();
	    try (
	    	 PreparedStatement ps = con.prepareStatement(
	    			"delete from dbo.Tables where layoutItemID = ?");) {
	    	for(Table table: tableList) {
				ps.setLong(1, table.getId());
	    		ps.addBatch();
	    	}
	    	ps.executeBatch();
	    } catch (SQLException ex) {
	            System.out.println(ex.getMessage());
	        }	
	}

	@Override
	public HashMap<Point, LayoutItem> getTableMap(long restaurantLayoutID) {
		HashMap<Point, LayoutItem> tableMap = new HashMap<>();
		try(Connection con = DBConnection.getInstance().getDBcon();
				PreparedStatement ps = con.prepareStatement(" select * \r\n"
						+ "from dbo.LayoutItems \r\n"
						+ "FULL OUTER JOIN dbo.Tables\r\n"
						+ "	ON dbo.LayoutItems.layoutItemID = dbo.Tables.layoutItemID\r\n"
						+ "where restaurantLayoutID = ?");
		){
			ps.setLong(1, restaurantLayoutID);
			ResultSet rs = ps.executeQuery();
			while(rs.next()) {
				if(rs.getString("type").equals("table")) {
					Table table = new Table(rs.getNString("name"), rs.getNString("type"), rs.getInt("capacity"));
					table.setId(rs.getLong("layoutItemID"));
					tableMap.put(new Point(rs.getInt("locationX"),rs.getInt("locationY")),table);
				}
			}
		return tableMap;
		}
		catch(SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public ArrayList<Long> getTableListById(ArrayList<Long> idList) {
		HashMap<Point,LayoutItem> itemMap = (HashMap<Point, LayoutItem>) restaurantLayout.getItemMap();
		Connection con = DBConnection.getInstance().getDBcon();
	    try (
	    	 PreparedStatement ps = con.prepareStatement(
	    			"insert into dbo.Tables(layoutItemID,capacity) values(?,?)");) {
	    	for(Point point: idMap.keySet()) {
	    		Table table = (Table) itemMap.get(point);
	    		ps.setInt(4, idMap.get(point));
				ps.setInt(5, table.getCapacity());
	    		ps.addBatch();
	    	}
	    	ps.executeBatch();
	    } catch (SQLException ex) {
	            System.out.println(ex.getMessage());
	        }	
	    return null;
	}

}
