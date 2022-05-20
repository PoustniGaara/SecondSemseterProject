package database;

import java.sql.SQLException;
import java.util.ArrayList;

import model.Meal;

public interface MealDAO {

	ArrayList<Meal> read() throws SQLException;

	ArrayList<Meal> getMenuMeals(int menuId) throws SQLException;

	Meal read(int id) throws SQLException;

	void create(Meal meal);

	void update(Meal meal);

	void delete(Meal meal);

} 
