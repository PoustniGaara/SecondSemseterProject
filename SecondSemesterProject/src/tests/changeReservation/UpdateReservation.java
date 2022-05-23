package tests.changeReservation;

import static org.junit.Assert.assertEquals;

import java.sql.SQLException;
import java.util.ArrayList;

import org.junit.After;
import org.junit.jupiter.api.Test;

import controller.ReservationController;
import database.TableConcreteDAO;
import model.Reservation;
import model.Table;

public class UpdateReservation {

	ReservationController cntrl;

	@Test
	void test() throws SQLException {

		// Arrange
		cntrl = new ReservationController();
		Reservation r = cntrl.getReservationById(41);
		r.setDuration(50);
		r.setGuests(50);
		r.setNote("Changed Note");
		
		ArrayList<Table> tables = new ArrayList<>();
		Table table1 = TableConcreteDAO.getInstance().read(213);
		tables.add(table1);
		r.setTables(tables);
		
		// Act
		cntrl.updateReservation(r);
		r = cntrl.getReservationById(41);

		// Assert
		assertEquals(r.getDuration(), 50);
		assertEquals(r.getGuests(), 50);
		assertEquals(r.getNote(), "Changed Note");
		assertEquals(r.getTables().get(0).getName(), "batchTest2");

	}

	@After
	public void cleanUp() {
		try {
			cntrl = new ReservationController();
			
			Reservation r = cntrl.getReservationById(41);
			r.setDuration(2);
			r.setGuests(11);
			r.setNote("Please, decorate the table with flowers :)");
			
			ArrayList<Table> tables = new ArrayList<>();
			Table table1 = null;
			tables.add(table1);
			r.setTables(tables);
			
			cntrl.updateReservation(r);
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}