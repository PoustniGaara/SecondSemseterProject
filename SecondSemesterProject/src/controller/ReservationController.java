package controller;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;

import database.ReservationConcreteDAO;
import model.Customer;
import model.Menu;
import model.Reservation;
import model.Table;

public class ReservationController {

	private ReservationConcreteDAO reservationDAO;
	private Reservation reservation;
	private CustomerController control = new CustomerController();

	// rename to startReservation
	public Reservation startReservation(Calendar timestamp, ArrayList<Table> tables) {
		reservation = new Reservation(timestamp, tables);
		return reservation;
	}

	public void confirmReservation(Customer customer, int guests, ArrayList<Menu> menus, String note)
			throws SQLException {
		reservation.setGuests(guests);
		reservation.setMenus(menus);
		reservation.setNote(note);

		if (customer == null) {
			control.createCustomer(customer);
		}

		reservation.setCustomer(customer);

		reservationDAO.create(reservation);
	}

	public ReservationController() {
		reservationDAO = ReservationConcreteDAO.getInstance();
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

	// rename checkByPhone
	public Customer checkCustomer(String phone) throws SQLException {
		if (control.findByPhone(phone) != null) {
			return control.findByPhone(phone);
		} 
		return null;
	}

}
