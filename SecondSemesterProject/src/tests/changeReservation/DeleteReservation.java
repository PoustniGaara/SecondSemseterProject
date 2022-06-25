package tests.changeReservation;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.awt.Point;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;

import org.junit.After;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import controller.CustomerController;
import controller.ReservationController;
import controller.RestaurantLayoutController;
import database.MenuConcreteDAO;
import database.TableConcreteDAO;
import model.Customer;
import model.LayoutItem;
import model.Meal;
import model.Menu;
import model.Reservation;
import model.Table;

public class DeleteReservation {

	ReservationController reservationCntrl;
	CustomerController customerCntrl;
	RestaurantLayoutController rlc;
	private int id;
	private Menu menu;
	private Table table;

	
	@BeforeEach
	public void setUp() throws Exception {

		rlc = new RestaurantLayoutController();
		customerCntrl = new CustomerController();

		Customer customer = new Customer("Martin", "Smith", "322114510", "martin@gmail.com", "Aalborg", "9000", "Vesterbro",
				"1");
		customerCntrl.createCustomer(customer);

		HashMap<Point, LayoutItem> itemMap = new HashMap<>();
		table = new Table("Table 01", "table", 4);
		itemMap.put(new Point(0, 1), table);
		rlc.save("Test Layout 1", 10, 10, itemMap);
		TableConcreteDAO.getInstance().createTables(itemMap, rlc.read("Test Layout 1").getId());

		ArrayList<Meal> meals = new ArrayList<Meal>();
		meals.add(new Meal("Cheesburger", "Burger", 9.99f));
		menu = new Menu("Menu 01", meals);
		MenuConcreteDAO.getInstance().create(menu);
		
		reservationCntrl = new ReservationController();

		Calendar calendar = new GregorianCalendar();
		calendar.setTimeInMillis(System.currentTimeMillis());

		ArrayList<Table> tables = new ArrayList<>();
		Table table1 = TableConcreteDAO.getInstance().read(1);
		tables.add(table1);

		Reservation reservation = reservationCntrl.startReservation(calendar, tables);

		ArrayList<Menu> menus = new ArrayList<>();
		menus.add(MenuConcreteDAO.getInstance().read(1));

		String phone = "322114510";
		Customer customer1 = reservationCntrl.checkCustomer(phone);

		String note = "Please, decorate the table with dead kittens :)";

		reservation.setCustomer(customer1);
		reservation.setTables(tables);
		reservation.setMenus(menus);

		// Act
		reservationCntrl.confirmReservation(customer1, 4,2, menus, note);
		id = reservation.getId().intValue();
	}

	@Test
	void test() throws SQLException {

		// Arrange


		// Act
		reservationCntrl.deleteReservation(id);

		// Assert
		assertEquals(reservationCntrl.getReservationById(id), null);
		cleanUp();

	}
	
	@After
	public void cleanUp() {
		try {
			customerCntrl.deleteCustomer(customerCntrl.findByPhone("322114510"));
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
