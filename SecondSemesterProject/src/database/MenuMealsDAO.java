package database;

import java.sql.SQLException;
import java.util.ArrayList;

import model.Meal;
import model.Menu;

public interface MenuMealsDAO {

	void create(Menu menu, ArrayList<Meal> meals);

	void update(Menu menu);

	void delete(Menu menu);
	
	ArrayList<Meal> getMenuMeals(int menuId) throws SQLException;

}
