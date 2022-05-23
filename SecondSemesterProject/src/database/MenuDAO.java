package database;

import java.sql.SQLException;
import java.util.ArrayList;

import model.Menu;

public interface MenuDAO {

	ArrayList<Menu> read() throws SQLException;

	Menu read(int id) throws SQLException;

	void create(Menu menu);

	void delete(Menu menu);

	void update(ArrayList<Menu> menus) throws SQLException;
	
}
