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
		for (Customer customer : customerDAO.read()) {
			if (customer.getPhone().equalsIgnoreCase(phone)) {
				return customer;
			}
		}
		return null;
	}

	public Customer getCustomerById(int id) {
		return customerDAO.read(id);
	}

	public ArrayList<Customer> getAllCustomers() {
		return customerDAO.read();
	}

	public void createCustomer(Customer customer) {
		customerDAO.create(customer);
	}

	public void updateCustomer(Customer customer) {
		customerDAO.update(customer);
	}

	public void deleteCustomer(Customer customer) {
		customerDAO.delete(customer);
	}
}