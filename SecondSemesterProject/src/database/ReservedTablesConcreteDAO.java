package database;

import java.sql.BatchUpdateException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

import model.Reservation;
import model.ReservedTableInfo;
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

	@Override
	public synchronized ArrayList<ReservedTableInfo> getReservedTableInfoByTime(int restaurantLayoutId, Calendar calendar) throws SQLException {
//		calendar.add(Calendar.MONTH, -1);
		Connection con = DBConnection.getInstance().getDBcon();
		ArrayList<ReservedTableInfo> list = new ArrayList<>();
		try {
			PreparedStatement ps = con.prepareStatement("select * from LayoutItems \r\n"
					+ "JOIN ReservedTables on LayoutItems.layoutItemID = ReservedTables.layoutItemID\r\n"
					+ "JOIN reservations on ReservedTables.reservationID = Reservations.reservationID\r\n"
					+ "JOIN Customers on Reservations.customerPhone = Customers.phone \r\n"
					+ "where restaurantLayoutID = ? AND timestamp BETWEEN ? AND ?");
			
			ps.setInt(1, restaurantLayoutId);
			
			java.util.Date dateTime = calendar.getTime();
			java.sql.Timestamp timestamp = new java.sql.Timestamp(dateTime.getTime());
			ps.setTimestamp(2, timestamp);
			System.out.println("timestamp 1 :" + timestamp.toString());
			Calendar calendar2 = (Calendar) calendar.clone(); // Get just 1 day in advance info 
			calendar2.add(Calendar.DAY_OF_MONTH, +1);
			java.util.Date dateTime2 = calendar2.getTime();
			java.sql.Timestamp timestamp2 = new java.sql.Timestamp(dateTime2.getTime());
			ps.setTimestamp(3, timestamp2);
			System.out.println("timestamp 2 :" + timestamp2.toString());
			
			ResultSet rs = ps.executeQuery();
			System.out.println("Resulset is :" + rs.next());
			while (rs.next()) {
				Timestamp timestampDB = rs.getTimestamp("timestamp");
				String name = rs.getString("name");
				String note = rs.getString("note");
				String phone = rs.getString("phone");
				int duration = rs.getInt("duration");
				int id = rs.getInt("reservationID");
				int layoutItemId = rs.getInt("layoutItemID");
				
				Calendar cal = new GregorianCalendar();
				cal.setTimeInMillis(timestampDB.getTime());
				
				ReservedTableInfo reservedTableInfo = new ReservedTableInfo(cal, name, phone, note, duration,id,layoutItemId);
				list.add(reservedTableInfo);
			}
			return list;
		} catch (SQLException e) {
			e.printStackTrace();
			throw new SQLException("Error in getting reservationTables:" + e.getMessage());
		}
	}

}
