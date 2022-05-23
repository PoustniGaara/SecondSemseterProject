package database;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class ConnectionCheck {
	
	private static ConnectionCheck instance;
	
	private ConnectionCheck() {
	}
	
	public String verifyConnection() throws SQLException {
		Connection con = DBConnection.getInstance().getDBcon();
		try {
			Statement statement = con.createStatement();
			ResultSet rs = statement.executeQuery("select 1 from dbo.RestaurantLayouts");
			while(rs.next()) {
				return rs.getString(1);
			}
		}
		catch(Exception e){
			throw new SQLException("Error with connection"+ e.getMessage());
		}
		return null;
		
	}
	
	public static ConnectionCheck getInstance() {
		if(instance != null) return instance;
		else return new ConnectionCheck();
	}

}
