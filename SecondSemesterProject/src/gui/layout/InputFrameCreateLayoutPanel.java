package gui.layout;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.sql.SQLException;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import controller.RestaurantLayoutController;
import gui.ToolPanel;
import gui.tools.FancyButtonOneClick;
import gui.tools.Fonts;
import gui.tools.ProjectColors;
import model.RestaurantLayout;

public class InputFrameCreateLayoutPanel extends JFrame {

	private static InputFrameCreateLayoutPanel instance;
	private JTextField nameTxtField, widthTxtField, heightTxtField;
	private FancyButtonOneClick createBtn;
	private LayoutEditorFrame layoutEditorFrame;
	private RestaurantLayoutController restaurantLayoutController;

	InputFrameCreateLayoutPanel(LayoutEditorFrame layoutEditorFrame) {

		// frame setup
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setTitle("create restaurant layout");
		setResizable(false);
		setBounds(100, 100, 400, 300);
		setVisible(true);

		// controller setup
		restaurantLayoutController = new RestaurantLayoutController();

		// LayoutEditorPanel setup
		this.layoutEditorFrame = layoutEditorFrame;

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

		JLabel nameLabel = new JLabel("Name:");
		nameLabel.setFont(Fonts.FONT18.get());
		gbc.anchor = GridBagConstraints.LAST_LINE_START;
		gbc.gridx = 0;
		gbc.gridy = 0;
		contentPane.add(nameLabel, gbc);

		nameTxtField = new JTextField();
		nameTxtField.setFont(Fonts.FONT20.get());
		gbc.gridwidth = 2;
		gbc.anchor = GridBagConstraints.FIRST_LINE_START;
		nameTxtField.setPreferredSize(new Dimension(300, 30));
		gbc.gridx = 0;
		gbc.gridy = 1;
		contentPane.add(nameTxtField, gbc);

		JLabel widthLabel = new JLabel("Width:");
		widthLabel.setFont(Fonts.FONT18.get());
		gbc.anchor = GridBagConstraints.LAST_LINE_START;
		gbc.gridwidth = 1;
		gbc.gridx = 0;
		gbc.gridy = 2;
		contentPane.add(widthLabel, gbc);

		widthTxtField = new JTextField();
		widthTxtField.setFont(Fonts.FONT20.get());
		gbc.anchor = GridBagConstraints.FIRST_LINE_START;
		widthTxtField.setPreferredSize(new Dimension(100, 30));
		gbc.gridx = 0;
		gbc.gridy = 3;
		contentPane.add(widthTxtField, gbc);

		JLabel heightLabel = new JLabel("Height:");
		heightLabel.setFont(Fonts.FONT18.get());
		gbc.anchor = GridBagConstraints.LAST_LINE_START;
		gbc.gridx = 0;
		gbc.gridy = 4;
		contentPane.add(heightLabel, gbc);

		heightTxtField = new JTextField();
		heightTxtField.setFont(Fonts.FONT20.get());
		heightTxtField.setPreferredSize(new Dimension(100, 30));
		gbc.anchor = GridBagConstraints.FIRST_LINE_START;
		gbc.gridx = 0;
		gbc.gridy = 5;
		contentPane.add(heightTxtField, gbc);

		createBtn = new FancyButtonOneClick(ProjectColors.BLACK.get(), ProjectColors.RED.get(),
				ProjectColors.RED.get());
		createBtn.setFont(Fonts.FONT20.get());
		createBtn.setBorderPainted(true);
//		createBtn.setPreferredSize(new Dimension(panelWidth/12, ToolPanel.getPanelHeight()/2));
		createBtn.addActionListener(e -> loadInput());
		createBtn.setText("Create");
		createBtn.setBorder(BorderFactory.createLineBorder(ProjectColors.BLACK.get(), 1));
		mainPanel.add(createBtn, BorderLayout.SOUTH);

	}

	private void loadInput() {
		//////////////////////////////////// name check
		String name = nameTxtField.getText();
		if (name.length() > 20) {
			JOptionPane.showMessageDialog(null, "Menu name is too long! \nMaximum name length is 20 characters",
					"Action denied", JOptionPane.WARNING_MESSAGE);
			return;
		}
		if (name.length() == 0) {
			JOptionPane.showMessageDialog(null, "Name field is empty! \nYou have to set some name", "Action denied",
					JOptionPane.WARNING_MESSAGE);
			return;
		}
		for (RestaurantLayout rl2 : layoutEditorFrame.getRestaurantLayoutMap().values()) {
			if (rl2.getName().equals(name)) {
				JOptionPane.showMessageDialog(null, "Restaurant layout already exists! \nchange the name of the layout",
						"Action denied", JOptionPane.WARNING_MESSAGE);
				return;
			}
		}
		try {
			RestaurantLayout rl = restaurantLayoutController.read(name);
			if (rl.getName().equals(name)) {
				JOptionPane.showMessageDialog(null, "The restaurant layout name is taken! \nTry different name",
						"Action denied", JOptionPane.WARNING_MESSAGE);
				return;
			}
		} catch (SQLException e) {
			JOptionPane.showMessageDialog(null, "Error in comparing name from DB! \nTry different name",
					"Action denied", JOptionPane.WARNING_MESSAGE);
			return;
		}
		//////////////////////////////////// Size X check
		int sizeX;
		try {
			sizeX = Integer.valueOf(widthTxtField.getText());
		} catch (NumberFormatException e) {
			JOptionPane.showMessageDialog(null, "Width has to be a number! \nPlease, input number in the width field",
					"Action denied", JOptionPane.WARNING_MESSAGE);
			return;
		}
		if (sizeX > 18) {
			JOptionPane.showMessageDialog(null, "Width is too big! \nMaximum width is 18 ", "Action denied",
					JOptionPane.WARNING_MESSAGE);
			return;
		}
		if (sizeX <= 0 || Integer.valueOf(sizeX) == null) {
			JOptionPane.showMessageDialog(null, "Incorrect width! \n You have to set width field correctly",
					"Action denied", JOptionPane.WARNING_MESSAGE);
			return;
		}
		//////////////////////////////////// Size Y check
		int sizeY;
		try {
			sizeY = Integer.valueOf(heightTxtField.getText());
		} catch (NumberFormatException e) {
			JOptionPane.showMessageDialog(null, "Height has to be a number! \nPlease, input number in the height field",
					"Action denied", JOptionPane.WARNING_MESSAGE);
			return;
		}
		if (sizeY > 18) {
			JOptionPane.showMessageDialog(null, "Height size is too big! \nMaximum height is 18", "Action denied",
					JOptionPane.WARNING_MESSAGE);
			return;
		}
		if (sizeY <= 0 || Integer.valueOf(sizeY) == null) {
			JOptionPane.showMessageDialog(null, "Incorrect height! \n You have to set height field correctly",
					"Action denied", JOptionPane.WARNING_MESSAGE);
			return;
		}
		layoutEditorFrame.prepareNewLayoutInterface(name, sizeX, sizeY);
		this.dispose();
		JOptionPane.showConfirmDialog(null, "Restaurant layout was created", "Creation of restaurant layout",
				JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
	}

}
