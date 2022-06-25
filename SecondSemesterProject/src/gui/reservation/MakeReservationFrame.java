package gui.reservation;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;
import javax.swing.ScrollPaneLayout;
import javax.swing.SpinnerDateModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingConstants;
import javax.swing.JSpinner.NumberEditor;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.MatteBorder;
import javax.swing.text.DateFormatter;
import javax.swing.text.NumberFormatter;

import controller.CustomerController;
import controller.ReservationController;
import database.MenuConcreteDAO;
import gui.tools.FancyButtonOneClick;
import gui.tools.Fonts;
import gui.tools.ProjectColors;
import gui.tools.Validator;
import model.Customer;
import model.Menu;
import model.Reservation;
import model.Table;

public class MakeReservationFrame extends JFrame {

	//matej
	private JPanel contentPane;
	private CardLayout cardLayout;
	private JPanel firstCard;
	private JPanel secondCard;
	private JPanel thirdCard;
	private JPanel fourthCard;
	private JPanel finalCard;
	private int currentCard;

	private ReservationController reservationController;
	private CustomerController customerController;

	private JTextField guestsField;
	private JFormattedTextField dateField;
	private JSpinner spinnerH;
	private JSpinner spinnerM;
	private JTextField phoneField;
	private JTextField noteField;
	private JComboBox<String> tables;

	private static ArrayList<Table> dbTables;
	private static ArrayList<Reservation> dbReservations;
	private static ArrayList<Menu> dbMenus;
	private static boolean resourcesLoaded = true; // was false

	private Calendar reservationTimeDate;
	private int guests, duration;
	private String phone;
	private Table reservedTable;
	private String note;
	private Customer customer;
	private ArrayList<Menu> reservedMenus;
	
	private final Font font = new Font("Segoe UI Semibold", Font.PLAIN, 20);
	private final Font italics = new Font("Segoe UI Semibold", Font.ITALIC, 20);
	private final Font placeholderFont = new Font("Segoe UI", Font.ITALIC, 18);

	private final Color darkGray = new Color(18, 18, 18);
	private final Color lightGray = new Color(89, 89, 89);
	
	public MakeReservationFrame(ArrayList<Table> tableList, Calendar calendar, int guestsNo, int duration) {
		
		reservationController = new ReservationController();
		customerController = new CustomerController();
		currentCard = 1;

		setTitle("New Reservation");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(0, 0, 600, 900);
		setUndecorated(false);
		setResizable(false);

		contentPane = new JPanel();
		setContentPane(contentPane);

		cardLayout = new CardLayout();
		contentPane.setLayout(cardLayout);
		
		try {
			dbMenus = MenuConcreteDAO.getInstance().read();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}


		// FIRST CARD
		firstCard = new JPanel();
		createFirstCard(calendar, guestsNo, tableList, duration);
		
		// SECOND CARD
		secondCard = new JPanel();
		thirdCard = new JPanel();
		fourthCard = new JPanel();
		finalCard = new JPanel();

		setVisible(true);
		contentPane.add(firstCard, "1");
		contentPane.add(secondCard, "2");
		contentPane.add(thirdCard, "3");
		contentPane.add(fourthCard, "4");
		contentPane.add(finalCard, "5");
		cardLayout.first(contentPane);
		

		
	}// end of constructor
	
