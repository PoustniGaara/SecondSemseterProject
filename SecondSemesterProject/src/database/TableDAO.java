package database;

import java.util.ArrayList;

import model.Table;

public interface TableDAO {

	ArrayList<Table> read();
	
	ArrayList<Table> getReservationTables(int reservationId);

	Table read(int id);

	void create(Table table);

	void update(Table table);

	void delete(Table table);
	

}
