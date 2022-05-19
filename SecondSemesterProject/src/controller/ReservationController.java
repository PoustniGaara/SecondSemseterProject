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

	public Reservation createReservation(Calendar timestamp, ArrayList<Table> tables) {
		reservation = new Reservation(timestamp, tables);
		return reservation;
	}

	public void confirmReservation(Customer customer, int guests, ArrayList<Menu> menus, String note)
			throws SQLException {
		reservation.setGuests(guests);
		reservation.setMenus(menus);
		reservation.setNote(note);

		if (customer != null) {
			reservation.setCustomer(customer);
		} else {
			control.createCustomer(customer);
		}
		System.out.println(reservation.getCustomer().getName() + ", " + reservation.getTables().toString());
		reservationDAO.create(reservation);
	}

	public ReservationController() {
		reservationDAO = ReservationConcreteDAO.getInstance();
	}

	public Reservation getReservationById(int id) {
		return reservationDAO.read(id);
	}

	public ArrayList<Reservation> getAllReservations() {
		return reservationDAO.read();
	}

	public void updateReservation(Reservation reservation) {
		reservationDAO.update(reservation);
	}

	public void deleteReservation(Reservation reservation) {
		reservationDAO.delete(reservation);
	}

	public Customer setPhone(String phone) {
		if (control.findByPhone(phone) != null) {
			return control.findByPhone(phone);
		}
		return null;
	}

}
