package gui;

import java.awt.Color;
import java.awt.Dimension;

import javax.swing.JPanel;

import gui.tools.ProjectColors;

public class FooterPanel extends JPanel {
	
	public static int panelHeight;
	public static int panelWidth;
	
	public FooterPanel() {
		
		//Panel dimensions setup
		int mainHeight = (int)MainFrame.height;
		int mainWidth = (int)MainFrame.width;
		panelHeight = (int) ((int)mainHeight*0.07);
		panelWidth = mainWidth;
		
		
		setPreferredSize(new Dimension(panelWidth, panelHeight));
		setBackground(Color.WHITE);
		setVisible(true);
	}

}
