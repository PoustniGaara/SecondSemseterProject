package tests.RestaurantLayout;

import static org.junit.Assert.assertEquals;

import java.sql.SQLException;

import org.junit.After;
import org.junit.jupiter.api.Test;

import controller.ReservationController;
import model.Reservation;


public class updateReservation {
	
	ReservationController cntrl;

	@Test
	void test() throws SQLException {
		
		// Arrange
		cntrl = new ReservationController();
		Reservation r = cntrl.getCompleteReservationById(2);
		r.setDuration(50);
		r.setGuests(50);
		r.setNote("Changed Note");
		
		// Act
		cntrl.updateReservation(r);
		
		// Assert
		assertEquals(cntrl.getCompleteReservationById(2).getDuration(), 50);
		assertEquals(cntrl.getCompleteReservationById(2).getGuests(), 50);
		assertEquals(cntrl.getCompleteReservationById(2).getNote(), "Changed Note");

	}

	@After
	public void cleanUp() {
		cntrl.getCompleteReservationById(2).setDuration(2);
		cntrl.getCompleteReservationById(2).setGuests(11);
		cntrl.getCompleteReservationById(2).setNote("Please, decorate the table with flowers :)");
	}
}