	private void createFirstCard(Calendar calendar, int guestsNo,ArrayList<Table> tableList, int duration) {
		firstCard.setLayout(new GridBagLayout());
		firstCard.setBackground(Color.WHITE);

		GridBagConstraints gbc = new GridBagConstraints();
		gbc.insets = new Insets(20, 20, 60, 20);
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.ipadx = 0;
		gbc.ipady = 0;
		gbc.gridwidth = GridBagConstraints.REMAINDER;
		gbc.anchor = GridBagConstraints.CENTER;
		gbc.fill = GridBagConstraints.HORIZONTAL;

		JLabel titleLabel = new JLabel("Creating new reservation", JLabel.CENTER);
		titleLabel.setFont(new Font("Segoe UI Semibold", Font.BOLD, 28));
		titleLabel.setForeground(darkGray);
		firstCard.add(titleLabel, gbc);

		JLabel guestsLabel = new JLabel("Number of guests:");
		guestsLabel.setForeground(darkGray);
		guestsLabel.setFont(font);
		gbc.gridy++;
		gbc.gridheight = 1;
		gbc.anchor = GridBagConstraints.WEST;
		gbc.insets = new Insets(25, 30, 10, 30);
		firstCard.add(guestsLabel, gbc);

		guestsField = new JTextField();
		guestsField.setFont(font);
		guestsField.setText(String.valueOf(guestsNo));
		guestsField.setBorder(new CompoundBorder(new LineBorder(darkGray, 1), new EmptyBorder(0, 10, 0, 0)));
		guestsField.setBackground(Color.WHITE);
		gbc.gridy++;
		gbc.insets = new Insets(10, 30, 25, 30);
		firstCard.add(guestsField, gbc);

		JLabel dateLabel = new JLabel("Date:");
		dateLabel.setForeground(darkGray);
		dateLabel.setFont(font);
		gbc.gridy++;
		gbc.insets = new Insets(25, 30, 10, 30);
		firstCard.add(dateLabel, gbc);

		DateFormat format = new SimpleDateFormat("dd.MM.yyyy");
		DateFormatter df = new DateFormatter(format);
		dateField = new JFormattedTextField(df);
		dateField.setValue(new Date());
		dateField.setFont(font);
		dateField.setBorder(new CompoundBorder(new LineBorder(darkGray, 1), new EmptyBorder(0, 10, 0, 0)));
		gbc.gridy++;
		gbc.insets = new Insets(10, 30, 25, 30);
		firstCard.add(dateField, gbc);
		
		setDateTxtFieldTxt(calendar);

		JLabel timeLabel = new JLabel("Time:");
		timeLabel.setForeground(darkGray);
		timeLabel.setFont(font);
		gbc.gridy++;
		gbc.insets = new Insets(25, 30, 10, 30);
		firstCard.add(timeLabel, gbc);
		

		SpinnerNumberModel numberModelHours = new SpinnerNumberModel(17,0,23,1);
		spinnerH = new JSpinner(numberModelHours);
		JSpinner.NumberEditor editorHours = new JSpinner.NumberEditor(spinnerH);
		NumberFormatter formatterHours = (NumberFormatter) editorHours.getTextField().getFormatter();
		formatterHours.setAllowsInvalid(false);
		formatterHours.setOverwriteMode(true);
		numberModelHours.setValue(calendar.get(Calendar.HOUR_OF_DAY));
		spinnerH.setEditor(editorHours);
		spinnerH.setFont(font);
		spinnerH.setBorder(new CompoundBorder(new LineBorder(darkGray, 1), new EmptyBorder(0, 10, 0, 0)));
		gbc.gridy++;
		gbc.gridwidth = 1;
		gbc.anchor = GridBagConstraints.NORTH;
		gbc.insets = new Insets(10, 30, 25, 30);
		firstCard.add(spinnerH, gbc);

		JLabel separator = new JLabel(":", JLabel.CENTER);
		separator.setForeground(darkGray);
		separator.setFont(font);
		gbc.gridx++;
		firstCard.add(separator, gbc);
		
		SpinnerNumberModel numberModelMinutes = new SpinnerNumberModel(0,0,50,10);
		spinnerM = new JSpinner(numberModelMinutes);
		JSpinner.NumberEditor editorMinutes = new JSpinner.NumberEditor(spinnerM);
		NumberFormatter formatterMinutes = (NumberFormatter) editorMinutes.getTextField().getFormatter();
		spinnerM.setValue(calendar.get(Calendar.MINUTE));
		spinnerM.setEditor(editorMinutes);
		formatterMinutes.setAllowsInvalid(false);
		formatterMinutes.setOverwriteMode(true);
		spinnerM.setFont(font);
		spinnerM.setBorder(new CompoundBorder(new LineBorder(darkGray, 1), new EmptyBorder(0, 10, 0, 0)));
		gbc.gridx++;
		firstCard.add(spinnerM, gbc);
		
		SpinnerNumberModel numberModelDuration = new SpinnerNumberModel(2, 1, 10, 1);
		JSpinner durationSpinner = new JSpinner(numberModelDuration);
		NumberEditor editorDuration = new NumberEditor(durationSpinner);
		NumberFormatter formatterDuration = (NumberFormatter) editorDuration.getTextField().getFormatter();
		formatterDuration.setOverwriteMode(true);
		formatterDuration.setAllowsInvalid(false);
		durationSpinner.setEditor(editorDuration);
		durationSpinner.setFont(Fonts.FONT20.get());
		durationSpinner.setBorder(new CompoundBorder(new LineBorder(darkGray, 1), new EmptyBorder(0, 10, 0, 0)));
		gbc.insets = new Insets(5, 30, 25, 30);
		gbc.gridy++;
		firstCard.add(durationSpinner, gbc);
		
		duration = (int) durationSpinner.getValue();

		JButton cancelButton = new JButton("Cancel");
		cancelButton.setFont(font);
		cancelButton.setBackground(new Color(242, 233, 228));
		cancelButton.setFocusable(false);
		cancelButton.addActionListener(e -> cancel());
		gbc.gridy++;
		gbc.gridx = 0;
		gbc.weightx = 1;
		gbc.anchor = GridBagConstraints.SOUTH;
		gbc.insets = new Insets(80, 30, 25, 30);
		firstCard.add(cancelButton, gbc);

		JButton nextButton = new JButton("Next >");
		nextButton.setFont(font);
		nextButton.setBackground(new Color(242, 233, 228));
		nextButton.setFocusable(false);
		nextButton.addActionListener(e -> {
			if (guestsField.getText().isEmpty() || guestsField.getText().isBlank()) {
				JOptionPane.showConfirmDialog(null,
						"The number of guests field must be filled!\nPlease fill the field in order to continue!",
						"Guests field", JOptionPane.OK_OPTION, JOptionPane.WARNING_MESSAGE);
				return;
			}
			guests = Integer.parseInt(guestsField.getText());
			if (guests <= 0) {
				JOptionPane.showConfirmDialog(null,
						"The number of guests cannot be zero or lower!\nPlease correct the information in the field!",
						"Number of guests", JOptionPane.OK_OPTION, JOptionPane.WARNING_MESSAGE);
				return;
			}

			Calendar date = Calendar.getInstance();
			date.setTime((Date) dateField.getValue());
			int year = date.get(Calendar.YEAR);
			int month = date.get(Calendar.MONTH) + 1;
			int day = date.get(Calendar.DAY_OF_MONTH);
//			date.setTime((Date) spinnerH.getValue());
			int hour = Integer.valueOf(spinnerH.getValue().toString());
//			date.setTime((Date) spinnerM.getValue());
			int minute = Integer.valueOf(spinnerM.getValue().toString());

			reservationTimeDate = (Calendar) Calendar.getInstance();
			reservationTimeDate.set(year, month, day, hour, minute, 0);
			reservationTimeDate.set(Calendar.MILLISECOND, 0);

			if (reservationTimeDate.before(Calendar.getInstance())) {
				JOptionPane.showConfirmDialog(null,
						"The date of reservation is in the past!\nPlease, select correct date, we don't have a time machine ;)",
						"Wrong date", JOptionPane.OK_OPTION, JOptionPane.WARNING_MESSAGE);
				return;
			}

			if (resourcesLoaded) {
				createSecondCard(tableList);
				next();
			} else {
				JOptionPane.showConfirmDialog(null,
						"The resources needed for cration of reservtaion hasn't been loaded!\nPlease, wait a bit and try again.",
						"Resources not loaded", JOptionPane.OK_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE);
			}
		});
		gbc.gridx = 2;
		firstCard.add(nextButton, gbc);
	}
	
