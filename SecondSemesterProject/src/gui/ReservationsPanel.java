package gui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Toolkit;
import java.sql.SQLException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.ScrollPaneConstants;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;

import controller.ReservationController;
import model.Menu;
import model.Reservation;
import model.Table;

@SuppressWarnings("serial")
public class ReservationsPanel extends JPanel {

	private static ReservationsPanel instance;
	private ReservationController reservationController;
	private JTextField searchBar;

	private int width;
	private int height;
	private JTable table;
	private DefaultTableModel tableModel;
	private float time;

	private ReservationsPanel() {
		reservationController = new ReservationController();
		Toolkit tk = Toolkit.getDefaultToolkit();
		setBounds(0, 100, tk.getScreenSize().width, tk.getScreenSize().height - 100);
		setLayout(new GridBagLayout());
		setBackground(Color.WHITE);
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.insets = new Insets(20, 25, 20, 25);
		gbc.fill = GridBagConstraints.NONE;
		gbc.anchor = GridBagConstraints.CENTER;
		gbc.weightx = 1;
		gbc.weighty = 1;

		// search bar
		searchBar = new JTextField();
		searchBar.setFont(new Font("Segoe UI Semibold", Font.PLAIN, 20));
		searchBar.setSize(new Dimension((int) (getWidth() * 0.45), 40));
		searchBar.setPreferredSize(new Dimension((int) (getWidth() * 0.45), 40));
		searchBar.setBorder(new CompoundBorder(new LineBorder(Color.BLACK, 1), new EmptyBorder(0, 10, 0, 0)));
		searchBar.setBackground(Color.WHITE);
		searchBar.setToolTipText("Search reservations by date or customer's name");
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.gridwidth = 2;
		gbc.gridheight = 1;
		add(searchBar, gbc);

		// table
		table = new JTable() {
			@Override
			public Component prepareRenderer(TableCellRenderer renderer, int row, int column) {
				Component component = super.prepareRenderer(renderer, row, column);
				int rendererWidth = component.getPreferredSize().width;
				TableColumn tableColumn = getColumnModel().getColumn(column);
				tableColumn.setPreferredWidth(
						Math.max(rendererWidth + getIntercellSpacing().width, tableColumn.getPreferredWidth()));
				return component;
			}
		};
		tableModel = new DefaultTableModel();
		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		table.setPreferredScrollableViewportSize(new Dimension(getWidth() / 2, getHeight() / 2 - 100));
		table.setFont(new Font("Tahoma", Font.PLAIN, 16));
		table.setModel(tableModel);
		table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
		table.setDefaultEditor(Object.class, null);
		table.setRowHeight((int) (table.getRowHeight() * 2));
		JTableHeader header = table.getTableHeader();
		header.setFont(new Font("Tahoma", Font.BOLD, 16));
		header.setResizingAllowed(false);
		header.setReorderingAllowed(false);
		table.setTableHeader(header);
		createColumns();
		try {
			populateTable(reservationController.getAllReservations());
		} catch (SQLException e) {
			JOptionPane.showMessageDialog(null,
					"An error occured, while getting reservation information! \nTry refreshing the table", "Error",
					JOptionPane.WARNING_MESSAGE);
		}

		// pane
		JScrollPane scrollPane = new JScrollPane(table);
		scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		scrollPane.setPreferredSize(new Dimension(getWidth() / 100 * 90, getHeight() / 100 * 50));
		scrollPane.setBorder(null);
		gbc.gridx = 0;
		gbc.gridy = 1;
		gbc.gridwidth = 4;
		gbc.gridheight = 3;
		gbc.fill = GridBagConstraints.VERTICAL;
		add(scrollPane, gbc);

		// search button
		JButton searchButton = new JButton("Search");
		searchButton.setFont(new Font("Segoe UI Semibold", Font.PLAIN, 16));
		searchButton.setBackground(new Color(242, 233, 228));
		searchButton.setPreferredSize(new Dimension((int) (getWidth() * 0.15), 40));
		searchButton.setFocusable(false);
		// searchButton.addActionListener(e -> search());
		gbc.gridx = 2;
		gbc.gridy = 0;
		gbc.gridwidth = 1;
		gbc.gridheight = 1;
		gbc.fill = GridBagConstraints.NONE;
		gbc.anchor = GridBagConstraints.WEST;
		add(searchButton, gbc);

		// show all button
		JButton showButton = new JButton("Show all");
		showButton.setFont(new Font("Segoe UI Semibold", Font.PLAIN, 16));
		showButton.setBackground(new Color(242, 233, 228));
		showButton.setPreferredSize(new Dimension((int) (getWidth() * 0.15), 40));
		showButton.setFocusable(false);
		// showButton.addActionListener(e ->
		// populateTable(productController.showAllProducts()));
		gbc.gridx = 3;
		gbc.gridy = 0;
		gbc.gridwidth = 1;
		gbc.gridheight = 1;
		gbc.fill = GridBagConstraints.NONE;
		gbc.anchor = GridBagConstraints.CENTER;
		add(showButton, gbc);

		// delete button
		JButton deleteButton = new JButton("Delete");
		deleteButton.setFont(new Font("Segoe UI Semibold", Font.PLAIN, 16));
		deleteButton.setBackground(new Color(242, 233, 228));
		deleteButton.setPreferredSize(new Dimension((int) (getWidth() * 0.15), 40));
		deleteButton.setFocusable(false);
		deleteButton.addActionListener(e -> delete());
		gbc.anchor = GridBagConstraints.NORTH;
		gbc.gridx = 0;
		gbc.gridy = 4;
		add(deleteButton, gbc);

		// modify button
		JButton modifyButton = new JButton("Modify");
		modifyButton.setFont(new Font("Segoe UI Semibold", Font.PLAIN, 16));
		modifyButton.setBackground(new Color(242, 233, 228));
		modifyButton.setPreferredSize(new Dimension((int) (getWidth() * 0.15), 40));
		modifyButton.setFocusable(false);
		// modifyButton.addActionListener(e -> modify());
		gbc.gridx = 1;
		gbc.gridy = 4;
		add(modifyButton, gbc);

		// add button
		JButton addButton = new JButton("Add");
		addButton.setFont(new Font("Segoe UI Semibold", Font.PLAIN, 16));
		addButton.setBackground(new Color(242, 233, 228));
		addButton.setPreferredSize(new Dimension((int) (getWidth() * 0.15), 40));
		addButton.setFocusable(false);
		// addButton.addActionListener(e -> add());
		gbc.gridx = 3;
		gbc.gridy = 4;
		add(addButton, gbc);

	}

