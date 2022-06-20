package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.ScrollPaneConstants;
import javax.swing.ScrollPaneLayout;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;

import controller.MenuController;
import database.ReservationConcreteDAO;
import gui.menu.AddMealInputFrame;
import gui.menu.AddMenuInputFrame;
import gui.reservation.ReservationsPanel;
import gui.tools.Fonts;
import gui.tools.ProjectColors;
import model.Meal;
import model.Menu;
import model.Reservation;

public class MenuPanel extends JPanel{
	
	private static MenuPanel instance;
	private DefaultTableModel menuTableModel, mealTableModel;
	private JTextField searchBar;
	private JTable menuTable, mealTable;
	private MenuController menuController;
	private JPanel currentPanel, menuPanel, mealPanel;
	private JButton switchButton;
	private ArrayList<Menu> menuList;
	private ArrayList<Meal> mealList;
	
	private MenuPanel() {
		
		// Panel dimensions setup
		int mainHeight = (int)MainFrame.height;
		int mainWidth = (int)MainFrame.width;
		int panelHeight = (int) ((int)mainHeight*0.84);
		int panelWidth = mainWidth;
		
		// panel functional setup
		setPreferredSize(new Dimension(panelWidth, panelHeight));
		setBackground(ProjectColors.GREY.get());
		setVisible(true);	
		setLayout(new BorderLayout());
		menuController = new MenuController();
		
		// -----------------------------------------------
		// ------------------ TOOL ---------------------
		// -----------------------------------------------
		
		// tool panel
		ToolPanel toolPanel = new ToolPanel();
		toolPanel.setBorder(new LineBorder(Color.GREEN));
		add(toolPanel, BorderLayout.NORTH);
		GridBagConstraints gbcTool = new GridBagConstraints();
		gbcTool.weightx = 1;
		gbcTool.weighty = 1;

		// search bar
		searchBar = new JTextField();
		searchBar.setFont(Fonts.FONT20.get());
		searchBar.setPreferredSize(new Dimension(ToolPanel.getPanelWidth()/4,
				ToolPanel.getPanelHeight()/3 ));
		searchBar.setBorder(new CompoundBorder(new LineBorder(Color.BLACK, 1), new EmptyBorder(0, 10, 0, 0)));
		searchBar.setBackground(Color.WHITE);
		searchBar.setToolTipText("Search by name");
		gbcTool.gridx = 0;
		gbcTool.gridy = 0;
		toolPanel.add(searchBar, gbcTool);

		// search button
		JButton searchButton = new JButton("Search");
		searchButton.setFont(Fonts.FONT20.get());
		searchButton.setBackground(ProjectColors.RED.get());
		searchButton.setPreferredSize(new Dimension(ToolPanel.getPanelWidth()/8,
				ToolPanel.getPanelHeight()/2 ));
		searchButton.setFocusable(false);
		// searchButton.addActionListener(e -> search());
		gbcTool.gridx = 1;
		gbcTool.anchor = GridBagConstraints.WEST;
		toolPanel.add(searchButton, gbcTool);
		
		// switch button
		switchButton = new JButton("Go to Meals");
		switchButton.setFont(Fonts.FONT20.get());
		switchButton.setBackground(ProjectColors.RED.get());
		switchButton.setPreferredSize(new Dimension(ToolPanel.getPanelWidth()/8,
				ToolPanel.getPanelHeight()/2 ));
		switchButton.setFocusable(false);
		switchButton.addActionListener(e -> switchPanel());
		gbcTool.gridx = 2;
		toolPanel.add(switchButton, gbcTool);
		
		// -----------------------------------------------
		// ------------------ MENU ---------------------
		// -----------------------------------------------
		
		// Menu panel
		menuPanel = new JPanel();
		currentPanel = menuPanel;
		menuPanel.setLayout(new GridBagLayout());
		menuPanel.setBackground(Color.WHITE);
		add(menuPanel, BorderLayout.CENTER);
		
		GridBagConstraints gbcMenu = new GridBagConstraints();
		gbcMenu.weightx = 1;
		gbcMenu.weighty = 1;
		
		// Menu table
		menuTable = new JTable() {
			@Override
			public Component prepareRenderer(TableCellRenderer renderer, int row, int column) {
				Component component = super.prepareRenderer(renderer, row, column);
				int rendererWidth = component.getPreferredSize().width;
				TableColumn tableColumn = getColumnModel().getColumn(column);
				tableColumn.setPreferredWidth(
						Math.max(rendererWidth + getIntercellSpacing().width, tableColumn.getPreferredWidth()));
				return component;
			}
		};
		menuTableModel = new DefaultTableModel();
		menuTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		menuTable.setPreferredScrollableViewportSize(new Dimension(getWidth(), getHeight() - 100));
		menuTable.setFont(new Font("Tahoma", Font.PLAIN, 16));
		menuTable.setModel(menuTableModel);
		menuTable.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
		menuTable.setDefaultEditor(Object.class, null);
		menuTable.setRowHeight((int) (menuTable.getRowHeight() * 2));
		JTableHeader menuTableHeader = menuTable.getTableHeader();
		menuTableHeader.setFont(new Font("Tahoma", Font.BOLD, 16));
		menuTableHeader.setResizingAllowed(false);
		menuTableHeader.setReorderingAllowed(false);
		menuTable.setTableHeader(menuTableHeader);
		createMenuTableColumns();
		try {
			menuList = menuController.getMenuList();
			populateMenuTable(menuList);
		} catch (SQLException e) {
			JOptionPane.showMessageDialog(null,
					"An error occured, while getting reservation information! \nTry refreshing the table", "Error",
					JOptionPane.WARNING_MESSAGE);
		}


		// Menu scroll pane
		JScrollPane menuScrollPane = new JScrollPane(menuTable);
		menuScrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		menuScrollPane.setPreferredSize(new Dimension(Integer.valueOf((int) (panelWidth*0.9)) , 
				Integer.valueOf((int) ((int) panelHeight*0.8))));
		menuScrollPane.setBorder(null);
		menuScrollPane.setLayout(new ScrollPaneLayout());
		gbcMenu.gridx = 0;
		gbcMenu.gridy = 0;
		menuPanel.add(menuScrollPane, gbcMenu);
		
		// -----------------------------------------------
		// ------------------ MEAL ---------------------
		// -----------------------------------------------
		
		// Meal panel
		mealPanel = new JPanel();
		mealPanel.setLayout(new GridBagLayout());
		mealPanel.setBackground(Color.WHITE);
				
		GridBagConstraints gbcMeal = new GridBagConstraints();
		gbcMeal.weightx = 1;
		gbcMeal.weighty = 1;
				
		mealTable = new JTable() {
			@Override
			public Component prepareRenderer(TableCellRenderer renderer, int row, int column) {
				Component component = super.prepareRenderer(renderer, row, column);
				int rendererWidth = component.getPreferredSize().width;
				TableColumn tableColumn = getColumnModel().getColumn(column);
				tableColumn.setPreferredWidth(
						Math.max(rendererWidth + getIntercellSpacing().width, tableColumn.getPreferredWidth()));
				return component;
			}
		};
		mealTableModel = new DefaultTableModel();
		mealTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		mealTable.setPreferredScrollableViewportSize(new Dimension(getWidth(), getHeight() - 100));
		mealTable.setFont(new Font("Tahoma", Font.PLAIN, 16));
		mealTable.setModel(mealTableModel);
		mealTable.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
		mealTable.setDefaultEditor(Object.class, null);
		mealTable.setRowHeight((int) (mealTable.getRowHeight() * 2));
		JTableHeader mealTableHeader = mealTable.getTableHeader();
		mealTableHeader.setFont(new Font("Tahoma", Font.BOLD, 16));
		mealTableHeader.setResizingAllowed(false);
		mealTableHeader.setReorderingAllowed(false);
		mealTable.setTableHeader(mealTableHeader);
		createMealTableColumns();
		try {
			mealList = menuController.getMealList();
			populateMealTable(mealList);
		} catch (SQLException e) {
			JOptionPane.showMessageDialog(null,
					"An error occured, while getting reservation information! \nTry refreshing the table", "Error",
					JOptionPane.WARNING_MESSAGE);
		}


		// Meal scroll pane
		JScrollPane mealScrollPane = new JScrollPane(mealTable);
		mealScrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		mealScrollPane.setPreferredSize(new Dimension(Integer.valueOf((int) (panelWidth*0.9)) , 
				Integer.valueOf((int) ((int) panelHeight*0.8))));
		mealScrollPane.setBorder(null);
		mealScrollPane.setLayout(new ScrollPaneLayout());
		gbcMeal.gridx = 0;
		gbcMeal.gridy = 0;
		mealPanel.add(mealScrollPane, gbcMeal);
		
		// -----------------------------------------------
		// ------------------ FOOTER ---------------------
		// -----------------------------------------------
		
		FooterPanel footerPanel = new FooterPanel();
		add(footerPanel, BorderLayout.SOUTH);
		footerPanel.setLayout(new GridBagLayout());
		GridBagConstraints gbcFooter = new GridBagConstraints();
		gbcFooter.weightx = 1;
		gbcFooter.weighty = 1;

		// delete button
		JButton deleteButton = new JButton("Delete");
		deleteButton.addActionListener(e -> delete());
		deleteButton.setFont(Fonts.FONT20.get());
		deleteButton.setBackground(ProjectColors.RED.get());
		deleteButton.setPreferredSize(new Dimension(FooterPanel.panelWidth/8, FooterPanel.panelHeight/2));
		deleteButton.setFocusable(false);
		gbcFooter.gridx = 0;
		gbcFooter.anchor = GridBagConstraints.WEST;
		footerPanel.add(deleteButton, gbcFooter);

		// add button
		JButton addButton = new JButton("Add");
		addButton.setFont(Fonts.FONT20.get());
		addButton.setBackground(ProjectColors.RED.get());
		addButton.addActionListener(e -> create());
		addButton.setPreferredSize(new Dimension(FooterPanel.panelWidth/8, FooterPanel.panelHeight/2));
		addButton.setFocusable(false);
		gbcFooter.gridx = 1;
		gbcFooter.anchor = GridBagConstraints.WEST;
		footerPanel.add(addButton, gbcFooter);


	}// End of constructor
	
