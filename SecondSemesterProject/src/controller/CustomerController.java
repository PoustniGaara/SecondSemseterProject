package controller;

import java.util.ArrayList;

import database.CustomerConcreteDAO;
import model.Customer;

public class CustomerController {

	private CustomerConcreteDAO customerDAO;

	public CustomerController() {
		customerDAO = CustomerConcreteDAO.getInstance();
	}

	public Customer findByPhone(String phone) {
		return customerDAO.read(phone);
	}

	public ArrayList<Customer> getAllCustomers() {
		return customerDAO.read();
	}

	public void createCustomer(Customer customer) {
		customerDAO.create(customer);
	}

	public void deleteCustomer(Customer customer) {
		customerDAO.delete(customer);
	}
}