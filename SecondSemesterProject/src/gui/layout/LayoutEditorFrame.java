package gui.layout;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Point;
import java.awt.Toolkit;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import controller.RestaurantLayoutController;
import gui.FooterPanel;
import gui.ToolPanel;
import gui.tools.FancyButtonOneClick;
import gui.tools.Fonts;
import gui.tools.ProjectColors;
import model.LayoutItem;
import model.RestaurantLayout;

public class LayoutEditorFrame extends JFrame {
	
	private int width,height;
	private JComboBox<String> layoutsCB;
	private RestaurantLayoutController rsController;
	private FancyButtonOneClick createBtn, deleteBtn, saveBtn;
	private JPanel mainPanel;
	private JTextField nameTxtField, widthTxtField, heightTxtField;
	private Component currentComponent;
	private RestaurantLayout currentRestaurantLayout;
	private HashMap<String,LayoutEditorPanel> editorPanelMap;
	private HashMap<String, RestaurantLayout> restaurantLayoutMap;
	private ArrayList<RestaurantLayout> rlList;
	
	public LayoutEditorFrame() {
		
		//controls
		rsController = new RestaurantLayoutController();
		
		//frame settings
		setVisible(true);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setExtendedState(getExtendedState() | JFrame.MAXIMIZED_BOTH);
		Toolkit tk = Toolkit.getDefaultToolkit();
		width = tk.getScreenSize().width;
		height = tk.getScreenSize().height;
		setTitle("Layout Editor");
		
		//main panel
		mainPanel = new JPanel();
		mainPanel.setLayout(new BorderLayout());
		this.add(mainPanel);
		
		//tool panel
		ToolPanel toolPanel = new ToolPanel();
		toolPanel.setLayout(new GridBagLayout());
		GridBagConstraints gbcTool = new GridBagConstraints();
		mainPanel.add(toolPanel, BorderLayout.NORTH);
		
		JLabel personLabel = new JLabel("layouts");
		personLabel.setFont(Fonts.FONT20.get());
		gbcTool.weightx = 0.5;
		gbcTool.weighty = 0.5;
		gbcTool.anchor = GridBagConstraints.LAST_LINE_START;
		gbcTool.gridx = 0;
		gbcTool.gridy = 0;
		toolPanel.add(personLabel,gbcTool);
		
		layoutsCB = new JComboBox(new DefaultComboBoxModel());
		layoutsCB.setMaximumRowCount(5);
		layoutsCB.setFont(Fonts.FONT20.get());
		layoutsCB.addActionListener(e -> switchLayout());
		layoutsCB.setPreferredSize(new Dimension(width/8, ToolPanel.getPanelHeight()/3));
		gbcTool.anchor = GridBagConstraints.FIRST_LINE_START;
		gbcTool.gridx = 0;
		gbcTool.gridy = 1;
		toolPanel.add(layoutsCB,gbcTool);
		
		JLabel nameLabel = new JLabel("name");
		nameLabel.setFont(Fonts.FONT20.get());
		gbcTool.anchor = GridBagConstraints.LAST_LINE_START;
		gbcTool.gridx = 1;
		gbcTool.gridy = 0;
		toolPanel.add(nameLabel,gbcTool);
		
		nameTxtField = new JTextField(20);
		nameTxtField.setFont(Fonts.FONT20.get());
		nameTxtField.setEnabled(false);
		gbcTool.anchor = GridBagConstraints.FIRST_LINE_START;
		gbcTool.gridx = 1;
		gbcTool.gridy = 1;
		toolPanel.add(nameTxtField,gbcTool);
		
		JLabel widthLabel = new JLabel("width");
		widthLabel.setFont(Fonts.FONT20.get());
		gbcTool.anchor = GridBagConstraints.LAST_LINE_START;
		gbcTool.gridx = 2;
		gbcTool.gridy = 0;
		toolPanel.add(widthLabel,gbcTool);
		
		widthTxtField = new JTextField(3);
		widthTxtField.setEnabled(false);
		widthTxtField.setFont(Fonts.FONT20.get());
		gbcTool.anchor = GridBagConstraints.FIRST_LINE_START;
		gbcTool.gridx = 2;
		gbcTool.gridy = 1;
		toolPanel.add(widthTxtField,gbcTool);
		
		JLabel heightLabel = new JLabel("height");
		heightLabel.setFont(Fonts.FONT20.get());
		gbcTool.anchor = GridBagConstraints.LAST_LINE_START;
		gbcTool.gridx = 3;
		gbcTool.gridy = 0;
		toolPanel.add(heightLabel,gbcTool);
		
		heightTxtField = new JTextField(3);
		heightTxtField.setEnabled(false);
		heightTxtField.setFont(Fonts.FONT20.get());
		gbcTool.anchor = GridBagConstraints.FIRST_LINE_START;
		gbcTool.gridx = 3;
		gbcTool.gridy = 1;
		toolPanel.add(heightTxtField,gbcTool);
		
		//footer panel
		FooterPanel footerPanel = new FooterPanel();
		mainPanel.add(footerPanel, BorderLayout.SOUTH);
		footerPanel.setLayout(new GridBagLayout());
		GridBagConstraints gbcFooter = new GridBagConstraints();
		gbcFooter.weightx = 0.2;
		gbcFooter.weighty = 0.5;
		
		createBtn = new FancyButtonOneClick(ProjectColors.BLACK.get(), ProjectColors.RED.get(), ProjectColors.RED.get());
		createBtn.setFont(Fonts.FONT20.get());
		createBtn.setBorderPainted(true);
		createBtn.setPreferredSize(new Dimension(width/8, ToolPanel.getPanelHeight()/2));
		createBtn.setText("create");
		createBtn.addActionListener(e -> openInputFrame());
//		createBtn.setBorder(borderBlack);
		gbcFooter.gridy = 0;
		gbcFooter.gridx = 0;
		footerPanel.add(createBtn, gbcFooter);
		
		deleteBtn = new FancyButtonOneClick(ProjectColors.BLACK.get(), ProjectColors.RED.get(), ProjectColors.RED.get());
		deleteBtn.setFont(Fonts.FONT20.get());
		deleteBtn.addActionListener(e -> delete());
		deleteBtn.setBorderPainted(true);
		deleteBtn.setPreferredSize(new Dimension(width/8, ToolPanel.getPanelHeight()/2));
		deleteBtn.setText("delete");
//		deleteBtn.setBorder(borderBlack);
		gbcFooter.gridx = 1;
		footerPanel.add(deleteBtn, gbcFooter);
		
		saveBtn = new FancyButtonOneClick(ProjectColors.BLACK.get(), ProjectColors.RED.get(), ProjectColors.RED.get());
		saveBtn.setFont(Fonts.FONT20.get());
		saveBtn.addActionListener(e -> save());
		saveBtn.setBorderPainted(true);
		saveBtn.setPreferredSize(new Dimension(width/8, ToolPanel.getPanelHeight()/2));
		saveBtn.setText("save");
//		saveBtn.setBorder(borderBlack);
		gbcFooter.gridx = 4;
		footerPanel.add(saveBtn, gbcFooter);
		
		//load 
		editorPanelMap = new HashMap<>();
		restaurantLayoutMap = new HashMap<>();
		loadStartData();
		
	} // end of constructor
	
