package gui;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.Point;
import java.awt.Toolkit;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import controller.RestaurantLayoutController;
import database.ConnectionCheckDAO;
import database.LayoutItemConcreteDAO;
import database.TableConcreteDAO;
import model.LayoutItem;
import model.RestaurantLayout;
import model.Table;

public class MainFrame extends JFrame {

	public static int width,height;
	private LoginPanel loginPanel;
	private OverviewPanel overviewPanel;
	private static MainFrame instance;
	private HeaderPanel headerPanel;
	private JPanel mainPanel;
	private MenuPanel menuPanel;
	private ReservationsPanel reservationsPanel;
	private JPanel currentPanel;
	
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MainFrame frame = MainFrame.getInstance();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	private MainFrame() {
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setExtendedState(this.getExtendedState() | JFrame.MAXIMIZED_BOTH);
		Toolkit tk = Toolkit.getDefaultToolkit();
		width = tk.getScreenSize().width;
		height = tk.getScreenSize().height;
		setBounds(0, 0, width, height);
		setTitle("Cafe Friends");
		setUndecorated(false);
		
		headerPanel = HeaderPanel.getInstance();
		overviewPanel = OverviewPanel.getInstance();
		menuPanel = MenuPanel.getInstance();
		reservationsPanel = ReservationsPanel.getInstance();
		
		mainPanel = new JPanel();
		mainPanel.setLayout(new BorderLayout());
		
		openLoginPanel();
	}
	
	public void showReservationsPanel() {
		currentPanel.setVisible(false);
		mainPanel.remove(currentPanel);
		mainPanel.add(reservationsPanel, BorderLayout.CENTER);
		reservationsPanel.setVisible(true);
		currentPanel = reservationsPanel;
	}
	
	public void showOverviewPanel() {
		currentPanel.setVisible(false);
		mainPanel.remove(currentPanel);
		mainPanel.add(overviewPanel, BorderLayout.CENTER);
		overviewPanel.setVisible(true);
		currentPanel = overviewPanel;
	}
	
	public void showMenuPanel() {
		currentPanel.setVisible(false);
		mainPanel.remove(currentPanel);
		mainPanel.add(menuPanel, BorderLayout.CENTER);
		menuPanel.setVisible(true);
		currentPanel = menuPanel;
	}
	public void setupScreen() {
		loginPanel.setVisible(false);
		
		mainPanel.add(headerPanel, BorderLayout.NORTH);
		mainPanel.add(overviewPanel, BorderLayout.CENTER);
		this.add(mainPanel);
		
		currentPanel = overviewPanel;
	}
	
	private void openLoginPanel() {
		loginPanel = LoginPanel.getInstance();
		this.add(loginPanel);
	}
	
	public static MainFrame getInstance() {
		if (instance == null)
			instance = new MainFrame();
		return instance;
	}

}
