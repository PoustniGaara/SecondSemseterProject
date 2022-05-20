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
	public void createTables(HashMap<Point, LayoutItem> itemMap, long restaurantLayoutID) throws SQLException {
		ArrayList<Table> tableList = getTableList(itemMap, restaurantLayoutID);
		Connection con = DBConnection.getInstance().getDBcon();
		try (PreparedStatement ps = con
				.prepareStatement("insert into dbo.Tables(layoutItemID,capacity) values(?,?)");) {
			for (Table table : tableList) {
				ps.setLong(1, table.getId());
				ps.setInt(2, table.getCapacity());
				ps.addBatch();
			}
			ps.executeBatch();
		} catch (SQLException e) {
			e.printStackTrace();
			throw new SQLException("Error in creating Tables:"+ e.getMessage());
		}
	}

	public ArrayList<Table> getReservationTables(int reservationid) throws SQLException {
		Connection con = DBConnection.getInstance().getDBcon();
		ArrayList<Table> tables = new ArrayList<Table>();

		try {
			Statement tablesStatement = con.createStatement();
			ResultSet tablesResultSet = tablesStatement.executeQuery(
					"SELECT * FROM Reservations JOIN ReservedTables ON Reservations.reservationID = ReservedTables.reservationID JOIN Tables ON ReservedTables.layoutItemID = Tables.layoutItemID JOIN LayoutItems ON Tables.layoutItemID = LayoutItems.layoutItemID WHERE ReservedTables.reservationID = "
							+ reservationid);
			while (tablesResultSet.next()) {
				String name = tablesResultSet.getString("name");
				String type = tablesResultSet.getString("type");
				int capacity = tablesResultSet.getInt("capacity");

				Table table = new Table(name, type, capacity);
				tables.add(table);
			}
			return tables;
		} catch (SQLException e) {
			e.printStackTrace();
			throw new SQLException("Error in getting reservationTables:"+ e.getMessage());
		}
	}

	public ArrayList<Table> read() throws SQLException {
		Connection con = DBConnection.getInstance().getDBcon();
		ArrayList<Table> tables = new ArrayList<Table>();
		try {
			Statement tablesStatement = con.createStatement();
			ResultSet tablesResultSet = tablesStatement.executeQuery(
					"SELECT * FROM Tables, LayoutItems WHERE Tables.layoutItemID = LayoutItems.layoutItemID ");
			while (tablesResultSet.next()) {
				Long id = tablesResultSet.getLong("layoutItemID");
				String name = tablesResultSet.getString("name");
				String type = tablesResultSet.getString("type");
				int capacity = tablesResultSet.getInt("capacity");

				Table table = new Table(name, type, capacity);
				table.setId(id);
				tables.add(table);
			}
			return tables;
		} catch (SQLException e) {
			e.printStackTrace();
			throw new SQLException("Error in getting Tables:"+ e.getMessage());
		}
	}

	public Table read(int id) throws SQLException {
		Connection con = DBConnection.getInstance().getDBcon();
		try {
			Statement tablesStatement = con.createStatement();
			ResultSet tablesResultSet = tablesStatement.executeQuery(
					"SELECT * FROM [Tables], LayoutItems WHERE [Tables].layoutItemID = LayoutItems.layoutItemId AND LayoutItems.layoutItemID = "
							+ id);
			while (tablesResultSet.next()) {
				String name = tablesResultSet.getString("name");
				String type = tablesResultSet.getString("type");
				int capacity = tablesResultSet.getInt("capacity");

				Table table = new Table(name, type, capacity);
				table.setId(id);
				return table;
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
			throw new SQLException("Error in getting Table:"+ e.getMessage());
		}
		return null;
	}

	@Override
	public void update(ArrayList<Table> tableList) throws SQLException {
		Connection con = DBConnection.getInstance().getDBcon();
		try (PreparedStatement ps = con.prepareStatement("update dbo.Tables capacity = ? where layoutItemID = ?");) {
			for (Table table : tableList) {
				ps.setInt(1, table.getCapacity());
				ps.setLong(2, table.getId());
				ps.addBatch();
			}
			ps.executeBatch();
		} catch (SQLException e) {
			e.printStackTrace();
			throw new SQLException("Error in updating Tables:"+ e.getMessage());
		}
	}

	@Override
	public void delete(ArrayList<Table> tableList) throws SQLException {
		Connection con = DBConnection.getInstance().getDBcon();
		try (PreparedStatement ps = con.prepareStatement("delete from dbo.Tables where layoutItemID = ?");) {
			for (Table table : tableList) {
				ps.setLong(1, table.getId());
				ps.addBatch();
			}
			ps.executeBatch();
		} catch (SQLException e) {
			e.printStackTrace();
			throw new SQLException("Error in deleting Table:"+ e.getMessage());
		}
	}

	@Override
	public HashMap<Point, LayoutItem> getTableMap(long restaurantLayoutID) throws SQLException {
		HashMap<Point, LayoutItem> tableMap = new HashMap<>();
		try (Connection con = DBConnection.getInstance().getDBcon();
				PreparedStatement ps = con.prepareStatement(
						" select * \r\n" + "from dbo.LayoutItems \r\n" + "FULL OUTER JOIN dbo.Tables\r\n"
								+ "	ON dbo.LayoutItems.layoutItemID = dbo.Tables.layoutItemID\r\n"
								+ "where restaurantLayoutID = ?");) {
			ps.setLong(1, restaurantLayoutID);
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				if (rs.getString("type").equals("table")) {
					Table table = new Table(rs.getString("name"), rs.getString("type"), rs.getInt("capacity"));
					table.setId(rs.getLong("layoutItemID"));
					tableMap.put(new Point(rs.getInt("locationX"), rs.getInt("locationY")), table);
				}
			}
			return tableMap;
		} catch (SQLException e) {
			e.printStackTrace();
			throw new SQLException("Error in updating Tables:"+ e.getMessage());
		}
	}

	@Override
	public ArrayList<Table> getTableList(HashMap<Point, LayoutItem> itemMap, long restaurantLayoutID) throws SQLException {
		ArrayList<Table> tableList = new ArrayList<>();
		Connection con = DBConnection.getInstance().getDBcon();
		try (PreparedStatement ps = con
				.prepareStatement("select * from dbo.LayoutItems where restaurantLayoutID = ?  and type = ?");) {
			ps.setLong(1, restaurantLayoutID);
			ps.setString(2, "table");
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				Table table = (Table) itemMap.get(new Point(rs.getInt("locationX"), rs.getInt("locationY")));
				table.setId(rs.getLong("layoutItemID"));
				tableList.add(table);
			}
			return tableList;
		} catch (SQLException e) {
			e.printStackTrace();
			throw new SQLException("Error in getting TableList:"+ e.getMessage());
		}
	}

}
