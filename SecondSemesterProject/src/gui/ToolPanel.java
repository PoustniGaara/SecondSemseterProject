package gui;

import java.awt.Dimension;
import java.awt.GridBagLayout;

import javax.swing.BorderFactory;
import javax.swing.JPanel;

import gui.tools.ProjectColors;

public class ToolPanel extends JPanel {

	private static int toolPanelHeight, toolPanelWidth;

	public ToolPanel() {

		// Panel dimensions setup
		int mainHeight = (int) MainFrame.height;
		int mainWidth = (int) MainFrame.width;
		int panelHeight = (int) ((int) mainHeight * 0.84);
		toolPanelWidth = mainWidth;
		toolPanelHeight = (int) (panelHeight * 0.12);

		setBackground(ProjectColors.WHITE.get());
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