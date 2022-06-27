package controller;

import java.sql.SQLException;

import database.LayoutItemConcreteDAO;
import database.LayoutItemDAO;
import model.LayoutItem;

public class LayoutItemController {
	
	private LayoutItemDAO layoutItemDAO;
	
	public LayoutItemController(){
		layoutItemDAO = LayoutItemConcreteDAO.getInstance();
	}
	
	public LayoutItem read(String name) throws SQLException {
		try {
			return layoutItemDAO.read(name);
		} catch (SQLException e) {
			throw new SQLException("Error in getting layout item from DB:" + e.getMessage());
		}
	}

}
