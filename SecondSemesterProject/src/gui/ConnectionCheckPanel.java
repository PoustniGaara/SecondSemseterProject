package gui;

import java.awt.Color;
import java.awt.Font;

import javax.swing.JLabel;
import javax.swing.JPanel;

import database.DBConnection;
import gui.tools.PColors;

public class ConnectionCheckPanel extends JPanel {
	
	private JLabel connLabel;
	private boolean isConnected = false;
	private static ConnectionCheckPanel instance;
	
	private ConnectionCheckPanel(){
		this.setBackground(PColors.get(PColors.GREEN));
		
		connLabel = new JLabel();
		connLabel.setFont(new Font("Monaco", Font.BOLD, 12));
		add(connLabel);
		
		// Lambda Runnable
		Runnable task = () -> { 
			while(this.isVisible()) {
			try{
				if(DBConnection.getInstance().getDBcon() != null) {
					if(isConnected == false) {
						connLabel.setText("Connected to database");
						connLabel.setForeground(Color.GREEN);
					}
				}
				else {
					if(isConnected == true) {
						connLabel.setText("Not connected to database");
						connLabel.setForeground(Color.RED);
					}
				}
		       Thread.sleep(5*1000); // sleep for 5 seconds
			}
			catch(InterruptedException e) {
			}
			}
		};

		new Thread(task).start();
	}
	
	public static ConnectionCheckPanel getInstance() {
		if(instance == null) return new ConnectionCheckPanel();
		else return instance;
	}
}

