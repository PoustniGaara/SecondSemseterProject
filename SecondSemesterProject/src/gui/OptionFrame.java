package gui;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Toolkit;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.Border;

import gui.tools.FButtonOneC;
import gui.tools.PColors;

public class OptionFrame extends JFrame {
	
	public static OptionFrame instance;
	private FButtonOneC editorBtn;
	
	private OptionFrame () {
		
		//frame settings
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		Toolkit tk = Toolkit.getDefaultToolkit();
		int width = MainFrame.width/4;
		int height = (int) (MainFrame.height*0.8);
		setBounds(MainFrame.width/4*3, (int) ((int)MainFrame.height*0.08), width, height);
		setTitle("Options");
		setVisible(true);
		
		//panel settings
		JPanel mainPanel = new JPanel();
		mainPanel.setPreferredSize(new Dimension(width,height));
		mainPanel.setLayout(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.weightx = 0.5;
		gbc.weighty = 0.5;
		this.add(mainPanel);
		
		//Panel support classes
		Font font1 = new Font("Monaco", Font.BOLD, 20);
		Border borderBlack = BorderFactory.createLineBorder(PColors.get(PColors.BLACK), 2);
		
		//panel components
		JLabel layoutLbl = new JLabel("Layout");
		layoutLbl.setFont(font1);
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.anchor = GridBagConstraints.FIRST_LINE_START;
		mainPanel.add(layoutLbl, gbc);
		
		JPanel layoutPanel = new JPanel();
		layoutPanel.setPreferredSize(new Dimension(width,height/4));
		layoutPanel.setLayout(new GridBagLayout());
		GridBagConstraints gbcLF = new GridBagConstraints();
		layoutPanel.setBorder(borderBlack);
		gbcLF.weightx = 0.5;
		gbcLF.weighty = 0.5;
		mainPanel.add(layoutPanel);
		
		editorBtn = new FButtonOneC(PColors.get(PColors.BLACK), PColors.get(PColors.RED), PColors.get(PColors.RED));
		editorBtn.setFont(font1);
		editorBtn.setBorderPainted(true);
		editorBtn.setPreferredSize(new Dimension(width/10, height/20));
		editorBtn.setText("open editor");
		editorBtn.setBorder(borderBlack);
		gbcLF.gridx = 0;
		gbcLF.gridy = 0;
		layoutPanel.add(editorBtn,gbcLF);
		
		JLabel description1 = new JLabel("-here you can create and customize layouts");
		description1.setFont(font1);
		gbcLF.gridx = 2;
		gbcLF.gridy = 0;
		layoutPanel.add(description1, gbcLF);
		
		
		
	}
	
	
	public static OptionFrame getInstance() {
		if(instance == null) return new OptionFrame();
		else return instance;
	}
	

}
