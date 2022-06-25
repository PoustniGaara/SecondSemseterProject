package gui.reservation;

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
import java.time.Duration;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.swing.DefaultListModel;
import javax.swing.DefaultListSelectionModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JSpinner.NumberEditor;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;
import javax.swing.ScrollPaneLayout;
import javax.swing.SpinnerDateModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.MatteBorder;
import javax.swing.text.DateFormatter;
import javax.swing.text.NumberFormatter;

import controller.ReservationController;
import database.MenuConcreteDAO;
import database.ReservationConcreteDAO;
import database.TableConcreteDAO;
import gui.ToolPanel;
import gui.tools.Fonts;
import gui.tools.ProjectColors;
import gui.tools.Validator;
import model.Customer;
import model.Menu;
import model.Reservation;
import model.Table;

@SuppressWarnings("serial")
public class CreateReservationFrame extends JFrame {

	private JPanel contentPane;
	private CardLayout cardLayout;
	private JPanel firstCard;
	private JPanel secondCard;
	private JPanel thirdCard;
	private JPanel fourthCard;
	private JPanel finalCard;
	private int currentCard;

	private ReservationController reservationController;

	private JSpinner guestsSpinner;
	private JFormattedTextField dateField;
	private JSpinner spinnerH;
	private JSpinner spinnerM;
	private JTextField phoneField;
	private JTextField noteField;
	private JList<String> tables;

	private static ArrayList<Table> dbTables;
	private static ArrayList<Reservation> dbReservations;
	private static ArrayList<Menu> dbMenus;
	private static boolean resourcesLoaded = false;

	private boolean card2Created = false;
	private boolean card3Created = false;
	private boolean card4Created = false;
	private boolean card5Created = false;

	private Calendar reservationTimeDate;
	private int guests;
	private int duration;
	private String phone;
	private ArrayList<Table> reservedTables;
	private String note;
	private Customer customer;
	private ArrayList<Menu> reservedMenus;

	private final Font font = Fonts.FONT20.get();
	private final Font italics = new Font("Dialog", Font.ITALIC, 20);
	private final Font placeholderFont = new Font("Dialog", Font.ITALIC, 20);

	private final Color darkGray = new Color(18, 18, 18);
	private final Color lightGray = new Color(89, 89, 89);

	public static void open() {
		SwingUtilities.invokeLater((Runnable) new Runnable() {
			public void run() {
				CreateReservationFrame frame = new CreateReservationFrame();
				frame.setVisible(true);
			}
		});
		new Thread(() -> loadResources()).start();
	}

