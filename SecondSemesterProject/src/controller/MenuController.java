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
	
	private MealConcreteDAO mealDAO;
	private MenuMealsConcreteDAO menuMealDAO;
	private MenuConcreteDAO menuDAO;
	
	public MenuController() {
		menuDAO = (MenuConcreteDAO) MenuConcreteDAO.getInstance();
		menuMealDAO = (MenuMealsConcreteDAO) MenuMealsConcreteDAO.getInstance();
		mealDAO = (MealConcreteDAO) MealConcreteDAO.getInstance();
	}
	
	public void deleteMenu(Menu menu) throws SQLException {
		try {
			menuDAO.delete(menu);
		} catch (SQLException e) {
			throw new SQLException("Error in deleting menu from DB:" + e.getMessage());

		}
	}
	
	public Menu getMenuById(int id) throws SQLException {
		try {
			return menuDAO.read(id);
		} catch (SQLException e) {
			throw new SQLException("Error in geting menu from DB:" + e.getMessage());
		}
	}
	
	public void createMenu(Menu menu) throws SQLException {
		try {
			menuDAO.create(menu);
		} catch (SQLException e) {
			throw new SQLException("Error in creatting menu to DB:" + e.getMessage());
		}
	}
	
	public ArrayList<Meal> getMealList() throws SQLException {
		try {
			return mealDAO.read();
		} catch (SQLException e) {
			throw new SQLException("Error in getting meals from DB:" + e.getMessage());
		}
	}
	
	public ArrayList<Menu> getMenuList() throws SQLException {
		try {
			return menuDAO.read();
		} catch (SQLException e) {
			throw new SQLException("Error in getting menus from DB:" + e.getMessage());
		}
	}

}
