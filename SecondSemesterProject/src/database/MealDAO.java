package database;

import java.sql.SQLException;
import java.util.ArrayList;

import model.Meal;

public interface MealDAO {

	ArrayList<Meal> read() throws SQLException;

	Meal read(int id) throws SQLException;

	void create(Meal meal) throws SQLException;

	void update(Meal meal);

	void delete(Meal meal) throws SQLException;

} 
