package controller;

import java.sql.SQLException;
import java.util.ArrayList;

import database.MealConcreteDAO;
import database.MealDAO;
import database.MenuConcreteDAO;
import database.MenuDAO;
import database.MenuMealsConcreteDAO;
import database.MenuMealsDAO;
import model.Meal;
import model.Menu;

public class MenuController {
	
	private MealDAO mealDAO;
	private MenuMealsDAO menuMealDAO;
	private MenuDAO menuDAO;
	
	public MenuController() {
		menuDAO = MenuConcreteDAO.getInstance();
		menuMealDAO = MenuMealsConcreteDAO.getInstance();
		mealDAO = MealConcreteDAO.getInstance();
	}
	
	public ArrayList<Meal> getMealList() throws SQLException {
		try {
			return mealDAO.read();
		} catch (SQLException e) {
			throw new SQLException("Error in getting RestaurantLayouts from DB:" + e.getMessage());
		}
	}
	
	public ArrayList<Menu> getMenuList() throws SQLException {
		try {
			return menuDAO.read();
		} catch (SQLException e) {
			throw new SQLException("Error in getting RestaurantLayouts from DB:" + e.getMessage());
		}
	}

}
