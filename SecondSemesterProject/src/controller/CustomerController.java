package controller;

import java.sql.SQLException;
import java.util.ArrayList;

import database.CustomerConcreteDAO;
import model.Customer;

public class CustomerController {

	private CustomerConcreteDAO customerDAO;

	public CustomerController() {
		customerDAO = (CustomerConcreteDAO) CustomerConcreteDAO.getInstance();
	}

	public Customer findByPhone(String phone) throws SQLException {
		return customerDAO.read(phone);
	}

	public ArrayList<Customer> getAllCustomers() throws SQLException {
		return customerDAO.read();
	}

	public void createCustomer(Customer customer) throws SQLException {
		customerDAO.create(customer);
	}

	public void deleteCustomer(Customer customer) throws SQLException {
		customerDAO.delete(customer);
	}
}