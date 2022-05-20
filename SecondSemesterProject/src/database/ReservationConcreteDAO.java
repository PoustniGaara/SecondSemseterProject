package database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

import model.Customer;
import model.Meal;
import model.Menu;
import model.Reservation;
import model.Table;

public class ReservationConcreteDAO implements ReservationDAO {

	private static ReservationConcreteDAO instance = new ReservationConcreteDAO();

	private ReservationConcreteDAO() {
	}

	public static ReservationConcreteDAO getInstance() {
		if (instance == null) {
			instance = new ReservationConcreteDAO();
		}
		return instance;
	}

	@Override
	public ArrayList<Reservation> read() {
		Connection con = DBConnection.getInstance().getDBcon();
		ArrayList<Reservation> reservations = new ArrayList<Reservation>();

		try {
			Statement statement = con.createStatement();
			ResultSet rs = statement.executeQuery("SELECT * FROM Reservations");
			while (rs.next()) {
				int id = rs.getInt("reservationID");
				Timestamp timestamp = rs.getTimestamp("timestamp");
				Calendar cal = new GregorianCalendar();
				cal.setTimeInMillis(timestamp.getTime());

				Reservation reservation = new Reservation(cal, TableConcreteDAO.getInstance().getReservationTables(id));
				reservations.add(reservation);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DBConnection.closeConnection();
		}
		return reservations;
	}

	@Override
	public Reservation read(int id) {
		Connection con = DBConnection.getInstance().getDBcon();

		try {
			Statement statement = con.createStatement();
			ResultSet rs = statement.executeQuery("SELECT * FROM Reservations WHERE reservationID = " + id);
			while (rs.next()) {
				Timestamp timestamp = rs.getTimestamp("timestamp");
				Calendar cal = new GregorianCalendar();
				cal.setTimeInMillis(timestamp.getTime());

				Reservation reservation = new Reservation(cal, TableConcreteDAO.getInstance().getReservationTables(id));
				return reservation;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DBConnection.closeConnection();
		}
		return null;
	}
	
	@Override
	public Reservation readAll(int id) {
		Connection con = DBConnection.getInstance().getDBcon();

		try {
			Statement statement = con.createStatement();
			ResultSet rs = statement.executeQuery("SELECT * FROM Reservations WHERE reservationID = " + id);
			while (rs.next()) {
				Long rID = rs.getLong("reservationID");
				Timestamp timestamp = rs.getTimestamp("timestamp");
				int duration = rs.getInt("duration");
				int guests = rs.getInt("noOfGuests");
				String note = rs.getString("note");
				String phone = rs.getString("customerPhone");
				
				Calendar cal = new GregorianCalendar();
				cal.setTimeInMillis(timestamp.getTime());

				Reservation reservation = new Reservation(cal, TableConcreteDAO.getInstance().getReservationTables(id));
				reservation.setId(rID);
				reservation.setDuration(duration);
				reservation.setGuests(guests);
				reservation.setNote(note);
				reservation.setCustomer(CustomerConcreteDAO.getInstance().read(phone));
				
				return reservation;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DBConnection.closeConnection();
		}
		return null;
	}

	@Override
	public void create(Reservation reservation) throws SQLException {
		Connection con = DBConnection.getInstance().getDBcon();
		try {
			con.setAutoCommit(false);

			Long id = createReservation(reservation);
			reservation.setId(id);
			ReservedTablesConcreteDAO.getInstance().create(reservation);
			ReservedMenusConcreteDAO.getInstance().create(reservation);
			con.commit();
			
		} catch (SQLException e) {
			e.printStackTrace();
			con.rollback();
		} finally {
			con.setAutoCommit(true);
		}
	}

	private Long createReservation(Reservation reservation) {
		Connection con = DBConnection.getInstance().getDBcon();
		try {
			PreparedStatement ps = con.prepareStatement(
					"INSERT INTO dbo.Reservations (timestamp, duration, noOfGuests, note, customerPhone)"
							+ "VALUES (?,?,?,?,?)",
					Statement.RETURN_GENERATED_KEYS);

			java.util.Date dateTime = reservation.getTimestamp().getTime();
			java.sql.Timestamp timestamp = new java.sql.Timestamp(dateTime.getTime());
			ps.setTimestamp(1, timestamp);
			ps.setInt(2, reservation.getDuration());
			ps.setInt(3, reservation.getGuests());
			ps.setString(4, reservation.getNote());
			ps.setString(5, reservation.getCustomer().getPhone());
			ps.execute();

			ResultSet generatedKeys = ps.getGeneratedKeys();
			if (generatedKeys.next()) {
				return generatedKeys.getLong(1);
			} else {
				throw new SQLException("Creating reservation failed, no ID obtained.");
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public void delete(Reservation reservation) {
		Connection con = DBConnection.getInstance().getDBcon();
		try {
			PreparedStatement ps = con.prepareStatement("DELETE FROM dbo.Reservations WHERE reservationID=?");
			ps.setLong(1, reservation.getId());
			ps.execute();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DBConnection.closeConnection();
		}
	}

	@Override
	public void update(Reservation reservation) {
		Connection con = DBConnection.getInstance().getDBcon();
		try {
			PreparedStatement ps = con.prepareStatement(
					"UPDATE dbo.Reservations SET timestamp=?, duration=?, noOfGuests=?, note=?, customerPhone=? WHERE reservationID=?");
			java.util.Date dateTime = reservation.getTimestamp().getTime();
			java.sql.Timestamp timestamp = new java.sql.Timestamp(dateTime.getTime());
			ps.setTimestamp(1, timestamp);
			ps.setInt(2, reservation.getDuration());
			ps.setInt(3, reservation.getGuests());
			ps.setString(4, reservation.getNote());
			ps.setString(5, reservation.getCustomer().getPhone());
			ps.setLong(6, reservation.getId());
			ps.execute();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DBConnection.closeConnection();
		}
	}

}
