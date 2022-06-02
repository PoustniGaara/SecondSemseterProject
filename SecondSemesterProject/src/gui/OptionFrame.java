package gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Toolkit;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.Border;

import gui.tools.FancyButtonMoreClick;
import gui.tools.FancyButtonOneClick;
import gui.tools.PColors;

public class OptionFrame extends JFrame {
	
	public static OptionFrame instance;
	private FancyButtonOneClick editorBtn;
	private FancyButtonMoreClick layoutBtn;
	
	private OptionFrame () {
		
		//frame settings
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		Toolkit tk = Toolkit.getDefaultToolkit();
		int width = MainFrame.width/4;
		int height = (int) (MainFrame.height*0.8);
		setBounds(MainFrame.width/4*3, (int) ((int)MainFrame.height*0.08), width, height);
		setTitle("Options");
		setVisible(true);
		setResizable(false);
		
		//panel settings
		JPanel mainPanel = new JPanel();
		mainPanel.setPreferredSize(new Dimension(width,height));
		mainPanel.setLayout(new BorderLayout());
		this.add(mainPanel);
		
		//Panel support classes
		Font font1 = new Font("Monaco", Font.BOLD, 20);
		Border borderBlack = BorderFactory.createLineBorder(PColors.BLACK.get(), 2);
		
		//panel components
		JPanel btnPanel = new JPanel();
		btnPanel.setPreferredSize(new Dimension(width, (int) (height*0.05)));
		btnPanel.setLayout(new GridBagLayout());
		GridBagConstraints gbcBtn = new GridBagConstraints();
		btnPanel.setBackground(PColors.GREEN.get());
		mainPanel.add(btnPanel, BorderLayout.NORTH);
		
		layoutBtn = new FancyButtonMoreClick(PColors.RED.get(), PColors.GREEN.get(),
				PColors.RED.get(), PColors.GREEN.get());
		layoutBtn.setText("Layout");
		layoutBtn.setFont(font1);
		layoutBtn.isClicked(true);
		layoutBtn.setPreferredSize(new Dimension((int) (btnPanel.getPreferredSize().getWidth()/4), 
				(int) btnPanel.getPreferredSize().getHeight()));

		gbcBtn.weightx = 0.5;
		gbcBtn.weighty = 0.5;
		gbcBtn.gridx = 0;
		gbcBtn.gridy = 0;
		gbcBtn.anchor = GridBagConstraints.LINE_START;
		btnPanel.add(layoutBtn, gbcBtn);
		
		JPanel layoutPanel = new JPanel();
		layoutPanel.setPreferredSize(new Dimension(width,height/4));
		layoutPanel.setLayout(new GridBagLayout());
		GridBagConstraints gbcLF = new GridBagConstraints();
		layoutPanel.setBorder(borderBlack);
		gbcLF.weightx = 0.4;
		gbcLF.weighty = 0.4;
		mainPanel.add(layoutPanel, BorderLayout.CENTER);
		
		editorBtn = new FancyButtonOneClick(PColors.BLACK.get(), PColors.RED.get(), PColors.RED.get());
		editorBtn.setFont(font1);
		editorBtn.setBorderPainted(true);
		editorBtn.setPreferredSize(new Dimension(width/4, height/20));
		editorBtn.addActionListener(e -> openLayoutEditorFrame());
		editorBtn.setText("open editor");
		editorBtn.setBorder(borderBlack);
		gbcLF.insets = new Insets(20,-20,0,0);
		gbcLF.anchor = GridBagConstraints.PAGE_START;
		gbcLF.gridx = 0;
		gbcLF.gridy = 0;
		layoutPanel.add(editorBtn,gbcLF);
		
		JLabel description1 = new JLabel("<html>-here you can create<br> and customize layouts</html>");
		description1.setFont(font1);
		gbcLF.anchor = GridBagConstraints.FIRST_LINE_START;
		gbcLF.gridx = 2;
		gbcLF.gridy = 0;
		layoutPanel.add(description1, gbcLF);
		
	}
	
	private void openLayoutEditorFrame() {
		new LayoutEditorFrame();
	}
	
	public static OptionFrame getInstance() {
		if(instance == null) return new OptionFrame();
		else return instance;
	}
	

}
