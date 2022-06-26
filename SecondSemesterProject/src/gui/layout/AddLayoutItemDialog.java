package gui.layout;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Point;
import java.sql.SQLException;

import javax.swing.BorderFactory;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import org.kordamp.ikonli.coreui.CoreUiFree;
import org.kordamp.ikonli.icomoon.Icomoon;
import org.kordamp.ikonli.swing.FontIcon;

import controller.LayoutItemController;
import gui.HeaderPanel;
import gui.tools.FancyButtonOneClick;
import gui.tools.Fonts;
import gui.tools.ProjectColors;
import model.LayoutItem;
import model.RestaurantLayout;
import model.Table;

public class AddLayoutItemDialog extends JFrame {

	private JTextField nameTxtField;
	private FancyButtonOneClick okBtn;
	private JComboBox<String> typeComboBox;
	private JComboBox<Integer> capacityComboBox;
	private LayoutEditorMiniPanel parentMiniPanel;
	private FontIcon tableIcon, barIcon, entranceIcon, plusIcon;
	private GridBagConstraints gbc;
	private JLabel iconLabel;
	private LayoutItemController layoutItemController;

	public AddLayoutItemDialog(LayoutEditorMiniPanel parentMiniPanel) {

		// frame setup
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setTitle("Add layout item");
		setResizable(false);
		setBounds(100, 100, 450, 400);
		setVisible(true);
		
		//controller setup
		layoutItemController = new LayoutItemController();

		// mini panel setup
		this.parentMiniPanel = parentMiniPanel;

		// main panel setup
		JPanel mainPanel = new JPanel();
		mainPanel.setLayout(new BorderLayout());
		mainPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		add(mainPanel);

		// icons setup
		tableIcon = FontIcon.of(Icomoon.ICM_SPOON_KNIFE);
		tableIcon.setIconSize(30);
		barIcon = FontIcon.of(Icomoon.ICM_GLASS2);
		barIcon.setIconSize(30);
		entranceIcon = FontIcon.of(Icomoon.ICM_ENTER);
		entranceIcon.setIconSize(30);
		plusIcon = FontIcon.of(CoreUiFree.POOL);
		plusIcon.setIconSize(30);

		// content pane setup
		JPanel contentPane = new JPanel();
		contentPane.setLayout(new GridBagLayout());
		gbc = new GridBagConstraints();
		mainPanel.add(contentPane, BorderLayout.CENTER);

		JLabel nameLabel = new JLabel("Name:");
		nameLabel.setFont(Fonts.FONT18.get());
		gbc.anchor = GridBagConstraints.LAST_LINE_START;
		gbc.weightx = 1;
		gbc.weighty = 1;
		gbc.gridx = 0;
		gbc.gridy = 0;
		contentPane.add(nameLabel, gbc);

		nameTxtField = new JTextField();
		nameTxtField.setFont(Fonts.FONT18.get());
		nameTxtField.setPreferredSize(new Dimension(300, 30));
		gbc.gridwidth = 2;
		gbc.anchor = GridBagConstraints.FIRST_LINE_START;
		gbc.gridx = 0;
		gbc.gridy = 1;
		contentPane.add(nameTxtField, gbc);

		iconLabel = new JLabel();
		gbc.anchor = GridBagConstraints.FIRST_LINE_START;
		gbc.gridx = 1;
		gbc.gridy = 3;
		contentPane.add(iconLabel, gbc);

		JLabel typeLabel = new JLabel("Type:");
		typeLabel.setFont(Fonts.FONT18.get());
		gbc.anchor = GridBagConstraints.LAST_LINE_START;
		gbc.gridx = 0;
		gbc.gridy = 2;
		contentPane.add(typeLabel, gbc);

		typeComboBox = new JComboBox<String>(new DefaultComboBoxModel<String>());
		populateTypeComboBox();
		typeComboBox.setMaximumRowCount(3);
		typeComboBox.setFont(Fonts.FONT20.get());
		typeComboBox.addActionListener(e -> checkTypeInput());
		typeComboBox.setPreferredSize(new Dimension(300, 30));
		gbc.anchor = GridBagConstraints.FIRST_LINE_START;
		gbc.gridx = 0;
		gbc.gridy = 3;
		contentPane.add(typeComboBox, gbc);

		setIcon();

		JLabel capacityLabel = new JLabel("Capacity:");
		capacityLabel.setFont(Fonts.FONT18.get());
		gbc.anchor = GridBagConstraints.LAST_LINE_START;
		gbc.gridx = 0;
		gbc.gridy = 4;
		contentPane.add(capacityLabel, gbc);

		capacityComboBox = new JComboBox(new DefaultComboBoxModel());
		populatecapacityLabel();
		capacityComboBox.setMaximumRowCount(4);
		capacityComboBox.setFont(Fonts.FONT20.get());
		capacityComboBox.setPreferredSize(new Dimension(300, 30));
		gbc.anchor = GridBagConstraints.FIRST_LINE_START;
		gbc.gridx = 0;
		gbc.gridy = 5;
		contentPane.add(capacityComboBox, gbc);

		okBtn = new FancyButtonOneClick(ProjectColors.BLACK.get(), ProjectColors.RED.get(), ProjectColors.RED.get());
		okBtn.setFont(Fonts.FONT20.get());
		okBtn.setBorderPainted(true);
		okBtn.addActionListener(e -> enterLayoutItem());
		okBtn.setText("Save");
		okBtn.setBorder(BorderFactory.createLineBorder(ProjectColors.BLACK.get(), 1));
		gbc.anchor = GridBagConstraints.FIRST_LINE_START;
		gbc.gridwidth = 2;
		gbc.gridx = 0;
		gbc.gridy = 6;
		mainPanel.add(okBtn, BorderLayout.SOUTH);

	}

