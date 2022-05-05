package gui;

import java.awt.Dimension;

import javax.swing.JPanel;

import gui.tools.PColors;

public class ReservationsPanel extends JPanel {
	
	private static ReservationsPanel instance;
	
	private ReservationsPanel() {
		
		//Panel dimensions setup
		int mainHeight = (int)MainFrame.height;
		int mainWidth = (int)MainFrame.width;
		int panelHeight = (int) ((int)mainHeight*0.84);
		int panelWidth = mainWidth;
		
		
		setPreferredSize(new Dimension(panelWidth, panelHeight));
		setBackground(PColors.get(PColors.BLACK));
		setVisible(true);		
	}
	
	public static ReservationsPanel getInstance() {
		if(instance == null) return new ReservationsPanel();
		else return instance;
	}
}
