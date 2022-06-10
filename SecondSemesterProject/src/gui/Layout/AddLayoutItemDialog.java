package gui.Layout;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.BorderFactory;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import gui.tools.FancyButtonOneClick;
import gui.tools.Fonts;
import gui.tools.ProjectColors;
import model.LayoutItem;

public class AddLayoutItemDialog extends JFrame  {
	
	private JTextField nameTxtField;
	private FancyButtonOneClick okBtn, cancelBtn;
	private JComboBox<String> typeComboBox;
	private JComboBox<Integer> capacityComboBox;
	
	public AddLayoutItemDialog(JPanel parent){
		
			//frame setup
			setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
			setTitle("Add layout item");
			setResizable(false);
			setBounds(100, 100, 450, 300);
			setVisible(true);
			
			//main panel setup
			JPanel mainPanel = new JPanel();
			mainPanel.setLayout(new BorderLayout());
			mainPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
			add(mainPanel);
			
			//content pane setup
			JPanel contentPane = new JPanel();
			contentPane.setLayout(new GridBagLayout());
			GridBagConstraints gbc = new GridBagConstraints();
			mainPanel.add(contentPane, BorderLayout.CENTER);
			
			JLabel nameLabel = new JLabel("name");
			nameLabel.setFont(Fonts.FONT20.get());
			gbc.anchor = GridBagConstraints.LAST_LINE_START;
			gbc.weightx = 1;
			gbc.weighty = 1;
			gbc.gridx = 0;
			gbc.gridy = 0;
			contentPane.add(nameLabel,gbc);
			
			nameTxtField = new JTextField(10);
			nameTxtField.setFont(Fonts.FONT20.get());
			gbc.gridwidth = 2;
			gbc.anchor = GridBagConstraints.FIRST_LINE_START;
			gbc.gridx = 0;
			gbc.gridy = 1;
			contentPane.add(nameTxtField,gbc);
			
			JLabel typeLabel = new JLabel("type");
			typeLabel.setFont(Fonts.FONT20.get());
			gbc.anchor = GridBagConstraints.LAST_LINE_START;
			gbc.gridx = 0;
			gbc.gridy = 2;
			contentPane.add(typeLabel,gbc);
			
			typeComboBox = new JComboBox(new DefaultComboBoxModel());
			populateTypeComboBox();
			typeComboBox.setMaximumRowCount(3);
			typeComboBox.setFont(Fonts.FONT15.get());
			typeComboBox.addActionListener(e -> checkTypeInput());
			typeComboBox.setPreferredSize(new Dimension(100, 30));
			gbc.anchor = GridBagConstraints.FIRST_LINE_START;
			gbc.gridx = 0;
			gbc.gridy = 3;
			contentPane.add(typeComboBox,gbc);
			
			JLabel capacityLabel = new JLabel("capacity");
			capacityLabel.setFont(Fonts.FONT20.get());
			gbc.anchor = GridBagConstraints.LAST_LINE_START;
			gbc.gridx = 0;
			gbc.gridy = 4;
			contentPane.add(capacityLabel,gbc);
			
			capacityComboBox = new JComboBox(new DefaultComboBoxModel());
			populatecapacityLabel();
			capacityComboBox.setMaximumRowCount(4);
			capacityComboBox.setFont(Fonts.FONT15.get());
			capacityComboBox.setPreferredSize(new Dimension(100, 30));
			gbc.anchor = GridBagConstraints.FIRST_LINE_START;
			gbc.gridx = 0;
			gbc.gridy = 5;
			contentPane.add(capacityComboBox,gbc);
			
			
			okBtn = new FancyButtonOneClick(ProjectColors.BLACK.get(), ProjectColors.RED.get(), ProjectColors.RED.get());
			okBtn.setFont(Fonts.FONT20.get());
			okBtn.setBorderPainted(true);
//			okBtn.setPreferredSize(new Dimension(panelWidth/12, ToolPanel.getPanelHeight()/2));
			okBtn.addActionListener(e -> enterLayoutItem());
			okBtn.setText("ok");
			okBtn.setBorder(BorderFactory.createLineBorder(ProjectColors.BLACK.get(), 1));
			gbc.anchor = GridBagConstraints.FIRST_LINE_START;
			gbc.gridx = 0;
			gbc.gridy = 6;
			contentPane.add(okBtn,gbc);
			
			cancelBtn = new FancyButtonOneClick(ProjectColors.BLACK.get(), ProjectColors.RED.get(), ProjectColors.RED.get());
			cancelBtn.setFont(Fonts.FONT20.get());
			cancelBtn.setBorderPainted(true);
//			cancelBtn.setPreferredSize(new Dimension(panelWidth/12, ToolPanel.getPanelHeight()/2));
			cancelBtn.addActionListener(e -> cancel());
			cancelBtn.setText("cancel");
			cancelBtn.setBorder(BorderFactory.createLineBorder(ProjectColors.BLACK.get(), 1));
			gbc.anchor = GridBagConstraints.LAST_LINE_END;
			gbc.gridx = 1;
			gbc.gridy = 6;
			contentPane.add(cancelBtn,gbc);
			
	}
	
	private void enterLayoutItem() {
		
	}
	
	private void checkTypeInput() {
		if(!typeComboBox.getSelectedItem().equals("table"))  // if there is not table
			capacityComboBox.setEnabled(false);
		else 
			capacityComboBox.setEnabled(true);

	}
	
	private void populatecapacityLabel() {
		for(int i=1; i<9; i++) {
			capacityComboBox.addItem(i);
		}
	}
	
	private void populateTypeComboBox() {
		typeComboBox.addItem("table");
		typeComboBox.addItem("bar");
		typeComboBox.addItem("entrance");
		
	}
	
	private void cancel() {
		this.dispose();
	}

}
