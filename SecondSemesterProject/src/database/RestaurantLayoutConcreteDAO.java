package database;

import java.awt.Point;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;

import model.LayoutItem;
import model.RestaurantLayout;

 public class RestaurantLayoutConcreteDAO implements RestaurantLayoutDAO {
	 
	 private static RestaurantLayoutConcreteDAO instance;
	 
	 private RestaurantLayoutConcreteDAO() {
		 
	 }

	@Override
	public ArrayList<RestaurantLayout> read() {
		ArrayList<RestaurantLayout> listOfRestaurantLayouts = new ArrayList<>();
		try(Connection con = DBConnection.getInstance().getDBcon();
			Statement st = con.createStatement();
		){
			ResultSet rs = st.executeQuery("select * from dbo.RestaurantLayouts");
			while(rs.next()) {
				HashMap<Point,LayoutItem> itemMap = 
						LayoutItemConcreteDAO.getInstance().getLayoutItems(rs.getLong("restaurantLayoutID"));
				RestaurantLayout restaurantLayout = new RestaurantLayout(rs.getString("name"),
						rs.getInt("locationX"), rs.getInt("locationY"),itemMap);
				listOfRestaurantLayouts.add(restaurantLayout);
			}
			return listOfRestaurantLayouts;
		}
		catch(SQLException e){
			e.printStackTrace();
		}
		return null;
	}
	
	@Override
	public void update(RestaurantLayout restaurantLayout) throws SQLException {
		Connection con = DBConnection.getInstance().getDBcon();
		try(PreparedStatement ps = con.prepareStatement("update dbo.RestaurantLayout set name = ?,"
				+ "set sizeX = ? , set sizeY = ? where restaurantLayoutID = ?")) {
			con.setAutoCommit(false);
			ps.setString(1, restaurantLayout.getName());
			ps.setInt(2, restaurantLayout.getSizeX());
			ps.setInt(3, restaurantLayout.getSizeY());
			ps.setLong(4, restaurantLayout.getId());
			LayoutItemConcreteDAO.getInstance().update(restaurantLayout.getItemMap(), restaurantLayout.getId());
			TableConcreteDAO.getInstance().update(restaurantLayout.getTableList());
			con.commit();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			con.rollback();
		}
		finally{
//			DBConnection.closeConnection();
			con.setAutoCommit(true);
		}
	}

	@Override
	public void delete(String restaurantLayoutName) throws SQLException {
		Connection con = DBConnection.getInstance().getDBcon();
		RestaurantLayout restaurantLayout = getRestaurantLayoutByName(restaurantLayoutName);
		try(PreparedStatement ps = con.prepareStatement("DELETE FROM dbo.RestaurantLayouts WHERE name = ?")
			){
			con.setAutoCommit(false);
			TableConcreteDAO.getInstance().delete(restaurantLayout.getTableList());
			LayoutItemConcreteDAO.getInstance().delete(restaurantLayout.getLayoutItems());
			ps.setString(1, restaurantLayoutName);
			con.commit();
		}
		catch(SQLException e){
		   // If there is any error.
			e.printStackTrace();
			con.rollback();
		}
		finally{
//			DBConnection.closeConnection();
			con.setAutoCommit(true);
		}
		
	}

	@Override
	public void saveRestaurantLayout(String name, int sizeX, int sizeY, HashMap<Point,LayoutItem> itemMap) throws SQLException {
		RestaurantLayout restaurantLayout = new RestaurantLayout(name,sizeX,sizeY,itemMap);
		Connection con = DBConnection.getInstance().getDBcon();
		try{
			con.setAutoCommit(false);
			long restaurantLayouID = createRestaurantLayout(restaurantLayout);
					LayoutItemConcreteDAO.getInstance().createLayoutItems(restaurantLayout.getItemMap(),
							restaurantLayouID);
			TableConcreteDAO.getInstance().createTables(restaurantLayout.getItemMap(),
					restaurantLayouID);
			con.commit();
		}
		catch(SQLException e){
			e.printStackTrace();
			con.rollback();
		}
		finally{
//			DBConnection.closeConnection();
			con.setAutoCommit(true);
		}
	}
	
	@Override
	public Long createRestaurantLayout(RestaurantLayout restaurantLayout) {
		Connection con = DBConnection.getInstance().getDBcon();
		try(
			PreparedStatement ps = con.prepareStatement("insert into dbo.RestaurantLayouts("
						+ "name,sizeX,sizeY) values(?,?,?)",Statement.RETURN_GENERATED_KEYS);
			) {
			ps.setString(1, restaurantLayout.getName());
			ps.setInt(2, restaurantLayout.getSizeX());
			ps.setInt(3, restaurantLayout.getSizeY());
			
		    int affectedRows = ps.executeUpdate();
		    
		    if(affectedRows == 0) 
		    	 throw new SQLException("Creating user failed, no rows affected.");
		        
		     try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
		         if(generatedKeys.next()) { restaurantLayout.setId(generatedKeys.getLong(1));
		         return restaurantLayout.getId();
		         }
		         else throw new SQLException("Creating restaurant layout failed, no ID obtained.");
		           
		        }
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		return null;
	}

//	public Map<Point, LayoutItem> getUpdatedItemMapByTableID(RestaurantLayout restaurantLayout) {
//		Map<Point, LayoutItem> itemMap = new HashMap<>(restaurantLayout.getItemMap());
//		try(Connection con = DBConnection.getInstance().getDBcon();
//				PreparedStatement ps = con.prepareStatement("select layoutItemID"
//						+ " from dbo.LayoutItems where restaurantLayoutID = ? and type = ?");
//			){
//			ps.setInt(1, getRestaurantLayoutID(restaurantLayout.getName()));
//			ps.setString(2, "table");
//			ResultSet rs = ps.executeQuery();
//			while(rs.next()) {
//					Table table = (Table) itemMap.get(new Point(rs.getInt("locationX"),
//							rs.getInt("locationY")));
//					table.setId(rs.getInt("layoutItemID"));
//					itemMap.replace(new Point(rs.getInt("locationX"),rs.getInt("locationY")), table);
//			}
//			return itemMap;
//		} catch (SQLException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		return null;
//	}
	
	@Override
	public RestaurantLayout getRestaurantLayoutByName(String name) {
		long restaurantLayoutID = 0;
		String restaurantLayoutName = null;
		int sizeX = 0;
		int sizeY = 0;
		HashMap<Point, LayoutItem> itemMap;
		try(Connection con = DBConnection.getInstance().getDBcon();
			PreparedStatement ps = con.prepareStatement(" select * from dbo.RestaurantLayouts where name = ?");
			){
			ps.setString(1, name);
			ResultSet rs = ps.executeQuery();
			while(rs.next()) {
				restaurantLayoutName = rs.getString("name"); 
				restaurantLayoutID = rs.getLong("restaurantLayoutID");
				sizeX = rs.getInt("sizeX");
				sizeY = rs.getInt("sizeY");
			}
		itemMap = LayoutItemConcreteDAO.getInstance().getLayoutItems(restaurantLayoutID);
		return new RestaurantLayout(restaurantLayoutName, sizeX, sizeY,itemMap);
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static RestaurantLayoutConcreteDAO getInstance() {
		if(instance == null) return new RestaurantLayoutConcreteDAO();
		else return instance;
	}



}
