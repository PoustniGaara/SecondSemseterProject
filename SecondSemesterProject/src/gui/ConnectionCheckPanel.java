package gui;

import java.awt.Color;
import java.awt.Font;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JLabel;
import javax.swing.JPanel;

import database.ConnectionCheckDAO;
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
		connLabel.setText("Not connected to database");
		connLabel.setForeground(Color.RED);
		
		// Lambda Runnable
		Runnable task = () -> { 
			boolean running = true;
			while(running) {
				try {
					checkConnection();
					Thread.sleep(5*1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
					return;
				}
				} // sleep for 5 seconds
		};
		
		new Thread(task).start();
	}
	
	private void checkConnection(){
		try{
			if(ConnectionCheckDAO.getInstance().verifyConnection().equals("1")) {
				if(isConnected == false) {
					connLabel.setText("Connected to database");
					connLabel.setForeground(Color.GREEN);
					this.repaint();
					repaintSuper();
					setConnected(true);
				}
			}
		}
		catch(Exception e) {
			if(isConnected == true) {
				connLabel.setText("Not connected to database");
				connLabel.setForeground(Color.RED);
				this.repaint();
				repaintSuper();
				setConnected(false);
			}
		}
	}
	
	private void repaintSuper() {
		HeaderPanel.getInstance().repaint();
	}
	
	public static ConnectionCheckPanel getInstance() {
		if(instance == null) return new ConnectionCheckPanel();
		else return instance;
	}
	
	private void setConnected(boolean state) {
		isConnected = state;
	}
}

