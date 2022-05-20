package database;

import java.sql.SQLException;
import java.util.ArrayList;

import model.Menu;

public interface MenuDAO {

	ArrayList<Menu> read() throws SQLException;

	ArrayList<Menu> getReservationMenus(int reservationId) throws SQLException;

	Menu read(int id) throws SQLException;

	void create(Menu menu);

	void update(Menu menu);

	void delete(Menu menu);

}
