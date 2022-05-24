package database;

import java.sql.BatchUpdateException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import model.Menu;
import model.Table;

public class MenuConcreteDAO implements MenuDAO {

	private static MenuDAO instance = new MenuConcreteDAO();

	private MenuConcreteDAO() {
	}

	public static MenuDAO getInstance() {
		if (instance == null) {
			instance = new MenuConcreteDAO();
		}
		return instance;
	}

	@Override
	public ArrayList<Menu> read() throws SQLException {
		Connection con = DBConnection.getInstance().getDBcon();
		ArrayList<Menu> menus = new ArrayList<Menu>();

		try {
			Statement menusStatement = con.createStatement();
			ResultSet menusResultSet = menusStatement.executeQuery("SELECT * FROM Menus");
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

	@Override
	public Menu read(int id) throws SQLException {
		Connection con = DBConnection.getInstance().getDBcon();

		try {
			Statement menusStatement = con.createStatement();
			ResultSet menusResultSet = menusStatement.executeQuery("SELECT * FROM Menus WHERE menuID = " + id);
			while (menusResultSet.next()) {
				String name = menusResultSet.getString("name");
				Menu menu = new Menu(name, MenuMealsConcreteDAO.getInstance().getMenuMeals(id));
				menu.setID(id);
				return menu;
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new SQLException("Error in getting RestaurantLayouts from DB:" + e.getMessage());
		}
		return null;
	}

	@Override
	public void create(Menu menu) {
		// TODO Auto-generated method stub

	}

	@Override
	public void update(ArrayList<Menu> menus) throws SQLException, BatchUpdateException {
		Connection con = DBConnection.getInstance().getDBcon();
		try (PreparedStatement ps = con.prepareStatement("update dbo.Menus SET name = ? where menuID = ?");) {
			con.setAutoCommit(true);
			for (Menu menu: menus) {
				ps.setString(1, menu.getName());
				ps.setInt(2, menu.getID());
				System.out.println(menu.getID());
				System.out.println(menu.getName());
				ps.addBatch();
			}
    		try {
    			ps.executeBatch();
    			con.commit();
    			}
    		 catch(BatchUpdateException e){
    		    con.rollback();
    		    throw new BatchUpdateException("Error in batching", e.getUpdateCounts());
    		    }
		} catch (SQLException e) {
			e.printStackTrace();
			throw new SQLException("Error in updating menus:" + e.getMessage());
		}
		finally {
			con.setAutoCommit(false);
		}
	}

	@Override
	public void delete(Menu menu) {
		// TODO Auto-generated method stub

	}

}