package gui.layout;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Toolkit;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.LineBorder;

import controller.RestaurantLayoutController;
import gui.FooterPanel;
import gui.OverviewPanel;
import gui.ToolPanel;
import gui.tools.FancyButtonOneClick;
import gui.tools.Fonts;
import gui.tools.ProjectColors;
import model.LayoutItem;
import model.RestaurantLayout;

@SuppressWarnings("serial")
public class LayoutEditorFrame extends JFrame {

	private int width, height;
	private JComboBox<String> layoutsCB;
	private RestaurantLayoutController rsController;
	private JButton createBtn, deleteBtn, saveBtn;
	private JPanel mainPanel;
	private JTextField nameTxtField, widthTxtField, heightTxtField;
	private Component currentComponent;
	private RestaurantLayout currentRestaurantLayout;
	private HashMap<String, LayoutEditorPanel> editorPanelMap;
	private HashMap<String, RestaurantLayout> restaurantLayoutMap;
	private ArrayList<RestaurantLayout> rlList;

	public LayoutEditorFrame() {

		// controls
		rsController = new RestaurantLayoutController();

		// frame settings
		setVisible(true);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setExtendedState(getExtendedState() | JFrame.MAXIMIZED_BOTH);
		Toolkit tk = Toolkit.getDefaultToolkit();
		width = tk.getScreenSize().width;
		height = tk.getScreenSize().height;
		setTitle("Layout Editor");

		// main panel
		mainPanel = new JPanel();
		mainPanel.setLayout(new BorderLayout());
		this.add(mainPanel);

		// tool panel
		ToolPanel toolPanel = new ToolPanel();
		toolPanel.setLayout(new GridBagLayout());
		toolPanel.setBackground(ProjectColors.BG.get());
		GridBagConstraints gbcTool = new GridBagConstraints();
		mainPanel.add(toolPanel, BorderLayout.NORTH);

		JLabel personLabel = new JLabel("Layouts:");
		personLabel.setFont(Fonts.FONT18.get());
		gbcTool.insets = new Insets(0, width / 25, 0, 0);
		gbcTool.weightx = 0.25;
		gbcTool.weighty = 0.5;
		gbcTool.anchor = GridBagConstraints.LAST_LINE_START;
		gbcTool.gridx = 0;
		gbcTool.gridy = 0;
		toolPanel.add(personLabel, gbcTool);

		layoutsCB = new JComboBox(new DefaultComboBoxModel());
		layoutsCB.setMaximumRowCount(5);
		layoutsCB.setFont(Fonts.FONT20.get());
		layoutsCB.addActionListener(e -> switchLayout());
		layoutsCB.setPreferredSize(new Dimension(width / 6, ToolPanel.getPanelHeight() / 3));
		gbcTool.anchor = GridBagConstraints.FIRST_LINE_START;
		gbcTool.gridx = 0;
		gbcTool.gridy = 1;
		toolPanel.add(layoutsCB, gbcTool);

		JLabel nameLabel = new JLabel("Name:");
		nameLabel.setFont(Fonts.FONT18.get());
		gbcTool.anchor = GridBagConstraints.LAST_LINE_START;
		gbcTool.gridx = 1;
		gbcTool.gridy = 0;
		toolPanel.add(nameLabel, gbcTool);

		nameTxtField = new JTextField();
		nameTxtField.setFont(Fonts.FONT20.get());
		nameTxtField.setPreferredSize(new Dimension(width / 6, ToolPanel.getPanelHeight() / 3));
		nameTxtField.setEnabled(false);
		gbcTool.anchor = GridBagConstraints.FIRST_LINE_START;
		gbcTool.gridx = 1;
		gbcTool.gridy = 1;
		toolPanel.add(nameTxtField, gbcTool);

		JLabel widthLabel = new JLabel("Width:");
		widthLabel.setFont(Fonts.FONT18.get());
		gbcTool.anchor = GridBagConstraints.LAST_LINE_START;
		gbcTool.gridx = 2;
		gbcTool.gridy = 0;
		toolPanel.add(widthLabel, gbcTool);

		widthTxtField = new JTextField();
		widthTxtField.setEnabled(false);
		widthTxtField.setPreferredSize(new Dimension(width / 19, ToolPanel.getPanelHeight() / 3));
		widthTxtField.setFont(Fonts.FONT20.get());
		gbcTool.anchor = GridBagConstraints.FIRST_LINE_START;
		gbcTool.gridx = 2;
		gbcTool.gridy = 1;
		toolPanel.add(widthTxtField, gbcTool);

		JLabel heightLabel = new JLabel("Height:");
		heightLabel.setFont(Fonts.FONT18.get());
		gbcTool.anchor = GridBagConstraints.LAST_LINE_START;
		gbcTool.gridx = 3;
		gbcTool.gridy = 0;
		toolPanel.add(heightLabel, gbcTool);

		heightTxtField = new JTextField();
		heightTxtField.setEnabled(false);
		heightTxtField.setPreferredSize(new Dimension(width / 19, ToolPanel.getPanelHeight() / 3));
		heightTxtField.setFont(Fonts.FONT20.get());
		gbcTool.anchor = GridBagConstraints.FIRST_LINE_START;
		gbcTool.gridx = 3;
		gbcTool.gridy = 1;
		toolPanel.add(heightTxtField, gbcTool);

		// footer panel
		FooterPanel footerPanel = new FooterPanel();
		mainPanel.add(footerPanel, BorderLayout.SOUTH);
		footerPanel.setLayout(new GridBagLayout());
		GridBagConstraints gbcFooter = new GridBagConstraints();
		gbcFooter.weightx = 0.2;
		gbcFooter.weighty = 0.5;

		createBtn = new JButton();
		createBtn.setFont(Fonts.FONT18.get());
		createBtn.setBackground(ProjectColors.BLUE.get());
		createBtn.setForeground(Color.white);
		createBtn.setFocusable(false);
		createBtn.setPreferredSize(new Dimension(FooterPanel.panelWidth / 6, (int) (FooterPanel.panelHeight * 0.6)));
		createBtn.setText("Add +");
		createBtn.addActionListener(e -> openInputFrame());
//		createBtn.setBorder(borderBlack);
		gbcFooter.gridy = 0;
		gbcFooter.gridx = 0;
		footerPanel.add(createBtn, gbcFooter);

		deleteBtn = new JButton();
		deleteBtn.setFont(Fonts.FONT18.get());
		deleteBtn.setForeground(new Color(195, 70, 70));
		deleteBtn.setBackground(new Color(252, 232, 232));
		deleteBtn.setBorder(new LineBorder(new Color(220, 48, 48), 1));
		deleteBtn.addActionListener(e -> delete());
		deleteBtn.setBorderPainted(true);
		deleteBtn.setPreferredSize(new Dimension(FooterPanel.panelWidth / 6, (int) (FooterPanel.panelHeight * 0.6)));
		deleteBtn.setText("Delete");
//		deleteBtn.setBorder(borderBlack);
		gbcFooter.gridx = 1;
		footerPanel.add(deleteBtn, gbcFooter);

		saveBtn = new JButton();
		saveBtn.setFont(Fonts.FONT18.get());
		saveBtn.setBackground(ProjectColors.BLUE1.get());
		saveBtn.setBorder(new LineBorder(ProjectColors.LIGHT_BLUE.get(), 1));
		saveBtn.setPreferredSize(new Dimension(FooterPanel.panelWidth / 6, (int) (FooterPanel.panelHeight * 0.6)));
		saveBtn.setFocusable(false);
		saveBtn.addActionListener(e -> save());
		saveBtn.setBorderPainted(true);
		saveBtn.setText("Save");
//		saveBtn.setBorder(borderBlack);
		gbcFooter.gridx = 4;
		footerPanel.add(saveBtn, gbcFooter);

		// load
		editorPanelMap = new HashMap<>();
		restaurantLayoutMap = new HashMap<>();
		loadStartData();

	} // end of constructor

