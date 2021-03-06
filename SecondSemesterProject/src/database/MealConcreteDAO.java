package database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import model.Meal;

public class MealConcreteDAO implements MealDAO {

	private static MealDAO instance = new MealConcreteDAO();

	private MealConcreteDAO() {
	}

	public static MealDAO getInstance() {
		if (instance == null) {
			instance = new MealConcreteDAO();
		}
		return instance;
	}

	@Override
	public ArrayList<Meal> read() throws SQLException {
		Connection con = DBConnection.getInstance().getDBcon();
		ArrayList<Meal> meals = new ArrayList<Meal>();

		try {
			Statement statement = con.createStatement();
			ResultSet rs = statement.executeQuery("SELECT * FROM Meals");
			while (rs.next()) {
				String name = rs.getString("name");
				String description = rs.getString("description");
				float price = rs.getFloat("price");
				Meal meal = new Meal(name, description, price);
				meal.setId(rs.getInt("mealID"));
				meals.add(meal);
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new SQLException("Error getting Meal from DB:" + e.getMessage());
		}
		return meals;
	}

	@Override
	public Meal read(int id) throws SQLException {
		Connection con = DBConnection.getInstance().getDBcon();
		try {
			Statement statement = con.createStatement();
			ResultSet rs = statement.executeQuery("SELECT * FROM Meals WHERE mealID = " + id);
			while (rs.next()) {
				String name = rs.getString("name");
				String description = rs.getString("description");
				float price = rs.getFloat("price");
				Meal meal = new Meal(name, description, price);
				meal.setId(rs.getInt("mealID"));
				return meal;
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new SQLException("Error getting Meal from DB:" + e.getMessage());
		}
		return null;
	}

	@Override
	public void create(Meal meal) throws SQLException {
		Connection con = DBConnection.getInstance().getDBcon();
		try {
			PreparedStatement ps = con
					.prepareStatement("INSERT INTO dbo.Meals (name, price, description) VALUES (?,?,?)");
			ps.setString(1, meal.getName());
			ps.setFloat(2, meal.getPrice());
			ps.setString(3, meal.getDescription());
			ps.execute();
		} catch (SQLException e) {
			e.printStackTrace();
			throw new SQLException("Error inserting Meal from DB:" + e.getMessage());
		}

	}

	@Override
	public void update(Meal meal) {
		// TODO Auto-generated method stub

	}

	@Override
	public void delete(Meal meal) throws SQLException {
		Connection con = DBConnection.getInstance().getDBcon();
		try {
			PreparedStatement ps = con.prepareStatement("DELETE FROM dbo.Meals WHERE mealID = ?");
			ps.setLong(1, meal.getId());
			ps.execute();
		} catch (SQLException e) {
			e.printStackTrace();
			throw new SQLException("Error deleting Menu from DB:" + e.getMessage());
		}

	}
}