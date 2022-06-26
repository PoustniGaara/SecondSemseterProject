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

	public void confirmReservation(Customer customer, int guests, int duration, ArrayList<Menu> menus, String note)
			throws SQLException {
		reservation.setGuests(guests);
		reservation.setMenus(menus);
		reservation.setNote(note);
		reservation.setDuration(duration);

		if (!isCustomer) {
			control.createCustomer(customer);
		}

		reservation.setCustomer(customer);
		reservationDAO.create(reservation);
	}

	public ReservationController() {
		reservationDAO = (ReservationConcreteDAO) ReservationConcreteDAO.getInstance();
	}

	public Reservation getReservationById(long id) throws SQLException {
		return reservationDAO.read(id);
	}

	public ArrayList<Reservation> getReservationByCustomer(String customerName) throws SQLException {
		return reservationDAO.readByCustomer(customerName);
	}

	public ArrayList<Reservation> getAllReservations() throws SQLException {
		return reservationDAO.read();
	}

	public void updateReservation(Reservation reservation) throws SQLException {
		reservationDAO.update(reservation);
	}

	public void deleteReservation(int reservationId) throws SQLException {
		reservationDAO.delete(reservationId);
	}

	public Customer checkCustomer(String phone) throws SQLException {
		Customer c = control.findByPhone(phone);
		if (c != null) {
			isCustomer = true;
			return c;
		}
		return c;
	}

	public ArrayList<ReservedTableInfo> getReservedTableInfo(int layoutItemId, Calendar calendar, int duration)
			throws SQLException {
		try {
			return ReservedTablesConcreteDAO.getInstance().getReservedTableInfoByTime(layoutItemId, calendar, duration);
		} catch (SQLException e) {
			throw new SQLException("Error getting Info from DB:" + e.getMessage());
		}
	}

}
