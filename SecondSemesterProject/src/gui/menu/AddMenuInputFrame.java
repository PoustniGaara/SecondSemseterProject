package gui.menu;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import gui.tools.FancyButtonOneClick;
import gui.tools.Fonts;
import gui.tools.ProjectColors;

public class AddMenuInputFrame extends JFrame{
	
	private FancyButtonOneClick createBtn;
	
	public AddMenuInputFrame() {
		
		//frame setup
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setTitle("Create new Menu");
		setResizable(false);
		setBounds(100, 100, 450, 300);
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
		
		
		// Create button
		createBtn = new FancyButtonOneClick(ProjectColors.BLACK.get(), ProjectColors.RED.get(), ProjectColors.RED.get());
		createBtn.setFont(Fonts.FONT20.get());
		createBtn.setBorderPainted(true);
//		createBtn.setPreferredSize(new Dimension(panelWidth/12, ToolPanel.getPanelHeight()/2));
//		createBtn.addActionListener(e -> loadInput());
		createBtn.setText("create");
		createBtn.setBorder(BorderFactory.createLineBorder(ProjectColors.BLACK.get(), 1));
		mainPanel.add(createBtn,BorderLayout.SOUTH);
		
	}

}