	private static void loadResources() {
		try {
			dbReservations = ReservationConcreteDAO.getInstance().read();
			dbTables = TableConcreteDAO.getInstance().read();
			dbMenus = MenuConcreteDAO.getInstance().read();
			resourcesLoaded = true;
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public CreateReservationFrame() {
		reservationController = new ReservationController();
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

		firstCard = new JPanel();
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

		createFirstCard();
	}

	private void createFirstCard() {
		firstCard.setLayout(new GridBagLayout());
		firstCard.setBackground(ProjectColors.BG.get());

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
		titleLabel.setFont(new Font("Dialog", Font.BOLD, 28));
		titleLabel.setForeground(darkGray);
		firstCard.add(titleLabel, gbc);

		JLabel guestsLabel = new JLabel("Number of guests:");
		guestsLabel.setForeground(darkGray);
		guestsLabel.setFont(font);
		gbc.gridy++;
		gbc.gridheight = 1;
		gbc.anchor = GridBagConstraints.WEST;
		gbc.insets = new Insets(25, 30, 5, 30);
		firstCard.add(guestsLabel, gbc);

		
		SpinnerNumberModel guestsNumberModel = new SpinnerNumberModel(2, 1, 100, 1);
		guestsSpinner = new JSpinner(guestsNumberModel);
		NumberEditor guestsEditor = new NumberEditor(guestsSpinner);
		NumberFormatter guestsformatter = (NumberFormatter) guestsEditor.getTextField().getFormatter();
		guestsformatter.setOverwriteMode(true);
		guestsformatter.setAllowsInvalid(false);
		guestsSpinner.setEditor(guestsEditor);
		guestsSpinner.setFont(font);
		guestsSpinner.setBorder(new CompoundBorder(new LineBorder(darkGray, 1), new EmptyBorder(0, 10, 0, 0)));
		gbc.gridy++;
		gbc.insets = new Insets(5, 30, 25, 30);
		firstCard.add(guestsSpinner, gbc);

		JLabel dateLabel = new JLabel("Date:");
		dateLabel.setForeground(darkGray);
		dateLabel.setFont(font);
		gbc.gridy++;
		gbc.insets = new Insets(25, 30, 5, 30);
		firstCard.add(dateLabel, gbc);

		DateFormat format = new SimpleDateFormat("dd.MM.yyyy");
		DateFormatter df = new DateFormatter(format);
		dateField = new JFormattedTextField(df);
		dateField.setValue(new Date());
		dateField.setFont(font);
		dateField.setBorder(new CompoundBorder(new LineBorder(darkGray, 1), new EmptyBorder(0, 10, 0, 0)));
		gbc.gridy++;
		gbc.insets = new Insets(5, 30, 25, 30);
		firstCard.add(dateField, gbc);

		JLabel timeLabel = new JLabel("Time:");
		timeLabel.setForeground(darkGray);
		timeLabel.setFont(font);
		gbc.gridy++;
		gbc.insets = new Insets(25, 30, 5, 30);
		firstCard.add(timeLabel, gbc);

		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.HOUR_OF_DAY, 12);
		calendar.set(Calendar.MINUTE, 0);

		SpinnerDateModel model = new SpinnerDateModel();
		model.setValue(calendar.getTime());

		spinnerH = new JSpinner(model);
		JSpinner.DateEditor editorH = new JSpinner.DateEditor(spinnerH, "HH");
		DateFormatter formatterH = (DateFormatter) editorH.getTextField().getFormatter();
		formatterH.setAllowsInvalid(false);
		formatterH.setOverwriteMode(true);
		spinnerH.setEditor(editorH);
		spinnerH.setFont(font);
		spinnerH.setBorder(new CompoundBorder(new LineBorder(darkGray, 1), new EmptyBorder(0, 10, 0, 0)));
		gbc.gridy++;
		gbc.gridwidth = 1;
		gbc.anchor = GridBagConstraints.NORTH;
		gbc.insets = new Insets(5, 30, 25, 30);
		firstCard.add(spinnerH, gbc);

		JLabel separator = new JLabel(":", JLabel.CENTER);
		separator.setForeground(darkGray);
		separator.setFont(font);
		gbc.gridx++;
		firstCard.add(separator, gbc);

		SpinnerDateModel model2 = new SpinnerDateModel();
		model2.setValue(calendar.getTime());

		spinnerM = new JSpinner(model2);
		JSpinner.DateEditor editorM = new JSpinner.DateEditor(spinnerM, "mm");
		DateFormatter formatterM = (DateFormatter) editorM.getTextField().getFormatter();
		formatterM.setAllowsInvalid(false);
		formatterM.setOverwriteMode(true);
		spinnerM.setEditor(editorM);
		spinnerM.setFont(font);
		spinnerM.setBorder(new CompoundBorder(new LineBorder(darkGray, 1), new EmptyBorder(0, 10, 0, 0)));
		gbc.gridx++;
		firstCard.add(spinnerM, gbc);

		JLabel durationLabel = new JLabel("Duration:");
		durationLabel.setFont(font);
		gbc.gridy++;
		gbc.gridx = 0;
		gbc.weightx = 1;
		gbc.insets = new Insets(25, 30, 5, 30);
		firstCard.add(durationLabel, gbc);

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

		JButton cancelButton = new JButton("Cancel");
		cancelButton.setFont(font);
		cancelButton.setForeground(new Color(195, 70, 70));
		cancelButton.setBackground(new Color(252, 232, 232));
		cancelButton.setBorder(new LineBorder(new Color(220, 48, 48), 1));
		cancelButton.setPreferredSize(new Dimension((int) (getWidth() * 0.3), 40));
		cancelButton.setFocusable(false);
		cancelButton.addActionListener(e -> cancel());
		gbc.gridy++;
		gbc.gridx = 0;
		gbc.weightx = 1;
		gbc.anchor = GridBagConstraints.SOUTH;
		gbc.insets = new Insets(40, 30, 25, 30);
		firstCard.add(cancelButton, gbc);

		JButton nextButton = new JButton("Next >");
		nextButton.setFont(font);
		nextButton.setBackground(ProjectColors.BLUE.get());
		nextButton.setForeground(Color.white);
		nextButton.setPreferredSize(new Dimension((int) (getWidth() * 0.3), 40));
		nextButton.setFocusable(false);
		nextButton.addActionListener(e -> {
			
			guests = (int) guestsSpinner.getValue();
			duration = (int) durationSpinner.getValue();

			Calendar date = Calendar.getInstance();
			date.setTime((Date) dateField.getValue());
			int year = date.get(Calendar.YEAR);
			int month = date.get(Calendar.MONTH);
			int day = date.get(Calendar.DAY_OF_MONTH);
			date.setTime((Date) spinnerH.getValue());
			int hour = date.get(Calendar.HOUR_OF_DAY);
			date.setTime((Date) spinnerM.getValue());
			int minute = date.get(Calendar.MINUTE);

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
				if (!card2Created)
					createSecondCard();
				populateTables();
				next();
			} else {
				JOptionPane.showConfirmDialog(null,
						"The resources needed for the next step of reservation creation haven't been loaded!\nPlease, wait a bit and try again.",
						"Resources not loaded", JOptionPane.OK_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE);
			}
		});
		gbc.gridx = 2;
		firstCard.add(nextButton, gbc);
	}

