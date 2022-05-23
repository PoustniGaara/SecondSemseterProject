package tests.changeReservation;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

import org.junit.After;
import org.junit.jupiter.api.Test;

import controller.ReservationController;
import database.MenuConcreteDAO;
import database.TableConcreteDAO;
import model.Customer;
import model.Menu;
import model.Reservation;
import model.Table;

public class DeleteReservation {

	ReservationController cntrl;

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

		// Act
		cntrl.confirmReservation(customer, 11, menus, note);
		int id = reservation.getId().intValue();
		cntrl.deleteReservation(reservation);

		// Assert
		assertEquals(cntrl.getReservationById(id), null);

	}

	@After
	public void cleanUp() {
	}
}
