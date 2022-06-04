package gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Toolkit;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import controller.RestaurantLayoutController;
import gui.tools.FancyButtonOneClick;
import gui.tools.Fonts;
import gui.tools.ProjectColors;
import model.RestaurantLayout;

public class LayoutEditorFrame extends JFrame {
	
	private int width,height;
	private JComboBox layoutsCB;
	private RestaurantLayoutController rsController;
	private FancyButtonOneClick createBtn, deleteBtn, saveBtn;
	private JPanel mainPanel;
	
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
		
		loadStartData();
		
		//tool panel
		ToolPanel toolPanel = new ToolPanel();
		toolPanel.setLayout(new GridBagLayout());
		GridBagConstraints gbcTool = new GridBagConstraints();
		mainPanel.add(toolPanel, BorderLayout.NORTH);
		
		JLabel personLabel = new JLabel("layouts");
		personLabel.setFont(Fonts.FONT20.get());
		gbcTool.anchor = GridBagConstraints.LAST_LINE_START;
		gbcTool.gridx = 0;
		gbcTool.gridy = 0;
		toolPanel.add(personLabel,gbcTool);
		
		layoutsCB = new JComboBox(new DefaultComboBoxModel());
		layoutsCB.setMaximumRowCount(5);
		layoutsCB.setFont(Fonts.FONT20.get());
		layoutsCB.setPreferredSize(new Dimension(width/8, ToolPanel.getPanelHeight()/3));
		gbcTool.anchor = GridBagConstraints.FIRST_LINE_START;
		gbcTool.gridx = 0;
		gbcTool.gridy = 1;
		toolPanel.add(layoutsCB,gbcTool);
		
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
//		createBtn.setBorder(borderBlack);
		gbcFooter.gridy = 0;
		gbcFooter.gridx = 0;
		footerPanel.add(createBtn, gbcFooter);
		
		deleteBtn = new FancyButtonOneClick(ProjectColors.BLACK.get(), ProjectColors.RED.get(), ProjectColors.RED.get());
		deleteBtn.setFont(Fonts.FONT20.get());
		deleteBtn.setBorderPainted(true);
		deleteBtn.setPreferredSize(new Dimension(width/8, ToolPanel.getPanelHeight()/2));
		deleteBtn.setText("delete");
//		deleteBtn.setBorder(borderBlack);
		gbcFooter.gridx = 1;
		footerPanel.add(deleteBtn, gbcFooter);
		
		saveBtn = new FancyButtonOneClick(ProjectColors.BLACK.get(), ProjectColors.RED.get(), ProjectColors.RED.get());
		saveBtn.setFont(Fonts.FONT20.get());
		saveBtn.setBorderPainted(true);
		saveBtn.setPreferredSize(new Dimension(width/8, ToolPanel.getPanelHeight()/2));
		saveBtn.setText("save");
//		saveBtn.setBorder(borderBlack);
		gbcFooter.gridx = 4;
		footerPanel.add(saveBtn, gbcFooter);
	}
	
	private void loadStartData() {
		try {
			ArrayList<RestaurantLayout> rlList = (ArrayList<RestaurantLayout>) rsController.read();
			if(rlList.size() == 0) {
				mainPanel.add(new NoLayoutInfoPanel(), BorderLayout.CENTER);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void populateLayoutCB() {
		try {
			ArrayList<RestaurantLayout> rlList = (ArrayList<RestaurantLayout>) rsController.read();
			for(RestaurantLayout rl : rlList) {
				layoutsCB.addItem(rl.getName());
			}
		} catch (SQLException e) {
			//IMPLEMENT
			e.printStackTrace();
		}
	}

}
