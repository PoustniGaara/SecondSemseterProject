package gui.reservation;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import javax.swing.DefaultListModel;
import javax.swing.DefaultListSelectionModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;
import javax.swing.SpinnerDateModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.JSpinner.NumberEditor;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.text.DateFormatter;
import javax.swing.text.NumberFormatter;

import controller.CustomerController;
import controller.ReservationController;
import database.MenuConcreteDAO;
import database.ReservationConcreteDAO;
import database.TableConcreteDAO;
import gui.tools.Fonts;
import gui.tools.ProjectColors;
import model.Menu;
import model.Reservation;
import model.Table;

@SuppressWarnings("serial")
public class UpdateReservationFrame extends JFrame {

	private ReservationController reservationController;
	private CustomerController customerController;
	private Reservation reservation;

	private JPanel contentPane;
	private JList<String> tables;
	private static ArrayList<Table> dbTables;
	private static ArrayList<Reservation> dbReservations;

	private final Font font = Fonts.FONT20.get();

	private final Color darkGray = new Color(18, 18, 18);
	private final Color lightGray = new Color(89, 89, 89);

	public static void open(Reservation reservation) {
		SwingUtilities.invokeLater((Runnable) new Runnable() {
			public void run() {
				UpdateReservationFrame frame = new UpdateReservationFrame(reservation);
				frame.setVisible(true);
			}
		});

		try {
			dbReservations = ReservationConcreteDAO.getInstance().read();
			dbTables = TableConcreteDAO.getInstance().read();
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
	}

	public UpdateReservationFrame(Reservation reservation) {
		this.reservation = reservation;
		reservationController = new ReservationController();
		customerController = new CustomerController();

		setTitle("Update Reservation");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(0, 0, 1000, 800);
		setUndecorated(false);
		setResizable(false);

		contentPane = new JPanel();
		setContentPane(contentPane);
		GridBagLayout gbl = new GridBagLayout();
		gbl.columnWidths = new int[] { 150, 150, 150, 150, 150, 150 };
		contentPane.setLayout(gbl);

		GridBagConstraints gbc = new GridBagConstraints();
		gbc.insets = new Insets(20, 30, 20, 30);
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.ipadx = 0;
		gbc.ipady = 0;
		gbc.gridwidth = GridBagConstraints.REMAINDER;
		gbc.anchor = GridBagConstraints.NORTH;
		gbc.fill = GridBagConstraints.HORIZONTAL;

		JLabel titleLabel = new JLabel("Updating reservation no. " + reservation.getId(), JLabel.CENTER);
		titleLabel.setFont(new Font("Segoe UI Semibold", Font.BOLD, 28));
		titleLabel.setForeground(darkGray);
		contentPane.add(titleLabel, gbc);

		JLabel guestsLabel = new JLabel("Number of guests:");
		guestsLabel.setForeground(darkGray);
		guestsLabel.setFont(font);
		gbc.gridy++;
		gbc.gridheight = 1;
		gbc.anchor = GridBagConstraints.WEST;
		gbc.gridwidth = 3;
		gbc.insets = new Insets(20, 30, 5, 30);
		contentPane.add(guestsLabel, gbc);

		JTextField guestsField = new JTextField();
		guestsField.setFont(font);
		guestsField.setBorder(new CompoundBorder(new LineBorder(darkGray, 1), new EmptyBorder(0, 10, 0, 0)));
		guestsField.setBackground(Color.WHITE);
		guestsField.setText(reservation.getGuests() + "");
		gbc.gridy++;
		gbc.insets = new Insets(5, 30, 20, 30);
		contentPane.add(guestsField, gbc);

		JLabel dateLabel = new JLabel("Date:");
		dateLabel.setForeground(darkGray);
		dateLabel.setFont(font);
		gbc.gridy++;
		gbc.insets = new Insets(20, 30, 5, 30);
		contentPane.add(dateLabel, gbc);

		DateFormat format = new SimpleDateFormat("dd.MM.yyyy");
		DateFormatter df = new DateFormatter(format);
		JFormattedTextField dateField = new JFormattedTextField(df);
		dateField.setValue(reservation.getTimestamp().getTime());
		dateField.setFont(font);
		dateField.setBorder(new CompoundBorder(new LineBorder(darkGray, 1), new EmptyBorder(0, 10, 0, 0)));
		gbc.gridy++;
		gbc.insets = new Insets(5, 30, 20, 30);
		contentPane.add(dateField, gbc);

		JLabel timeLabel = new JLabel("Time:");
		timeLabel.setForeground(darkGray);
		timeLabel.setFont(font);
		gbc.gridy++;
		gbc.insets = new Insets(20, 30, 5, 30);
		contentPane.add(timeLabel, gbc);

		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.HOUR_OF_DAY, 12);
		calendar.set(Calendar.MINUTE, 0);

		SpinnerDateModel model = new SpinnerDateModel();
		model.setValue(calendar.getTime());

		JSpinner spinnerH = new JSpinner(model);
		JSpinner.DateEditor editorH = new JSpinner.DateEditor(spinnerH, "HH");
		DateFormatter formatterH = (DateFormatter) editorH.getTextField().getFormatter();
		formatterH.setAllowsInvalid(false);
		formatterH.setOverwriteMode(true);
		spinnerH.setEditor(editorH);
		spinnerH.setFont(font);
		spinnerH.setValue(reservation.getTimestamp().getTime());
		spinnerH.setBorder(new CompoundBorder(new LineBorder(darkGray, 1), new EmptyBorder(0, 10, 0, 0)));
		gbc.gridy++;
		gbc.gridwidth = 1;
		// gbc.anchor = GridBagConstraints.NORTH;
		gbc.insets = new Insets(5, 30, 20, 30);
		contentPane.add(spinnerH, gbc);

		JLabel separator = new JLabel(":", JLabel.CENTER);
		separator.setForeground(darkGray);
		separator.setFont(font);
		gbc.gridx++;
		contentPane.add(separator, gbc);

		SpinnerDateModel model2 = new SpinnerDateModel();
		model2.setValue(calendar.getTime());

		JSpinner spinnerM = new JSpinner(model2);
		JSpinner.DateEditor editorM = new JSpinner.DateEditor(spinnerM, "mm");
		DateFormatter formatterM = (DateFormatter) editorM.getTextField().getFormatter();
		formatterM.setAllowsInvalid(false);
		formatterM.setOverwriteMode(true);
		spinnerM.setEditor(editorM);
		spinnerM.setFont(font);
		spinnerM.setValue(reservation.getTimestamp().getTime());
		spinnerM.setBorder(new CompoundBorder(new LineBorder(darkGray, 1), new EmptyBorder(0, 10, 0, 0)));
		gbc.gridx++;
		contentPane.add(spinnerM, gbc);

		JLabel tablesLabel = new JLabel("Table(s):");
		tablesLabel.setForeground(darkGray);
		tablesLabel.setFont(font);
		gbc.gridx = 0;
		gbc.gridy++;
		gbc.gridwidth = 3;
		gbc.insets = new Insets(20, 30, 5, 30);
		contentPane.add(tablesLabel, gbc);

		tables = new JList<String>();
		tables.setFont(font);
		// tables.setBorder(new CompoundBorder(new LineBorder(darkGray, 1), new
		// EmptyBorder(0, 10, 0, 0)));
		tables.setBackground(Color.WHITE);

		ArrayList<Table> availableTables = dbTables;
		if (!dbReservations.isEmpty()) {
			for (Reservation r : dbReservations) {
				if (Math.abs(r.getTimestamp().getTimeInMillis() - reservation.getTimestamp().getTimeInMillis()) > r
						.getDuration() * 3600000) {
					for (Table t : r.getTables()) {
						availableTables.remove(t);
					}
				}
			}
		}
		DefaultListModel<String> listModel = new DefaultListModel<String>();
		if (availableTables.isEmpty()) {
			listModel.addElement("No tables");
			tables.setEnabled(false);
		} else {
			for (Table t : availableTables) {
				listModel.addElement(t.getName());
			}
		}
		tables.setModel(listModel);
		tables.setSelectionModel(new DefaultListSelectionModel() {
			@Override
			public void setSelectionInterval(int index0, int index1) {
				if (super.isSelectedIndex(index0)) {
					super.removeSelectionInterval(index0, index1);
				} else {
					super.addSelectionInterval(index0, index1);
				}
				fireValueChanged(index0, index1);
			}
		});

		JScrollPane tablesScrollPane = new JScrollPane(tables);
		tablesScrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		tablesScrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
		tablesScrollPane.setBackground(Color.WHITE);
		// tablesScrollPane.setPreferredSize(new Dimension(contentPane.getWidth() / 100
		// * 35, contentPane.getHeight() / 100 * 20));
		gbc.gridy++;
		gbc.ipady = 100;
		gbc.gridwidth = 3;
		gbc.weighty = 0;
		gbc.fill = GridBagConstraints.BOTH;
		gbc.gridheight = 1;
		gbc.insets = new Insets(5, 30, 20, 30);
		contentPane.add(tablesScrollPane, gbc);

		JLabel durationLabel = new JLabel("Duration:");
		durationLabel.setFont(font);
		gbc.gridy = 1;
		gbc.gridx = 3;
		gbc.ipady = 0;
		gbc.gridheight = 1;
		gbc.weighty = 0;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.insets = new Insets(20, 30, 5, 30);
		contentPane.add(durationLabel, gbc);

		SpinnerNumberModel numberModelDuration = new SpinnerNumberModel(2, 1, 10, 1);
		JSpinner durationSpinner = new JSpinner(numberModelDuration);
		NumberEditor editorDuration = new NumberEditor(durationSpinner);
		NumberFormatter formatterDuration = (NumberFormatter) editorDuration.getTextField().getFormatter();
		formatterDuration.setOverwriteMode(true);
		formatterDuration.setAllowsInvalid(false);
		durationSpinner.setEditor(editorDuration);
		durationSpinner.setFont(Fonts.FONT20.get());
		durationSpinner.setBorder(new CompoundBorder(new LineBorder(darkGray, 1), new EmptyBorder(0, 10, 0, 0)));
		durationSpinner.setValue(reservation.getDuration());
		gbc.insets = new Insets(5, 30, 25, 30);
		gbc.gridy++;
		contentPane.add(durationSpinner, gbc);

		JLabel phoneLabel = new JLabel("Customer:");
		phoneLabel.setForeground(darkGray);
		phoneLabel.setFont(font);
		gbc.gridy++;
		gbc.insets = new Insets(20, 30, 5, 30);
		contentPane.add(phoneLabel, gbc);

		JTextField phoneField = new JTextField();
		phoneField.setFont(font);
		phoneField.setBorder(new CompoundBorder(new LineBorder(darkGray, 1), new EmptyBorder(0, 10, 0, 0)));
		phoneField.setBackground(Color.WHITE);
		phoneField.setText(reservation.getCustomer().getPhone());
		gbc.gridy++;
		gbc.insets = new Insets(5, 30, 20, 30);
		contentPane.add(phoneField, gbc);

		JLabel noteLabel = new JLabel("Note:");
		noteLabel.setForeground(darkGray);
		noteLabel.setFont(font);
		gbc.gridy++;
		gbc.insets = new Insets(20, 30, 5, 30);
		contentPane.add(noteLabel, gbc);

		JTextField noteField = new JTextField();
		noteField.setFont(font);
		noteField.setBorder(new CompoundBorder(new LineBorder(darkGray, 1), new EmptyBorder(0, 10, 0, 0)));
		noteField.setBackground(Color.WHITE);
		noteField.setText(reservation.getNote());
		gbc.gridy++;
		gbc.insets = new Insets(5, 30, 20, 30);
		contentPane.add(noteField, gbc);

		ArrayList<Menu> dbMenus = new ArrayList<>();
		try {
			dbMenus = MenuConcreteDAO.getInstance().read();
		} catch (SQLException e) {
			e.printStackTrace();
		}

		JPanel menuPanel = new JPanel();
		menuPanel.setLayout(new GridBagLayout());
		menuPanel.setBackground(Color.WHITE);

		GridBagConstraints gbcM = new GridBagConstraints();
		gbcM.anchor = GridBagConstraints.NORTH;
		gbcM.fill = GridBagConstraints.HORIZONTAL;
		gbcM.gridy = 0;
		gbcM.gridx = 0;
		gbcM.insets = new Insets(10, 10, 20, 10);

		String[] menus = new String[dbMenus.size()];
		for (int x = 0; x < dbMenus.size(); x++) {
			menus[x] = dbMenus.get(x).getName();
		}

		ArrayList<JComboBox<String>> menuComboBoxes = new ArrayList<>();

		for (int i = 1; i < reservation.getGuests() + 1; i++) {
			JLabel label = new JLabel("Menu for person " + i + ":");
			label.setForeground(darkGray);
			label.setVerticalAlignment(SwingConstants.TOP);
			label.setFont(font);
			gbcM.gridwidth = GridBagConstraints.RELATIVE;
			gbcM.gridx = 0;
			menuPanel.add(label, gbcM);
			gbcM.gridwidth = GridBagConstraints.REMAINDER;

			JComboBox<String> menuBox = new JComboBox<String>(menus);
			menuBox.setFont(font);

			// menuBox.setBorder(new CompoundBorder(new LineBorder(darkGray, 1), new
			// EmptyBorder(0, 10, 0, 0)));
			menuBox.setBackground(Color.WHITE);
			menuBox.addItem("No menu");
			if (reservation.getMenus() != null) {
				menuBox.setSelectedItem(reservation.getMenus().get(i).getName());
			}
			menuComboBoxes.add(menuBox);
			gbcM.gridx = 1;
			menuPanel.add(menuBox, gbcM);
			gbcM.gridy++;
		}

		JLabel menusLabel = new JLabel("Menus:");
		menusLabel.setForeground(darkGray);
		menusLabel.setFont(font);
		gbc.gridy++;
		gbc.insets = new Insets(20, 30, 5, 30);
		contentPane.add(menusLabel, gbc);

		JScrollPane menuScrollPane = new JScrollPane(menuPanel);
		menuScrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		menuScrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
		menuScrollPane
				.setMinimumSize(new Dimension(contentPane.getWidth() / 100 * 35, contentPane.getHeight() / 100 * 20));
		// scrollPane.setBorder(null);
		menuScrollPane.setBackground(Color.WHITE);
		gbc.gridy++;
		gbc.ipady = 120;
		gbc.gridheight = GridBagConstraints.RELATIVE;
		gbc.insets = new Insets(5, 30, 20, 30);
		contentPane.add(menuScrollPane, gbc);
		menuPanel.revalidate();
		menuPanel.repaint();

		JButton cancelButton = new JButton("Cancel");
		cancelButton.setFont(font);
		cancelButton.setForeground(new Color(195, 70, 70));
		cancelButton.setBackground(new Color(252, 232, 232));
		cancelButton.setBorder(new LineBorder(new Color(220, 48, 48), 1));
		cancelButton.setPreferredSize(new Dimension((int) (getWidth() * 0.2), 40));
		cancelButton.setFocusable(false);
		cancelButton.addActionListener(e -> cancel());
		gbc.gridy = 10;
		gbc.ipady = 0;
		gbc.gridx = 0;
		gbc.weightx = 0;
		gbc.gridwidth = 3;
		gbc.gridheight = 1;
		gbc.anchor = GridBagConstraints.NORTH;
		gbc.insets = new Insets(20, 30, 20, 30);
		contentPane.add(cancelButton, gbc);

		JButton nextButton = new JButton("Change");
		nextButton.setFont(font);
		nextButton.setBackground(ProjectColors.BLUE.get());
		nextButton.setForeground(Color.white);
		nextButton.setPreferredSize(new Dimension((int) (getWidth() * 0.2), 40));
		nextButton.addActionListener(e -> {

//			if(guestsField.getText() == reservation.getGuests())
//			JOptionPane.showConfirmDialog(null,
//					"The number of guests cannot be zero or lower!\nPlease correct the information in the field!",
//					"Number of guests", JOptionPane.OK_OPTION, JOptionPane.WARNING_MESSAGE);
		});
		gbc.gridx = 3;
		gbc.gridwidth = 3;
		contentPane.add(nextButton, gbc);
	}

	private void cancel() {
		this.dispose();
	}
}
