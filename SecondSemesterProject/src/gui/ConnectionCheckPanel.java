package gui;

import java.awt.Color;
import java.awt.Font;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JLabel;
import javax.swing.JPanel;

import database.ConnectionCheck;
import database.DBConnection;
import gui.tools.ProjectColors;

public class ConnectionCheckPanel extends JPanel {
	
	private JLabel connLabel;
	private boolean isConnected = false;
	private static ConnectionCheckPanel instance;
	private ConnectionCheckPanel(){
		
		this.setBackground(ProjectColors.GREEN.get());
		
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
					Thread.sleep(5*1000);
					Thread.currentThread().setPriority(1);
					checkConnection();
				} catch (InterruptedException e) {
					e.printStackTrace();
					return;
				}
				} // sleep for 5 seconds
		};
		
		new Thread(task).start();
		
	} // end of constructor
	
	private void checkConnection(){
		try{
			if(ConnectionCheck.getInstance().verifyConnection().equals("1")) {
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

