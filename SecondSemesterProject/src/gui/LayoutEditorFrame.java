package gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Toolkit;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class LayoutEditorFrame extends JFrame {
	
	private int width,height;
	
	public LayoutEditorFrame() {
		
		setVisible(true);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setExtendedState(this.getExtendedState() | JFrame.MAXIMIZED_BOTH);
		Toolkit tk = Toolkit.getDefaultToolkit();
		width = tk.getScreenSize().width;
		height = tk.getScreenSize().height;
		setBounds(0, 0, width, height);
		setTitle("Layout Editor");
		
		//main panel
		JPanel mainPanel = new JPanel();
		mainPanel.setLayout(new BorderLayout());
		this.add(mainPanel);
		
		//tool panel
		ToolPanel toolPanel = new ToolPanel();
		toolPanel.setLayout(new GridBagLayout());
		GridBagConstraints gbcTool = new GridBagConstraints();
		mainPanel.add(toolPanel, BorderLayout.NORTH);
		
		JLabel personLabel = new JLabel("no. of persons");
		personLabel.setFont(font1);
		gbcTool.anchor = GridBagConstraints.LAST_LINE_START;
		gbcTool.gridx = 0;
		gbcTool.gridy = 0;
		toolPanel.add(personLabel,gbcTool);
		
		personCB = new JComboBox(new DefaultComboBoxModel());
		populatePersonCB();
		personCB.setMaximumRowCount(5);
		personCB.setFont(font1);
		personCB.setPreferredSize(new Dimension(panelWidth/8, ToolPanel.getPanelHeight()/3));
		gbcTool.anchor = GridBagConstraints.FIRST_LINE_START;
		gbcTool.gridx = 0;
		gbcTool.gridy = 1;
		toolPanel.add(personCB,gbcTool);
		
		//footer panel
		FooterPanel footerPanel = new FooterPanel();
		mainPanel.add(footerPanel, BorderLayout.SOUTH);
		footerPanel.setLayout(new GridBagLayout());
		GridBagConstraints gbcFooter = new GridBagConstraints();
		gbcFooter.weightx = 0.2;
		gbcFooter.weighty = 0.5;
	}

}
