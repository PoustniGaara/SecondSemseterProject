package database;

import java.sql.SQLException;

import model.Reservation;

public interface ReservedMenusDAO {
	
	void create(Reservation reservation) throws SQLException;

}
