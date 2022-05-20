package database;

import java.sql.SQLException;
import java.util.ArrayList;

import model.Customer;

public interface CustomerDAO {

	ArrayList<Customer> read() throws SQLException;

	Customer read(String phone) throws SQLException;

	void create(Customer customer) throws SQLException;

	void delete(Customer customer) throws SQLException;

}
