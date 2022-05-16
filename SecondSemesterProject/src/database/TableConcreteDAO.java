package database;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import model.Table;

public class TableConcreteDAO {

	private static TableConcreteDAO instance = new TableConcreteDAO();

	private TableConcreteDAO() {
	}

	public static TableConcreteDAO getInstance() {
		if (instance == null) {
			instance = new TableConcreteDAO();
		}
		return instance;
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

}