	private void createSecondCard() {
		secondCard.setLayout(new GridBagLayout());
		secondCard.setBackground(ProjectColors.BG.get());
		card2Created = true;
		reservedTables = new ArrayList<>();

		GridBagConstraints gbc = new GridBagConstraints();
		gbc.insets = new Insets(20, 20, 20, 20);
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
				+ reservationTimeDate.get(Calendar.DATE) + ". " + (reservationTimeDate.get(Calendar.MONTH) + 1) + ". "
				+ reservationTimeDate.get(Calendar.YEAR) + " at " + reservationTimeDate.get(Calendar.HOUR_OF_DAY) + ":"
				+ (reservationTimeDate.get(Calendar.MINUTE) < 10 ? "0" + reservationTimeDate.get(Calendar.MINUTE)
						: reservationTimeDate.get(Calendar.MINUTE)),
				JLabel.CENTER);
		reservationLabel.setForeground(darkGray);
		reservationLabel.setFont(italics);
		reservationLabel.setBorder(new MatteBorder(0, 0, 1, 0, darkGray));
		gbc.gridy++;
		gbc.anchor = GridBagConstraints.WEST;
		gbc.insets = new Insets(20, 30, 20, 30);
		secondCard.add(reservationLabel, gbc);

		JLabel tablesLabel = new JLabel("Table(s):");
		tablesLabel.setForeground(darkGray);
		tablesLabel.setFont(font);
		gbc.gridy++;
		gbc.insets = new Insets(20, 30, 5, 30);
		secondCard.add(tablesLabel, gbc);

		tables = new JList<String>();
		tables.setFont(font);
		tables.setBorder(new CompoundBorder(new LineBorder(darkGray, 1), new EmptyBorder(0, 10, 0, 0)));
		tables.setBackground(Color.WHITE);

		JScrollPane scrollPane = new JScrollPane(tables);
		scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
		scrollPane.setBackground(Color.WHITE);
		gbc.gridy++;
		gbc.gridheight = 1;
		gbc.insets = new Insets(5, 30, 20, 30);
		secondCard.add(scrollPane, gbc);

