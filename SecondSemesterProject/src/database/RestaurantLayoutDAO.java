package database;

import java.util.ArrayList;

import model.RestaurantLayout;

public interface RestaurantLayoutDAO {
	
	ArrayList<RestaurantLayout> read();

	RestaurantLayout read(String name);

	void create(RestaurantLayout restaurantLayout);

	void update(RestaurantLayout restaurantLayout);

	void delete(RestaurantLayout restaurantLayout);

}
