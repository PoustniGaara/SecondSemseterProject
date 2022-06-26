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
import javax.swing.border.MatteBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;

import controller.MenuController;
import gui.menu.AddMealInputFrame;
import gui.menu.AddMenuInputFrame;
import gui.tools.Fonts;
import gui.tools.ProjectColors;
import model.Meal;
import model.Menu;

public class MenuPanel extends JPanel {

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
		menuController = new MenuController();

		// panel functional setup
		setBounds(0, 100, MainFrame.width, MainFrame.height - 100);
		setBackground(ProjectColors.BG.get());
		setVisible(true);
		setLayout(new BorderLayout());

		// -----------------------------------------------
		// ------------------ TOOL ---------------------
		// -----------------------------------------------

		// tool panel
		ToolPanel toolPanel = new ToolPanel();
		toolPanel.setBackground(ProjectColors.BG.get());
		add(toolPanel, BorderLayout.NORTH);

		GridBagConstraints gbcTool = new GridBagConstraints();
		gbcTool.fill = GridBagConstraints.NONE;
		gbcTool.anchor = GridBagConstraints.CENTER;
		gbcTool.weightx = 0.5;
		gbcTool.weighty = 0.2;

		// search bar
		searchBar = new JTextField();
		searchBar.setFont(Fonts.FONT18.get());
		searchBar.setSize(new Dimension((int) (getWidth() * 0.45), 40));
		searchBar.setPreferredSize(new Dimension((int) (getWidth() * 0.45), 40));
		searchBar.setBorder(
				new CompoundBorder(new LineBorder(new Color(212, 221, 233), 1), new EmptyBorder(0, 10, 0, 0)));
		searchBar.setBackground(Color.WHITE);
		searchBar.setToolTipText("Search Menus by name");
		gbcTool.gridx = 0;
		gbcTool.gridy = 0;
		gbcTool.gridwidth = 2;
		gbcTool.gridheight = 1;
		gbcTool.insets = new Insets(20, 35, 20, 0);
		gbcTool.anchor = GridBagConstraints.WEST;
		toolPanel.add(searchBar, gbcTool);

		// search button
		JButton searchButton = new JButton("Search");
		searchButton.setFont(Fonts.FONT18.get());
		searchButton.setBackground(Color.white);
		searchButton.setBorder(new LineBorder(ProjectColors.BLUE3.get(), 1));
		searchButton.setPreferredSize(new Dimension((int) (getWidth() * 0.15), 40));
		searchButton.setFocusable(false);
		// searchButton.addActionListener(e -> search());
		gbcTool.gridx = 2;
		gbcTool.gridwidth = 1;
		gbcTool.insets = new Insets(20, 0, 20, 35);
		gbcTool.anchor = GridBagConstraints.WEST;
		toolPanel.add(searchButton, gbcTool);

		// switch button
		switchButton = new JButton("Go to Meals");
		switchButton.setFont(Fonts.FONT18.get());
		switchButton.setBackground(Color.white);
		switchButton.setBorder(new LineBorder(ProjectColors.LIGHT_BLUE.get(), 1));
		switchButton.setPreferredSize(new Dimension((int) (getWidth() * 0.15), 40));
		switchButton.setFocusable(false);
		switchButton.addActionListener(e -> switchPanel());
		gbcTool.gridx = 3;
		gbcTool.anchor = GridBagConstraints.EAST;
		toolPanel.add(switchButton, gbcTool);

		// -----------------------------------------------
		// ------------------ MENU ---------------------
		// -----------------------------------------------

		// Menu panel
		menuPanel = new JPanel();
		currentPanel = menuPanel;
		menuPanel.setLayout(new GridBagLayout());
		menuPanel.setBackground(Color.WHITE);
		menuPanel.setBorder(new MatteBorder(0, 0, 1, 0, ProjectColors.BLUE1.get()));
		add(menuPanel, BorderLayout.CENTER);

		GridBagConstraints gbcMenu = new GridBagConstraints();
		gbcMenu.insets = new Insets(20, 35, 20, 35);
		gbcMenu.fill = GridBagConstraints.BOTH;
		gbcMenu.anchor = GridBagConstraints.CENTER;
		gbcMenu.gridwidth = GridBagConstraints.REMAINDER;
		gbcMenu.gridheight = GridBagConstraints.REMAINDER;
		gbcMenu.weightx = 1;
		gbcMenu.weighty = 1;
		gbcMenu.gridx = 0;
		gbcMenu.gridy = 0;

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
		menuTable.setBackground(ProjectColors.BLUEtable.get());
		menuTable.setShowGrid(false);
		menuTable.setShowHorizontalLines(true);
		menuTable.setFont(Fonts.TABLE_FONT.get());
		menuTable.setModel(menuTableModel);
		menuTable.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
		menuTable.setDefaultEditor(Object.class, null);
		menuTable.setRowHeight((int) (menuTable.getRowHeight() * 2));
		menuTable.setBorder(null);
		menuTable.setFillsViewportHeight(true);
		JTableHeader menuTableHeader = menuTable.getTableHeader();
		menuTableHeader.setFont(Fonts.FONT18.get());
		menuTableHeader.setResizingAllowed(false);
		menuTableHeader.setReorderingAllowed(false);
		menuTableHeader.setBackground(ProjectColors.BLUE1.get());
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
		menuScrollPane.setBorder(null);
		menuScrollPane.setLayout(new ScrollPaneLayout());
		menuPanel.add(menuScrollPane, gbcMenu);

