package gui;

import java.awt.Dimension;

import javax.swing.JLabel;
import javax.swing.JPanel;

import gui.tools.PColors;

public class MenuPanel extends JPanel{
	
	private static MenuPanel instance;
	
	private MenuPanel() {
		
		//Panel dimensions setup
		int mainHeight = (int)MainFrame.height;
		int mainWidth = (int)MainFrame.width;
		int panelHeight = (int) ((int)mainHeight*0.84);
		int panelWidth = mainWidth;
		
		
		setPreferredSize(new Dimension(panelWidth, panelHeight));
		setBackground(PColors.get(PColors.YELLOW));
		setVisible(true);		


	}
	
	public static MenuPanel getInstance() {
		if(instance == null) return new MenuPanel();
		else return instance;
	}

}
