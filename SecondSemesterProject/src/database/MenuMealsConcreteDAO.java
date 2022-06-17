package database;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import model.Meal;
import model.Menu;

public class MenuMealsConcreteDAO implements MenuMealsDAO {

	private static MenuMealsDAO instance = new MenuMealsConcreteDAO();

	private MenuMealsConcreteDAO() {
	}

	public static MenuMealsDAO getInstance() {
		if (instance == null) {
			instance = new MenuMealsConcreteDAO();
		}
		return instance;
	}

	@Override
	public void create(Menu menu, ArrayList<Meal> meals) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void update(Menu menu) {
		// TODO Auto-generated method stub

	}

	@Override
	public void delete(Menu menu) {
		// TODO Auto-generated method stub

	}

	public ArrayList<Meal> getMenuMeals(int menuId) throws SQLException {
		Connection con = DBConnection.getInstance().getDBcon();
		ArrayList<Meal> meals = new ArrayList<>();
		try {
			Statement menusStatement = con.createStatement();
			ResultSet menusResultSet = menusStatement.executeQuery(
					"SELECT * FROM MenuMeals JOIN Meals ON MenuMeals.mealID = Meals.mealID WHERE MenuMeals.menuID = "
							+ menuId);
			System.out.print("RESULT SELT:" + menusResultSet.next());
			System.out.print("MENU ID :"+menuId);
			while (menusResultSet.next()) {
				String name = menusResultSet.getString("name");
				String description = menusResultSet.getString("description");
				float price = menusResultSet.getFloat("price");
				Meal meal = new Meal(name, description, price);
				meals.add(meal);
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new SQLException("Error getting Menu's meals from DB:" + e.getMessage());
		}
		return meals;
	}

}
