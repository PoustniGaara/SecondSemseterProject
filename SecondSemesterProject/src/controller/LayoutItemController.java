package controller;

import java.sql.SQLException;

import database.LayoutItemConcreteDAO;
import database.LayoutItemDAO;
import model.LayoutItem;

public class LayoutItemController {
	
	private final LayoutItemDAO layoutItemDAO = LayoutItemConcreteDAO.getInstance();
	
	public LayoutItemController(){
	}
	
	public LayoutItem read(String name) throws SQLException {
		try {
			return layoutItemDAO.read(name);
		} catch (SQLException e) {
			throw new SQLException("Error in getting layout item from DB:" + e.getMessage());
		}
	}

}