	private void createSecondCard(ArrayList<Table> tableList) {
		secondCard.setLayout(new GridBagLayout());
		secondCard.setBackground(Color.WHITE);

		GridBagConstraints gbc = new GridBagConstraints();
		gbc.insets = new Insets(20, 20, 40, 20);
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.ipadx = 0;
		gbc.ipady = 0;
		gbc.gridwidth = GridBagConstraints.REMAINDER;
		gbc.anchor = GridBagConstraints.CENTER;
		gbc.fill = GridBagConstraints.HORIZONTAL;

		JLabel titleLabel = new JLabel("Creating new reservation", JLabel.CENTER);
		titleLabel.setFont(new Font("Segoe UI Semibold", Font.BOLD, 28));
		titleLabel.setForeground(darkGray);
		secondCard.add(titleLabel, gbc);

		JLabel reservationLabel = new JLabel("For " + guests + " guest" + (guests > 1 ? "s" : "") + " on "
				+ reservationTimeDate.get(Calendar.DATE) + ". " + reservationTimeDate.get(Calendar.MONTH) + ". "
				+ reservationTimeDate.get(Calendar.YEAR) + " at " + reservationTimeDate.get(Calendar.HOUR_OF_DAY) + ":"
				+ (reservationTimeDate.get(Calendar.MINUTE) < 10 ? "0" + reservationTimeDate.get(Calendar.MINUTE)
						: reservationTimeDate.get(Calendar.MINUTE)));
		reservationLabel.setForeground(darkGray);
		reservationLabel.setFont(italics);
		reservationLabel.setBorder(new MatteBorder(0, 0, 1, 0, darkGray));
		gbc.gridy++;
		gbc.anchor = GridBagConstraints.WEST;
		gbc.insets = new Insets(20, 30, 25, 30);
		secondCard.add(reservationLabel, gbc);

		JLabel tablesLabel = new JLabel("Table(s):");
		tablesLabel.setForeground(darkGray);
		tablesLabel.setFont(font);
		gbc.gridy++;
		gbc.insets = new Insets(25, 30, 10, 30);
		secondCard.add(tablesLabel, gbc);

		tables = new JComboBox<String>();
		tables.setFont(font);
		tables.setBorder(new CompoundBorder(new LineBorder(darkGray, 1), new EmptyBorder(0, 10, 0, 0)));
		tables.setBackground(Color.WHITE);
		gbc.gridy++;
		gbc.insets = new Insets(10, 30, 25, 30);
		secondCard.add(tables, gbc);
		populateTableCB(tableList);

		JLabel phoneLabel = new JLabel("Customer phone:");
		phoneLabel.setForeground(darkGray);
		phoneLabel.setFont(font);
		gbc.gridy++;
		gbc.insets = new Insets(25, 30, 10, 30);
		secondCard.add(phoneLabel, gbc);

		phoneField = new JTextField();
		phoneField.setFont(font);
		phoneField.setBorder(new CompoundBorder(new LineBorder(darkGray, 1), new EmptyBorder(0, 10, 0, 0)));
		phoneField.setBackground(Color.WHITE);
		gbc.gridy++;
		gbc.insets = new Insets(10, 30, 25, 30);
		secondCard.add(phoneField, gbc);

		JLabel noteLabel = new JLabel("Note:");
		noteLabel.setForeground(darkGray);
		noteLabel.setFont(font);
		gbc.gridy++;
		gbc.insets = new Insets(25, 30, 10, 30);
		secondCard.add(noteLabel, gbc);

		noteField = new JTextField();
		noteField.setFont(font);
		noteField.setBorder(new CompoundBorder(new LineBorder(darkGray, 1), new EmptyBorder(0, 10, 0, 0)));
		noteField.setBackground(Color.WHITE);
		gbc.gridy++;
		gbc.insets = new Insets(10, 30, 40, 30);
		secondCard.add(noteField, gbc);

		JButton cancelButton = new JButton("Cancel");
		cancelButton.setFont(font);
		cancelButton.setBackground(new Color(242, 233, 228));
		cancelButton.addActionListener(e -> cancel());
		gbc.gridy++;
		gbc.anchor = GridBagConstraints.SOUTH;
		gbc.gridwidth = 1;
		gbc.insets = new Insets(40, 30, 20, 20);
		secondCard.add(cancelButton, gbc);

		JButton prevButton = new JButton("< Back");
		prevButton.setFont(font);
		prevButton.setBackground(new Color(242, 233, 228));
		// prevButton.addActionListener();
		gbc.gridx = 1;
		gbc.insets = new Insets(40, 20, 20, 20);
		secondCard.add(prevButton, gbc);

		JButton nextButton = new JButton("Next >");
		nextButton.setFont(font);
		nextButton.setBackground(new Color(242, 233, 228));
		nextButton.addActionListener(e -> {
			for (Table t : tableList) { // was dbtTables
				if (tables.getSelectedItem().equals(t.getName())) {
					reservedTable = t;
				}
			}

			if (reservedTable == null) {
				JOptionPane.showConfirmDialog(null,
						"No table was selected!\nPlease, try selecting a table from the drop down menu!",
						"Table selection", JOptionPane.OK_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE);
				return;
			}

			String input = phoneField.getText();
			if (input != null && !input.isEmpty() && !input.isBlank()) {
				try {
					Long.parseLong(input);
				} catch (Exception ex) {
					JOptionPane.showConfirmDialog(null, "The input value in the phone number field is incorect!",
							"Customer's phone number", JOptionPane.OK_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE);
					return;
				}
				phone = input;
			} else {
				JOptionPane.showConfirmDialog(null, "The phone number field must be filled!", "Phone number",
						JOptionPane.OK_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE);
				return;
			}
			note = noteField.getText();

			ArrayList<Table> rt = new ArrayList<>();
			rt.add(reservedTable);
			reservationController.startReservation(reservationTimeDate, rt);

			createThirdCard();
			next();
		});
		gbc.gridx = 2;
		gbc.insets = new Insets(40, 20, 20, 30);
		secondCard.add(nextButton, gbc);
	}
	
