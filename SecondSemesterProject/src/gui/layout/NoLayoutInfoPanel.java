package gui.layout;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;

import javax.swing.JLabel;
import javax.swing.JPanel;

import gui.MainFrame;
import gui.tools.Fonts;
import gui.tools.ProjectColors;

public class NoLayoutInfoPanel extends JPanel {
	
	public NoLayoutInfoPanel() {
		setVisible(true);
		this.setLayout(new BorderLayout());
		setBackground(ProjectColors.GREY.get());
		setPreferredSize(new Dimension(MainFrame.width, MainFrame.height));
		
		JLabel noLayoutLabel = new JLabel("There is no layout to display");
		noLayoutLabel.setHorizontalAlignment(JLabel.CENTER);
		noLayoutLabel.setVerticalAlignment(JLabel.CENTER);
		noLayoutLabel.setFont(Fonts.FONT20.get());
		noLayoutLabel.setForeground(Color.black);
		
		add(noLayoutLabel, BorderLayout.CENTER);
	}

}
