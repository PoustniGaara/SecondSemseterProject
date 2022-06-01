package gui;

import java.awt.Dimension;

import javax.swing.JPanel;

import gui.tools.PColors;

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
		setBackground(PColors.get(PColors.GREEN));
		setVisible(true);
	}

}
