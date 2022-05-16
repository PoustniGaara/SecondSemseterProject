package database;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import model.Meal;
import model.Menu;

public class MealConcreteDAO implements MealDAO {

	private static MealConcreteDAO instance = new MealConcreteDAO();

	private MealConcreteDAO() {
	}

	public static MealConcreteDAO getInstance() {
		if (instance == null) {
			instance = new MealConcreteDAO();
		}
		return instance;
	}

	@Override
	public ArrayList<Meal> read() {
		Connection con = DBConnection.getInstance().getDBcon();
		ArrayList<Meal> meals = new ArrayList<Meal>();

		try {
			Statement menusStatement = con.createStatement();
			ResultSet menusResultSet = menusStatement.executeQuery("SELECT * FROM Meals");
			while (menusResultSet.next()) {
				String name = menusResultSet.getString("name");
				String description = menusResultSet.getString("description");
				float price = menusResultSet.getFloat("price");
				Meal meal = new Meal(name, description, price);
				meals.add(meal);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DBConnection.closeConnection();
		}
		return meals;
	}

	@Override
	public Meal read(int id) {
		Connection con = DBConnection.getInstance().getDBcon();

		try {
			Statement menusStatement = con.createStatement();
			ResultSet menusResultSet = menusStatement.executeQuery("SELECT * FROM Meals WHERE menuID = " + id);
			while (menusResultSet.next()) {
				String name = menusResultSet.getString("name");
				String description = menusResultSet.getString("description");
				float price = menusResultSet.getFloat("price");
				Meal meal = new Meal(name, description, price);
				return meal;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DBConnection.closeConnection();
		}
		return null;
	}

	@Override
	public void create(Meal meal) {
		// TODO Auto-generated method stub

	}

	@Override
	public void update(Meal meal) {
		// TODO Auto-generated method stub

	}

	@Override
	public void delete(Meal meal) {
		// TODO Auto-generated method stub

	}

	@Override
	public ArrayList<Meal> getMenuMeals(int menuId) {
		Connection con = DBConnection.getInstance().getDBcon();
		ArrayList<Meal> meals = new ArrayList<>();
		try {
			Statement menusStatement = con.createStatement();
			ResultSet menusResultSet = menusStatement.executeQuery(
					"SELECT * FROM MenuMeals JOIN Meals ON MenuMeals.mealID = Meals.mealID WHERE MenuMeals.menuID = "
							+ menuId);
			while (menusResultSet.next()) {
				String name = menusResultSet.getString("name");
				String description = menusResultSet.getString("description");
				float price = menusResultSet.getFloat("price");
				Meal meal = new Meal(name, description, price);
				meals.add(meal);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DBConnection.closeConnection();
		}
		return meals;
	}
}