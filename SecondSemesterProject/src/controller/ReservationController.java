package controller;

import java.util.ArrayList;
import java.util.Calendar;

import model.Customer;
import model.Menu;
import model.Reservation;
import model.Table;

public class ReservationController {

	private ReservationConcreteDAO reservationDAO;
	private Reservation reservation;

	public void createReservation(Calendar timestamp, ArrayList<Table> tables) {
		reservation = new Reservation(timestamp, tables);
	}

	public void confirmReservation(String name, String surname, String phone, int guests, ArrayList<Menu> menus,
			String note) {
		reservation.setCustomer(null);
		reservation.setGuests(guests);
		reservation.setMenus(menus);
		reservation.setNote(note);
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

	public void createReservation(Reservation reservation) {
		reservationDAO.create(reservation);
	}

	public void updateReservation(Reservation reservation) {
		reservationDAO.update(reservation);
	}

	public void deleteReservation(Reservation reservation) {
		reservationDAO.delete(reservation);
	}

}
