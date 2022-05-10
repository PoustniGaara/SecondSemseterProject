package database;

import java.awt.Point;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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
		
	}

	@Override
	public void delete(RestaurantLayout restaurantLayout) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void saveRestaurantLayout(RestaurantLayout restaurantLayout) throws SQLException {
		Connection con = DBConnection.getInstance().getDBcon();
		try{
			con.setAutoCommit(false);
			createRestaurantLayout(restaurantLayout);
			createLayoutItems(restaurantLayout);
			createTables(restaurantLayout);
			con.commit();
		}
		catch(SQLException e){
		   // If there is any error.
			e.printStackTrace();
			con.rollback();
		}
		finally{
			con.close();
		}
		
	}

	@Override
	public void createLayoutItems(RestaurantLayout restaurantLayout) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void createTables(RestaurantLayout restaurantLayout) {
//		  String SQL = "INSERT INTO dbo.Tables(first_name,last_name) "
//	                + "VALUES(?,?)";
//	        try (
//	        		Connection con = DBConnection.getInstance().getDBcon();
//	    			PreparedStatement ps = con.prepareStatement(" select * from dbo.RestaurantLayouts where name = ?");) {
//	            int count = 0;
//
//	            for (Table table : list) {
//	            	ps.setString(1, actor.getFirstName());
//	            	ps.setString(2, actor.getLastName());
//
//	                statement.addBatch();
//	                count++;
//	                // execute every 100 rows or less
//	                if (count % 100 == 0 || count == list.size()) {
//	                    statement.executeBatch();
//	                }
//	            }
//	        } catch (SQLException ex) {
//	            System.out.println(ex.getMessage());
//	        }
		
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
	public List<Table> getRestaurantLayoutTableList(int restaurantLayoutID) {
		List<Table> tableList = new ArrayList<>();
		try(Connection con = DBConnection.getInstance().getDBcon();
			PreparedStatement ps = con.prepareStatement("select * from dbo.Tables where LayoutItemID = ?");
		){
			
			
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		return null;
	}

}
