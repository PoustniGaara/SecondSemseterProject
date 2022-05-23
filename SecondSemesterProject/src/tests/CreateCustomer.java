package tests;

import static org.junit.Assert.assertEquals;

import java.sql.SQLException;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import controller.CustomerController;
import model.Customer;

public class CreateCustomer {

	CustomerController cntrl;

	@Test
	void test() throws SQLException {
		// Arrange
		cntrl = new CustomerController();
		Customer customer = new Customer("John", "Smith", "987654321", "john@gmail.com", "Aalborg", "9000", "Vesterbro",
				"1");
		// Act
		cntrl.createCustomer(customer);

		// Assert
		assertEquals(customer.getPhone(), cntrl.findByPhone("987654321").getPhone());
	}

	@AfterEach
	public void cleanUp() {
		try {
			cntrl.deleteCustomer(cntrl.findByPhone("987654321"));
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