	private void createThirdCard() {
		thirdCard.setLayout(new GridBagLayout());
		thirdCard.setBackground(Color.WHITE);

		GridBagConstraints gbc = new GridBagConstraints();
		gbc.insets = new Insets(20, 20, 30, 20);
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.ipadx = 0;
		gbc.ipady = 0;
		gbc.gridwidth = GridBagConstraints.REMAINDER;
		gbc.anchor = GridBagConstraints.CENTER;
		gbc.fill = GridBagConstraints.HORIZONTAL;

		JLabel titleLabel = new JLabel("Creating new reservation", JLabel.CENTER);
		titleLabel.setFont(new Font("Segoe UI Semibold", Font.BOLD, 28));
		titleLabel.setForeground(darkGray);
		thirdCard.add(titleLabel, gbc);

		JLabel reservationLabel = new JLabel("For " + guests + " guest" + (guests > 1 ? "s" : "") + " on "
				+ reservationTimeDate.get(Calendar.DATE) + ". " + reservationTimeDate.get(Calendar.MONTH) + ". "
				+ reservationTimeDate.get(Calendar.YEAR) + " at " + reservationTimeDate.get(Calendar.HOUR_OF_DAY) + ":"
				+ (reservationTimeDate.get(Calendar.MINUTE) < 10 ? "0" + reservationTimeDate.get(Calendar.MINUTE)
						: reservationTimeDate.get(Calendar.MINUTE)));
		reservationLabel.setForeground(darkGray);
		reservationLabel.setFont(italics);
		reservationLabel.setBorder(new MatteBorder(0, 0, 1, 0, darkGray));
		gbc.gridy++;
		gbc.anchor = GridBagConstraints.WEST;
		gbc.insets = new Insets(20, 30, 20, 30);
		thirdCard.add(reservationLabel, gbc);

		try {
			customer = reservationController.checkCustomer(phone);
		} catch (SQLException e1) {
		}
		boolean isCustomer = customer != null;

		JLabel phoneLabel = new JLabel("Customer phone:");
		phoneLabel.setForeground(Color.BLACK);
		phoneLabel.setFont(font);
		gbc.gridy++;
		gbc.insets = new Insets(20, 30, 10, 30);
		thirdCard.add(phoneLabel, gbc);

		phoneField = new JTextField();
		phoneField.setFont(font);
		phoneField.setBorder(new CompoundBorder(new LineBorder(darkGray, 1), new EmptyBorder(0, 10, 0, 0)));
		phoneField.setBackground(Color.WHITE);
		phoneField.setText(phone);
		if (isCustomer) {
			phoneField.setEditable(false);
		}
		gbc.gridy++;
		gbc.insets = new Insets(10, 30, 20, 30);
		thirdCard.add(phoneField, gbc);

		JLabel nameLabel = new JLabel("Name:");
		nameLabel.setForeground(darkGray);
		nameLabel.setFont(font);
		gbc.gridy++;
		gbc.insets = new Insets(20, 30, 10, 30);
		thirdCard.add(nameLabel, gbc);

		JTextField nameField = new JTextField();
		nameField.setFont(font);
		nameField.setBorder(new CompoundBorder(new LineBorder(darkGray, 1), new EmptyBorder(0, 10, 0, 0)));
		nameField.setBackground(Color.WHITE);
		if (isCustomer) {
			nameField.setEditable(false);
			nameField.setForeground(lightGray);
			nameField.setText(customer.getName());
		}
		gbc.gridy++;
		gbc.insets = new Insets(10, 30, 20, 30);
		thirdCard.add(nameField, gbc);

		JLabel surnameLabel = new JLabel("Surname:");
		surnameLabel.setForeground(darkGray);
		surnameLabel.setFont(font);
		gbc.gridy++;
		gbc.insets = new Insets(20, 30, 10, 30);
		thirdCard.add(surnameLabel, gbc);

		JTextField surnameField = new JTextField();
		surnameField.setFont(font);
		surnameField.setBorder(new CompoundBorder(new LineBorder(darkGray, 1), new EmptyBorder(0, 10, 0, 0)));
		surnameField.setBackground(Color.WHITE);
		if (isCustomer) {
			surnameField.setEditable(false);
			surnameField.setForeground(lightGray);
			surnameField.setText(customer.getSurname());
		}
		gbc.gridy++;
		gbc.insets = new Insets(10, 30, 20, 30);
		thirdCard.add(surnameField, gbc);

		JLabel addressLabel = new JLabel("Address:");
		addressLabel.setForeground(darkGray);
		addressLabel.setFont(font);
		gbc.gridy++;
		gbc.insets = new Insets(20, 30, 10, 30);
		thirdCard.add(addressLabel, gbc);

		JTextField streetField = new JTextField("Street");
		streetField.setFont(placeholderFont);
		streetField.setBorder(new CompoundBorder(new LineBorder(darkGray, 1), new EmptyBorder(0, 10, 0, 0)));
		streetField.setBackground(Color.WHITE);
		streetField.setForeground(lightGray);
		if (isCustomer) {
			streetField.setEditable(false);
			streetField.setText(customer.getStreet());
		} else {
			addPlaceholder(streetField, "Street");
		}
		gbc.gridy++;
		gbc.gridwidth = 2;
		gbc.weightx = 1;
		gbc.insets = new Insets(10, 30, 20, 15);
		thirdCard.add(streetField, gbc);

		JTextField streetNoField = new JTextField("Street number");
		streetNoField.setFont(placeholderFont);
		streetNoField.setForeground(lightGray);
		streetNoField.setBorder(new CompoundBorder(new LineBorder(darkGray, 1), new EmptyBorder(0, 10, 0, 0)));
		streetNoField.setBackground(Color.WHITE);
		if (isCustomer) {
			streetNoField.setEditable(false);
			streetNoField.setText(customer.getStreetNumber());
		} else {
			addPlaceholder(streetNoField, "Street number");
		}
		gbc.gridx = 2;
		gbc.insets = new Insets(10, 15, 20, 30);
		gbc.gridwidth = 1;
		thirdCard.add(streetNoField, gbc);

		JTextField townField = new JTextField("Town");
		townField.setFont(placeholderFont);
		townField.setForeground(lightGray);
		townField.setBorder(new CompoundBorder(new LineBorder(darkGray, 1), new EmptyBorder(0, 10, 0, 0)));
		townField.setBackground(Color.WHITE);
		if (isCustomer) {
			townField.setEditable(false);
			townField.setText(customer.getTown());
		} else {
			addPlaceholder(townField, "Town");
		}
		gbc.gridx = 0;
		gbc.gridy++;
		gbc.gridwidth = 2;
		gbc.insets = new Insets(10, 30, 20, 15);
		thirdCard.add(townField, gbc);

		JTextField zipField = new JTextField("Zip code");
		zipField.setFont(placeholderFont);
		zipField.setForeground(lightGray);
		zipField.setBorder(new CompoundBorder(new LineBorder(darkGray, 1), new EmptyBorder(0, 10, 0, 0)));
		zipField.setBackground(Color.WHITE);
		if (isCustomer) {
			zipField.setEditable(false);
			zipField.setText(customer.getZipCode());
		} else {
			addPlaceholder(zipField, "Zip code");
		}
		gbc.gridx = 2;
		gbc.gridwidth = 1;
		gbc.insets = new Insets(10, 15, 20, 30);
		thirdCard.add(zipField, gbc);

		JButton cancelButton = new JButton("Cancel");
		cancelButton.setFont(font);
		cancelButton.setBackground(new Color(242, 233, 228));
		cancelButton.addActionListener(e -> cancel());
		gbc.gridy++;
		gbc.gridx = 0;
		gbc.anchor = GridBagConstraints.SOUTH;
		gbc.gridwidth = 1;
		gbc.insets = new Insets(30, 30, 20, 20);
		thirdCard.add(cancelButton, gbc);

		JButton prevButton = new JButton("< Back");
		prevButton.setFont(font);
		prevButton.setBackground(new Color(242, 233, 228));
		// prevButton.addActionListener();
		gbc.gridx = 1;
		gbc.insets = new Insets(30, 20, 20, 20);
		thirdCard.add(prevButton, gbc);

		JButton nextButton = new JButton("Next >");
		nextButton.setFont(font);
		nextButton.setBackground(new Color(242, 233, 228));
		nextButton.addActionListener(e -> {

			for (Component c : thirdCard.getComponents()) {
				if (c instanceof JTextField) {
					if (!Validator.isFilled((JTextField) c)) {
						((JTextField) c).setBorder(new LineBorder(Color.RED));
						JOptionPane.showConfirmDialog(null, "The " + c.getName() + " field must be filled!",
								"Empty field", JOptionPane.OK_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE);
						return;
					}
				}
			}

			if (!Validator.isNumber(phoneField) && !Validator.isNumber(streetNoField)
					&& !Validator.isNumber(zipField)) {
				JOptionPane.showConfirmDialog(null, "The input value in number input fields is incorect!",
						"Wrong number input", JOptionPane.OK_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE);
				return;
			}

			customer = new Customer(nameField.getText(), surnameField.getText(), phone, "", townField.getText(),
					zipField.getText(), streetField.getText(), streetNoField.getText());
			createFourthCard();
			next();
		});
		gbc.gridx = 2;
		gbc.insets = new Insets(30, 20, 20, 30);
		thirdCard.add(nextButton, gbc);
		
	}
	
