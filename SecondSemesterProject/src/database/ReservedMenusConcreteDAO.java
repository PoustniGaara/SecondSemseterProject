package database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import model.Reservation;

public class ReservedMenusConcreteDAO implements ReservedMenusDAO{
	
	private static ReservedMenusConcreteDAO instance = new ReservedMenusConcreteDAO();

	public static ReservedMenusConcreteDAO getInstance() {
		if (instance == null) {
			instance = new ReservedMenusConcreteDAO();
		}
		return instance;
	}

	@Override
	public void create(Reservation reservation) {
		Connection con = DBConnection.getInstance().getDBcon();
		try {
			PreparedStatement ps = con
					.prepareStatement("INSERT INTO dbo.ReservedMenus (reservationID, menuID, amount)"
							+ "VALUES (?,?,?)");
			
			ps.setInt(1, reservation.getId());
			ps.setInt(2, 1);
			ps.setInt(3, reservation.getGuests());
			ps.execute();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DBConnection.closeConnection();
		}	
	}

}