	// add are you sure? *
	private void delete() {// if it is layout in database
		if (rlList.contains(currentRestaurantLayout)) {
			try {
				// SYSTEM
				rsController.deleteRestaurantLayout(currentRestaurantLayout.getName());
				rlList.remove(currentRestaurantLayout);
				restaurantLayoutMap.remove(currentRestaurantLayout.getName());
				editorPanelMap.remove(currentRestaurantLayout.getName());
				// GUI
				layoutsCB.removeItem(currentRestaurantLayout.getName());
				displayNextLayout();
				OverviewPanel.getInstance().refreshLayouts();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		} else { // if it is only demo layout
					// SYSTEM
			restaurantLayoutMap.remove(currentRestaurantLayout.getName());
			editorPanelMap.remove(currentRestaurantLayout.getName());
			// GUI
			layoutsCB.removeItem(currentRestaurantLayout.getName());
			displayNextLayout();
		}
		JOptionPane.showConfirmDialog(null, "Restaurant layout was successfuly deleted", "Delete restaurant layout",
				JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
	}

	private void save() {
		// SYSTEM
		Thread dbThread = new Thread(new Runnable() {
			public void run() {
				try {
					//check for DB values
					String rLName = nameTxtField.getText();
					RestaurantLayout rl = rsController.read(rLName);
					if (rLName.equals(rl.getName())) {
						JOptionPane.showMessageDialog(null,
								"Restaurant layout already exists! \nchange the name of the layout", "Action denied",
								JOptionPane.WARNING_MESSAGE);
						return;
					}
					rsController.save(currentRestaurantLayout.getName(), currentRestaurantLayout.getSizeX(),
							currentRestaurantLayout.getSizeY(), currentRestaurantLayout.getItemMap());

					// refresh overview
					OverviewPanel.getInstance().refreshLayouts();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
		dbThread.start();
		JOptionPane.showConfirmDialog(null, "Restaurant layout was successfuly saved", "Save of restaurant layout",
				JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
	}

	private void displayNextLayout() {
		if (restaurantLayoutMap.keySet() != null) {// If there is still some layouts to display
			for (String name : restaurantLayoutMap.keySet()) {
				// SYSTEM
				currentRestaurantLayout = restaurantLayoutMap.get(name);
				// GUI
				currentComponent = editorPanelMap.get(name);
				mainPanel.add(currentComponent, BorderLayout.CENTER); // add editor panel
				mainPanel.repaint();
				mainPanel.revalidate();
				setTextFields(currentRestaurantLayout.getName(), currentRestaurantLayout.getSizeX(),
						currentRestaurantLayout.getSizeY());
				setComboBox(currentRestaurantLayout.getName());
				return;
			}
		} else { // if there is no panel to display
			currentComponent = new NoLayoutInfoPanel();
			mainPanel.add(currentComponent, BorderLayout.CENTER);
			setTextFields("", 0, 0);
			layoutsCB.setSelectedItem(null);
		}
	}

	private void switchLayout() {
		// SYSTEM
		RestaurantLayout rl = restaurantLayoutMap.get(layoutsCB.getSelectedItem().toString());
		currentRestaurantLayout = rl;
		// GUI
		if (currentComponent != null) {
			mainPanel.remove(currentComponent);
			currentComponent = editorPanelMap.get(layoutsCB.getSelectedItem().toString());
			System.out.println(editorPanelMap.get(layoutsCB.getSelectedItem().toString()));
			mainPanel.add(currentComponent, BorderLayout.CENTER); // add editor panel
			mainPanel.repaint();
			mainPanel.revalidate();
			setTextFields(rl.getName(), rl.getSizeX(), rl.getSizeY()); // change header
			setComboBox(rl.getName());
		}
	}

	public void maximize() {
		setState(java.awt.Frame.NORMAL);
	}

	public void prepareNewLayoutInterface(String name, int sizeX, int sizeY) {
		if (currentComponent != null) { // protect from null pointer
			mainPanel.remove(currentComponent);
		}
		// SYSTEM PART
		currentRestaurantLayout = new RestaurantLayout(name, sizeX, sizeY, new HashMap<Point, LayoutItem>());
		restaurantLayoutMap.put(name, currentRestaurantLayout);
		// GUI PART
		LayoutEditorPanel newLayoutEditorPanel = new LayoutEditorPanel();
		newLayoutEditorPanel.loadEmptyMiniPanels(sizeX, sizeY);
		editorPanelMap.put(name, newLayoutEditorPanel);
		setTextFields(name, sizeX, sizeY);
		setComboBox(name);
		currentComponent = newLayoutEditorPanel;
		mainPanel.add(currentComponent, BorderLayout.CENTER);
		mainPanel.repaint();
		mainPanel.revalidate();
	}

	private void openInputFrame() {
		new InputFrameCreateLayoutPanel(this);
	}

	private void loadStartData() {
		try {
			rlList = (ArrayList<RestaurantLayout>) rsController.read();
			if (rlList.size() != 0) {
				// create panels and save them in map
				for (RestaurantLayout rl : rlList) {
					restaurantLayoutMap.put(rl.getName(), rl);
					layoutsCB.addItem(rl.getName()); // populate layoutsCB
					LayoutEditorPanel layoutEditorPanel = new LayoutEditorPanel();
					layoutEditorPanel.loadExistingMiniPanels(rl);
					editorPanelMap.put(rl.getName(), layoutEditorPanel);
				}
				// prepare current panel and setup
				// SYSTEM
				currentRestaurantLayout = restaurantLayoutMap.get(rlList.get(0).getName());
				// GUI
				setTextFields(currentRestaurantLayout.getName(), currentRestaurantLayout.getSizeX(),
						currentRestaurantLayout.getSizeY());
				setComboBox(currentRestaurantLayout.getName());
				currentComponent = editorPanelMap.get(rlList.get(0).getName());
				mainPanel.add(editorPanelMap.get(rlList.get(0).getName()), BorderLayout.CENTER);

			} else { // if there is no layout in the database
				currentComponent = new NoLayoutInfoPanel();
				mainPanel.add(currentComponent, BorderLayout.CENTER);
			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void setComboBox(String name) {
		int count = layoutsCB.getItemCount();
		for (int i = 0; i < count; i++) {
			if (layoutsCB.getItemAt(i).toString().equals(name)) {
				layoutsCB.setSelectedItem(name);
				return;
			}
		}
		layoutsCB.addItem(name);
		layoutsCB.setSelectedItem(name);
	}

	private void setTextFields(String name, int sizeX, int sizeY) {
		nameTxtField.setText(name);
		widthTxtField.setText(String.valueOf(sizeX));
		heightTxtField.setText(String.valueOf(sizeY));
	}

	public void addItemToCurrentLayoutItemMap(Point point, LayoutItem layoutItem) {
		currentRestaurantLayout.getItemMap().put(point, layoutItem);
	}

	public void deleteItemFromCurrentLayoutItemMap(Point point) {
		currentRestaurantLayout.getItemMap().remove(point);
	}
	
	public HashMap<String, RestaurantLayout> getRestaurantLayoutMap(){
		return this.restaurantLayoutMap;
	}

}
