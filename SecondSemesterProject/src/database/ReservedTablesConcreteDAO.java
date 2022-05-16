package database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

import model.Reservation;

public class ReservedTablesConcreteDAO implements ReservedTablesDAO{
	
	private static ReservedTablesConcreteDAO instance = new ReservedTablesConcreteDAO();

	public static ReservedTablesConcreteDAO getInstance() {
		if (instance == null) {
			instance = new ReservedTablesConcreteDAO();
		}
		return instance;
	}

	@Override
	public void create(Reservation reservation) {
		Connection con = DBConnection.getInstance().getDBcon();
		try {
			PreparedStatement ps = con
					.prepareStatement("INSERT INTO dbo.ReservedTables (layoutItemID, reservationID)"
							+ "VALUES (?,?)", Statement.RETURN_GENERATED_KEYS);
			ps.setInt(1, ps.getGeneratedKeys().getInt(1));
			ps.setInt(2, reservation.getId());	
			ps.execute();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DBConnection.closeConnection();
		}	
	}


}