	private void delete() {
		if(currentPanel.equals(menuPanel)) {
			try {
				if (menuTable.getSelectedRow() != -1) {
					if (JOptionPane.showConfirmDialog(null,
							"Are you sure you want to delete the menu?\nThis action is permanent!",
							"Menu deletion", JOptionPane.YES_NO_OPTION,
							JOptionPane.WARNING_MESSAGE) == JOptionPane.YES_OPTION) {
						int id = Integer.parseInt(menuTable.getValueAt(menuTable.getSelectedRow(), 0).toString());
						menuTableModel.removeRow(menuTable.getSelectedRow());
						if (menuController.getMenuById(id) != null) {
							Menu menu = menuController.getMenuById(id);
							menuController.deleteMenu(menu);
						}
					}
				} else {
					JOptionPane.showMessageDialog(null, "You must select a menu in the list you want to delete!",
							"Error", JOptionPane.WARNING_MESSAGE);
				}
			} catch (SQLException e) {
				JOptionPane.showMessageDialog(null,
						"An error occured, while getting menu information! \nTry refreshing the table", "Error",
						JOptionPane.WARNING_MESSAGE);
			}
		}
	}
	
	// here should be passed menu without problems!
	public void createMeal(Meal meal) {
		try {
			menuController.createMeal(meal);
			mealList.clear();
			mealList.addAll(menuController.getMealList());
			populateMealTable(mealList);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	// here should be passed menu without problems!
	public void createMenu(Menu menu) {
		try {
			menuController.createMenu(menu);
			menuList.clear();
			menuList.addAll(menuController.getMenuList());
			populateMenuTable(menuList);
		} catch (SQLException e) {
			// TODO Auto-generated catch block ****
		}
	}
	
	private void create() {
		if(currentPanel.equals(menuPanel)) {
			new AddMenuInputFrame();
		}
		else {
			new AddMealInputFrame();
		}
	}
	
	private void createMealTableColumns() {
		Object[] columns = { "ID", "Name", "Price", "Description"};
		for (Object o : columns) {
			mealTableModel.addColumn(o);
		}
	}
	
	private void populateMealTable(ArrayList<Meal> meals) {
		mealTableModel.setRowCount(0);
		if (!meals.isEmpty()) {
			for (Meal meal : meals) {
				mealTableModel.addRow(new Object[] {meal.getId(), meal.getName(), 
						meal.getPrice(), meal.getDescription() });
			}
			mealTableModel.fireTableDataChanged();
		}
	}
	
	private void createMenuTableColumns() {
		Object[] columns = { "ID", "Name", "Meals"};
		for (Object o : columns) {
			menuTableModel.addColumn(o);
		}
	}
	
	private void populateMenuTable(ArrayList<Menu> menus) {
		menuTableModel.setRowCount(0);
		String string = new String("");
		if (!menus.isEmpty()) {
			for (Menu menu : menus) {
				for(Meal meal : menu.getMeals()) {
					string += (meal.getName()+ " ");
				}
				menuTableModel.addRow(new Object[] {menu.getID(), menu.getName(), string });
			}
			menuTableModel.fireTableDataChanged();
		}
	}
	
	private void switchPanel() {
		if(currentPanel.equals(menuPanel)) {
			currentPanel = mealPanel;
			this.remove(menuPanel);
			add(currentPanel, BorderLayout.CENTER);
			switchButton.setText("Go to Menus");
		}
		else {
			currentPanel = menuPanel;
			this.remove(mealPanel);
			add(currentPanel, BorderLayout.CENTER);
			switchButton.setText("Go to Meals");
		}
	}
	
	public ArrayList<Meal> getMealList(){
		return mealList;
	}
	
	public ArrayList<Menu> getMenuList(){
		return menuList;
	}
	
	public static MenuPanel getInstance() {
		if(instance == null) return instance = new MenuPanel();
		else return instance;
	}

}