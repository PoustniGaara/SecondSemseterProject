package database;

import model.Reservation;

public interface ReservedMenusDAO {
	
	void create(Reservation reservation, long id);

}
