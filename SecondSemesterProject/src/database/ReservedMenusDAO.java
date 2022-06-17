package database;

import java.sql.SQLException;
import java.util.ArrayList;

import model.Menu;
import model.Reservation;

public interface ReservedMenusDAO {

	void create(Reservation reservation) throws SQLException;

	void update(Reservation reservation);

	void delete(Reservation reservation);
	
	ArrayList<Menu> getReservationMenus(long reservationId) throws SQLException;

}
