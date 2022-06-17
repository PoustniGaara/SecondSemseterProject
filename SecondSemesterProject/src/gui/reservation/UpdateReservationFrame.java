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
import java.util.Date;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;
import javax.swing.ScrollPaneLayout;
import javax.swing.SpinnerDateModel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.text.DateFormatter;

import controller.CustomerController;
import controller.ReservationController;
import database.MenuConcreteDAO;
import model.Menu;
import model.Reservation;
import model.Table;

@SuppressWarnings("serial")
public class UpdateReservationFrame extends JFrame {

	private ReservationController reservationController;
	private CustomerController customerController;
	private Reservation reservation;

	private JPanel contentPane;
	private JTextField textField;

	private final Font font = new Font("Segoe UI Semibold", Font.PLAIN, 20);
	private final Font italics = new Font("Segoe UI Semibold", Font.ITALIC, 20);
	private final Font placeholderFont = new Font("Segoe UI", Font.ITALIC, 18);

	private final Color darkGray = new Color(18, 18, 18);
	private final Color lightGray = new Color(89, 89, 89);

	public static void open(Reservation reservation) {
		SwingUtilities.invokeLater((Runnable) new Runnable() {
			public void run() {
				UpdateReservationFrame frame = new UpdateReservationFrame(reservation);
				frame.setVisible(true);
			}
		});
	}

