package tests.RestaurantLayout;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.After;
import org.junit.Before;
import org.junit.jupiter.api.Test;

import controller.RestaurantLayoutController;
import model.RestaurantLayout;

class createRestaurantLayoutTest {
	
	RestaurantLayoutController rlc;
	
	//This test tests if the creation of the RestaurantLayout in the database is successful
	
	@Before
	public void setUp() {
		 rlc = new RestaurantLayoutController();
	}

	@Test
	void test() {
		//Arrange
		RestaurantLayout restaurantLayout = new RestaurantLayout();
		restaurantLayout.setName("TestEclipse");
		restaurantLayout.setSizeX(0);
		restaurantLayout.setSizeY(0);
		
		//Act
		rlc.createRestaurantLayout(restaurantLayout);
		
		//Assert
		fail("Not yet implemented");
	}
	
	/** Fixture for pay station testing. */
	@After
	public void cleanUp() {
		ps.setReady();
	}

}
