package tests.createReservation;

import static org.junit.Assert.assertThrows;

import java.sql.SQLException;
import java.util.ArrayList;

import org.junit.After;
import org.junit.Test;

import com.microsoft.sqlserver.jdbc.SQLServerException;

import controller.CustomerController;
import database.MenuConcreteDAO;
import database.TableConcreteDAO;
import model.Customer;
import model.Table;


public class CreateCustomerInvalidPhone {
	
	CustomerController customerCntrl;
	Customer customer;
	
	@Test
	public void test() throws SQLException {
		// Arange
		customerCntrl = new CustomerController();
		String phone = "322114512";
		customer = new Customer("John", "Smith", phone, "john@gmail.com", "Aalborg", "9000", "Vesterbro", "1");

		//Act
		assertThrows(java.sql.SQLException.class, () -> customerCntrl.createCustomer(customer));
		
	}
	
	@After
	public void cleanUp() {
		
	}
}
