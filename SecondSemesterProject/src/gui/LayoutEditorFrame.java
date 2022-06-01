package gui;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Toolkit;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class LayoutEditorFrame extends JFrame {
	
	private int width,height;
	
	public LayoutEditorFrame() {
		
		setVisible(true);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setExtendedState(this.getExtendedState() | JFrame.MAXIMIZED_BOTH);
		Toolkit tk = Toolkit.getDefaultToolkit();
		width = tk.getScreenSize().width;
		height = tk.getScreenSize().height;
		setBounds(0, 0, width, height);
		setTitle("Layout Editor");
		
		//main panel
		JPanel mainPanel = new JPanel();
		mainPanel.setLayout(new BorderLayout());
		
		
		//footer panel
		FooterPanel footerPanel = new FooterPanel();
		mainPanel.add(footerPanel, BorderLayout.SOUTH);
		footerPanel.setLayout(new GridBagLayout());
		GridBagConstraints gbcFooter = new GridBagConstraints();
		gbcFooter.weightx = 0.2;
		gbcFooter.weighty = 0.5;
	}

}
