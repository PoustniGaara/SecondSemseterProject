package model;

import java.util.Calendar;

public class ReservedTableInfo {
	
	private Calendar timestamp;
	private String name;
	private String phone;
	private String note;
	private int duration;
	private int id;
	
	public String toString() {
		return new String("ID: "+id+" Name: "+name+" Phone: "+phone+" Note: "+note+ " Duration: " +String.valueOf(duration));
	}
	
	public int getId() {
		return id;
	}
	
	public void setId(int id) {
		this.id = id;
	}

	public int getDuration() {
		return duration;
	}
	
	public void setDuration(int duration) {
		this.duration = duration;
	}
	
	public Calendar getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(Calendar timestamp) {
		this.timestamp = timestamp;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getNote() {
		return note;
	}
	public void setNote(String note) {
		this.note = note;
	}
	public ReservedTableInfo(Calendar timestamp, String name, String phone, String note, int duration, int id) {
		this.id = id;
		this.duration = duration;
		this.timestamp = timestamp;
		this.name = name;
		this.phone = phone;
		this.note = note;
	}
	
}