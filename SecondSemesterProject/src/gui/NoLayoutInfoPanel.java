package gui;

import java.awt.BorderLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;

import gui.tools.Fonts;

public class NoLayoutInfoPanel extends JPanel {
	
	public NoLayoutInfoPanel() {
		super();
		this.setLayout(new BorderLayout());
		
		JLabel noLayoutLabel = new JLabel("There is no layout to display.");
		noLayoutLabel.setFont(Fonts.FONT20.get());
		
		add(noLayoutLabel, BorderLayout.CENTER);
	}

}
