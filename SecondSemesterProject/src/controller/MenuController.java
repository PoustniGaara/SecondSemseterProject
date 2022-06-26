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
		mealDAO =  MealConcreteDAO.getInstance();
	}
	
	public void deleteMeal(Meal meal) throws SQLException {
		try {
			mealDAO.delete(meal);
		} catch (SQLException e) {
			throw new SQLException("Error in deleting meal from DB:" + e.getMessage());
		}
	}
	
	public Meal getMealById(int id) throws SQLException {
		try {
			return mealDAO.read(id);
		} catch (SQLException e) {
			throw new SQLException("Error in getting meal from DB:" + e.getMessage());
		}
	}
	
	public void createMeal(Meal meal) throws SQLException{
		try {
			mealDAO.create(meal);
		} catch (SQLException e) {
			throw new SQLException("Error in deleting menu from DB:" + e.getMessage());
		}
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