		// -----------------------------------------------
		// ------------------ MEAL ---------------------
		// -----------------------------------------------

		// Meal panel
		mealPanel = new JPanel();
		mealPanel.setLayout(new GridBagLayout());
		mealPanel.setBackground(Color.WHITE);

		GridBagConstraints gbcMeal = new GridBagConstraints();
		gbcMeal.insets = new Insets(20, 35, 20, 35);
		gbcMeal.fill = GridBagConstraints.BOTH;
		gbcMeal.anchor = GridBagConstraints.CENTER;
		gbcMeal.gridwidth = GridBagConstraints.REMAINDER;
		gbcMeal.gridheight = GridBagConstraints.REMAINDER;
		gbcMeal.weightx = 1;
		gbcMeal.weighty = 1;
		gbcMeal.gridx = 0;
		gbcMeal.gridy = 0;

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
		mealTable.setBackground(ProjectColors.BLUEtable.get());
		mealTable.setShowGrid(false);
		mealTable.setShowHorizontalLines(true);
		mealTable.setFont(Fonts.TABLE_FONT.get());
		mealTable.setModel(mealTableModel);
		mealTable.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
		mealTable.setDefaultEditor(Object.class, null);
		mealTable.setRowHeight((int) (mealTable.getRowHeight() * 2));
		mealTable.setBorder(null);
		mealTable.setFillsViewportHeight(true);
		JTableHeader mealTableHeader = mealTable.getTableHeader();
		mealTableHeader.setFont(Fonts.FONT18.get());
		mealTableHeader.setResizingAllowed(false);
		mealTableHeader.setReorderingAllowed(false);
		mealTableHeader.setBackground(ProjectColors.BLUE1.get());
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
		mealScrollPane.setBorder(null);
		mealScrollPane.setLayout(new ScrollPaneLayout());
		mealPanel.add(mealScrollPane, gbcMeal);

		// -----------------------------------------------
		// ------------------ FOOTER ---------------------
		// -----------------------------------------------

		FooterPanel footerPanel = new FooterPanel();
		footerPanel.setBackground(Color.WHITE);
		add(footerPanel, BorderLayout.SOUTH);
		GridBagLayout footergbl = new GridBagLayout();
		footergbl.columnWidths = new int[] { 500, 170, 170, 170 };
		footerPanel.setLayout(footergbl);

		GridBagConstraints gbcFooter = new GridBagConstraints();
		gbcFooter.insets = new Insets(20, 5, 0, 15);
		gbcFooter.fill = GridBagConstraints.VERTICAL;
		gbcFooter.anchor = GridBagConstraints.SOUTHWEST;
		gbcFooter.gridwidth = 1;
		gbcFooter.weightx = 1;
		gbcFooter.weighty = 0;
		gbcFooter.ipadx = 0;
		gbcFooter.ipady = 0;
		gbcFooter.gridx = 0;
		gbcFooter.gridy = 0;

		JLabel conCheck = ConnectionCheckLabel.getInstance();
		footerPanel.add(conCheck, gbcFooter);

		// delete button
		JButton deleteButton = new JButton("Delete");
		deleteButton.addActionListener(e -> delete());
		deleteButton.setFont(Fonts.FONT18.get());
		deleteButton.setForeground(new Color(195, 70, 70));
		deleteButton.setBackground(new Color(252, 232, 232));
		deleteButton.setBorder(new LineBorder(new Color(220, 48, 48), 1));
		deleteButton.setPreferredSize(new Dimension(FooterPanel.panelWidth / 6, (int) (FooterPanel.panelHeight * 0.6)));
		deleteButton.setFocusable(false);
		gbcFooter.insets = new Insets(10, 15, 10, 15);
		gbcFooter.anchor = GridBagConstraints.EAST;
		gbcFooter.gridx = 1;
		footerPanel.add(deleteButton, gbcFooter);

		JButton modifyButton = new JButton("Change");
		modifyButton.setFont(Fonts.FONT18.get());
		modifyButton.setBackground(ProjectColors.BLUE1.get());
		modifyButton.setBorder(new LineBorder(ProjectColors.LIGHT_BLUE.get(), 1));
		modifyButton.setPreferredSize(new Dimension(FooterPanel.panelWidth / 6, (int) (FooterPanel.panelHeight * 0.6)));
		modifyButton.setFocusable(false);
		// modifyButton.addActionListener(e -> change());
		gbcFooter.gridx = 2;
		footerPanel.add(modifyButton, gbcFooter);