		JLabel phoneLabel = new JLabel("Customer phone:");
		phoneLabel.setForeground(darkGray);
		phoneLabel.setFont(font);
		gbc.gridy++;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.insets = new Insets(20, 30, 5, 30);
		secondCard.add(phoneLabel, gbc);

		phoneField = new JTextField();
		phoneField.setFont(font);
		phoneField.setBorder(new CompoundBorder(new LineBorder(darkGray, 1), new EmptyBorder(0, 10, 0, 0)));
		phoneField.setBackground(Color.WHITE);
		gbc.gridy++;
		gbc.insets = new Insets(5, 30, 20, 30);
		secondCard.add(phoneField, gbc);

		JLabel noteLabel = new JLabel("Note:");
		noteLabel.setForeground(darkGray);
		noteLabel.setFont(font);
		gbc.gridy++;
		gbc.insets = new Insets(20, 30, 5, 30);
		secondCard.add(noteLabel, gbc);

		noteField = new JTextField();
		noteField.setFont(font);
		noteField.setBorder(new CompoundBorder(new LineBorder(darkGray, 1), new EmptyBorder(0, 10, 0, 0)));
		noteField.setBackground(Color.WHITE);
		gbc.gridy++;
		gbc.insets = new Insets(5, 30, 40, 30);
		secondCard.add(noteField, gbc);

		JButton cancelButton = new JButton("Cancel");
		cancelButton.setFont(font);
		cancelButton.setForeground(new Color(195, 70, 70));
		cancelButton.setBackground(new Color(252, 232, 232));
		cancelButton.setBorder(new LineBorder(new Color(220, 48, 48), 1));
		cancelButton.setPreferredSize(new Dimension((int) (getWidth() * 0.2), 40));
		cancelButton.setFocusable(false);
		cancelButton.addActionListener(e -> cancel());
		gbc.gridy++;
		gbc.anchor = GridBagConstraints.SOUTH;
		gbc.gridwidth = 1;
		gbc.insets = new Insets(40, 30, 20, 20);
		secondCard.add(cancelButton, gbc);

		JButton prevButton = new JButton("< Back");
		prevButton.setFont(font);
		prevButton.setBackground(ProjectColors.BLUE1.get());
		prevButton.setBorder(new LineBorder(ProjectColors.LIGHT_BLUE.get(), 1));
		prevButton.setPreferredSize(new Dimension((int) (getWidth() * 0.2), 40));
		prevButton.addActionListener(e -> back(1));
		gbc.gridx = 1;
		gbc.insets = new Insets(40, 20, 20, 20);
		secondCard.add(prevButton, gbc);

		JButton nextButton = new JButton("Next >");
		nextButton.setFont(font);
		nextButton.setBackground(ProjectColors.BLUE.get());
		nextButton.setForeground(Color.white);
		nextButton.setFocusable(false);
		nextButton.setPreferredSize(new Dimension((int) (getWidth() * 0.2), 40));
		nextButton.addActionListener(e -> {
			if (tables.getSelectedValue() == null) {
				JOptionPane.showConfirmDialog(null,
						"No table was selected!\nPlease, select a table from the drop down menu!", "Table selection",
						JOptionPane.OK_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE);
				return;
			}
			reservedTables.clear();
			int tCapacity = 0;
			for (Table t : dbTables) {
				for (String s : tables.getSelectedValuesList()) {
					if (t.getName().equals(s)) {
						reservedTables.add(t);
						tCapacity += t.getCapacity();
					}
				}
			}

			if (tCapacity < guests) {
				JOptionPane.showConfirmDialog(null,
						"The capacity of selected tables is less then the number of guest!\nPlease select more tables.",
						"Table selection", JOptionPane.OK_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE);
				return;
			}

			if (tCapacity >= 2 * guests) {
				int option = JOptionPane.showConfirmDialog(null,
						"The capacity of selected tables is more then the number of guest!\nAre you sure you want to select these tables?",
						"Table selection", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
				if (option != JOptionPane.YES_OPTION)
					return;

			}

			String input = phoneField.getText();
			if (input != null && !input.isEmpty() && !input.isBlank()) {
				try {
					Long.parseLong(input);
				} catch (Exception ex) {
					JOptionPane.showConfirmDialog(null, "The input value in the phone number field is incorect!",
							"Incorrect phone number", JOptionPane.WARNING_MESSAGE);
					return;
				}
				phone = input;
			} else {
				JOptionPane.showConfirmDialog(null, "The phone number field must be filled!", "Phone number",
						JOptionPane.OK_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE);
				return;
			}
			note = noteField.getText();

			reservationController.startReservation(reservationTimeDate, reservedTables);
			if (!card3Created)
				createThirdCard();
			next();
		});
		gbc.gridx = 2;
		gbc.insets = new Insets(40, 20, 20, 30);
		secondCard.add(nextButton, gbc);
	}