	public UpdateReservationFrame(Reservation reservation) {
		this.reservation = reservation;
		reservationController = new ReservationController();
		customerController = new CustomerController();

		setTitle("Update Reservation");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(0, 0, 1000, 900);
		setUndecorated(false);
		setResizable(false);

		contentPane = new JPanel();
		setContentPane(contentPane);
		GridBagLayout gbl = new GridBagLayout();
		gbl.columnWidths = new int[] { 150, 150, 150, 150, 150, 150 };
		contentPane.setLayout(gbl);

		GridBagConstraints gbc = new GridBagConstraints();
		gbc.insets = new Insets(20, 20, 40, 20);
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.ipadx = 0;
		gbc.ipady = 0;
		gbc.gridwidth = GridBagConstraints.REMAINDER;
		gbc.anchor = GridBagConstraints.CENTER;
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
		gbc.insets = new Insets(25, 30, 10, 30);
		contentPane.add(guestsLabel, gbc);

		JTextField guestsField = new JTextField();
		guestsField.setFont(font);
		guestsField.setBorder(new CompoundBorder(new LineBorder(darkGray, 1), new EmptyBorder(0, 10, 0, 0)));
		guestsField.setBackground(Color.WHITE);
		guestsField.setText(reservation.getGuests() + "");
		gbc.gridy++;
		gbc.insets = new Insets(10, 30, 25, 30);
		contentPane.add(guestsField, gbc);

		JLabel dateLabel = new JLabel("Date:");
		dateLabel.setForeground(darkGray);
		dateLabel.setFont(font);
		gbc.gridy++;
		gbc.insets = new Insets(25, 30, 10, 30);
		contentPane.add(dateLabel, gbc);

		DateFormat format = new SimpleDateFormat("dd.MM.yyyy");
		DateFormatter df = new DateFormatter(format);
		JFormattedTextField dateField = new JFormattedTextField(df);
		dateField.setValue(reservation.getTimestamp().getTime());
		dateField.setFont(font);
		dateField.setBorder(new CompoundBorder(new LineBorder(darkGray, 1), new EmptyBorder(0, 10, 0, 0)));
		gbc.gridy++;
		gbc.insets = new Insets(10, 30, 25, 30);
		contentPane.add(dateField, gbc);

		JLabel timeLabel = new JLabel("Time:");
		timeLabel.setForeground(darkGray);
		timeLabel.setFont(font);
		gbc.gridy++;
		gbc.insets = new Insets(25, 30, 10, 30);
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
		gbc.insets = new Insets(10, 30, 25, 30);
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
		gbc.insets = new Insets(25, 30, 10, 30);
		contentPane.add(tablesLabel, gbc);

		JComboBox<String> tables = new JComboBox<String>();
		tables.setFont(font);
		tables.setBorder(new CompoundBorder(new LineBorder(darkGray, 1), new EmptyBorder(0, 10, 0, 0)));
		tables.setBackground(Color.WHITE);
		for (Table t : reservation.getTables()) {
			tables.addItem(t.getName());
		}
		gbc.gridy++;
		gbc.insets = new Insets(10, 30, 25, 30);
		contentPane.add(tables, gbc);

		JLabel phoneLabel = new JLabel("Customer:");
		phoneLabel.setForeground(darkGray);
		phoneLabel.setFont(font);
		gbc.gridy = 1;
		gbc.gridx = 3;
		gbc.insets = new Insets(25, 30, 10, 30);
		contentPane.add(phoneLabel, gbc);

		JTextField phoneField = new JTextField();
		phoneField.setFont(font);
		phoneField.setBorder(new CompoundBorder(new LineBorder(darkGray, 1), new EmptyBorder(0, 10, 0, 0)));
		phoneField.setBackground(Color.WHITE);
		phoneField.setText(reservation.getCustomer().getPhone());
		gbc.gridy++;
		gbc.insets = new Insets(10, 30, 25, 30);
		contentPane.add(phoneField, gbc);

		JLabel noteLabel = new JLabel("Note:");
		noteLabel.setForeground(darkGray);
		noteLabel.setFont(font);
		gbc.gridy++;
		gbc.insets = new Insets(25, 30, 10, 30);
		contentPane.add(noteLabel, gbc);

		JTextField noteField = new JTextField();
		noteField.setFont(font);
		noteField.setBorder(new CompoundBorder(new LineBorder(darkGray, 1), new EmptyBorder(0, 10, 0, 0)));
		noteField.setBackground(Color.WHITE);
		noteField.setText(reservation.getNote());
		gbc.gridy++;
		gbc.insets = new Insets(10, 30, 40, 30);
		contentPane.add(noteField, gbc);

		ArrayList<Menu> dbMenus = new ArrayList<>();
		try {
			dbMenus = MenuConcreteDAO.getInstance().read();
		} catch (SQLException e) {
			e.printStackTrace();
		}

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
		scrollPane.setMinimumSize(new Dimension(contentPane.getWidth() / 100 * 40, contentPane.getHeight() / 100 * 30));
		scrollPane.setBorder(null);
		scrollPane.setLayout(new ScrollPaneLayout());
		scrollPane.setBackground(Color.WHITE);
		gbc.gridy++;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.gridwidth = GridBagConstraints.REMAINDER;
		gbc.gridheight = GridBagConstraints.RELATIVE;
		gbc.weightx = 0;
		gbc.weighty = 0;
		contentPane.add(scrollPane, gbc);

		JPanel menuPanel = new JPanel();
		menuPanel.setLayout(new GridBagLayout());
		menuPanel.setBackground(Color.WHITE);
		scrollPane.setViewportView(menuPanel);

		GridBagConstraints gbcM = new GridBagConstraints();
		gbcM.anchor = GridBagConstraints.NORTH;
		gbcM.fill = GridBagConstraints.HORIZONTAL;
		gbcM.gridy = 0;
		gbcM.gridx = 0;
		gbcM.insets = new Insets(10, 10, 10, 10);

		String[] menus = new String[dbMenus.size()];
		for (int x = 0; x < dbMenus.size(); x++) {
			menus[x] = dbMenus.get(x).getName();
		}

		ArrayList<JComboBox<String>> menuComboBoxes = new ArrayList<>();

		for (int i = 1; i <= reservation.getGuests(); i++) {
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
			menuBox.setBorder(new CompoundBorder(new LineBorder(darkGray, 1), new EmptyBorder(0, 10, 0, 0)));
			menuBox.setBackground(Color.WHITE);
			menuBox.addItem("No menu");
			if (reservation.getMenus() != null) {
				menuBox.setSelectedItem(reservation.getMenus().get(i).getName());
			}
			menuComboBoxes.add(menuBox);

			gbcM.insets = new Insets(10, 10, 20, 10);
			gbcM.gridx = 1;
			menuPanel.add(menuBox, gbcM);
			gbcM.gridy++;
			gbcM.insets = new Insets(20, 10, 10, 10);
		}

		JButton cancelButton = new JButton("Cancel");
		cancelButton.setFont(font);
		cancelButton.setBackground(new Color(242, 233, 228));
		cancelButton.setFocusable(false);
		cancelButton.addActionListener(e -> cancel());
		gbc.gridy = 9;
		gbc.gridx = 0;
		gbc.weightx = 0;
		gbc.gridwidth = 3;
		gbc.anchor = GridBagConstraints.NORTH;
		gbc.insets = new Insets(40, 30, 25, 30);
		contentPane.add(cancelButton, gbc);

		JButton nextButton = new JButton("Update");
		nextButton.setFont(font);
		nextButton.setBackground(new Color(242, 233, 228));
		nextButton.setFocusable(false);
		nextButton.addActionListener(e -> JOptionPane.showConfirmDialog(null,
				"The number of guests cannot be zero or lower!\nPlease correct the information in the field!",
				"Number of guests", JOptionPane.OK_OPTION, JOptionPane.WARNING_MESSAGE));
		gbc.gridx = 3;
		gbc.gridwidth = 3;
		contentPane.add(nextButton, gbc);
	}

	private void cancel() {
		this.dispose();
	}
}
