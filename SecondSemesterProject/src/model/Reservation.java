package model;

import java.util.ArrayList;
import java.util.Calendar;

public class Reservation {

	private Long id;
	private Calendar timestamp;
	private int duration = 2;
	private int guests;
	private String note;
	private ArrayList<Table> tables;
	private Customer customer;
	private ArrayList<Menu> menus;
	
	public Reservation(Calendar timestamp, ArrayList<Table> tables) {
		this.timestamp = timestamp;
		this.tables = tables;
	}

	public Long getId() {
		return id;
	}

	public Calendar getTimestamp() {
		return timestamp;
	}

	public int getDuration() {
		return duration;
	}

	public int getGuests() {
		return guests;
	}

	public String getNote() {
		return note;
	}

	public ArrayList<Table> getTables() {
		return tables;
	}

	public Customer getCustomer() {
		return customer;
	}

	public ArrayList<Menu> getMenus() {
		return menus;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public void setTimestamp(Calendar timestamp) {
		this.timestamp = timestamp;
	}

	public void setDuration(int duration) {
		this.duration = duration;
	}

	public void setGuests(int guests) {
		this.guests = guests;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public void setTables(ArrayList<Table> tables) {
		this.tables = tables;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}

	public void setMenus(ArrayList<Menu> menus) {
		this.menus = menus;
	}
}
