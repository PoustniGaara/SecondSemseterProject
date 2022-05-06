package database;

import java.util.ArrayList;

import model.Reservation;

public interface ReservationDAO {

	ArrayList<Reservation> read();

	Reservation read(int id);

	void create(Reservation reservation);

	void update(Reservation reservation);

	void delete(Reservation reservation);

}