	private void createThirdCard() {
		thirdCard.setLayout(new GridBagLayout());
		thirdCard.setBackground(ProjectColors.BG.get());
		card3Created = true;

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
				+ reservationTimeDate.get(Calendar.DATE) + ". " + (reservationTimeDate.get(Calendar.MONTH) + 1) + ". "
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
		nameLabel.setName("name");
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
		surnameField.setName("surname");
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
		streetField.setFont(font);
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
		streetNoField.setFont(font);
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
		townField.setFont(font);
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
		zipField.setFont(font);
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
		cancelButton.setForeground(new Color(195, 70, 70));
		cancelButton.setBackground(new Color(252, 232, 232));
		cancelButton.setBorder(new LineBorder(new Color(220, 48, 48), 1));
		cancelButton.setPreferredSize(new Dimension((int) (getWidth() * 0.2), 40));
		cancelButton.setFocusable(false);
		cancelButton.addActionListener(e -> cancel());
		gbc.gridy++;
		gbc.gridx = 0;
		gbc.anchor = GridBagConstraints.SOUTH;
		gbc.gridwidth = 1;
		gbc.insets = new Insets(30, 30, 20, 20);
		thirdCard.add(cancelButton, gbc);

		JButton prevButton = new JButton("< Back");
		prevButton.setFont(font);
		prevButton.setBackground(ProjectColors.BLUE1.get());
		prevButton.setBorder(new LineBorder(ProjectColors.LIGHT_BLUE.get(), 1));
		prevButton.setPreferredSize(new Dimension((int) (getWidth() * 0.2), 40));
		prevButton.addActionListener(e -> back(2));
		gbc.gridx = 1;
		gbc.insets = new Insets(30, 20, 20, 20);
		thirdCard.add(prevButton, gbc);

		JButton nextButton = new JButton("Next >");
		nextButton.setFont(font);
		nextButton.setBackground(ProjectColors.BLUE.get());
		nextButton.setForeground(Color.white);
		nextButton.setPreferredSize(new Dimension((int) (getWidth() * 0.2), 40));

		nextButton.setFocusable(false);
		nextButton.addActionListener(e -> {

			for (Component c : thirdCard.getComponents()) {
				if (c instanceof JTextField) {
					if (!Validator.isFilled((JTextField) c)) {
						JOptionPane.showConfirmDialog(null, "The " + c.getName() + " field must be filled!",
								"Empty field", JOptionPane.OK_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE);
						return;
					}
				}
			}

			if (!Validator.isNumber(phoneField) || !Validator.isNumber(streetNoField)
					|| !Validator.isNumber(zipField)) {
				JOptionPane.showConfirmDialog(null, "The input value in number input fields is incorect!",
						"Wrong number input", JOptionPane.OK_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE);
				return;
			}

			customer = new Customer(nameField.getText(), surnameField.getText(), phone, "", townField.getText(),
					zipField.getText(), streetField.getText(), streetNoField.getText());
			if (!card4Created)
				createFourthCard();
			next();
		});
		gbc.gridx = 2;
		gbc.insets = new Insets(30, 20, 20, 30);
		thirdCard.add(nextButton, gbc);

	}

