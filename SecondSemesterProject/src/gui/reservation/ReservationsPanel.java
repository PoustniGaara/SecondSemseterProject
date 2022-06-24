package gui.reservation;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
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
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.ScrollPaneConstants;
import javax.swing.ScrollPaneLayout;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.MatteBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;

import controller.ReservationController;
import database.ReservationConcreteDAO;
import gui.ConnectionCheckLabel;
import gui.FooterPanel;
import gui.MainFrame;
import gui.ToolPanel;
import gui.tools.Fonts;
import gui.tools.ProjectColors;
import model.Menu;
import model.Reservation;
import model.Table;

@SuppressWarnings("serial")
public class ReservationsPanel extends JPanel {

	private static ReservationsPanel instance;
	private ReservationController reservationController;
	private JTextField searchBar;

	private JTable table;
	private DefaultTableModel tableModel;

	private ReservationsPanel() {
		reservationController = new ReservationController();

		setBounds(0, 100, MainFrame.width, MainFrame.height - 100);
		setLayout(new BorderLayout());
		setBackground(ProjectColors.BG.get());

		// -----------------------------------------------
		// ------------------ HEADER ---------------------
		// -----------------------------------------------

		// Tool Panel
		ToolPanel toolPanel = new ToolPanel();
		toolPanel.setBackground(ProjectColors.BG.get());
		add(toolPanel, BorderLayout.NORTH);

		GridBagConstraints gbcTool = new GridBagConstraints();
		gbcTool.fill = GridBagConstraints.NONE;
		gbcTool.anchor = GridBagConstraints.CENTER;
		gbcTool.weightx = 0.5;
		gbcTool.weighty = 0;

		// search bar
		searchBar = new JTextField();
		searchBar.setFont(Fonts.FONT18.get());
		searchBar.setSize(new Dimension((int) (getWidth() * 0.45), 40));
		searchBar.setPreferredSize(new Dimension((int) (getWidth() * 0.45), 40));
		searchBar.setBorder(
				new CompoundBorder(new LineBorder(new Color(212, 221, 233), 1), new EmptyBorder(0, 10, 0, 0)));
		searchBar.setBackground(Color.WHITE);
		searchBar.setToolTipText("Search reservations by date or customer's name");
		gbcTool.gridx = 0;
		gbcTool.gridy = 0;
		gbcTool.gridwidth = 2;
		gbcTool.gridheight = 1;
		gbcTool.insets = new Insets(20, 35, 20, 0);
		gbcTool.anchor = GridBagConstraints.WEST;
		toolPanel.add(searchBar, gbcTool);

		// search button
		JButton searchButton = new JButton("Search");
		searchButton.setFont(Fonts.FONT18.get());
		searchButton.setBackground(Color.white);
		searchButton.setBorder(new LineBorder(ProjectColors.BLUE3.get(), 1));
		searchButton.setPreferredSize(new Dimension((int) (getWidth() * 0.15), 40));
		searchButton.setFocusable(false);
		searchButton.addActionListener(e -> search());
		gbcTool.gridx = 2;
		gbcTool.gridwidth = 1;
		gbcTool.insets = new Insets(20, 0, 20, 35);
		gbcTool.anchor = GridBagConstraints.WEST;
		toolPanel.add(searchButton, gbcTool);

		// show all button
		JButton showButton = new JButton("Refresh");
		showButton.setFont(Fonts.FONT18.get());
		showButton.setBackground(Color.white);
		showButton.setBorder(new LineBorder(ProjectColors.LIGHT_BLUE.get(), 1));
		showButton.setPreferredSize(new Dimension((int) (getWidth() * 0.15), 40));
		showButton.setFocusable(false);
		showButton.addActionListener(e -> new Thread(() -> {
			try {
				populateTable(reservationController.getAllReservations());
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
		}).start());
		gbcTool.gridx = 3;
		gbcTool.anchor = GridBagConstraints.EAST;
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
		table.setBackground(ProjectColors.BLUEtable.get());
		table.setShowGrid(false);
		table.setShowHorizontalLines(true);
		table.setFont(Fonts.TABLE_FONT.get());
		table.setModel(tableModel);
		table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
		table.setDefaultEditor(Object.class, null);
		table.setRowHeight((int) (table.getRowHeight() * 2));
		table.setBorder(null);
		JTableHeader header = table.getTableHeader();
		header.setFont(Fonts.FONT18.get());
		header.setResizingAllowed(false);
		header.setReorderingAllowed(false);
		header.setBackground(ProjectColors.BLUE1.get());
		table.setTableHeader(header);
		createColumns();
		try {
			populateTable(reservationController.getAllReservations());
		} catch (SQLException e) {
			JOptionPane.showMessageDialog(null,
					"An error occured, while getting reservation information! \nTry refreshing the table", "Error",
					JOptionPane.WARNING_MESSAGE);
		}
		table.getColumnModel().getColumn(0).setWidth(0);
		table.getColumnModel().getColumn(0).setMinWidth(0);
		table.getColumnModel().getColumn(0).setMaxWidth(0);
		table.setFillsViewportHeight(true);

		JPanel mainPanel = new JPanel();
		mainPanel.setLayout(new GridBagLayout());
		mainPanel.setBackground(Color.white);
		add(mainPanel, BorderLayout.CENTER);
		mainPanel.setBorder(new MatteBorder(0, 0, 1, 0, ProjectColors.BLUE1.get()));

		GridBagConstraints gbcMain = new GridBagConstraints();
		gbcMain.insets = new Insets(20, 35, 20, 35);
		gbcMain.fill = GridBagConstraints.BOTH;
		gbcMain.anchor = GridBagConstraints.CENTER;
		gbcMain.gridwidth = GridBagConstraints.REMAINDER;
		gbcMain.gridheight = GridBagConstraints.REMAINDER;
		gbcMain.weightx = 1;
		gbcMain.weighty = 1;
		gbcMain.gridx = 0;
		gbcMain.gridy = 0;

		// pane
		JScrollPane scrollPane = new JScrollPane(table);
		scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		scrollPane.setBorder(null);
		scrollPane.setLayout(new ScrollPaneLayout());
		mainPanel.add(scrollPane, gbcMain);

		// -----------------------------------------------
		// ------------------ Footer ---------------------
		// -----------------------------------------------

		FooterPanel footerPanel = new FooterPanel();
		footerPanel.setBackground(Color.WHITE);
		add(footerPanel, BorderLayout.SOUTH);
		GridBagLayout footergbl = new GridBagLayout();
		footergbl.columnWidths = new int[] { 550, 170, 170, 170 };
		footerPanel.setLayout(footergbl);

		GridBagConstraints gbcFooter = new GridBagConstraints();
		gbcFooter.insets = new Insets(20, 5, 0, 15);
		gbcFooter.fill = GridBagConstraints.VERTICAL;
		gbcFooter.anchor = GridBagConstraints.SOUTHWEST;
		gbcFooter.gridwidth = 1;
		gbcFooter.weightx = 1;
		gbcFooter.weighty = 0;
		gbcFooter.ipadx = 0;
		gbcFooter.ipady = 0;
		gbcFooter.gridx = 0;
		gbcFooter.gridy = 0;

		JLabel conCheck = ConnectionCheckLabel.getInstance();
		footerPanel.add(conCheck, gbcFooter);

		// delete button
		JButton deleteButton = new JButton("Delete");
		deleteButton.setFont(Fonts.FONT18.get());
		deleteButton.setForeground(new Color(195, 70, 70));
		deleteButton.setBackground(new Color(252, 232, 232));
		deleteButton.setBorder(new LineBorder(new Color(220, 48, 48), 1));
		deleteButton.setPreferredSize(new Dimension((int) (getWidth() * 0.15), 40));
		deleteButton.setFocusable(false);
		deleteButton.addActionListener(e -> new Thread(() -> {
			delete();
		}).start());
		gbcFooter.insets = new Insets(10, 15, 10, 15);
		gbcFooter.anchor = GridBagConstraints.EAST;
		gbcFooter.gridx = 1;
		footerPanel.add(deleteButton, gbcFooter);

		// modify button
		JButton modifyButton = new JButton("Change");
		modifyButton.setFont(Fonts.FONT18.get());
		modifyButton.setBackground(ProjectColors.BLUE1.get());
		modifyButton.setBorder(new LineBorder(ProjectColors.LIGHT_BLUE.get(), 1));
		modifyButton.setPreferredSize(new Dimension((int) (getWidth() * 0.15), 40));
		modifyButton.setFocusable(false);
		modifyButton.addActionListener(e -> change());
		gbcFooter.gridx = 2;
		gbcFooter.ipadx = 20;
		footerPanel.add(modifyButton, gbcFooter);

		// add button
		JButton addButton = new JButton("Add +");
		addButton.setFont(Fonts.FONT18.get());
		addButton.setBackground(ProjectColors.BLUE.get());
		addButton.setForeground(Color.white);
		addButton.setPreferredSize(new Dimension((int) (getWidth() * 0.15), 40));
		addButton.setFocusable(false);
		addButton.addActionListener(e -> add());
		gbcFooter.gridx = 3;
		gbcFooter.insets = new Insets(10, 15, 10, 35);
		footerPanel.add(addButton, gbcFooter);
	}

	private void search() {
		String search = searchBar.getText();
		if (search.isEmpty() || search.isBlank())
			return;

		ArrayList<Reservation> reservations = new ArrayList<>();
		try {
			reservations = reservationController.getReservationByCustomer(search);
		} catch (SQLException e) {
			e.printStackTrace();
		}

		if (reservations.isEmpty()) {
			JOptionPane.showMessageDialog(null, "There are no reservations associated with the search enty!", "Error",
					JOptionPane.WARNING_MESSAGE);
			searchBar.setText("");
		} else {
			populateTable(reservations);
		}
	}

	private void change() {
		try {
			if (table.getSelectedRow() != -1) {
				int id = Integer.parseInt(table.getValueAt(table.getSelectedRow(), 0).toString());
				Reservation reservation = reservationController.getReservationById(id);
				if (reservation != null) {
					UpdateReservationFrame.open(reservation);
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

	private void add() {
		CreateReservationFrame.open();
	}

	private void delete() {
		try {
			if (table.getSelectedRow() != -1) {
				if (JOptionPane.showConfirmDialog(null,
						"Are you sure you want to delete the reservation?\nThis action is permanent!",
						"Reservation deletion", JOptionPane.YES_NO_OPTION,
						JOptionPane.WARNING_MESSAGE) == JOptionPane.YES_OPTION) {
					long id = Long.parseLong(table.getValueAt(table.getSelectedRow(), 0).toString());
					tableModel.removeRow(table.getSelectedRow());
					reservationController.deleteReservation((int) id);
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
		Object[] columns = { "id", "Date", "Time", "Duration", "Customer", "Guests", "Tables", "Menus", "Note" };
		for (Object o : columns) {
			tableModel.addColumn(o);
		}
	}

	public void populateTable(ArrayList<Reservation> reservations) {
		tableModel.setRowCount(0);
		if (!reservations.isEmpty()) {
			for (Reservation reservation : reservations) {
				tableModel.addRow(
						new Object[] { reservation.getId(), toDate(reservation.getTimestamp().getTimeInMillis()),
								toTime(reservation.getTimestamp().getTimeInMillis()), reservation.getDuration() + " h",
								reservation.getCustomer().getSurname().toUpperCase() + " "
										+ reservation.getCustomer().getName(),
								reservation.getGuests(), tableNames(reservation), menuNames(reservation),
								reservation.getNote().length() > 40
										? reservation.getNote().substring(0, 40).concat("...")
										: reservation.getNote() });
			}
			tableModel.fireTableDataChanged();
			table.repaint();
		}
	}

	private String tableNames(Reservation reservation) {
		if (reservation.getTables().isEmpty()) {
			return "No table";
		}

		String s = "";
		for (Table t : reservation.getTables()) {
			s += t.getName() + ", ";
		}
		return s.substring(0, s.length() - 2);
	}

	private String menuNames(Reservation reservation) {
		if (reservation.getMenus() == null)
			return "no menus";

		if (reservation.getMenus().isEmpty())
			return "no menus";

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
