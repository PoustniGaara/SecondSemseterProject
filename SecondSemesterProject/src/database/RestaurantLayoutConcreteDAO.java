package database;

import java.awt.Point;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
	public void createRestaurantLayout(RestaurantLayout restaurantLayout) {
		try(Connection con = DBConnection.getInstance().getDBcon();
			PreparedStatement ps = con.prepareStatement("insert into dbo.RestaurantLayouts("
						+ "name,sizeX,sizeY) values(?,?,?)");){
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
	public void createLayoutItems(RestaurantLayout restaurantLayout) {
		HashMap<Point,LayoutItem> itemMap = (HashMap<Point, LayoutItem>) restaurantLayout.getItemMap();
		int rsID = getRestaurantLayoutID(restaurantLayout.getName());
        try (
        		Connection con = DBConnection.getInstance().getDBcon();
        		PreparedStatement ps = con.prepareStatement("insert into dbo.LayoutItems(name,type,"
        				+ "locationX,locationY,restaurantLayoutID) values(?,?,?,?,?)");) {
        	int count = 0;

        	for (Map.Entry<Point,LayoutItem> entry : itemMap.entrySet()) {
        		ps.setString(1, entry.getValue().getName());
        		ps.setString(2, entry.getValue().getType());
        		ps.setInt(3, (int) entry.getKey().getX());
        		ps.setInt(4, (int) entry.getKey().getY());
        		ps.setInt(5, rsID);

        		ps.addBatch();
        		count++;
      	 		// execute every 100 rows or less
        		if (count % 100 == 0 || count == itemMap.size()) {
        			ps.executeBatch();
        		}
        	}
        	} catch (SQLException ex) {
        	System.out.println(ex.getMessage());
        	}
		
	}

	@Override
	public void createTables(RestaurantLayout restaurantLayout) {
		HashMap<Point,LayoutItem> itemMap = 
				(HashMap<Point, LayoutItem>) getUpdatedItemMapByTableID(restaurantLayout);
	    try (
	        Connection con = DBConnection.getInstance().getDBcon();
	    	PreparedStatement ps = con.prepareStatement(
	    			"insert into dbo.Tables(layoutItemID,capacity) values(?,?)");) {
	    	int count = 0;
	        for (Map.Entry<Point,LayoutItem> entry : itemMap.entrySet()) {
	        	
	        	boolean isTable = entry.getValue() instanceof LayoutItem; 
	        	
				if(isTable) {
					Table table = (Table) entry.getValue();
					ps.setInt(4, entry.getValue().getId());
					ps.setInt(5, table.getCapacity());
				}
	            ps.addBatch();
	            count++;
	            // execute every 100 rows or less
	            if (count % 100 == 0 || count == itemMap.size()) {
	                ps.executeBatch();
	            }
	         }
	    } catch (SQLException ex) {
	            System.out.println(ex.getMessage());
	        }	
	}
	
	@Override
	public Map<Point, LayoutItem> getUpdatedItemMapByTableID(RestaurantLayout restaurantLayout) {
		Map<Point, LayoutItem> itemMap = new HashMap<>(restaurantLayout.getItemMap());
		try(Connection con = DBConnection.getInstance().getDBcon();
				PreparedStatement ps = con.prepareStatement("select layoutItemID"
						+ " from dbo.LayoutItems where restaurantLayoutID = ? and type = ?");
			){
			ps.setInt(1, getRestaurantLayoutID(restaurantLayout.getName()));
			ps.setString(2, "table");
			ResultSet rs = ps.executeQuery();
			while(rs.next()) {
					Table table = (Table) itemMap.get(new Point(rs.getInt("locationX"),
							rs.getInt("locationY")));
					table.setId(rs.getInt("layoutItemID"));
					itemMap.replace(new Point(rs.getInt("locationX"),rs.getInt("locationY")), table);
			}
			return itemMap;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	public static RestaurantLayoutConcreteDAO getInstance() {
		if(instance == null) return new RestaurantLayoutConcreteDAO();
		else return instance;
	}

	@Override
	public RestaurantLayout getRestaurantLayoutByName(String name) {
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
	public HashMap<Point, LayoutItem> getLayoutItems(int restaurantLayoutID) {
		HashMap<Point, LayoutItem> itemMap = new HashMap<>();
		HashMap<Integer, Integer> tableCapacityMap = getTablesCapacity(restaurantLayoutID);
		try(Connection con = DBConnection.getInstance().getDBcon();
				PreparedStatement ps = con.prepareStatement(" select * from dbo.LayoutItems where restaurantLayoutID = ?");
				){
			ps.setInt(1, restaurantLayoutID);
			ResultSet rs = ps.executeQuery();
			while(rs.next()) {
				if(rs.getString("type").equals("table")) {
					Table table = new Table(rs.getNString("name"),rs.getNString("type"),
							tableCapacityMap.get(rs.getInt("layoutItemID")));
					itemMap.put(new Point(rs.getInt("locationX"),rs.getInt("locationY")), table);
				}
				else {
					itemMap.put(new Point(rs.getInt("locationX"),rs.getInt("locationY")),
							new LayoutItem(rs.getNString("name"), rs.getNString("type")));
				}
			}
			return itemMap;
		}
		catch(SQLException e) {
			
		}
		return null;
	}
	
	@Override
	public HashMap<Integer, Integer> getTablesCapacity(int restaurantLayoutID) {
		try(Connection con = DBConnection.getInstance().getDBcon();
				PreparedStatement ps = con.prepareStatement(" select * from dbo.LayoutItems where restaurantLayoutID = ?");
				){
			
		}
		catch(SQLException e) {
			
		}
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

	@Override
	public Integer getRestaurantLayoutID(String restaurantLayoutName) {
		Integer id = null;
		try(Connection con = DBConnection.getInstance().getDBcon();
			PreparedStatement ps = con.prepareStatement("select restaurantLayoutID"
					+ " from dbo.RestaurantLayouts where name = ?");
		){
			ResultSet rs = ps.executeQuery();
			while(rs.next()) {
				id = rs.getInt("restaurantLayoutID"); 
			}	
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		return id;
	}




}
