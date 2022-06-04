package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import gui.tools.FancyButtonOneClick;
import gui.tools.ProjectColors;

public class LoginPanel extends JPanel {
	
	private JPanel inputPanel;
	private FancyButtonOneClick btn;
	private JTextField inputField;
	private String password = "";
	private static LoginPanel instance;
	private MainFrame frame;
	
	private LoginPanel() {
		
		int width = (int) MainFrame.width;
		int height = (int) MainFrame.height;
		setVisible(true);
		setPreferredSize(new Dimension(width, height));
		setLayout(new GridBagLayout());
		setBackground(ProjectColors.GREY.get());
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.weightx = 1;
		gbc.weighty = 0.4;
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.anchor = GridBagConstraints.CENTER;
		gbc.anchor = GridBagConstraints.PAGE_END;
		
		JLabel lbl = new JLabel("Café Peace");
		lbl.setFont(new Font("Monaco", Font.PLAIN, 100));
		lbl.setForeground(ProjectColors.BLACK.get());
		this.add(lbl, gbc);
		
		inputPanel = new JPanel();
		inputPanel.setVisible(true);
		inputPanel.setPreferredSize(new Dimension(width/2,height/2));
		inputPanel.setBackground(ProjectColors.GREEN.get());
		gbc.anchor = GridBagConstraints.PAGE_START;
		gbc.weighty = 0.7;
		gbc.gridx = 0;
		gbc.gridy = 1;
		this.add(inputPanel, gbc);
		inputPanel.setLayout(new GridBagLayout());
		GridBagConstraints gbcIP = new GridBagConstraints();
		gbcIP.weightx = 1;
		gbcIP.weighty = 1;
		gbcIP.gridx = 0;
		gbcIP.gridy = 0;
		
		JLabel passswordLabel = new JLabel("Password:");
		passswordLabel.setFont(new Font("Monaco", Font.PLAIN, 25));
		passswordLabel.setForeground(ProjectColors.BLACK.get());
		gbcIP.anchor = GridBagConstraints.LINE_END;
		gbcIP.weightx = 0.1;
		inputPanel.add(passswordLabel, gbcIP);
		
		
		inputField = new JTextField();
		inputField.setPreferredSize(new Dimension((int)inputPanel.getPreferredSize().getWidth() /100 * 50
				,(int) inputPanel.getPreferredSize().getHeight() / 100 * 8));
		inputField.setFont(new Font("Monaco", Font.PLAIN, 25));
		gbcIP.anchor = GridBagConstraints.LINE_START;
		gbcIP.gridx = 1;
		gbcIP.weightx = 0.9;
		gbcIP.insets = new Insets(0,20,0,0);
		gbcIP.gridy = 0;
		inputPanel.add(inputField, gbcIP);
		
		btn = new FancyButtonOneClick(ProjectColors.BLACK.get(), ProjectColors.GREEN.get(), ProjectColors.RED.get());
		btn.setText("Login");
		btn.setFont(new Font("Monaco", Font.PLAIN, 25));
		btn.setPreferredSize(new Dimension((int)inputPanel.getPreferredSize().getWidth() /100 * 98, 
				(int) inputPanel.getPreferredSize().getHeight() / 100 * 15));
		btn.addActionListener(e -> login());
		gbcIP.insets = new Insets(0,0,0,0);
		gbcIP.anchor = GridBagConstraints.CENTER;
		gbcIP.gridwidth = 2;
		gbcIP.weightx = 2;
		gbcIP.weighty = 0.1;
		gbcIP.gridx = 0;
		gbcIP.gridy = 1;
		inputPanel.add(btn, gbcIP);
		
	}
	
	public void login() {
		frame = MainFrame.getInstance();
		String pass = inputField.getText();
		if(pass.equals(password))
			frame.setupScreen();
		else
			System.out.print("Wrong login not implemented");
	}
	
	public static LoginPanel getInstance() {
		if(instance == null)
			return new LoginPanel();
		else
			return instance;
	}

}
