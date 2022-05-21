package model;

import java.util.ArrayList;

public class Menu {

	private String name;
	private ArrayList<Meal> meals;
	private int id;

	public Menu(String name, ArrayList<Meal> meals) {
		this.name = name;
		this.meals = meals;
	}

	public String getName() {
		return name;
	}

	public ArrayList<Meal> getMeals() {
		return meals;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setMeals(ArrayList<Meal> meals) {
		this.meals = meals;
	}

	public int getID() {
		return id;
	}

	public void setID(int value) {
		id = value;
	}
}