	private void createFourthCard() {
		fourthCard.setLayout(new GridBagLayout());
		fourthCard.setBackground(ProjectColors.BG.get());
		card4Created = true;
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
				+ reservationTimeDate.get(Calendar.DATE) + ". " + (reservationTimeDate.get(Calendar.MONTH) + 1) + ". "
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
		cancelButton.setForeground(new Color(195, 70, 70));
		cancelButton.setBackground(new Color(252, 232, 232));
		cancelButton.setBorder(new LineBorder(new Color(220, 48, 48), 1));
		cancelButton.setPreferredSize(new Dimension((int) (getWidth() * 0.2), 40));
		cancelButton.setFocusable(false);
		cancelButton.addActionListener(e -> cancel());
		gbc.gridy++;
		gbc.gridx = 0;
		gbc.anchor = GridBagConstraints.SOUTH;
		gbc.gridwidth = 1;
		gbc.insets = new Insets(20, 30, 20, 20);
		fourthCard.add(cancelButton, gbc);

		JButton prevButton = new JButton("< Back");
		prevButton.setFont(font);
		prevButton.setBackground(ProjectColors.BLUE1.get());
		prevButton.setBorder(new LineBorder(ProjectColors.LIGHT_BLUE.get(), 1));
		prevButton.setPreferredSize(new Dimension((int) (getWidth() * 0.2), 40));
		prevButton.addActionListener(e -> back(3));
		gbc.gridx = 1;
		gbc.insets = new Insets(20, 20, 20, 20);
		fourthCard.add(prevButton, gbc);

		JButton nextButton = new JButton("Next >");
		nextButton.setFont(font);
		nextButton.setBackground(ProjectColors.BLUE.get());
		nextButton.setForeground(Color.white);
		nextButton.setPreferredSize(new Dimension((int) (getWidth() * 0.2), 40));
		nextButton.setFocusable(false);
		nextButton.addActionListener(e -> {
			reservedMenus.clear();
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
			if (!card5Created)
				createFinalCard();
			next();
		});
		gbc.gridx = 2;
		gbc.insets = new Insets(20, 20, 20, 30);
		fourthCard.add(nextButton, gbc);
	}

