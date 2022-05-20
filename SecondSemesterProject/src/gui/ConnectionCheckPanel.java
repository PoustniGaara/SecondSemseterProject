package gui;

import java.awt.Color;
import java.awt.Font;

import javax.swing.JLabel;
import javax.swing.JPanel;

import database.DBConnection;

public class ConnectionCheckPanel extends JPanel {
	
	private JLabel connLabel;
	private boolean isConnected = false;
	
	public ConnectionCheckPanel(){
		
		connLabel = new JLabel();
		connLabel.setFont(new Font("Monaco", Font.BOLD, 20));
		add(connLabel);
		
		// Lambda Runnable
		Runnable task = () -> {
			if(DBConnection.getInstance().getDBcon() != null) {
				if(isConnected == false) {
					connLabel.setText("Connected to database");
					connLabel.setBackground(Color.GREEN);
				}
			}
			else {
				if(isConnected == true) {
					connLabel.setText("Not connected to database");
					connLabel.setBackground(Color.RED);
				}
			}
		};

		new Thread(task).start();

	}
}

