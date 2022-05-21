package database;

import java.sql.SQLException;

import model.Reservation;

public interface ReservedTablesDAO {

	void create(Reservation reservation) throws SQLException;

	void update(Reservation reservation) throws SQLException;

	void delete(Reservation reservation) throws SQLException;

}
