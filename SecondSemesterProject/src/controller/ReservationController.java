package controller;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Calendar;

import javax.swing.JComponent;

import database.ReservationConcreteDAO;
import database.ReservedTablesConcreteDAO;
import database.TableConcreteDAO;
import model.Customer;
import model.Menu;
import model.Reservation;
import model.ReservedTableInfo;
import model.Table;

public class ReservationController {

	private ReservationConcreteDAO reservationDAO;
	private Reservation reservation;
	private boolean isCustomer = false;
	private CustomerController control = new CustomerController();

	public Reservation startReservation(Calendar timestamp, ArrayList<Table> tables) {
		reservation = new Reservation(timestamp, tables);
		return reservation;
	}
	
	public ArrayList<ReservedTableInfo> getReservedTableInfo(int layoutItemId, Calendar calendar) throws SQLException{
		try {
			return ReservedTablesConcreteDAO.getInstance().getReservedTableInfoByTime(layoutItemId, calendar);
		} catch (SQLException e) {
			throw new SQLException("Error getting Info from DB:" + e.getMessage());
		}
	}

	public void confirmReservation(Customer customer, int guests, ArrayList<Menu> menus, String note)
			throws SQLException {
		reservation.setGuests(guests);
		reservation.setMenus(menus);
		reservation.setNote(note);

		if (!isCustomer) {
			control.createCustomer(customer);
		}

		reservation.setCustomer(customer);
		reservationDAO.create(reservation);
	}

	public ReservationController() {
		reservationDAO = (ReservationConcreteDAO) ReservationConcreteDAO.getInstance();
	}

	public Reservation getReservationById(int id) throws SQLException {
		return reservationDAO.read(id);
	}

	public ArrayList<Reservation> getAllReservations() throws SQLException {
		return reservationDAO.read();
	}

	public void updateReservation(Reservation reservation) throws SQLException {
		reservationDAO.update(reservation);
	}

	public void deleteReservation(Reservation reservation) throws SQLException {
		reservationDAO.delete(reservation);
	}

	public Customer checkCustomer(String phone) throws SQLException {
		Customer c = control.findByPhone(phone);
		if (c != null) {
			isCustomer = true;
			return c;
		}
		return c;
	}

	/**
	 * Returns a list of tables available in the 2 hour window of the date and time
	 * in the time parameter.
	 *
	 * @param time Calendar object with the date and time of reservation.
	 *
	 * @return Arraylist of Tables
	 */
	public ArrayList<Table> getAvailableTables(Calendar time) {
		ArrayList<Table> tables = null;
		ArrayList<Reservation> reservations = null;

		try {
			tables = TableConcreteDAO.getInstance().read();
			reservations = reservationDAO.read();
		} catch (SQLException e) {
			e.printStackTrace();
		}

		for (Reservation r : reservations) {
			//LocalDateTime rtime = LocalDateTime.ofEpochSecond(r.getTimestamp(), 0, null)
			if (Math.abs(r.getTimestamp().getTimeInMillis() - time.getTimeInMillis()) > r.getDuration() * 3600000) {
				for (Table t : r.getTables()) {
					tables.remove(t);
				}
			}
		}
		return tables;
	}

}
