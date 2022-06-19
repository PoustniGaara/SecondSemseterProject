package database;

import java.sql.SQLException;
import java.util.ArrayList;

import model.Reservation;
import model.Table;

public interface ReservedTablesDAO {

	void create(Reservation reservation) throws SQLException;

	void update(Reservation reservation) throws SQLException;

	void delete(Reservation reservation) throws SQLException;
	
	ArrayList<Table> getReservationTables(long reservationid) throws SQLException;

}
