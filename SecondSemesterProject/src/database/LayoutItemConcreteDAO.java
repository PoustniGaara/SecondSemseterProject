package database;

import java.awt.Point;
import java.sql.BatchUpdateException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import model.LayoutItem;
import model.RestaurantLayout;

public class LayoutItemConcreteDAO implements LayoutItemDAO {
	
	private static LayoutItemDAO instance;
	
	private LayoutItemConcreteDAO() {
	}
	
	@Override
	public HashMap<Point, LayoutItem> getLayoutItems(long restaurantLayoutID) throws SQLException {
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
					layoutItem.setId(rs.getLong("layoutItemID"));
					itemMap.put(new Point(rs.getInt("locationX"),rs.getInt("locationY")),layoutItem);
				}
			}
		itemMap.putAll(tableMap);
		return itemMap;
		}
		catch(SQLException e) {
			e.printStackTrace();
			throw new SQLException("Error in getting LayoutItems:"+ e.getMessage());
		}
	}
	
	@Override
	public void createLayoutItems(HashMap<Point,LayoutItem> itemMap, long restaurantLayoutID) throws SQLException,BatchUpdateException {
		Connection con = DBConnection.getInstance().getDBcon();
		try (PreparedStatement ps = con.prepareStatement("insert into dbo.LayoutItems(name,type,"
        				+ "locationX,locationY,restaurantLayoutID) values(?,?,?,?,?)")
        	) {
			con.setAutoCommit(false);
        	for (Map.Entry<Point,LayoutItem> entry : itemMap.entrySet()) {
        		ps.setString(1, entry.getValue().getName());
        		ps.setString(2, entry.getValue().getType());
        		ps.setInt(3, (int) entry.getKey().getX());
        		ps.setInt(4, (int) entry.getKey().getY());
        		ps.setLong(5, restaurantLayoutID);
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
			throw new SQLException("Error in creating LayoutItems:"+ e.getMessage());
        	}
		finally {
			con.setAutoCommit(true);
		}
	}

	@Override
	public void update(HashMap<Point,LayoutItem> itemMap, long restaurantLayoutID) throws SQLException,BatchUpdateException {
		Connection con = DBConnection.getInstance().getDBcon();
		try(PreparedStatement ps = con.prepareStatement("update dbo.LayoutItems set name = ?, type = ?,"
				+ "locationX = ?, locationY = ? where restaurantLayouID = ?");
		){
		con.setAutoCommit(false);
		for(Entry<Point, LayoutItem> entry : itemMap.entrySet()) {
			ps.setString(1, entry.getValue().getName());
			ps.setString(2, entry.getValue().getType());
			ps.setInt(3, (int) entry.getKey().getX());
			ps.setInt(4, (int) entry.getKey().getY());
			ps.setLong(5, restaurantLayoutID);
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
			throw new SQLException("Error in updating LayoutItems:"+ e.getMessage());
		}
		finally {
			con.setAutoCommit(true);
		}
	}

	@Override
	public void delete(ArrayList<LayoutItem> layoutItemList) throws SQLException, BatchUpdateException  {
		Connection con = DBConnection.getInstance().getDBcon();
	    try(PreparedStatement ps = con.prepareStatement(
	    		"delete from dbo.LayoutItems where layoutItemID = ?");
	    ){
	    con.setAutoCommit(false);
	    for(LayoutItem layoutItem : layoutItemList) {
				ps.setLong(1, layoutItem.getId());
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
	    }
	    catch (SQLException e) {
			e.printStackTrace();
			throw new SQLException("Error in deleting LayoutItems:"+ e.getMessage());
		}
	    finally {
	    	con.setAutoCommit(true);
	    }
	}
	
	public static LayoutItemDAO getInstance() {
		if(instance == null) return instance = new LayoutItemConcreteDAO();
		else return instance;
	}

	@Override
	public LayoutItem read(String name) throws SQLException {
		long id = 0;
		String lIName = "";
		String type = "";
		Connection con = DBConnection.getInstance().getDBcon();
		try(PreparedStatement ps = con.prepareStatement(" select * from dbo.LayoutItems where name = ?");
			){
			ps.setString(1, name);
			ResultSet rs = ps.executeQuery();
			while(rs.next()) {
				id = rs.getLong("LayoutItemID");
				lIName = rs.getString("name"); 
				type = rs.getString("type");
			}
			LayoutItem layoutItem = new LayoutItem(lIName, type);
			layoutItem.setId(id);
		return layoutItem;
		}
		catch(Exception e) {
			e.printStackTrace();
			throw new SQLException("Error in getting RestaurantLayout:"+ e.getMessage());
		}
	}

}
