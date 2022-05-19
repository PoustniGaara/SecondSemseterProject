package tests.RestaurantLayout;

import static org.junit.Assert.assertEquals;

import java.sql.SQLException;

import org.junit.After;
import org.junit.jupiter.api.Test;

import controller.CustomerController;
import model.Customer;

public class createCustomer {

	CustomerController cntrl;

	@Test
	void test() throws SQLException {
		// Arrange
		cntrl = new CustomerController();
		Customer customer = new Customer("Maùo", "Rolko", "52785254", "matko@gmail.com", "Aalborg", "9000",
				"Holbergsgade", "16");
		// Act
		cntrl.createCustomer(customer);

		// Assert
		assertEquals(customer.getPhone(), cntrl.findByPhone("52785254").getPhone());
	}

	@After
	public void cleanUp() {
		//cntrl.deleteCustomer(cntrl.findByPhone("52785254"));
	}

}