	//add are you sure? * 
	private void delete() {// if it is layout in database
		if(rlList.contains(currentRestaurantLayout)) {
			try {
				//SYSTEM
				rsController.deleteRestaurantLayout(currentRestaurantLayout.getName());
				rlList.remove(currentRestaurantLayout);
				restaurantLayoutMap.remove(currentRestaurantLayout.getName());
				editorPanelMap.remove(currentRestaurantLayout.getName());
				//GUI
				layoutsCB.removeItem(currentRestaurantLayout.getName());
				displayNextLayout();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		else { // if it is only demo layout
			//SYSTEM
			restaurantLayoutMap.remove(currentRestaurantLayout.getName());
			editorPanelMap.remove(currentRestaurantLayout.getName());
			//GUI
			layoutsCB.removeItem(currentRestaurantLayout.getName());
			displayNextLayout();
		}
	}
	
	private void save() {
		try {
			//SYSTEM
			rsController.save(currentRestaurantLayout.getName(),
					currentRestaurantLayout.getSizeX(),currentRestaurantLayout.getSizeY()
					, currentRestaurantLayout.getItemMap());
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void displayNextLayout() {
		if(restaurantLayoutMap.keySet() != null) {// If there is still some layouts to display
			for(String name : restaurantLayoutMap.keySet()) {
				//SYSTEM
				currentRestaurantLayout = restaurantLayoutMap.get(name);
				//GUI
				currentComponent = editorPanelMap.get(name);
				mainPanel.add(currentComponent, BorderLayout.CENTER); //add editor panel
				mainPanel.repaint();
				mainPanel.revalidate();
				setTextFields(currentRestaurantLayout.getName(), 
						currentRestaurantLayout.getSizeX(), currentRestaurantLayout.getSizeY()); 
				setComboBox(currentRestaurantLayout.getName());
				return;
			}
		}
		else { // if there is no panel to display
			currentComponent = new NoLayoutInfoPanel();
			mainPanel.add(currentComponent , BorderLayout.CENTER);
			setTextFields("",0,0);
			layoutsCB.setSelectedItem(null);
		}
	}
	
	private void switchLayout() {
		//SYSTEM
		RestaurantLayout rl = restaurantLayoutMap.get(layoutsCB.getSelectedItem().toString());
		currentRestaurantLayout = rl;
		//GUI
		if(currentComponent != null) {
		mainPanel.remove(currentComponent);
		currentComponent = editorPanelMap.get(layoutsCB.getSelectedItem().toString());
		mainPanel.add(currentComponent, BorderLayout.CENTER); //add editor panel
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
		if(currentComponent != null) { // protect from null pointer
		mainPanel.remove(currentComponent); 
		}
		//SYSTEM PART
		currentRestaurantLayout = new RestaurantLayout(name, sizeX, sizeY, new HashMap<Point,LayoutItem>());
		restaurantLayoutMap.put(name, currentRestaurantLayout);
		//GUI PART
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
			if(rlList.size() != 0) {
				// create panels and save them in map 
				for(RestaurantLayout rl : rlList) { 
					restaurantLayoutMap.put(rl.getName(), rl);
					layoutsCB.addItem(rl.getName()); // populate layoutsCB
					LayoutEditorPanel layoutEditorPanel = new LayoutEditorPanel();
					layoutEditorPanel.loadExistingMiniPanels(rl);
					editorPanelMap.put(rl.getName(), layoutEditorPanel);
				} 
				// prepare current panel and setup
				//SYSTEM
				currentRestaurantLayout = restaurantLayoutMap.get(rlList.get(0).getName());
				//GUI
				setTextFields(currentRestaurantLayout.getName(), currentRestaurantLayout.getSizeX(), 
						currentRestaurantLayout.getSizeY());
				setComboBox(currentRestaurantLayout.getName());
				currentComponent = editorPanelMap.get(rlList.get(0).getName());
				mainPanel.add(editorPanelMap.get(rlList.get(0).getName()), BorderLayout.CENTER);

			}
			else { // if there is no layout in the database
				currentComponent = new NoLayoutInfoPanel();
				mainPanel.add(currentComponent , BorderLayout.CENTER);
			}
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void setComboBox(String name) {
		int count = layoutsCB.getItemCount();
		for(int i = 0; i < count; i++) {
			if(layoutsCB.getItemAt(i).toString().equals(name)) {
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

}
