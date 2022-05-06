package database;

import java.util.ArrayList;

import model.Customer;

public interface CustomerDAO {

	ArrayList<Customer> read();

	Customer read(String phone);

	void create(Customer customer);

	//void update(Customer customer);

	void delete(Customer customer);

}
