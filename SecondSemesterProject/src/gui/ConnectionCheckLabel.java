package gui;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JLabel;
import javax.swing.JPanel;

import database.ConnectionCheck;
import database.DBConnection;
import gui.tools.ProjectColors;

public class ConnectionCheckLabel extends JLabel {

	private boolean isConnected = false;
	private static ConnectionCheckLabel instance;

	private ConnectionCheckLabel() {
		setFont(new Font("Monaco", Font.BOLD, 12));
		setText("Not connected to database");
		setForeground(new Color(220, 48, 48));

		Runnable task = () -> {
			boolean running = true;
			while (running) {
				try {
					Thread.sleep(5 * 1000);
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

	private void checkConnection() {
		try {
			if (ConnectionCheck.getInstance().verifyConnection().equals("1")) {
				if (isConnected == false) {
					this.setText("Connected to database");
					this.setForeground(new Color(56, 193, 114));// (56,193,114) new Color(116, 217, 159)
					this.repaint();
					repaintSuper();
					setConnected(true);
				}
			}
		} catch (Exception e) {
			if (isConnected == true) {
				this.setText("Not connected to database");
				this.setForeground(new Color(220, 48, 48));
				this.repaint();
				repaintSuper();
				setConnected(false);
			}
		}
	}

	private void repaintSuper() {
		HeaderPanel.getInstance().repaint();
	}

	public static ConnectionCheckLabel getInstance() {
		if (instance == null)
			return new ConnectionCheckLabel();
		else
			return instance;
	}

	private void setConnected(boolean state) {
		isConnected = state;
	}
}
