package tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import java.sql.SQLException;

import org.junit.After;
import org.junit.jupiter.api.Test;

import controller.ReservationController;
import model.Reservation;

public class GetReservationByIdTest {
	
	ReservationController cntrl;

	@Test
	void test() throws SQLException {

		// Arrange
		cntrl = new ReservationController();
		Reservation reservation = cntrl.getReservationById(41);
		
		Reservation reservation2 = cntrl.getReservationById(1001);

		// Act

		// Assert
		assertNotEquals(reservation, null);
		assertEquals(reservation2, null);

	}

	@After
	public void cleanUp() {

	}
}
