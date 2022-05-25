package tests.createReservation;



import static org.junit.Assert.assertEquals;

import java.sql.SQLException;
import java.util.ArrayList;

import org.junit.After;
import org.junit.Test;

import controller.CustomerController;
import database.MenuConcreteDAO;
import database.TableConcreteDAO;
import model.Customer;
import model.Table;

public class CreateCustomerValidPhone {
	
	CustomerController customerCntrl;
	Customer customer;
	
	@Test
	public void test() throws Exception {
		// Arange
		customerCntrl = new CustomerController();
		String phone = "387654359";
		customer = new Customer("John", "Smith", phone, "john@gmail.com", "Aalborg", "9000", "Vesterbro", "1");
		System.out.println(customer.getPhone());
		
		//Act
		customerCntrl.createCustomer(customer);
		
		// Assert
		assertEquals(customerCntrl.findByPhone(phone).getPhone(), customer.getPhone());
		
		cleanUp();
	}
	
	@After
	public void cleanUp() {
		try {
			customerCntrl.deleteCustomer(customer);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
