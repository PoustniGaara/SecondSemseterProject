package database;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import model.Meal;
import model.Menu;

public class MenuConcreteDAO implements MenuDAO {

	private static MenuConcreteDAO instance = new MenuConcreteDAO();

	private MenuConcreteDAO() {
	}

	public static MenuConcreteDAO getInstance() {
		if (instance == null) {
			instance = new MenuConcreteDAO();
		}
		return instance;
	}

	@Override
	public ArrayList<Menu> read() {
		Connection con = DBConnection.getInstance().getDBcon();
		ArrayList<Menu> menus = new ArrayList<Menu>();

		try {
			Statement menusStatement = con.createStatement();
			ResultSet menusResultSet = menusStatement.executeQuery("SELECT * FROM Menus");
			while (menusResultSet.next()) {
				String name = menusResultSet.getString("name");
				int menuid = menusResultSet.getInt("menuID");
				Menu menu = new Menu(name, MealConcreteDAO.getInstance().getMenuMeals(menuid));
				menu.setID(menuid);
				menus.add(menu);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} 
		return menus;
	}

	@Override
	public Menu read(int id) {
		Connection con = DBConnection.getInstance().getDBcon();

		try {
			Statement menusStatement = con.createStatement();
			ResultSet menusResultSet = menusStatement.executeQuery("SELECT * FROM Menus WHERE menuID = " + id);
			while (menusResultSet.next()) {
				String name = menusResultSet.getString("name");
				Menu menu = new Menu(name, MealConcreteDAO.getInstance().getMenuMeals(id));
				menu.setID(id);
				return menu;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public void create(Menu menu) {
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

	@Override
	public ArrayList<Menu> getReservationMenus(int reservationId) {
		Connection con = DBConnection.getInstance().getDBcon();
		ArrayList<Menu> menus = new ArrayList<>();
		try {
			Statement menusStatement = con.createStatement();
			ResultSet menusResultSet = menusStatement.executeQuery(
					"SELECT * FROM ReservedMenus JOIN Menus ON ReservedMenus.menuID = Menus.menuID WHERE ReservedMenus.reservationID = "
							+ reservationId);
			while (menusResultSet.next()) {
				String name = menusResultSet.getString("name");
				int menuid = menusResultSet.getInt("menuID");
				Menu menu = new Menu(name, MealConcreteDAO.getInstance().getMenuMeals(menuid));
				menu.setID(menuid);
				menus.add(menu);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} 
		return menus;
	}
}