	private void delete() {
		Runnable task = () -> {
			boolean running = true;
			while (running) {
				try {
					time += 0.1f;
					Thread.sleep(100);
				} catch (InterruptedException e) {
					e.printStackTrace();
					return;
				}
			} // sleep for 5 seconds
		};
		new Thread(task).start();
		
		System.out.println("DB 1: " + time + " seconds");
		try {
			if (!reservationController.getAllReservations().isEmpty()) {
				System.out.println("DB 2: " + time + " seconds");
				if (table.getSelectedRow() != -1) {
					int id = Integer.parseInt(table.getValueAt(table.getSelectedRow(), 3).toString());
					if (reservationController.getReservationById(id) != null) {
						System.out.println("DB 3: " + time + " seconds");
						Reservation r = reservationController.getReservationById(id);
						if (JOptionPane.showConfirmDialog(null,
								"Are you sure you want to delete this reservation?\nThis action is permanent!",
								"Reservation deletion", JOptionPane.YES_NO_OPTION,
								JOptionPane.WARNING_MESSAGE) == JOptionPane.YES_OPTION) {
							reservationController.deleteReservation(r);
							System.out.println("DB 4: " + time + " seconds");
							populateTable(reservationController.getAllReservations());
							System.out.println("DB 5: " + time + " seconds");
						}
					}
				} else {
					JOptionPane.showMessageDialog(null, "You must select a reservation in the list you want to delete!",
							"Error", JOptionPane.WARNING_MESSAGE);
				}
			}
		} catch (SQLException e) {
			JOptionPane.showMessageDialog(null,
					"An error occured, while getting reservation information! \nTry refreshing the table", "Error",
					JOptionPane.WARNING_MESSAGE);
		}
	}

	private void createColumns() {
		Object[] columns = { "Date", "Time", "Duration", "ID", "Customer", "Guests", "Tables", "Menus", "Note" };
		for (Object o : columns) {
			tableModel.addColumn(o);
		}
	}

	public void populateTable(ArrayList<Reservation> reservations) {
		tableModel.setRowCount(0);
		if (!reservations.isEmpty()) {
			for (Reservation reservation : reservations) {
				tableModel.addRow(new Object[] { toDate(reservation.getTimestamp().getTimeInMillis()),
						toTime(reservation.getTimestamp().getTimeInMillis()), reservation.getDuration() + " h",
						reservation.getId(),
						reservation.getCustomer().getSurname().toUpperCase() + " "
								+ reservation.getCustomer().getName(),
						reservation.getGuests(), tableNames(reservation), menuNames(reservation),
						reservation.getNote() });
			}
		}
	}

	private String tableNames(Reservation reservation) {
		if (reservation.getTables().isEmpty()) {
			return "no table :/";
		}

		String s = "";
		for (Table t : reservation.getTables()) {
			s += t.getName() + ", ";
		}
		return s.substring(0, s.length() - 2);
	}

	private String menuNames(Reservation reservation) {
		if (reservation.getMenus().isEmpty()) {
			return "no menu :/";
		}

		String s = "";
		for (Menu m : reservation.getMenus()) {
			s += m.getName() + ", ";
		}
		return s.substring(0, s.length() - 2);
	}

	public static ReservationsPanel getInstance() {
		if (instance == null)
			return new ReservationsPanel();
		else
			return instance;
	}

	private String toDate(long timestamp) {
		LocalDate date = Instant.ofEpochMilli(timestamp).atZone(ZoneId.systemDefault()).toLocalDate();
		return date.format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));
	}

	private String toTime(long timestamp) {
		LocalTime time = Instant.ofEpochMilli(timestamp).atZone(ZoneId.systemDefault()).toLocalTime();
		return time.format(DateTimeFormatter.ofPattern("HH:mm"));
	}
}
