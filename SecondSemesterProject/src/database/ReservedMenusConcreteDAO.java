package database;

import java.sql.BatchUpdateException;
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

	private static ReservedMenusDAO instance = new ReservedMenusConcreteDAO();

	public static ReservedMenusDAO getInstance() {
		if (instance == null) {
			instance = new ReservedMenusConcreteDAO();
		}
		return instance;
	}

	@Override
	public void create(Reservation reservation) throws SQLException, BatchUpdateException {
		Connection con = DBConnection.getInstance().getDBcon();
		try {
			PreparedStatement ps = con
					.prepareStatement("INSERT INTO ReservedMenus (reservationID, menuID, amount) VALUES (?,?,?)");
			HashMap<Menu, Integer> groupedMenus = groupMenus(reservation.getMenus());
			con.setAutoCommit(false);
			for (Menu m : groupedMenus.keySet()) {
				ps.setLong(1, reservation.getId());
				ps.setInt(2, m.getID());
				ps.setInt(3, groupedMenus.get(m));
				ps.addBatch();
			}
			try {
				ps.executeBatch();
				con.commit();
			} catch (BatchUpdateException e) {
				con.rollback();
				throw new BatchUpdateException("Error in batching", e.getUpdateCounts());
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new SQLException("Error in getting RestaurantLayouts from DB:" + e.getMessage());
		} finally {
			con.setAutoCommit(true);
		}
	}

	@Override
	public ArrayList<Menu> getReservationMenus(long reservationId) throws SQLException {
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
				int amount = menusResultSet.getInt("amount");
				Menu menu = new Menu(name, MenuMealsConcreteDAO.getInstance().getMenuMeals(menuid));
				menu.setID(menuid);
				for (int i = 1; i <= amount; i++)
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

	}

	@Override
	public void delete(Reservation reservation) {

	}
}