	private void createFinalCard() {
		finalCard.setLayout(new GridBagLayout());
		finalCard.setBackground(ProjectColors.BG.get());
		card5Created = true;

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

		JLabel guestFinalLabel = new JLabel("Guest" + (guests > 1 ? "s" : "") + ": " + guests);
		guestFinalLabel.setFont(font);
		gbc.insets = new Insets(10, 30, 10, 30);
		gbc.gridy++;
		finalCard.add(guestFinalLabel, gbc);

		JLabel dateFinalLabel = new JLabel("Date: " + reservationTimeDate.get(Calendar.DATE) + "."
				+ (reservationTimeDate.get(Calendar.MONTH) + 1) + "." + reservationTimeDate.get(Calendar.YEAR));
		dateFinalLabel.setFont(font);
		gbc.gridy++;
		finalCard.add(dateFinalLabel, gbc);

		JLabel timeFinalLabel = new JLabel("Time: " + reservationTimeDate.get(Calendar.HOUR_OF_DAY) + ":"
				+ (reservationTimeDate.get(Calendar.MINUTE) < 10 ? "0" + reservationTimeDate.get(Calendar.MINUTE)
						: reservationTimeDate.get(Calendar.MINUTE)));
		timeFinalLabel.setFont(font);
		gbc.gridy++;
		finalCard.add(timeFinalLabel, gbc);

		JLabel durationFinalLabel = new JLabel("Duration: " + duration + " h");
		durationFinalLabel.setFont(font);
		gbc.gridy++;
		finalCard.add(durationFinalLabel, gbc);

		JLabel customerFinalLabel = new JLabel(
				"Customer: " + customer.getName() + " " + customer.getSurname() + " (" + customer.getPhone() + ")");
		customerFinalLabel.setFont(font);
		gbc.gridy++;
		finalCard.add(customerFinalLabel, gbc);

		JLabel tablesFinalLabel = new JLabel("Table(s): " + tableNames(reservedTables));
		tablesFinalLabel.setFont(font);
		gbc.gridy++;
		finalCard.add(tablesFinalLabel, gbc);

		JLabel noteFinalLabel = new JLabel("Note: " + note);
		noteFinalLabel.setFont(font);
		gbc.gridy++;
		finalCard.add(noteFinalLabel, gbc);

		JButton cancelButton = new JButton("Cancel");
		cancelButton.setFont(font);
		cancelButton.setForeground(new Color(195, 70, 70));
		cancelButton.setBackground(new Color(252, 232, 232));
		cancelButton.setBorder(new LineBorder(new Color(220, 48, 48), 1));
		cancelButton.setPreferredSize(new Dimension((int) (getWidth() * 0.2), 40));
		cancelButton.setFocusable(false);
		cancelButton.addActionListener(e -> cancel());
		gbc.gridy++;
		gbc.gridx = 0;
		gbc.anchor = GridBagConstraints.SOUTH;
		gbc.gridwidth = 1;
		gbc.insets = new Insets(40, 30, 20, 20);
		finalCard.add(cancelButton, gbc);

		JButton prevButton = new JButton("< Back");
		prevButton.setFont(font);
		prevButton.setBackground(ProjectColors.BLUE1.get());
		prevButton.setBorder(new LineBorder(ProjectColors.LIGHT_BLUE.get(), 1));
		prevButton.setPreferredSize(new Dimension((int) (getWidth() * 0.2), 40));
		prevButton.addActionListener(e -> back(4));
		gbc.gridx = 1;
		finalCard.add(prevButton, gbc);

		JButton createButton = new JButton("Create");
		createButton.setFont(font);
		createButton.setBackground(ProjectColors.BLUE.get());
		createButton.setForeground(Color.white);
		createButton.setPreferredSize(new Dimension((int) (getWidth() * 0.2), 40));
		createButton.addActionListener(e -> {
			new Thread(() -> {
				try {
					reservationController.confirmReservation(customer, guests, duration, reservedMenus, note);
					JOptionPane.showConfirmDialog(null, "New reservation was created successfully", "New reservtaion",
							JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
					ReservationsPanel.repopulateTable();
				} catch (SQLException e1) {
					JOptionPane.showConfirmDialog(null,
							"An error occured while creating the reservation.\nPlease try again.", "Error",
							JOptionPane.OK_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE);
					return;
				}
			}).start();
			cancel();
		});
		gbc.gridx = 2;
		finalCard.add(createButton, gbc);
	}

	private void populateTables() {
		ArrayList<Table> availableTables = dbTables;
		List<Long> ids = new ArrayList<Long>(dbTables.parallelStream().map(t -> t.getId()).toList());
		if (!dbReservations.isEmpty()) {
			for (Reservation r : dbReservations) {
				if (Math.max(r.getTimestamp().getTimeInMillis(), reservationTimeDate.getTimeInMillis()) < Math.min(
						r.getTimestamp().getTimeInMillis() + (r.getDuration() * 3600000),
						reservationTimeDate.getTimeInMillis() + (duration * 3600000))) {
					for (Table t : r.getTables()) {
						if (ids.contains(t.getId())) {
							availableTables.remove(ids.indexOf(t.getId()));
							ids.remove(t.getId());
						}
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

	private void cancel() {
		this.dispose();
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

	private void back(int card) {
		cardLayout.show(contentPane, card + "");
		currentCard = card;
	}

	private String tableNames(ArrayList<Table> tables) {
		String s = "";
		for (Table t : tables) {
			s += t.getName() + (tables.get(tables.size() - 1) != t ? ", " : "");
		}
		return s;
	}
}
