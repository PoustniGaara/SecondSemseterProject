package gui.menu;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import controller.MenuController;
import gui.MenuPanel;
import gui.tools.FancyButtonOneClick;
import gui.tools.Fonts;
import gui.tools.ProjectColors;
import model.Meal;
import model.Menu;

public class AddMenuInputFrame extends JFrame {

	private FancyButtonOneClick createBtn;
	private JComboBox<String> meal1CB, meal2CB, meal3CB;
	private JTextField nameTxtField;

	public AddMenuInputFrame() {

		// frame setup
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setTitle("Create new Menu");
		setResizable(false);
		setBounds(100, 100, 400, 350);
		setVisible(true);

		// main panel setup
		JPanel mainPanel = new JPanel();
		mainPanel.setLayout(new BorderLayout());
		mainPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(mainPanel);

		// content pane setup
		JPanel contentPane = new JPanel();
		contentPane.setLayout(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();
		mainPanel.add(contentPane, BorderLayout.NORTH);

		JLabel nameLbl = new JLabel("Name");
		nameLbl.setFont(Fonts.FONT20.get());
		gbc.weightx = 1;
		gbc.weighty = 1;
		gbc.anchor = GridBagConstraints.LAST_LINE_START;
		gbc.gridx = 0;
		gbc.gridy = 0;
		contentPane.add(nameLbl, gbc);

		nameTxtField = new JTextField(10);
		nameTxtField.setFont(Fonts.FONT20.get());
//		gbc.gridwidth = 2;
		gbc.anchor = GridBagConstraints.FIRST_LINE_START;
		gbc.gridx = 0;
		gbc.gridy = 1;
		contentPane.add(nameTxtField, gbc);

		JLabel meal1Lbl = new JLabel("Meal 1");
		meal1Lbl.setFont(Fonts.FONT20.get());
		gbc.weightx = 1;
		gbc.anchor = GridBagConstraints.LAST_LINE_START;
		gbc.gridx = 0;
		gbc.gridy = 2;
		contentPane.add(meal1Lbl, gbc);

		meal1CB = new JComboBox(new DefaultComboBoxModel());
		meal1CB.setMaximumRowCount(3);
		meal1CB.setFont(Fonts.FONT18.get());
		meal1CB.setPreferredSize(new Dimension(100, 30));
		gbc.anchor = GridBagConstraints.FIRST_LINE_START;
		gbc.gridx = 0;
		gbc.gridy = 3;
		contentPane.add(meal1CB, gbc);

		JLabel meal2Lbl = new JLabel("Meal 2");
		meal2Lbl.setFont(Fonts.FONT20.get());
		gbc.anchor = GridBagConstraints.LAST_LINE_START;
		gbc.gridx = 0;
		gbc.gridy = 4;
		contentPane.add(meal2Lbl, gbc);

		meal2CB = new JComboBox(new DefaultComboBoxModel());
		meal2CB.setMaximumRowCount(3);
		meal2CB.setFont(Fonts.FONT18.get());
		meal2CB.setPreferredSize(new Dimension(100, 30));
		gbc.anchor = GridBagConstraints.FIRST_LINE_START;
		gbc.gridx = 0;
		gbc.gridy = 5;
		contentPane.add(meal2CB, gbc);

		JLabel meal3Lbl = new JLabel("Meal 3");
		meal3Lbl.setFont(Fonts.FONT20.get());
		gbc.anchor = GridBagConstraints.LAST_LINE_START;
		gbc.gridx = 0;
		gbc.gridy = 6;
		contentPane.add(meal3Lbl, gbc);

		meal3CB = new JComboBox(new DefaultComboBoxModel());
		meal3CB.setMaximumRowCount(3);
		meal3CB.setFont(Fonts.FONT18.get());
		meal3CB.setPreferredSize(new Dimension(100, 30));
		gbc.anchor = GridBagConstraints.FIRST_LINE_START;
		gbc.gridx = 0;
		gbc.gridy = 7;
		contentPane.add(meal3CB, gbc);

		populateMealComboBox();

		// Create button
		createBtn = new FancyButtonOneClick(ProjectColors.BLACK.get(), ProjectColors.RED.get(),
				ProjectColors.RED.get());
		createBtn.setFont(Fonts.FONT20.get());
		createBtn.setBorderPainted(true);
//		createBtn.setPreferredSize(new Dimension(panelWidth/12, ToolPanel.getPanelHeight()/2));
		createBtn.addActionListener(e -> createMenu());
		createBtn.setText("create");
		createBtn.setBorder(BorderFactory.createLineBorder(ProjectColors.BLACK.get(), 1));
		mainPanel.add(createBtn, BorderLayout.SOUTH);

	}

	private void createMenu() {
		String menuName = nameTxtField.getText(); // check from invalid input **
		ArrayList<Meal> selectedMeals = new ArrayList<>();
		String meal1 = meal1CB.getSelectedItem().toString();
		String meal2 = meal2CB.getSelectedItem().toString();
		String meal3 = meal3CB.getSelectedItem().toString();
		;
		for (Meal meal : MenuPanel.getInstance().getMealList()) {
			if (meal1.equals(meal.getName()))
				selectedMeals.add(meal);
			if (meal2.equals(meal.getName()))
				selectedMeals.add(meal);
			if (meal2.equals(meal.getName()))
				selectedMeals.add(meal);
		}
		Menu menu = new Menu(menuName, selectedMeals);
		MenuPanel.getInstance().createMenu(menu);
		dispose();
	}

	private void populateMealComboBox() {
		for (Meal meal : MenuPanel.getInstance().getMealList()) {
			meal1CB.addItem(meal.getName());
			meal2CB.addItem(meal.getName());
			meal3CB.addItem(meal.getName());
		}
	}

}
