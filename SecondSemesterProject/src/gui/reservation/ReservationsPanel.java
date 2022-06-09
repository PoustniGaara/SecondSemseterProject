package gui.reservation;

import java.awt.*;
import java.sql.SQLException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Random;

import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.ScrollPaneConstants;
import javax.swing.ScrollPaneLayout;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;

import controller.ReservationController;
import database.MenuConcreteDAO;
import database.ReservationConcreteDAO;
import database.TableConcreteDAO;
import database.TableDAO;
import gui.FooterPanel;
import gui.ToolPanel;
import model.*;
import model.Menu;

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
		setLayout(new BorderLayout());
		setBackground(Color.WHITE);

		// -----------------------------------------------
		// ------------------ HEADER ---------------------
		// -----------------------------------------------

		// Tool Panel
		ToolPanel toolPanel = new ToolPanel();
		toolPanel.setBorder(new LineBorder(Color.GREEN));
		add(toolPanel, BorderLayout.NORTH);

		GridBagConstraints gbcTool = new GridBagConstraints();
		gbcTool.insets = new Insets(20, 25, 20, 25);
		gbcTool.fill = GridBagConstraints.NONE;
		gbcTool.anchor = GridBagConstraints.CENTER;
		gbcTool.weightx = 0.5;
		gbcTool.weighty = 0;

		// search bar
		searchBar = new JTextField();
		searchBar.setFont(new Font("Segoe UI Semibold", Font.PLAIN, 20));
		searchBar.setSize(new Dimension((int) (getWidth() * 0.45), 40));
		searchBar.setPreferredSize(new Dimension((int) (getWidth() * 0.45), 40));
		searchBar.setBorder(new CompoundBorder(new LineBorder(Color.BLACK, 1), new EmptyBorder(0, 10, 0, 0)));
		searchBar.setBackground(Color.WHITE);
		searchBar.setToolTipText("Search reservations by date or customer's name");
		gbcTool.gridx = 0;
		gbcTool.gridy = 0;
		gbcTool.gridwidth = 2;
		gbcTool.gridheight = 1;
		toolPanel.add(searchBar, gbcTool);

		// search button
		JButton searchButton = new JButton("Search");
		searchButton.setFont(new Font("Segoe UI Semibold", Font.PLAIN, 16));
		searchButton.setBackground(new Color(242, 233, 228));
		searchButton.setPreferredSize(new Dimension((int) (getWidth() * 0.15), 40));
		searchButton.setFocusable(false);
		// searchButton.addActionListener(e -> search());
		gbcTool.gridx = 2;
		gbcTool.gridwidth = 1;
		gbcTool.anchor = GridBagConstraints.WEST;
		toolPanel.add(searchButton, gbcTool);

		// show all button
		JButton showButton = new JButton("Show all");
		showButton.setFont(new Font("Segoe UI Semibold", Font.PLAIN, 16));
		showButton.setBackground(new Color(242, 233, 228));
		showButton.setPreferredSize(new Dimension((int) (getWidth() * 0.15), 40));
		showButton.setFocusable(false);
		// showButton.addActionListener(e ->
		// populateTable(productController.showAllProducts()));
		gbcTool.gridx = 3;
		gbcTool.anchor = GridBagConstraints.CENTER;
		toolPanel.add(showButton, gbcTool);

		// -----------------------------------------------
		// ------------------ CENTER ---------------------
		// -----------------------------------------------

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
		table.setPreferredScrollableViewportSize(new Dimension(getWidth(), getHeight() - 100));
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

		JPanel mainPanel = new JPanel();
		mainPanel.setLayout(new GridBagLayout());
		mainPanel.setBackground(Color.WHITE);
		add(mainPanel, BorderLayout.CENTER);

		GridBagConstraints gbcMain = new GridBagConstraints();
		gbcMain.insets = new Insets(20, 25, 20, 25);
		gbcMain.fill = GridBagConstraints.VERTICAL;
		gbcMain.anchor = GridBagConstraints.CENTER;
		gbcMain.gridwidth = 1;
		gbcMain.weightx = 1;
		gbcMain.weighty = 1;
		gbcMain.gridx = 0;
		gbcMain.gridy = 0;

		// pane
		JScrollPane scrollPane = new JScrollPane(table);
		scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		scrollPane.setPreferredSize(new Dimension(getWidth() / 100 * 90, getHeight() / 100 * 50));
		scrollPane.setBorder(null);
		scrollPane.setLayout(new ScrollPaneLayout());
		mainPanel.add(scrollPane, gbcMain);

		// -----------------------------------------------
		// ------------------ Footer ---------------------
		// -----------------------------------------------

		FooterPanel footerPanel = new FooterPanel();
		add(footerPanel, BorderLayout.SOUTH);
		footerPanel.setLayout(new GridBagLayout());

		GridBagConstraints gbcFooter = new GridBagConstraints();
		gbcFooter.insets = new Insets(10, 15, 10, 15);
		gbcFooter.fill = GridBagConstraints.VERTICAL;
		gbcFooter.anchor = GridBagConstraints.WEST;
		gbcFooter.gridwidth = 1;
		gbcFooter.weightx = 1;
		gbcFooter.weighty = 1;
		gbcFooter.ipadx = 20;
		gbcFooter.ipady = 0;
		gbcFooter.gridx = 0;
		gbcFooter.gridy = 0;

		// modify button
		JButton modifyButton = new JButton("Modify");
		modifyButton.setFont(new Font("Segoe UI Semibold", Font.PLAIN, 16));
		modifyButton.setBackground(new Color(242, 233, 228));
		modifyButton.setPreferredSize(new Dimension((int) (getWidth() * 0.15), 40));
		modifyButton.setFocusable(false);
		// modifyButton.addActionListener(e -> modify());
		footerPanel.add(modifyButton, gbcFooter);

		// delete button
		JButton deleteButton = new JButton("Delete");
		deleteButton.setFont(new Font("Segoe UI Semibold", Font.PLAIN, 16));
		deleteButton.setBackground(new Color(242, 233, 228));
		deleteButton.setPreferredSize(new Dimension((int) (getWidth() * 0.15), 40));
		deleteButton.setFocusable(false);
		deleteButton.addActionListener(e -> delete());
		gbcFooter.gridx = 1;
		gbcFooter.anchor = GridBagConstraints.WEST;
		footerPanel.add(deleteButton, gbcFooter);

		// add button
		JButton addButton = new JButton("Add");
		addButton.setFont(new Font("Segoe UI Semibold", Font.PLAIN, 16));
		addButton.setBackground(new Color(242, 233, 228));
		addButton.setPreferredSize(new Dimension((int) (getWidth() * 0.15), 40));
		addButton.setFocusable(false);
		addButton.addActionListener(e -> add());
		gbcFooter.gridx = 4;
		gbcFooter.anchor = GridBagConstraints.EAST;
		footerPanel.add(addButton, gbcFooter);

	}

	private void add() {
		CreateReservationFrame.open();
		try {
			populateTable(reservationController.getAllReservations());
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

//		try {
//			Calendar calendar = new GregorianCalendar();
//			Random r = new Random();
//			calendar.set(2022, r.nextInt(11 - 6) + 6, r.nextInt(30 - 1) + 1, r.nextInt(20 - 12) + 12, 0);
//
//			Table t = TableConcreteDAO.getInstance().read(1);
//			ArrayList<Table> tables = new ArrayList<>();
//			tables.add(t);
//
//			Reservation reservation = reservationController.startReservation(calendar, tables);
//
//			Menu m = MenuConcreteDAO.getInstance().read(1);
//			ArrayList<Menu> menus = new ArrayList<>();
//			menus.add(m);
//			menus.add(m);
//			menus.add(m);
//			menus.add(m);
//
//			String phone = "187654351";
//			Customer customer = reservationController.checkCustomer(phone);
//
//			String note = "Please, decorate the table with dead kittens :)";
//
//			reservationController.confirmReservation(customer, 4, menus, note);
//
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
	}

	private void delete() {
		try {
			if (table.getSelectedRow() != -1) {
				if (!reservationController.getAllReservations().isEmpty()) {
					int id = Integer.parseInt(table.getValueAt(table.getSelectedRow(), 3).toString());
					if (reservationController.getReservationById(id) != null) {
						Reservation r = reservationController.getReservationById(id);
						if (JOptionPane.showConfirmDialog(null,
								"Are you sure you want to delete this reservation?\nThis action is permanent!",
								"Reservation deletion", JOptionPane.YES_NO_OPTION,
								JOptionPane.WARNING_MESSAGE) == JOptionPane.YES_OPTION) {
							reservationController.deleteReservation(r);
							populateTable(reservationController.getAllReservations());
						}
					}
				}
			} else {
				JOptionPane.showMessageDialog(null, "You must select a reservation in the list you want to delete!",
						"Error", JOptionPane.WARNING_MESSAGE);
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
	
	public static void repopulateTable() {
		try {
			ReservationsPanel.getInstance().populateTable(ReservationConcreteDAO.getInstance().read());
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
