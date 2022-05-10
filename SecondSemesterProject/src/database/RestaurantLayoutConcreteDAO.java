package database;

import java.awt.Point;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import model.LayoutItem;
import model.RestaurantLayout;
import model.Table;

 public class RestaurantLayoutConcreteDAO implements RestaurantLayoutDAO {
	 
	 private static RestaurantLayoutConcreteDAO instance;
	 
	 private RestaurantLayoutConcreteDAO() {
		 
	 }

	@Override
	public ArrayList<RestaurantLayout> read() {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public void createRestaurantLayout(RestaurantLayout restaurantLayout) {
		try(Connection con = DBConnection.getInstance().getDBcon();
			PreparedStatement ps = con.prepareStatement("insert into dbo.RestaurantLayouts("
						+ "name,sizeX,sizeY) values(?,?,?)");
		){
			ps.setString(1, restaurantLayout.getName());
			ps.setInt(2, restaurantLayout.getSizeX());
			ps.setInt(3, restaurantLayout.getSizeY());
			ps.executeUpdate();
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		
	}

	@Override
	public void update(RestaurantLayout restaurantLayout) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void delete(RestaurantLayout restaurantLayout) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void saveRestaurantLayout(RestaurantLayout restaurantLayout) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void createLayoutItems(List<LayoutItem> layoutItems) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void createTables(List<Table> layoutItems) {
		// TODO Auto-generated method stub
		
	}
	
	public static RestaurantLayoutConcreteDAO getInstance() {
		if(instance == null) return new RestaurantLayoutConcreteDAO();
		else return instance;
	}

	@Override
	public RestaurantLayout getRestaurantLayout(String name) {
		try(Connection con = DBConnection.getInstance().getDBcon();
			PreparedStatement ps = con.prepareStatement(" select * from dbo.RestaurantLayouts where name = ?");
			){
			ps.setString(1, name);
			ResultSet rs = ps.executeQuery();
			while(rs.next()) {
				String returnName = rs.getString("name");   
				int sizeX = rs.getInt("sizeX");
				int sizeY = rs.getInt("sizeY");
				HashMap<Point, LayoutItem> itemMap;
//				return new RestaurantLayout(rs.getTimestamp("date"), date, price, deliveryDate, deliveryStatus,
//						delivery,customerId);
			}
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public HashMap<Point, LayoutItem> getItemMap(String name) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Table> getRestaurantLayoutItemsList(int restaurantLayoutID) {
		try(Connection con = DBConnection.getInstance().getDBcon();
			PreparedStatement ps = con.prepareStatement("select * from dbo.Tables where layoutItemID = ?");
				){
			
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		return null;
	}

}
