package database;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;

import model.Reservation;
import model.ReservedTableInfo;
import model.Table;

public interface ReservedTablesDAO {

	void create(Reservation reservation) throws SQLException;

	void update(Reservation reservation) throws SQLException;

	void delete(Reservation reservation) throws SQLException;
	
	ArrayList<Table> getReservationTables(int reservationid) throws SQLException;
	
	ArrayList<ReservedTableInfo> getReservedTableInfoByTime(int restaurantLayoutId, Calendar calendar) throws SQLException;

}
