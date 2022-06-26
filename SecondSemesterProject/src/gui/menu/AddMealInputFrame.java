package gui.menu;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import gui.MenuPanel;
import gui.tools.FancyButtonOneClick;
import gui.tools.Fonts;
import gui.tools.ProjectColors;
import model.Meal;

public class AddMealInputFrame extends JFrame {
	
	private JTextField nameTxtField,priceTxtField;
	private FancyButtonOneClick createBtn;
	private JTextArea descriptionTxtArea;
	
	public AddMealInputFrame(){
		//frame setup
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setTitle("Create new Meal");
		setResizable(false);
		setBounds(100, 100, 400, 350);
		setVisible(true);
		
		//main panel setup
		JPanel mainPanel = new JPanel();
		mainPanel.setLayout(new BorderLayout());
		mainPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(mainPanel);
		
		//content pane setup
		JPanel contentPane = new JPanel();
		contentPane.setLayout(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();
		mainPanel.add(contentPane, BorderLayout.NORTH);	
		
		JLabel nameLbl = new JLabel("Name");
		nameLbl.setFont(Fonts.FONT20.get());
		gbc.weightx = 1;
		gbc.weighty = 0.3;
		gbc.anchor = GridBagConstraints.LAST_LINE_START;
		gbc.gridx = 0;
		gbc.gridy = 0;
		contentPane.add(nameLbl,gbc);
		
		nameTxtField = new JTextField();
		nameTxtField.setPreferredSize(new Dimension(300,30));
		nameTxtField.setFont(Fonts.FONT20.get());
		gbc.anchor = GridBagConstraints.FIRST_LINE_START;
		gbc.gridx = 0;
		gbc.gridy = 1;
		contentPane.add(nameTxtField,gbc);
		
		JLabel priceLbl = new JLabel("Price");
		priceLbl.setFont(Fonts.FONT20.get());
		gbc.anchor = GridBagConstraints.LAST_LINE_START;
		gbc.gridx = 0;
		gbc.gridy = 2;
		contentPane.add(priceLbl,gbc);
		
		priceTxtField = new JTextField();
		priceTxtField.setFont(Fonts.FONT20.get());
		priceTxtField.setPreferredSize(new Dimension(300,30));
		gbc.anchor = GridBagConstraints.FIRST_LINE_START;
		gbc.gridx = 0;
		gbc.gridy = 3;
		contentPane.add(priceTxtField,gbc);
		
		JLabel descriptionLbl = new JLabel("Description");
		descriptionLbl.setFont(Fonts.FONT20.get());
		gbc.anchor = GridBagConstraints.LAST_LINE_START;
		gbc.gridx = 0;
		gbc.gridy = 4;
		contentPane.add(descriptionLbl,gbc);
		
		descriptionTxtArea = new JTextArea();
		descriptionTxtArea.setFont(Fonts.FONT20.get());
		descriptionTxtArea.setPreferredSize(new Dimension(300,60));
		gbc.anchor = GridBagConstraints.FIRST_LINE_START;
		gbc.gridx = 0;
		gbc.gridy = 5;
		contentPane.add(descriptionTxtArea,gbc);
		
		// Create button
		createBtn = new FancyButtonOneClick(ProjectColors.BLACK.get(), ProjectColors.RED.get(), ProjectColors.RED.get());
		createBtn.setFont(Fonts.FONT20.get());
		createBtn.setBorderPainted(true);
//		createBtn.setPreferredSize(new Dimension(panelWidth/12, ToolPanel.getPanelHeight()/2));
		createBtn.addActionListener(e -> createMeal());
		createBtn.setText("create");
		createBtn.setBorder(BorderFactory.createLineBorder(ProjectColors.BLACK.get(), 1));
		mainPanel.add(createBtn,BorderLayout.SOUTH);
	}
	
	private void createMeal() {
		float price = -1;
		String mealName = nameTxtField.getText(); // check input
		if(mealName.length() > 20) {
			JOptionPane.showMessageDialog(null,
					"Menu name is too long! \nMaximum name length is 20 characters", "Action denied",
					JOptionPane.WARNING_MESSAGE);
			return;
		}
		if(mealName.length() == 0) {
			JOptionPane.showMessageDialog(null,
					"Name field is empty! \nYou have to set some name", "Action denied",
					JOptionPane.WARNING_MESSAGE);
			return;
		}
		try {
		price = Float.valueOf(priceTxtField.getText());
		}
		catch(NumberFormatException e) {
			JOptionPane.showMessageDialog(null,
					"Price has to be a number! \nPlease, input number in the price field", "Action denied",
					JOptionPane.WARNING_MESSAGE);
			return;
		}
		if(price <= 0 || Float.valueOf(price) == null) {
			JOptionPane.showMessageDialog(null,
					"Incorrent price value! \n Set price field correctly", "Action denied",
					JOptionPane.WARNING_MESSAGE);
			return;
		}
		String description = descriptionTxtArea.getText();
		MenuPanel.getInstance().createMeal(new Meal(mealName,description,price));
		dispose();
		JOptionPane.showConfirmDialog(null, "Meal was create", "Creation of meal",
				JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
	}

}
