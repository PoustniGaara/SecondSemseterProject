package database;

import java.sql.SQLException;
import java.util.ArrayList;

import model.Reservation;

public interface ReservedTablesDAO {

		void create(Reservation reservation) throws SQLException;

}