	private void setIcon() {
		switch (typeComboBox.getSelectedItem().toString()) {
		case "Table":
			iconLabel.setIcon(tableIcon);
			break;
		case "Bar":
			iconLabel.setIcon(barIcon);
			break;
		case "Entrance":
			iconLabel.setIcon(entranceIcon);
			break;
		case "None":
			iconLabel.setIcon(plusIcon);
		}
	}

	private void enterLayoutItem() {
		addLayoutItemToGUI();
		putLayoutItemToItemMap();
	}

	private void putLayoutItemToItemMap() {
		LayoutItem layoutItem;
		Point point = new Point(parentMiniPanel.getLocationX(), parentMiniPanel.getLocationY());
		switch (typeComboBox.getSelectedItem().toString()) {
		case "Table":
			layoutItem = new Table(nameTxtField.getText(), "table",
					Integer.valueOf(capacityComboBox.getSelectedItem().toString()));
			HeaderPanel.getInstance().getLayoutEditorFrame().addItemToCurrentLayoutItemMap(point, layoutItem);
			break;
		case "Bar":
			layoutItem = new LayoutItem(nameTxtField.getText(), "bar");
			HeaderPanel.getInstance().getLayoutEditorFrame().addItemToCurrentLayoutItemMap(point, layoutItem);
			break;
		case "Entrance":
			layoutItem = new LayoutItem(nameTxtField.getText(), "entrance");
			HeaderPanel.getInstance().getLayoutEditorFrame().addItemToCurrentLayoutItemMap(point, layoutItem);
			break;
		default:
			HeaderPanel.getInstance().getLayoutEditorFrame().deleteItemFromCurrentLayoutItemMap(point);
		}
	}

	private void addLayoutItemToGUI() {
		if (typeComboBox.getSelectedItem().toString().equals("None")) {
			parentMiniPanel.setName("");
			parentMiniPanel.setIcon((FontIcon) iconLabel.getIcon());
		}
		////////////////////// CHECK Name
		
		String name = nameTxtField.getText();
		if (name.length() > 20) {
			JOptionPane.showMessageDialog(null, "Menu name is too long! \nMaximum name length is 20 characters",
					"Action denied", JOptionPane.WARNING_MESSAGE);
			return;
		}
		if (name.length() == 0) {
			JOptionPane.showMessageDialog(null,
					"Name field is empty! \nYou have to set some name", "Action denied",
					JOptionPane.WARNING_MESSAGE);
			return;
		}
			try {
				LayoutItem lI = layoutItemController.read(name);
				if (lI.getName().equals(name)) {
					JOptionPane.showMessageDialog(null, "The layout item name is taken! \nTry different name",
							"Action denied", JOptionPane.WARNING_MESSAGE);
					return;
				}
			} catch (SQLException e) {
				JOptionPane.showMessageDialog(null, "Error in comparing name from DB! \nTry different name",
						"Action denied", JOptionPane.WARNING_MESSAGE);
				return;
			}
		parentMiniPanel.setNameLabel(name);
		
		parentMiniPanel.setIcon((FontIcon) iconLabel.getIcon());
		if (capacityComboBox.isEnabled())
			parentMiniPanel.setCapacityLabel(capacityComboBox.getSelectedItem().toString());
		else
			parentMiniPanel.setCapacityLabel("");
		dispose();
	}

	private void checkTypeInput() {
		setIcon();
		if (!typeComboBox.getSelectedItem().equals("Table")) // if there is not table
			capacityComboBox.setEnabled(false);
		else
			capacityComboBox.setEnabled(true);
	}

	private void populatecapacityLabel() {
		for (int i = 1; i < 9; i++) {
			capacityComboBox.addItem(i);
		}
	}

	private void populateTypeComboBox() {
		typeComboBox.addItem("Table");
		typeComboBox.addItem("Bar");
		typeComboBox.addItem("Entrance");
		typeComboBox.addItem("None");

	}

}