	private void createFourthCard() {
		fourthCard.setLayout(new GridBagLayout());
		fourthCard.setBackground(Color.WHITE);
		reservedMenus = new ArrayList<>();

		GridBagConstraints gbc = new GridBagConstraints();
		gbc.insets = new Insets(20, 20, 30, 20);
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.ipadx = 0;
		gbc.ipady = 0;
		gbc.gridwidth = GridBagConstraints.REMAINDER;
		gbc.anchor = GridBagConstraints.CENTER;
		gbc.fill = GridBagConstraints.HORIZONTAL;

		JLabel titleLabel = new JLabel("Creating new reservation", JLabel.CENTER);
		titleLabel.setFont(new Font("Segoe UI Semibold", Font.BOLD, 28));
		titleLabel.setForeground(darkGray);
		fourthCard.add(titleLabel, gbc);

		JLabel reservationLabel = new JLabel("For " + guests + " guest" + (guests > 1 ? "s" : "") + " on "
				+ reservationTimeDate.get(Calendar.DATE) + ". " + reservationTimeDate.get(Calendar.MONTH) + ". "
				+ reservationTimeDate.get(Calendar.YEAR) + " at " + reservationTimeDate.get(Calendar.HOUR_OF_DAY) + ":"
				+ (reservationTimeDate.get(Calendar.MINUTE) < 10 ? "0" + reservationTimeDate.get(Calendar.MINUTE)
						: reservationTimeDate.get(Calendar.MINUTE)));
		reservationLabel.setForeground(darkGray);
		reservationLabel.setFont(italics);
		reservationLabel.setBorder(new MatteBorder(0, 0, 1, 0, darkGray));
		gbc.gridy++;
		gbc.anchor = GridBagConstraints.WEST;
		gbc.insets = new Insets(20, 30, 20, 30);
		fourthCard.add(reservationLabel, gbc);

		JLabel tablesLabel = new JLabel("Menu(s):");
		tablesLabel.setForeground(darkGray);
		tablesLabel.setFont(font);
		gbc.gridy++;
		gbc.insets = new Insets(20, 30, 1, 30);
		fourthCard.add(tablesLabel, gbc);

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
		// scrollPane.setPreferredSize(new Dimension(fourthCard.getWidth() / 100 * 70,
		// fourthCard.getHeight() / 100 * 50));
		scrollPane.setMinimumSize(new Dimension(fourthCard.getWidth() / 100 * 40, fourthCard.getHeight() / 100 * 30));
		scrollPane.setBorder(null);
		scrollPane.setLayout(new ScrollPaneLayout());
		scrollPane.setBackground(Color.WHITE);
		gbc.gridy++;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.gridwidth = GridBagConstraints.REMAINDER;
		gbc.weightx = 0;
		gbc.weighty = 0;
		fourthCard.add(scrollPane, gbc);

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

		for (int i = 1; i <= guests; i++) {
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
		cancelButton.addActionListener(e -> cancel());
		gbc.gridy++;
		gbc.gridx = 0;
		gbc.anchor = GridBagConstraints.SOUTH;
		gbc.gridwidth = 1;
		gbc.insets = new Insets(20, 30, 20, 20);
		fourthCard.add(cancelButton, gbc);

		JButton prevButton = new JButton("< Back");
		prevButton.setFont(font);
		prevButton.setBackground(new Color(242, 233, 228));
		// prevButton.addActionListener();
		gbc.gridx = 1;
		gbc.insets = new Insets(20, 20, 20, 20);
		fourthCard.add(prevButton, gbc);

		JButton nextButton = new JButton("Next >");
		nextButton.setFont(font);
		nextButton.setBackground(new Color(242, 233, 228));
		nextButton.addActionListener(e -> {
			for (JComboBox<String> j : menuComboBoxes) {
				String selMenu = j.getSelectedItem().toString();
				if (!selMenu.equalsIgnoreCase("No menu")) {
					for (Menu m : dbMenus) {
						if (m.getName().equalsIgnoreCase(selMenu)) {
							reservedMenus.add(m);
						}
					}
				}
			}

			createFinalCard();
			next();
		});
		gbc.gridx = 2;
		gbc.insets = new Insets(20, 20, 20, 30);
		fourthCard.add(nextButton, gbc);
		
	}
	
	private void createFinalCard() {
		finalCard.setLayout(new GridBagLayout());
		finalCard.setBackground(Color.WHITE);

		GridBagConstraints gbc = new GridBagConstraints();
		gbc.insets = new Insets(20, 20, 60, 20);
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.ipadx = 0;
		gbc.ipady = 0;
		gbc.gridwidth = GridBagConstraints.REMAINDER;
		gbc.anchor = GridBagConstraints.CENTER;
		gbc.fill = GridBagConstraints.HORIZONTAL;

		JLabel titleLabel = new JLabel("Create new reservation?", JLabel.CENTER);
		titleLabel.setFont(new Font("Segoe UI Semibold", Font.BOLD, 28));
		titleLabel.setForeground(darkGray);
		finalCard.add(titleLabel, gbc);

		JTextArea reservationLabel = new JTextArea("Guest" + (guests > 1 ? "s" : "") + ": " + guests + "\nDate: "
				+ reservationTimeDate.get(Calendar.DATE) + ". " + reservationTimeDate.get(Calendar.MONTH) + "."
				+ reservationTimeDate.get(Calendar.YEAR) + "\nTime: " + reservationTimeDate.get(Calendar.HOUR_OF_DAY)
				+ ":"
				+ (reservationTimeDate.get(Calendar.MINUTE) < 10 ? "0" + reservationTimeDate.get(Calendar.MINUTE)
						: reservationTimeDate.get(Calendar.MINUTE))
				+ "\nCustomer: " + customer.getName() + " " + customer.getSurname() + " (" + customer.getPhone()
				+ ")\nTable: " + reservedTable.getName() + " (" + reservedTable.getCapacity() + ")\nNote: " + note);
		reservationLabel.setForeground(darkGray);
		reservationLabel.setFont(italics);
		reservationLabel.setLineWrap(true);
		reservationLabel.setEditable(false);
		gbc.gridy++;
		finalCard.add(reservationLabel, gbc);

		JButton cancelButton = new JButton("Cancel");
		cancelButton.setFont(font);
		cancelButton.setBackground(new Color(242, 233, 228));
		cancelButton.addActionListener(e -> cancel());
		gbc.gridy++;
		gbc.gridx = 0;
		gbc.anchor = GridBagConstraints.SOUTH;
		gbc.gridwidth = 1;
		gbc.insets = new Insets(20, 30, 20, 20);
		finalCard.add(cancelButton, gbc);

		JButton prevButton = new JButton("< Back");
		prevButton.setFont(font);
		prevButton.setBackground(new Color(242, 233, 228));
		// prevButton.addActionListener();
		gbc.gridx = 1;
		gbc.insets = new Insets(20, 20, 20, 20);
		finalCard.add(prevButton, gbc);

		JButton createButton = new JButton("Create");
		createButton.setFont(font);
		createButton.setBackground(new Color(242, 233, 228));
		createButton.addActionListener(e -> {

			new Thread(() -> {
				try {
					reservationController.confirmReservation(customer, guests, duration, reservedMenus, note);
					JOptionPane.showConfirmDialog(null, "New reservation was created successfully", "New reservtaion",
							JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
					ReservationsPanel.repopulateTable();
				} catch (SQLException e1) {
					JOptionPane.showConfirmDialog(null,
							"An error eccured while creating the reservation.\nPlease try again.", "Error",
							JOptionPane.OK_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE);
					return;
				}
			}).start();
			cancel();
		});
		gbc.gridx = 2;
		gbc.insets = new Insets(20, 20, 30, 20);
		finalCard.add(createButton, gbc);
	}
	
	private void addPlaceholder(JTextField field, String placeholder) {
		field.addFocusListener(new FocusListener() {
			@Override
			public void focusGained(FocusEvent e) {
				if (field.getText().equals(placeholder)) {
					field.setText("");
					field.setFont(font);
					field.setForeground(darkGray);
				}
			}

			@Override
			public void focusLost(FocusEvent e) {
				if (field.getText().isEmpty()) {
					field.setFont(placeholderFont);
					field.setForeground(lightGray);
					field.setText(placeholder);
				}
			}
		});
	}

	
	private void cancel() {
		this.dispose();
	}
	
	private void next() {
		if (currentCard == 5) {
			cardLayout.show(contentPane, "1");
			currentCard = 1;
		} else {
			currentCard++;
			cardLayout.show(contentPane, "" + currentCard);
		}
	}
	
//	private void makeReservationClicked() {
//		// dont forget to deselected after :) 
//	}
//	
//	private void setTimeTxtFieldTxt(Calendar calendar) {
//		String time;
//		if(calendar.get(Calendar.MINUTE) == 0) {
//			time = String.valueOf(calendar.get(Calendar.HOUR_OF_DAY)) + ":" +
//					String.valueOf(calendar.get(Calendar.MINUTE)) +"0";
//			timeTxtField.setText(time);
//		}
//		else {
//			time = String.valueOf(calendar.get(Calendar.HOUR_OF_DAY)) + ":" +
//				String.valueOf(calendar.get(Calendar.MINUTE));
//			timeTxtField.setText(time);
//		}
//	}
	
	private void setDateTxtFieldTxt(Calendar calendar) {
		String date = String.valueOf(calendar.get(Calendar.DAY_OF_MONTH)) + "." +
				String.valueOf(calendar.get(Calendar.MONTH) +1) + "." +
				String.valueOf(calendar.get(Calendar.YEAR));
		dateField.setText(date);
	}
	
	private void populateTableCB(ArrayList<Table> tableList) {
		for(Table table : tableList) {
			tables.addItem(table.getName());
		}
	}

}
