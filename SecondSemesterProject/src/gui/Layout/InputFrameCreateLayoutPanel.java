package gui.Layout;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import gui.ToolPanel;
import gui.tools.FancyButtonOneClick;
import gui.tools.Fonts;
import gui.tools.ProjectColors;

public class InputFrameCreateLayoutPanel extends JFrame {
	
	private static InputFrameCreateLayoutPanel instance;
	private JTextField nameTxtField, widthTxtField, heightTxtField;
	private FancyButtonOneClick createBtn;

	
	private InputFrameCreateLayoutPanel() {
		
		//frame setup
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setTitle("create restaurant layout");
		setResizable(false);
		setBounds(100, 100, 450, 300);
		setVisible(true);
		
		//main panel setup
		JPanel mainPanel = new JPanel();
		mainPanel.setLayout(new BorderLayout());
		mainPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(mainPanel);
		
		//content pane setup
		JPanel contentPane = new JPanel();
		contentPane.setLayout(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();
		mainPanel.add(contentPane, BorderLayout.NORTH);
		
		JLabel nameLabel = new JLabel("name");
		nameLabel.setFont(Fonts.FONT20.get());
		gbc.anchor = GridBagConstraints.LAST_LINE_START;
		gbc.gridx = 0;
		gbc.gridy = 0;
		contentPane.add(nameLabel,gbc);
		
		nameTxtField = new JTextField(20);
		nameTxtField.setFont(Fonts.FONT20.get());
		gbc.gridwidth = 2;
		gbc.anchor = GridBagConstraints.FIRST_LINE_START;
		gbc.gridx = 0;
		gbc.gridy = 1;
		contentPane.add(nameTxtField,gbc);
		
		JLabel widthLabel = new JLabel("width");
		widthLabel.setFont(Fonts.FONT20.get());
		gbc.anchor = GridBagConstraints.LAST_LINE_START;
		gbc.gridwidth = 1;
		gbc.gridx = 0;
		gbc.gridy = 2;
		contentPane.add(widthLabel,gbc);
		
		widthTxtField = new JTextField(3);
		widthTxtField.setFont(Fonts.FONT20.get());
		gbc.anchor = GridBagConstraints.FIRST_LINE_START;
		gbc.gridx = 0;
		gbc.gridy = 3;
		contentPane.add(widthTxtField,gbc);
		
		JLabel heightLabel = new JLabel("height");
		heightLabel.setFont(Fonts.FONT20.get());
		gbc.anchor = GridBagConstraints.LAST_LINE_START;
		gbc.gridx = 1;
		gbc.gridy = 2;
		contentPane.add(heightLabel,gbc);
		
		heightTxtField = new JTextField(3);
		heightTxtField.setFont(Fonts.FONT20.get());
		gbc.anchor = GridBagConstraints.FIRST_LINE_START;
		gbc.gridx = 1;
		gbc.gridy = 3;
		contentPane.add(heightTxtField,gbc);
		
		createBtn = new FancyButtonOneClick(ProjectColors.BLACK.get(), ProjectColors.RED.get(), ProjectColors.RED.get());
		createBtn.setFont(Fonts.FONT20.get());
		createBtn.setBorderPainted(true);
//		createBtn.setPreferredSize(new Dimension(panelWidth/12, ToolPanel.getPanelHeight()/2));
		createBtn.addActionListener(e -> loadInput());
		createBtn.setText("create");
		createBtn.setBorder(BorderFactory.createLineBorder(ProjectColors.BLACK.get(), 1));
		mainPanel.add(createBtn,BorderLayout.SOUTH);
		
		
	}
	
	private void loadInput() {
		String name = nameTxtField.getText();
		int sizeX = Integer.valueOf(widthTxtField.getText());
		int sizeY = Integer.valueOf(widthTxtField.getText());
		LayoutEditorFrame.getInstance().prepareNewLayoutInterface(name,sizeX, sizeY);
		this.dispose();
	}
	
	public static InputFrameCreateLayoutPanel getInstance() {
		if(instance == null) return new InputFrameCreateLayoutPanel();
		else return instance;
	}

}
