package database;

import java.awt.Point;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import model.LayoutItem;
import model.RestaurantLayout;
import model.Table;

public class LayoutItemConcreteDAO implements LayoutItemDAO {
	
	private static LayoutItemConcreteDAO instance;
	
	private LayoutItemConcreteDAO() {
		
	}
	
	@Override
	public HashMap<Point, LayoutItem> getLayoutItems(long restaurantLayoutID) {
		HashMap<Point, LayoutItem> tableMap = TableConcreteDAO.getInstance().getTableMap(restaurantLayoutID);
		Connection con = DBConnection.getInstance().getDBcon();
		HashMap<Point, LayoutItem> itemMap = new HashMap<>();
		try(PreparedStatement ps = con.prepareStatement(" select * from dbo.LayoutItems where restaurantLayoutID = ?");
		){
			ps.setLong(1, restaurantLayoutID);
			ResultSet rs = ps.executeQuery();
			while(rs.next()) {
				if(!rs.getString("type").equals("table")) {
					LayoutItem layoutItem = new LayoutItem(rs.getString("name"), rs.getString("type"));
					layoutItem.setId(rs.getLong("layoutItemUD"));
					itemMap.put(new Point(rs.getInt("locationX"),rs.getInt("locationY")),layoutItem);
				}
			}

		itemMap.putAll(tableMap);
		return itemMap;
		}
		catch(SQLException e) {
		}
		return null;
	}
	
	@Override
	public void createLayoutItems(HashMap<Point,LayoutItem> itemMap, long restaurantLayoutID) {
		Connection con = DBConnection.getInstance().getDBcon();
        try (PreparedStatement ps = con.prepareStatement("insert into dbo.LayoutItems(name,type,"
        				+ "locationX,locationY,restaurantLayoutID) values(?,?,?,?,?)")
        	) {
        	for (Map.Entry<Point,LayoutItem> entry : itemMap.entrySet()) {
        		ps.setString(1, entry.getValue().getName());
        		ps.setString(2, entry.getValue().getType());
        		ps.setInt(3, (int) entry.getKey().getX());
        		ps.setInt(4, (int) entry.getKey().getY());
        		ps.setLong(5, restaurantLayoutID);
        		ps.addBatch();
        		}
        	ps.executeBatch();
	
        } catch (SQLException ex) {
        	System.out.println(ex.getMessage());
        	}
	}

	@Override
	public void update(HashMap<Point,LayoutItem> itemMap, long restaurantLayoutID) {
		Connection con = DBConnection.getInstance().getDBcon();
		try(PreparedStatement ps = con.prepareStatement("update dbo.LayoutItems set name = ?, type = ?,"
				+ "locationX = ?, locationY = ? where restaurantLayouID = ?");
		){
			for(Entry<Point, LayoutItem> entry : itemMap.entrySet()) {
				ps.setString(1, entry.getValue().getName());
				ps.setString(2, entry.getValue().getType());
				ps.setInt(3, (int) entry.getKey().getX());
				ps.setInt(4, (int) entry.getKey().getY());
				ps.setLong(5, restaurantLayoutID);
				ps.addBatch();
			}
			ps.executeBatch();
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	@Override
	public void delete(ArrayList<LayoutItem> layoutItemList) {
		Connection con = DBConnection.getInstance().getDBcon();
	    try(PreparedStatement ps = con.prepareStatement(
	    		"delete from dbo.LayoutItems where layoutItemID = ?");
	    ){
		    for(LayoutItem layoutItem : layoutItemList) {
				ps.setLong(1, layoutItem.getId());
		    	ps.addBatch();
		    }
		    ps.executeBatch();
		} 
	    catch (SQLException ex) {
	    	System.out.println(ex.getMessage());
		}	
	}
	
	public static LayoutItemConcreteDAO getInstance() {
		if(instance == null) return new LayoutItemConcreteDAO();
		else return instance;
	}

}
