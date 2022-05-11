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
	public void create(Reservation reservation) throws SQLException {
		Connection con = DBConnection.getInstance().getDBcon();
		try{
			con.setAutoCommit(false);
			// do 
			
			PreparedStatement ps = con
					.prepareStatement("INSERT INTO dbo.Reservations (timestamp, duration, noOfGuests, note, phone)"
							+ "VALUES (?,?,?,?,?)");
			ps.setTimestamp(1, reservation.getTimestamp());
			ps.setInt(2, reservation.getDuration());
			ps.setInt(3, reservation.getGuests());
			ps.setString(4, reservation.getNote());
			ps.setString(5, reservation.getCustomer().getPhone());
			ps.execute();
			
			con.commit();
		}
		catch(SQLException e){
			e.printStackTrace();
			con.rollback();
		}
		finally{
			con.close();
		}
	}

	@Override
	public void delete(Reservation reservation) {
		Connection con = DBConnection.getInstance().getDBcon();
		try { 
			PreparedStatement ps = con
					.prepareStatement("DELETE FROM dbo.Reservations WHERE id=?");
			ps.setInt(1, reservation.getId());
			ps.execute();
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	@Override
	public void update(Reservation reservation) {
		Connection con = DBConnection.getInstance().getDBcon();
		try{
			con.setAutoCommit(false);
			// do 
			
			PreparedStatement ps = con
					.prepareStatement("UPDATE dbo.Reservations SET timestamp=?, SET duration=?, SET noOfGuests=?, SET note=?, SET phone=?"
							+ "WHERE id=?");
			ps.setTimestamp(1, reservation.getTimestamp());
			ps.setInt(2, reservation.getDuration());
			ps.setInt(3, reservation.getGuests());
			ps.setString(4, reservation.getNote());
			ps.setString(5, reservation.getCustomer().getPhone());
			ps.setInt(6, reservation.getId());
			ps.execute();
			
			con.commit();
		}
		catch(SQLException e){
			e.printStackTrace();
			con.rollback();
		}
		finally{
			con.close();
		}	
	}

}
