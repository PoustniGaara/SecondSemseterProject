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

	public void createReservation(Calendar timestamp, ArrayList<Table> tables) {
		reservation = new Reservation(timestamp, tables);
	}

	public void confirmReservation(String name, String surname, String phone, int guests, ArrayList<Menu> menus, String note) throws SQLException {
		reservation.setGuests(guests);
		reservation.setMenus(menus);
		reservation.setNote(note);
		
		if(setPhone(phone) != null) {
			reservation.setCustomer(setPhone(phone));
		} else {
			control.createCustomer(new Customer (name, surname, null, phone, null, null, null, null));
		}
		
		createReservation(reservation);
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

	private void createReservation(Reservation reservation) throws SQLException {
		reservationDAO.create(reservation);
	}

	public void updateReservation(Reservation reservation) {
		reservationDAO.update(reservation);
	}

	public void deleteReservation(Reservation reservation) {
		reservationDAO.delete(reservation);
	}
	
	private Customer setPhone(String phone) {
		if (control.findByPhone(phone) != null) {
			return control.findByPhone(phone);
		}
		return null;
	}

}
