package database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;

import model.Menu;
import model.Reservation;

public class ReservedMenusConcreteDAO implements ReservedMenusDAO {

	private static ReservedMenusConcreteDAO instance = new ReservedMenusConcreteDAO();

	public static ReservedMenusConcreteDAO getInstance() {
		if (instance == null) {
			instance = new ReservedMenusConcreteDAO();
		}
		return instance;
	}

	@Override
	public void create(Reservation reservation) throws SQLException {
		Connection con = DBConnection.getInstance().getDBcon();
		try {
			PreparedStatement ps = con
					.prepareStatement("INSERT INTO ReservedMenus (reservationID, menuID, amount) VALUES (?,?,?)");
			HashMap<Menu, Integer> groupedMenus = groupMenus(reservation.getMenus());
			for (Menu m : groupedMenus.keySet()) {
				ps.setLong(1, reservation.getId());
				ps.setInt(2, m.getID());
				ps.setInt(3, groupedMenus.get(m));
				ps.addBatch();
			}
			ps.executeBatch();

		} catch (SQLException e) {
			e.printStackTrace();
			throw new SQLException("Error in getting RestaurantLayouts from DB:" + e.getMessage());
		}
	}

	public ArrayList<Menu> getReservationMenus(int reservationId) throws SQLException {
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
				Menu menu = new Menu(name, MenuMealsConcreteDAO.getInstance().getMenuMeals(menuid));
				menu.setID(menuid);
				menus.add(menu);
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new SQLException("Error in getting RestaurantLayouts from DB:" + e.getMessage());
		}
		return menus;
	}

	private HashMap<Menu, Integer> groupMenus(ArrayList<Menu> menus) throws SQLException {
		HashMap<Menu, Integer> groupedMenus = new HashMap<>();
		for (Menu m : menus) {
			if (groupedMenus.containsKey(m)) {
				groupedMenus.put(m, groupedMenus.get(m) + 1);
			} else {
				groupedMenus.put(m, 1);
			}
		}
		return groupedMenus;
	}

	@Override
	public void update(Reservation reservation) {
		// TODO Auto-generated method stub

	}

	@Override
	public void delete(Reservation reservation) {
		// TODO Auto-generated method stub

	}

}
