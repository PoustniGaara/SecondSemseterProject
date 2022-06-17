package database;

import java.sql.BatchUpdateException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import model.Reservation;
import model.Table;

public class ReservedTablesConcreteDAO implements ReservedTablesDAO {

	private static ReservedTablesDAO instance = new ReservedTablesConcreteDAO();

	public static ReservedTablesDAO getInstance() {
		if (instance == null) {
			instance = new ReservedTablesConcreteDAO();
		}
		return instance;
	}

	@Override
	public void create(Reservation reservation) throws SQLException, BatchUpdateException {
		Connection con = DBConnection.getInstance().getDBcon();

		try {
			PreparedStatement ps = con
					.prepareStatement("INSERT INTO dbo.ReservedTables (layoutItemID, reservationID) VALUES (?,?)");
			con.setAutoCommit(false);
			for (Table table : reservation.getTables()) {
				ps.setLong(1, table.getId());
				ps.setLong(2, reservation.getId());
				ps.addBatch();
			}
			try {
				ps.executeBatch();
				con.commit();
			} catch (BatchUpdateException e) {
				con.rollback();
				throw new BatchUpdateException("Error in batching", e.getUpdateCounts());
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new SQLException("Error in getting RestaurantLayouts from DB:" + e.getMessage());
		} finally {
			con.setAutoCommit(true);
		}
	}

	@Override
	public ArrayList<Table> getReservationTables(long reservationid) throws SQLException {
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
			throw new SQLException("Error in getting reservationTables:" + e.getMessage());
		}
	}

	@Override
	public void update(Reservation reservation) throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public void delete(Reservation reservation) throws SQLException {
		// TODO Auto-generated method stub

	}

}
