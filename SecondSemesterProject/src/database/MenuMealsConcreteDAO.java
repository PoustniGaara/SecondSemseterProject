package database;

public class MenuMealsConcreteDAO implements MenuMealsDAO{
	
	private static MenuMealsConcreteDAO instance = new MenuMealsConcreteDAO();

	private MenuMealsConcreteDAO() {
	}

	public static MenuMealsConcreteDAO getInstance() {
		if (instance == null) {
			instance = new MenuMealsConcreteDAO();
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
