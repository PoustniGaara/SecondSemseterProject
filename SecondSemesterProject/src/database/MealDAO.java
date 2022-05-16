package database;

import java.util.ArrayList;

import model.Meal;

public interface MealDAO {

	ArrayList<Meal> read();

	ArrayList<Meal> getMenuMeals(int menuId);

	Meal read(int id);

	void create(Meal meal);

	void update(Meal meal);

	void delete(Meal meal);

} 
