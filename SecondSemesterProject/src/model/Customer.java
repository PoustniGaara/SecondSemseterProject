package model;

public class Customer {

	private String name;
	private String surname;
	private String email;
	private String phone;
	private String town;
	private String zipCode;
	private String street;
	private String streetNumber;
	private boolean existingCustomer;

	public Customer(String name, String surname, String phone, String email, String town, String zipCode, String street,
			String streetNumber) {
		this.name = name;
		this.surname = surname;
		this.town = town;
		this.zipCode = zipCode;
		this.street = street;
		this.streetNumber = streetNumber;
		this.email = email;
		this.phone = phone;
	}

	public String getName() {
		return name;
	}

	public String getSurname() {
		return surname;
	}

	public String getEmail() {
		return email;
	}

	public String getPhone() {
		return phone;
	}

	public String getTown() {
		return town;
	}

	public String getZipCode() {
		return zipCode;
	}

	public String getStreet() {
		return street;
	}

	public String getStreetNumber() {
		return streetNumber;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setSurname(String surname) {
		this.surname = surname;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public void setTown(String town) {
		this.town = town;
	}

	public void setZipCode(String zipCode) {
		this.zipCode = zipCode;
	}

	public void setStreet(String street) {
		this.street = street;
	}

	public void setStreetNumber(String streetNumber) {
		this.streetNumber = streetNumber;
	}

	public void setExistingCustomer(boolean state) {
		existingCustomer = state;
	}

	public boolean isExistingCustomer() {
		return existingCustomer;
	}

}
