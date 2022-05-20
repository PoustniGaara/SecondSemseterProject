package database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

import model.Reservation;
import model.Table;

public class ReservedTablesConcreteDAO implements ReservedTablesDAO {

	private static ReservedTablesConcreteDAO instance = new ReservedTablesConcreteDAO();

	public static ReservedTablesConcreteDAO getInstance() {
		if (instance == null) {
			instance = new ReservedTablesConcreteDAO();
		}
		return instance;
	}

	@Override
	public void create(Reservation reservation) throws SQLException {
		Connection con = DBConnection.getInstance().getDBcon();

		try {
			PreparedStatement ps = con
					.prepareStatement("INSERT INTO dbo.ReservedTables (layoutItemID, reservationID) VALUES (?,?)");

			for (Table table : reservation.getTables()) {
				ps.setLong(1, table.getId());
				ps.setLong(2, reservation.getId());
				ps.addBatch();
			}
			ps.executeBatch();

		}
		catch(SQLException e){
			e.printStackTrace();
			throw new SQLException("Error in getting RestaurantLayouts from DB:"+ e.getMessage());
		}
	}

}
