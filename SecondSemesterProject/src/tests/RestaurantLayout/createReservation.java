package tests.RestaurantLayout;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import org.junit.After;
import org.junit.Before;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import controller.ReservationController;
import database.MenuConcreteDAO;
import database.TableConcreteDAO;
import model.Customer;
import model.Menu;
import model.Reservation;
import model.Table;

public class createReservation {

	ReservationController cntrl;

	@Before
	public void setUp() {
	}

	@Test
	void test() throws SQLException {
		// Arrange
		cntrl = new ReservationController();

		Calendar calendar = new GregorianCalendar();
		calendar.setTimeInMillis(System.currentTimeMillis());

		ArrayList<Table> tables = new ArrayList<>();
		Table table1 = TableConcreteDAO.getInstance().read(213);
		tables.add(table1);

		Reservation reservation = cntrl.startReservation(calendar, tables);

		ArrayList<Menu> menus = new ArrayList<>();
		menus.add(MenuConcreteDAO.getInstance().read(1));
		
		String phone = "52785254";
		Customer customer = cntrl.checkCustomer(phone);

		String note = "Please, decorate the table with flowers :)";

		cntrl.confirmReservation(customer, 11, menus, note);

		reservation.setCustomer(customer);
		reservation.setTables(tables);
		reservation.setMenus(menus);

		// Act

		// Assert
		assertEquals(reservation.getNote(), cntrl.getReservationById(3).getNote());
	}

	/** Fixture for pay station testing. */
	@AfterEach
	public void cleanUp() {
		try {
			cntrl.deleteReservation(cntrl.getReservationById(3));
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
