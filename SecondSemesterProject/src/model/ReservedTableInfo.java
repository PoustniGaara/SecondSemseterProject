package model;

import java.util.Calendar;

public class ReservedTableInfo {
	
	private Calendar calendar;
	private String name;
	private String phone;
	private String note;
	private int duration;
	private int id;
	private int layoutItemId;
	
	public String toString() {
		return new String("ID: "+id+" Name: "+name+" Phone: "+phone+" Note: "+note+ " Duration: " +String.valueOf(duration)
		+ "layoutItemId:"+layoutItemId);
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
	
	public Calendar getCalendar() {
		return calendar;
	}
	public void setTimestamp(Calendar timestamp) {
		this.calendar = timestamp;
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
	
	public void setLayoutItemId(int layoutItemId) {
		this.layoutItemId = layoutItemId;
	}
	
	public int getLayoutItemId() {
		return this.layoutItemId;
	}
	public ReservedTableInfo(Calendar calendar, String name, String phone, String note, int duration, int id,int layoutItemId) {
		this.id = id;
		this.duration = duration;
		this.calendar = calendar;
		this.name = name;
		this.phone = phone;
		this.note = note;
		this.layoutItemId = layoutItemId;
	}
	
}