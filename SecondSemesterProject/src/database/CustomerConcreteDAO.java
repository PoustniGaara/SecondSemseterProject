package database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import model.Customer;

public class CustomerConcreteDAO implements CustomerDAO {

	private static CustomerConcreteDAO instance = new CustomerConcreteDAO();

	private CustomerConcreteDAO() {
	}

	public static CustomerConcreteDAO getInstance() {
		if (instance == null) {
			instance = new CustomerConcreteDAO();
		}
		return instance;
	}

	@Override
	public ArrayList<Customer> read() {
		ArrayList<Customer> customers = new ArrayList<Customer>();
		Connection con = DBConnection.getInstance().getDBcon();
		try {
			Statement statement = con.createStatement();
			ResultSet rs = statement.executeQuery("SELECT * FROM dbo.Customers");
			while (rs.next()) {
				String phone = rs.getString("phone");
				String name = rs.getString("name");
				String surname = rs.getString("surname");
				String email = rs.getString("email");
				String town = rs.getString("town");
				String zipcode = rs.getString("zipcode");
				String street = rs.getString("street");
				String streetNumber = rs.getString("streetNumber");
				Customer customer = new Customer(name, surname, phone, email, town, zipcode, street, streetNumber);
				customers.add(customer);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DBConnection.closeConnection();
		}
		return customers;
	}

	@Override
	public Customer read(String phone) {
		Connection con = DBConnection.getInstance().getDBcon();
		try {
			PreparedStatement statement = con.prepareStatement("SELECT * FROM dbo.Customers WHERE phone=?");
			statement.setString(1, phone);
			ResultSet rs = statement.executeQuery();
			while (rs.next()) {
				String name = rs.getString("name");
				String surname = rs.getString("surname");
				String email = rs.getString("email");
				String town = rs.getString("town");
				String zipcode = rs.getString("zipcode");
				String street = rs.getString("street");
				String streetNumber = rs.getString("streetNumber");
				Customer customer = new Customer(name, surname, phone, email, town, zipcode, street, streetNumber);
				return customer;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DBConnection.closeConnection();
		}
		return null;
	}

	@Override
	public void create(Customer customer) {
		Connection con = DBConnection.getInstance().getDBcon();
		try {
			PreparedStatement ps = con.prepareStatement(
					"INSERT INTO dbo.Customers (phone, name, surname, email, town, zipcode, street, streetNumber)"
							+ "VALUES (?,?,?,?,?,?,?,?)");
			ps.setString(1, customer.getPhone());
			ps.setString(2, customer.getName());
			ps.setString(3, customer.getSurname());
			ps.setString(4, customer.getEmail());
			ps.setString(5, customer.getTown());
			ps.setString(6, customer.getZipCode());
			ps.setString(7, customer.getStreet());
			ps.setString(8, customer.getStreetNumber());
			ps.execute();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DBConnection.closeConnection();
		}
	}

	@Override
	public void delete(Customer customer) {
		Connection con = DBConnection.getInstance().getDBcon();
		try {
			PreparedStatement ps = con.prepareStatement("DELETE FROM dbo.Customers WHERE phone=?");
			ps.setString(1, customer.getPhone());
			ps.execute();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DBConnection.closeConnection();
		}
	}

}
