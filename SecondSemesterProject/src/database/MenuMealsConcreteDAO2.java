package database;

public class MenuMealsConcreteDAO2 implements MenuMealsDAO{
	
	private static MenuMealsConcreteDAO2 instance = new MenuMealsConcreteDAO2();

	private MenuMealsConcreteDAO2() {
	}

	public static MenuMealsConcreteDAO2 getInstance() {
		if (instance == null) {
			instance = new MenuMealsConcreteDAO2();
		}
		return instance;
	}

	@Override
	public void create() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void update() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void delete() {
		// TODO Auto-generated method stub
		
	}
	
}