		// add button
		JButton addButton = new JButton("Add +");
		addButton.setFont(Fonts.FONT18.get());
		addButton.setBackground(ProjectColors.BLUE.get());
		addButton.setForeground(Color.white);
		addButton.setPreferredSize(new Dimension(FooterPanel.panelWidth / 6, (int) (FooterPanel.panelHeight * 0.6)));
		addButton.setFocusable(false);
		addButton.addActionListener(e -> create());
		gbcFooter.gridx = 3;
		gbcFooter.insets = new Insets(10, 15, 10, 35);
		footerPanel.add(addButton, gbcFooter);
	}

	private void delete() {
		if (currentPanel.equals(menuPanel)) {
			try {
				if (menuTable.getSelectedRow() != -1) {
					if (JOptionPane.showConfirmDialog(null,
							"Are you sure you want to delete the menu?\nThis action is permanent!", "Menu deletion",
							JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE) == JOptionPane.YES_OPTION) {
						int id = Integer.parseInt(menuTable.getValueAt(menuTable.getSelectedRow(), 0).toString());
						menuTableModel.removeRow(menuTable.getSelectedRow());
						if (menuController.getMenuById(id) != null) {
							Menu menu = menuController.getMenuById(id);
							menuController.deleteMenu(menu);
							JOptionPane.showConfirmDialog(null, "Menu was deleted", "Deletion of menu",
									JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
						}
					}
				} else {
					JOptionPane.showMessageDialog(null, "You must select a menu from the list you want to delete!",
							"Error", JOptionPane.WARNING_MESSAGE);
				}
			} catch (SQLException e) {
				JOptionPane.showMessageDialog(null,
						"An error occured, while getting menu information! \nTry refreshing the table", "Error",
						JOptionPane.WARNING_MESSAGE);
			}
		}
		else {
			try {
				if (mealTable.getSelectedRow() != -1) {
					if (JOptionPane.showConfirmDialog(null,
							"Are you sure you want to delete the meal?\nThis action is permanent!", "Meal deletion",
							JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE) == JOptionPane.YES_OPTION) {
						int id = Integer.parseInt(mealTable.getValueAt(mealTable.getSelectedRow(), 0).toString());
						mealTableModel.removeRow(mealTable.getSelectedRow());
						if (menuController.getMealById(id) != null) {
							Meal meal = menuController.getMealById(id);
							menuController.deleteMeal(meal);
							JOptionPane.showConfirmDialog(null, "Meal was deleted successfuly", "Deletion of meal",
									JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
						}
					}
				} else {
					JOptionPane.showMessageDialog(null, "You must select a meal from the list you want to delete!",
							"Error", JOptionPane.WARNING_MESSAGE);
				}
			} catch (SQLException e) {
				JOptionPane.showMessageDialog(null,
						"An error occured, while getting meal information! \nTry refreshing the table", "Error",
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
		if (currentPanel.equals(menuPanel)) {
			new AddMenuInputFrame();
		} else {
			new AddMealInputFrame();
		}
	}

	private void createMealTableColumns() {
		Object[] columns = { "ID", "Name", "Price", "Description" };
		for (Object o : columns) {
			mealTableModel.addColumn(o);
		}
	}

	private void populateMealTable(ArrayList<Meal> meals) {
		mealTableModel.setRowCount(0);
		if (!meals.isEmpty()) {
			for (Meal meal : meals) {
				mealTableModel
						.addRow(new Object[] { meal.getId(), meal.getName(), meal.getPrice(), meal.getDescription() });
			}
			mealTableModel.fireTableDataChanged();
		}
	}

	private void createMenuTableColumns() {
		Object[] columns = { "ID", "Name", "Meals" };
		for (Object o : columns) {
			menuTableModel.addColumn(o);
		}
	}

	private void populateMenuTable(ArrayList<Menu> menus) {
		menuTableModel.setRowCount(0);
		String string = new String("");
		if (!menus.isEmpty()) {
			for (Menu menu : menus) {
				for (Meal meal : menu.getMeals()) {
					string += (meal.getName() + " ");
				}
				menuTableModel.addRow(new Object[] { menu.getID(), menu.getName(), string });
			}
			menuTableModel.fireTableDataChanged();
		}
	}

	private void switchPanel() {
		if (currentPanel.equals(menuPanel)) {
			currentPanel = mealPanel;
			this.remove(menuPanel);
			add(currentPanel, BorderLayout.CENTER);
			switchButton.setText("Go to Menus");
		} else {
			currentPanel = menuPanel;
			this.remove(mealPanel);
			add(currentPanel, BorderLayout.CENTER);
			switchButton.setText("Go to Meals");
		}
	}

	public ArrayList<Meal> getMealList() {
		return mealList;
	}

	public ArrayList<Menu> getMenuList() {
		return menuList;
	}

	public static MenuPanel getInstance() {
		if (instance == null)
			return instance = new MenuPanel();
		else
			return instance;
	}

}