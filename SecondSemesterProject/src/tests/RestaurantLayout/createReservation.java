package tests.RestaurantLayout;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;

import org.junit.After;
import org.junit.Before;
import org.junit.jupiter.api.Test;

import controller.ReservationController;
import model.Menu;
import model.Reservation;
import model.Table;

public class createReservation {
	
	ReservationController cntrl;
	
	@Before
	public void setUp() {
		 cntrl = new ReservationController();
	}

	@Test
	void test() throws SQLException {
		//Arrange
		Calendar calendar = Calendar.getInstance();
		ArrayList<Table> tables = new ArrayList<>();
		ArrayList<Menu> menu = new ArrayList<>();
		
		cntrl.createReservation(calendar, tables);
		
		String phone = "32423424";
		cntrl.setPhone(phone);
		
		String name = cntrl.setPhone(phone).getName();
		String sureName = cntrl.setPhone(phone).getSurname();
		String note = "somebody work hard today";
		
		Reservation reservation = new Reservation(calendar, tables);
		reservation.setCustomer(cntrl.setPhone(phone));
		reservation.setTables(tables);
		reservation.setMenus(menu);
		
		//Act
		cntrl.confirmReservation(name, sureName, phone, 10, menu, note);
		
		//Assert	
		assertEquals(reservation, cntrl.getReservationById(1));
	}
	
	/** Fixture for pay station testing. */
	@After
	public void cleanUp() {
//		ps.setReady();
	}
	
}
