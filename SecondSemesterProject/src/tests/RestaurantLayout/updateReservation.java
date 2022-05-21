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
		Reservation r = cntrl.getReservationById(2);
		r.setDuration(50);
		r.setGuests(50);
		r.setNote("Changed Note");

		// Act
		cntrl.updateReservation(r);

		// Assert
		assertEquals(cntrl.getReservationById(2).getDuration(), 50);
		assertEquals(cntrl.getReservationById(2).getGuests(), 50);
		assertEquals(cntrl.getReservationById(2).getNote(), "Changed Note");

	}

	@After
	public void cleanUp() {
		try {
			cntrl.getReservationById(2).setDuration(2);

			cntrl.getReservationById(2).setGuests(11);
			cntrl.getReservationById(2).setNote("Please, decorate the table with flowers :)");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
