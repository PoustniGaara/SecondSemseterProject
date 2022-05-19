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

			Calendar c1 = Calendar.getInstance();
			c1.set(Calendar.HOUR_OF_DAY, 20);
			c1.set(Calendar.MINUTE, 15);
			c1.set(Calendar.SECOND, 0);
			c1.set(Calendar.MILLISECOND, 0);
			java.util.Date dateTime = c1.getTime();
			java.sql.Timestamp timestamp = new java.sql.Timestamp(dateTime.getTime());
			System.out.println("timestamp: " + timestamp.toString());
			ps.setTimestamp(1, timestamp);
			ps.setInt(2, reservation.getDuration());
			ps.setInt(3, reservation.getGuests());
			ps.setString(4, reservation.getNote());
			ps.setString(5, reservation.getCustomer().getPhone());
			ps.execute();

			ResultSet generatedKeys = ps.getGeneratedKeys();
			if (generatedKeys.next()) {
				System.out.println("! KEY: " + generatedKeys.getLong(1));
				return generatedKeys.getLong(1);
			} else {
				throw new SQLException("Creating reservation failed, no ID obtained.");
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
		System.out.println("! NO KEY ! ");
		return null;
	}

	@Override
	public void delete(Reservation reservation) {
		Connection con = DBConnection.getInstance().getDBcon();
		try {
			PreparedStatement ps = con.prepareStatement("DELETE FROM dbo.Reservations WHERE id=?");
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
					"UPDATE dbo.Reservations SET timestamp=?, SET duration=?, SET noOfGuests=?, SET note=?, SET phone=?"
							+ "WHERE id=?");
			Timestamp timestamp = new Timestamp(reservation.getTimestamp().getTimeInMillis());
			ps.setTimestamp(1, timestamp);
			ps.setInt(2, reservation.getDuration());
			ps.setInt(3, reservation.getGuests());
			ps.setString(4, reservation.getNote());
			ps.setString(5, reservation.getCustomer().getPhone());
			ps.setLong(6, reservation.getId());
			ps.execute();

			con.commit();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DBConnection.closeConnection();
		}
	}

}
