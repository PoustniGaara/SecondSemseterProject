package database;

import java.sql.SQLException;
import java.util.ArrayList;

import model.Reservation;

public interface ReservationDAO {

	ArrayList<Reservation> read();

	Reservation read(int id);
	
	Reservation readAll(int id);

	void create(Reservation reservation) throws SQLException;

	void update(Reservation reservation);

	void delete(Reservation reservation);

}
