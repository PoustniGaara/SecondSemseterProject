package tests.createLayout;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.awt.Point;
import java.sql.SQLException;
import java.util.HashMap;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import controller.RestaurantLayoutController;
import model.LayoutItem;
import model.RestaurantLayout;
import model.Table;

class SaveRestaurantLayoutValidName {

	// This test tests if the creation of the RestaurantLayout in to the database is
	// successful. This test is expected to be successful because the name of the layout is valid.

	RestaurantLayoutController rlc;

	@BeforeEach
	public void setUp() {
		rlc = new RestaurantLayoutController();
	}

	@Test
	public void test() {
		// Arrange
		HashMap<Point, LayoutItem> itemMap = new HashMap<>();
		itemMap.put(new Point(0, 1), new Table("test", "table", 5));
		itemMap.put(new Point(0, 2), new Table("test1", "table", 6));
		itemMap.put(new Point(0, 3), new Table("test2", "table", 7));
		itemMap.put(new Point(0, 4), new Table("test3", "table", 8));
		itemMap.put(new Point(0, 5), new LayoutItem("test4", "bar"));
		itemMap.put(new Point(0, 5), new LayoutItem("test5", "entrance"));
		RestaurantLayout restaurantLayout = new RestaurantLayout("TestEclipse", 0, 0, itemMap);

		// Act
		try {
			rlc.save("TestEclipse", 0, 0, itemMap);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// Assert
		try {
			assertEquals(restaurantLayout.getName(),
					rlc.read(restaurantLayout.getName()).getName(), "are equal");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@AfterEach
	public void cleanUp() {
		try {
			rlc.deleteRestaurantLayout("TestEclipse");
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

}
