package database;

import java.sql.SQLException;
import java.util.ArrayList;

import model.Reservation;

public interface ReservationDAO {

	ArrayList<Reservation> read() throws SQLException;

	Reservation read(int id) throws SQLException;
	
	void create(Reservation reservation) throws SQLException;

	void update(Reservation reservation) throws SQLException;

	void delete(Reservation reservation) throws SQLException;

}
