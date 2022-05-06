package database;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

import model.Customer;
import model.Reservation;

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
		ArrayList<Reservation> reservations = new ArrayList<Reservation>();
		Connection con = DBConnection.getInstance().getDBcon();
		try {
			Statement statement = con.createStatement();
			ResultSet rs = statement.executeQuery("SELECT * FROM dbo.Reservations");
			while (rs.next()) {
				int id = rs.getInt("reservationID");
				Timestamp timestamp = rs.getTimestamp("timestamp");
				int duration = rs.getInt("duration");
				int guests = rs.getInt("noOfGuests");
				String note = rs.getString("note");
				String phone = rs.getString("phone");
				Calendar cal = new GregorianCalendar();
				cal.setTimeInMillis(timestamp.getTime());
				
				Reservation reservation = new Reservation(cal, null)
				Customer customer = new Customer(name, surname, phone, email, town, zipcode, street, streetNumber);
				customers.add(customer);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DBConnection.closeConnection();
		}
		return customers;
	}

	@Override
	public Reservation read(int id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void create(Reservation reservation) {
		// TODO Auto-generated method stub

	}

	@Override
	public void delete(Reservation reservation) {
		// TODO Auto-generated method stub

	}

}
