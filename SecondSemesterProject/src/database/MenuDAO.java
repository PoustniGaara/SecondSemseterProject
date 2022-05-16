package database;

import java.util.ArrayList;

import model.Menu;

public interface MenuDAO {

	ArrayList<Menu> read();

	ArrayList<Menu> getReservationMenus(int reservationId);

	Menu read(int id);

	void create(Menu menu);

	void update(Menu menu);

	void delete(Menu menu);

}
