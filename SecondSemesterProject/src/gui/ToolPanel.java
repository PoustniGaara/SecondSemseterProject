package gui;

import java.awt.Dimension;
import java.awt.GridBagLayout;

import javax.swing.JPanel;

import gui.tools.PColors;

public class ToolPanel extends JPanel {
	
	private static int toolPanelHeight,toolPanelWidth;
	
	public ToolPanel() {
		
		//Panel dimensions setup
		int mainHeight = (int)MainFrame.height;
		int mainWidth = (int)MainFrame.width;
		int panelHeight = (int) ((int)mainHeight*0.84);
		int toolPanelWidth = mainWidth;
		int toolPanelHeight = (int) (panelHeight*0.12);
		
		setBackground(PColors.get(PColors.GREY));
		setPreferredSize(new Dimension(toolPanelWidth, toolPanelHeight));
		setLayout(new GridBagLayout());
	}
	
	public static int getPanelHeight() {
		return toolPanelHeight;
	}
	
	public static int getPanelWidth() {
		return toolPanelWidth;
	}

}