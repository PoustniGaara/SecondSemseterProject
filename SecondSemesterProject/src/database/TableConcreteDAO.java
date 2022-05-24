package database;

import java.awt.Point;
import java.sql.BatchUpdateException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;

import model.LayoutItem;
import model.Table;

public class TableConcreteDAO implements TableDAO {

	private static TableDAO instance = new TableConcreteDAO();

	private TableConcreteDAO() {
	}

	public static TableDAO getInstance() {
		if (instance == null) {
			instance = new TableConcreteDAO();
		}
		return instance;
	}

	@Override
	public void createTables(HashMap<Point, LayoutItem> itemMap, long restaurantLayoutID) throws SQLException,BatchUpdateException {
		ArrayList<Table> tableList = getTableList(itemMap, restaurantLayoutID);
		Connection con = DBConnection.getInstance().getDBcon();
		try (PreparedStatement ps = con
				.prepareStatement("insert into dbo.Tables(layoutItemID,capacity) values(?,?)");) {
			con.setAutoCommit(false);
			for (Table table : tableList) {
				ps.setLong(1, table.getId());
				ps.setInt(2, table.getCapacity());
				ps.addBatch();
			}
			try {
    			ps.executeBatch();
    			con.commit();
    			}
    		 catch(BatchUpdateException e){
    		    con.rollback();
    		    throw new BatchUpdateException("Error in batching", e.getUpdateCounts());
    		    }
		} catch (SQLException e) {
			e.printStackTrace();
			throw new SQLException("Error in creating Tables:" + e.getMessage());
		}
		finally {
			con.setAutoCommit(true);
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
			throw new SQLException("Error in getting Tables:" + e.getMessage());
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
			throw new SQLException("Error in getting Table:" + e.getMessage());
		}
		return null;
	}

	@Override
	public void update(ArrayList<Table> tableList) throws SQLException, BatchUpdateException {
		Connection con = DBConnection.getInstance().getDBcon();
		try (PreparedStatement ps = con.prepareStatement("update dbo.Tables SET capacity = ? where layoutItemID = ?");) {
			con.setAutoCommit(false);
			for (Table table : tableList) {
				ps.setInt(1, table.getCapacity());
				ps.setLong(2, table.getId());
				ps.addBatch();
			}
    		try {
    			ps.executeBatch();
    			con.commit();
    			}
    		 catch(BatchUpdateException e){
    		    con.rollback();
    		    throw new BatchUpdateException("Error in batching", e.getUpdateCounts());
    		    }
		} catch (SQLException e) {
			e.printStackTrace();
			throw new SQLException("Error in updating Tables:" + e.getMessage());
		}
		finally {
			con.setAutoCommit(true);
		}
	}

	@Override
	public void delete(ArrayList<Table> tableList) throws SQLException,BatchUpdateException {
		Connection con = DBConnection.getInstance().getDBcon();
		try (PreparedStatement ps = con.prepareStatement("delete from dbo.Tables where layoutItemID = ?");) {
			con.setAutoCommit(false);
			for (Table table : tableList) {
				ps.setLong(1, table.getId());
				ps.addBatch();
			}
    		try {
    			ps.executeBatch();
    			con.commit();
    			}
    		 catch(BatchUpdateException e){
    		    con.rollback();
    		    throw new BatchUpdateException("Error in batching", e.getUpdateCounts());
    		    }
		} catch (SQLException e) {
			e.printStackTrace();
			throw new SQLException("Error in deleting Table:" + e.getMessage());
		}
		finally {
			con.setAutoCommit(true);
		}
	}

	@Override
	public HashMap<Point, LayoutItem> getTableMap(long restaurantLayoutID) throws SQLException {
		HashMap<Point, LayoutItem> tableMap = new HashMap<>();
		Connection con = DBConnection.getInstance().getDBcon();
		try (PreparedStatement ps = con.prepareStatement(" select * \r\n" + "from dbo.LayoutItems \r\n"
				+ "FULL OUTER JOIN dbo.Tables\r\n" + "	ON dbo.LayoutItems.layoutItemID = dbo.Tables.layoutItemID\r\n"
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
			throw new SQLException("Error in updating Tables:" + e.getMessage());
		}
	}

	@Override
	public ArrayList<Table> getTableList(HashMap<Point, LayoutItem> itemMap, long restaurantLayoutID)
			throws SQLException {
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
			throw new SQLException("Error in getting TableList:" + e.getMessage());
		}
	}
}
