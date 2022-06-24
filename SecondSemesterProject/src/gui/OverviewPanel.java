package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.image.ImageObserver;
import java.sql.Date;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Properties;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.BorderFactory;
import javax.swing.DefaultComboBoxModel;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerDateModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingConstants;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.text.DateFormatter;
import javax.swing.text.NumberFormatter;

import org.jdatepicker.impl.JDatePanelImpl;
import org.jdatepicker.impl.JDatePickerImpl;
import org.jdatepicker.impl.UtilDateModel;
import org.kordamp.ikonli.coreui.CoreUiFree;
import org.kordamp.ikonli.swing.FontIcon;

import controller.ReservationController;
import controller.RestaurantLayoutController;
import gui.layout.LayoutMiniPanel;
import gui.layout.LayoutPanel;
import gui.layout.NoLayoutInfoPanel;
import gui.reservation.MakeReservationFrame;
import gui.tools.DateLabelFormatter;
import gui.tools.FancyButtonMoreClick;
import gui.tools.FancyButtonOneClick;
import gui.tools.Fonts;
import gui.tools.ProjectColors;
import gui.tools.ToolBorderPanel;
import model.LayoutItem;
import model.Reservation;
import model.ReservedTableInfo;
import model.RestaurantLayout;
import model.Table;

public class OverviewPanel extends JPanel {

	private static OverviewPanel instance;
	private JComboBox<String> timeCB, layoutCB;
	private JSpinner hourSpinner, minuteSpinner, durationSpinner;
	private JComboBox<Integer> personCB, durationCB;
	private FancyButtonMoreClick dayBackBtn, dayForwardBtn;
	private FancyButtonOneClick nowBtn, calendarBtn, makeReservationBtn;
	private Calendar calendar;
	private ArrayList<RestaurantLayout> layoutList;
	private RestaurantLayoutController restaurantLayoutController;
	private HashMap<String, LayoutPanel> layoutPanelMap;
	private LayoutPanel currentLayoutPanel;
	private ReservationController reservationController;
	private HashMap<String, ArrayList<ReservedTableInfo>> reservedTableInfoMap;
	private UtilDateModel calendarModel;
	private JDatePickerImpl datePicker;
	private boolean automaticSystemTime = true;
	private ToolBorderPanel personBorderPanel, timeBorderPanel, durationBorderPanel, calendarBorderPanel;
	private ArrayList<Table> selectedTables;

	private OverviewPanel() {

		// control
		restaurantLayoutController = new RestaurantLayoutController();
		reservationController = new ReservationController();

		// Panel dimensions setup
		int mainHeight = (int) MainFrame.height;
		int mainWidth = (int) MainFrame.width;
		int panelHeight = (int) ((int) mainHeight * 0.84);
		int panelWidth = mainWidth;
		int toolPanelHeight = (int) (panelHeight * 0.12);

		// Panel functional setup
		setPreferredSize(new Dimension(panelWidth, panelHeight));
		setBackground(ProjectColors.GREY.get());
		setLayout(new BorderLayout());
		setVisible(true);

		// load data
		selectedTables = new ArrayList<Table>();
		try {
			layoutList = (ArrayList<RestaurantLayout>) restaurantLayoutController.read();
			System.out.print("RL FIRST ID: " + layoutList.get(0).getId());
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		setCalendarCurrentTime();
		loadLayouts(1, calendar, 1);

		// Panel support classes
		Border borderGreen = BorderFactory.createLineBorder(ProjectColors.GREEN.get(), 1);
		Border borderBlack = BorderFactory.createLineBorder(ProjectColors.BLACK.get(), 2);
		Border borderRed = BorderFactory.createLineBorder(ProjectColors.RED.get(), 2);
		Font font1 = new Font("Monaco", Font.BOLD, 20);

		// -----------------------------------------------
		// ------------------ TOOL ---------------------
		// -----------------------------------------------

		// Tool Panel
		ToolPanel toolPanel = new ToolPanel();
//		toolPanel.setBorder(borderGreen);
		GridBagConstraints gbcTool = new GridBagConstraints();
		gbcTool.weightx = 1;
		gbcTool.weighty = 1;
		add(toolPanel, BorderLayout.NORTH);

		// Person border panel
		personBorderPanel = new ToolBorderPanel(0,0,0,0);
		//new ToolBorderPanel((int) ((panelWidth/8)*0.98),(int) (ToolPanel.getPanelHeight()*0.2),
		//(int) ((panelWidth/8)*0.98),(int) (ToolPanel.getPanelHeight()*0.96));
		personBorderPanel.setPreferredSize(new Dimension(panelWidth/8, ToolPanel.getPanelHeight()));
		personBorderPanel.setLayout(new GridBagLayout());
		GridBagConstraints gbcPersonBP = new GridBagConstraints();
		gbcPersonBP.weightx = 1;
		gbcPersonBP.weighty = 1;
		gbcTool.gridx = 3;		
		gbcTool.gridy = 0;
		gbcTool.fill = GridBagConstraints.BOTH;
		toolPanel.add(personBorderPanel,gbcTool);

		// Person ComboBox
		personCB = new JComboBox<Integer>(new DefaultComboBoxModel());
		populatePersonCB();
		personCB.setMaximumRowCount(5);
		personCB.addActionListener(e -> PersonComboBoxAction());
		personCB.setFont(Fonts.FONT20.get());
		personCB.setPreferredSize(new Dimension(panelWidth/14, ToolPanel.getPanelHeight()/2));
		gbcPersonBP.anchor = GridBagConstraints.CENTER;
		gbcPersonBP.gridx = 0;
		gbcPersonBP.gridy = 0;
		personBorderPanel.add(personCB,gbcPersonBP);

		JLabel personLabel = new JLabel("Persons");
		personLabel.setFont(Fonts.FONT20.get());
		gbcPersonBP.weighty = 0.5;
		gbcPersonBP.anchor = GridBagConstraints.PAGE_END;
		gbcPersonBP.gridx = 0;
		gbcPersonBP.gridy = 1;
		personBorderPanel.add(personLabel,gbcPersonBP);

		// Time border panel
		timeBorderPanel = new ToolBorderPanel(460,5,460,120);
//		timeBorderPanel.setBackground(Color.yellow);
		timeBorderPanel.setPreferredSize(new Dimension(panelWidth/6, ToolPanel.getPanelHeight()));
		timeBorderPanel.setLayout(new GridBagLayout());
		GridBagConstraints gbcTimeBP = new GridBagConstraints();
		gbcPersonBP.weightx = 1;
		gbcPersonBP.weighty = 1;
		gbcTool.gridx = 1;
		gbcTool.gridy = 0;
		gbcTool.fill = GridBagConstraints.BOTH;
		toolPanel.add(timeBorderPanel,gbcTool);

		// Hour label
		JLabel hourLabel = new JLabel("Hours");
		hourLabel.setFont(Fonts.FONT20.get());
		gbcTimeBP.anchor = GridBagConstraints.LAST_LINE_START;
		gbcTimeBP.gridx = 0;
		gbcTimeBP.gridy = 0;
		timeBorderPanel.add(hourLabel,gbcTimeBP);

		// Hours spinner
		SpinnerNumberModel numberModelHours = new SpinnerNumberModel(17, 0, 23, 1);
		hourSpinner = new JSpinner(numberModelHours);
		hourSpinner.addChangeListener(e -> hourSpinnerChanged());
		JSpinner.NumberEditor editorHours = new JSpinner.NumberEditor(hourSpinner);
		NumberFormatter formatterHours = (NumberFormatter) editorHours.getTextField().getFormatter();
		formatterHours.setAllowsInvalid(false);
		formatterHours.setOverwriteMode(true);
		hourSpinner.setEditor(editorHours);
		hourSpinner.setFont(Fonts.FONT20.get());
		hourSpinner.setPreferredSize(new Dimension(panelWidth / 16, ToolPanel.getPanelHeight() / 2));
//		hourSpinner.setBorder(new CompoundBorder(new LineBorder(darkGray, 1), new EmptyBorder(0, 10, 0, 0)));
		gbcTimeBP.anchor = GridBagConstraints.FIRST_LINE_START;
		gbcTimeBP.gridx = 0;
		gbcTimeBP.gridy = 1;
		timeBorderPanel.add(hourSpinner, gbcTimeBP);
		setValueOfHourSpinner();

		// Separator label
		JLabel separatorLabel = new JLabel(":", JLabel.CENTER);
		separatorLabel.setPreferredSize(new Dimension(panelWidth / 30, ToolPanel.getPanelHeight() / 2));
		separatorLabel.setFont(Fonts.FONT20.get());
		gbcTimeBP.gridheight = 2;
		gbcTimeBP.gridx = 1;
		timeBorderPanel.add(separatorLabel, gbcTimeBP);

		// Minute label
		JLabel minuteLabel = new JLabel("Minutes");
		minuteLabel.setFont(Fonts.FONT20.get());
		gbcTimeBP.gridheight = 1;
		gbcTimeBP.anchor = GridBagConstraints.LAST_LINE_START;
		gbcTimeBP.gridx = 2;
		gbcTimeBP.gridy = 0;
		timeBorderPanel.add(minuteLabel,gbcTimeBP);

		// Minute spinner
		SpinnerNumberModel numberModelMinutes = new SpinnerNumberModel(0, 0, 50, 10);
		minuteSpinner = new JSpinner(numberModelMinutes);
		minuteSpinner.addChangeListener(e -> minuteSpinnerChanged());
		JSpinner.NumberEditor editorMinutes = new JSpinner.NumberEditor(minuteSpinner);
		NumberFormatter formatterMinutes = (NumberFormatter) editorMinutes.getTextField().getFormatter();
		minuteSpinner.setEditor(editorMinutes);
		formatterMinutes.setAllowsInvalid(false);
		formatterMinutes.setOverwriteMode(true);
		minuteSpinner.setFont(Fonts.FONT20.get());
		minuteSpinner.setPreferredSize(new Dimension(panelWidth / 16, ToolPanel.getPanelHeight() / 2));
//		minuteSpinner.setBorder(new CompoundBorder(new LineBorder(darkGray, 1), new EmptyBorder(0, 10, 0, 0)));
		gbcTimeBP.anchor = GridBagConstraints.FIRST_LINE_START;
		gbcTimeBP.gridx = 2;
		gbcTimeBP.gridy = 1;
		timeBorderPanel.add(minuteSpinner, gbcTimeBP);
		setValueOfMinuteSpinner();

		// Time label
		JLabel timeLabel = new JLabel("Time");
		timeLabel.setFont(Fonts.FONT20.get());
		gbcTimeBP.anchor = GridBagConstraints.PAGE_END;
		gbcTimeBP.gridheight = 1;
		gbcTimeBP.weighty = 0.1;
		gbcTimeBP.gridwidth = 3;
		gbcTimeBP.gridx = 0;
		gbcTimeBP.gridy = 2;
		timeBorderPanel.add(timeLabel, gbcTimeBP);

		// Duration border panel
		durationBorderPanel = new ToolBorderPanel(350,5,350,120);
		//new ToolBorderPanel((int) ((panelWidth/8)*0.98),(int) (ToolPanel.getPanelHeight()*0.2),
		//(int) ((panelWidth/8)*0.98),(int) (ToolPanel.getPanelHeight()*0.96));
		durationBorderPanel.setPreferredSize(new Dimension(panelWidth/8, ToolPanel.getPanelHeight()));
		durationBorderPanel.setLayout(new GridBagLayout());
		GridBagConstraints gbcDurationBP = new GridBagConstraints();
		gbcDurationBP.weightx = 1;
		gbcDurationBP.weighty = 1;
		gbcTool.gridx = 2;
		gbcTool.fill = GridBagConstraints.BOTH;
		toolPanel.add(durationBorderPanel,gbcTool);

		// Duration spinner
		SpinnerNumberModel numberModelDuration = new SpinnerNumberModel(2,1,8,1);
		durationSpinner = new JSpinner(numberModelDuration);
		JSpinner.NumberEditor editorDuration = new JSpinner.NumberEditor(durationSpinner);
		NumberFormatter formatterDuration = (NumberFormatter) editorDuration.getTextField().getFormatter();
		formatterDuration.setAllowsInvalid(false);
		formatterDuration.setOverwriteMode(true);
		durationSpinner.setEditor(editorDuration);
		durationSpinner.setFont(Fonts.FONT20.get());
		durationSpinner.setPreferredSize(new Dimension(panelWidth/14, ToolPanel.getPanelHeight()/2));
//		durationSpinner.setBorder(new CompoundBorder(new LineBorder(darkGray, 1), new EmptyBorder(0, 10, 0, 0)));
		gbcDurationBP.anchor = GridBagConstraints.CENTER;
		gbcDurationBP.gridx = 0;
		gbcDurationBP.gridy = 0;
		durationBorderPanel.add(durationSpinner, gbcDurationBP);

		// Duration label
		JLabel durationLabel = new JLabel("Duration:");
		durationLabel.setFont(Fonts.FONT20.get());
		gbcDurationBP.anchor = GridBagConstraints.PAGE_END;
		gbcDurationBP.weighty = 0.5;
		gbcDurationBP.gridx = 0;
		gbcDurationBP.gridy = 1;
		durationBorderPanel.add(durationLabel,gbcDurationBP);

		// Calendar border panel
		calendarBorderPanel = new ToolBorderPanel(695,5,695,120);
		//new ToolBorderPanel((int) ((panelWidth/8)*0.98),(int) (ToolPanel.getPanelHeight()*0.2),
		//(int) ((panelWidth/8)*0.98),(int) (ToolPanel.getPanelHeight()*0.96));
		calendarBorderPanel.setPreferredSize(new Dimension(panelWidth/3, ToolPanel.getPanelHeight()));
		calendarBorderPanel.setLayout(new GridBagLayout());
		GridBagConstraints gbcCalendarBP = new GridBagConstraints();
		gbcCalendarBP.weightx = 1;
		gbcCalendarBP.weighty = 1;
		gbcTool.gridx = 0;
		gbcTool.fill = GridBagConstraints.BOTH;
		toolPanel.add(calendarBorderPanel,gbcTool);

		// DayBack button
		dayBackBtn = new FancyButtonMoreClick(ProjectColors.BLUE3.get(), ProjectColors.BLUE3.get(),
				ProjectColors.BLUE3.get(),ProjectColors.BLUE3.get(),ProjectColors.BLUE3.get());
		FontIcon leftArrowIcon = FontIcon.of(CoreUiFree.ARROW_LEFT);
		leftArrowIcon.setIconSize(100);
		dayBackBtn.setIcon(leftArrowIcon);
		dayBackBtn.addActionListener(e -> dayBackClicked());
		dayBackBtn.setPreferredSize(new Dimension(panelWidth/12, (int) (ToolPanel.getPanelHeight()*0.80)));
		gbcCalendarBP.anchor = GridBagConstraints.CENTER;
		gbcCalendarBP.gridheight = 2;
		gbcCalendarBP.gridx = 0;
		gbcCalendarBP.gridy = 0;
		calendarBorderPanel.add(dayBackBtn,gbcCalendarBP);

		//Calendar IconLabel
		JLabel calendarLabel = new JLabel();
		calendarLabel.setLayout(new BorderLayout());
		FontIcon calendarIcon = FontIcon.of(CoreUiFree.CALENDAR);
		calendarIcon.setIconSize(32);
		calendarLabel.setIcon(calendarIcon);
		calendarLabel.setVerticalAlignment(SwingConstants.CENTER);
		calendarLabel.setHorizontalAlignment(SwingConstants.CENTER);
//	    gbcCalendarBP.insets = new Insets(0,0,-panelWidth/80,0);
		gbcCalendarBP.gridx = 1;
		calendarBorderPanel.add(calendarLabel,gbcCalendarBP);

		// Date picker
		calendarModel = new UtilDateModel();
		Properties properties = new Properties();
		properties.put("text.today", "Today");
		properties.put("text.month", "Month");
		properties.put("text.year", "Year");
		JDatePanelImpl datePanel = new JDatePanelImpl(calendarModel, properties);
		datePicker = new JDatePickerImpl(datePanel, new DateLabelFormatter());
		datePicker.getComponent(0).setFont(Fonts.FONT20.get());
		datePicker.addActionListener(e -> DateChangeFromDatePicker());
		datePicker.setButtonFocusable(false);
		datePicker.setPreferredSize(new Dimension(panelWidth / 12, ToolPanel.getPanelHeight() / 3));
		datePicker.getComponent(0).setPreferredSize(new Dimension(panelWidth / 12, ToolPanel.getPanelHeight() / 3)); // JFormattedTextField
		datePicker.getComponent(1).setPreferredSize(new Dimension(40, ToolPanel.getPanelHeight() / 3));// JButton

	    gbcCalendarBP.insets = new Insets(0,-panelWidth/200,0,0);

		gbcCalendarBP.gridx = 2;
		calendarBorderPanel.add(datePicker,gbcCalendarBP);

		setDateOfDatePicker();

		// Now button
		nowBtn = new FancyButtonOneClick(ProjectColors.BLACK.get(), ProjectColors.RED.get(), ProjectColors.RED.get());
		nowBtn.setFont(font1);
		nowBtn.setBorderPainted(true);
		nowBtn.setPreferredSize(new Dimension(panelWidth / 32, ToolPanel.getPanelHeight() / 3));
		nowBtn.setText("now");
		nowBtn.setBorder(borderBlack);
		nowBtn.addActionListener(e -> nowBtnClicked());
	    gbcCalendarBP.insets = new Insets(0,-panelWidth/160,0,0);
		gbcCalendarBP.gridy = 0;
		gbcCalendarBP.gridx = 3;
		calendarBorderPanel.add(nowBtn,gbcCalendarBP);

		dayForwardBtn = new FancyButtonMoreClick(Color.white, ProjectColors.BLUE.get(), ProjectColors.BLUE.get(),
				ProjectColors.LIGHT_BLUE.get(), Color.white);
		FontIcon rightArrowIcon = FontIcon.of(CoreUiFree.ARROW_RIGHT);
		rightArrowIcon.setIconSize(100);
		dayForwardBtn.setIcon(rightArrowIcon);
		dayForwardBtn.addActionListener(e -> dayForwardClicked());
		dayForwardBtn.setPreferredSize(new Dimension(panelWidth / 12, (int) (ToolPanel.getPanelHeight() * 0.80)));
		gbcCalendarBP.anchor = GridBagConstraints.CENTER;
		gbcCalendarBP.insets = new Insets(0,0,0,0);
		gbcCalendarBP.gridy = 0;
		gbcCalendarBP.gridx = 4;
		calendarBorderPanel.add(dayForwardBtn,gbcCalendarBP);

		// Date label
		JLabel dateLbl = new JLabel("Date");  
		dateLbl.setFont(Fonts.FONT20.get());
		gbcCalendarBP.gridheight = 1;
		gbcCalendarBP.gridx = 0;
		gbcCalendarBP.gridwidth = 5;
		gbcCalendarBP.gridy = 1;
		gbcCalendarBP.anchor = GridBagConstraints.PAGE_END;
		calendarBorderPanel.add(dateLbl,gbcCalendarBP);

		// -----------------------------------------------
		// ------------------ MAIN PANEL ---------------------
		// -----------------------------------------------

		// -----------------------------------------------
		// ------------------ FOOTER PANEL ---------------------
		// -----------------------------------------------

		// footer panel
		FooterPanel footerPanel = new FooterPanel();
		add(footerPanel, BorderLayout.SOUTH);
		footerPanel.setLayout(new GridBagLayout());
		GridBagConstraints gbcFooter = new GridBagConstraints();
		gbcFooter.weightx = 0.2;
		gbcFooter.weighty = 0.5;

		JLabel layoutLbl = new JLabel("Layout:");
		layoutLbl.setFont(font1);
		gbcFooter.gridx = 0;
		gbcFooter.gridy = 0;
		gbcFooter.anchor = GridBagConstraints.LAST_LINE_START;
		footerPanel.add(layoutLbl, gbcFooter);

		layoutCB = new JComboBox<String>(new DefaultComboBoxModel());
		layoutCB.addActionListener(e -> switchLayout());
		layoutCB.setMaximumRowCount(5);
		layoutCB.setFont(font1);
		layoutCB.setPreferredSize(new Dimension(FooterPanel.panelWidth / 8, FooterPanel.panelHeight / 3));
		gbcFooter.anchor = GridBagConstraints.FIRST_LINE_START;
		gbcFooter.gridx = 0;
		gbcFooter.gridy = 1;
		footerPanel.add(layoutCB, gbcFooter);

		loadLayoutCB();

		makeReservationBtn = new FancyButtonOneClick(ProjectColors.BLACK.get(), ProjectColors.RED.get(),
				ProjectColors.RED.get());
		makeReservationBtn
				.setPreferredSize(new Dimension(FooterPanel.panelWidth / 6, (int) (FooterPanel.panelHeight * 0.8)));
		makeReservationBtn.setBorderPainted(true);
		makeReservationBtn.setFont(Fonts.FONT20.get());
		makeReservationBtn.addActionListener(e -> makeNewReservationClicked());
		makeReservationBtn.setBorder(borderBlack);
		makeReservationBtn.setText("Make Reservation");
		gbcFooter.anchor = GridBagConstraints.LINE_END;
		gbcFooter.gridy = 0;
		gbcFooter.gridx = 1;
		gbcFooter.gridheight = 2;
		gbcFooter.weightx = 1;
		footerPanel.add(makeReservationBtn, gbcFooter);
		
		// timer setup
		setAutomaticTime(true); // because its turned of in setup method
		startTimerForGettingReservationInfoFromDB();// 

	} // end of constructor
	
	private void DateChangeFromDatePicker() {
		setAutomaticTime(false);
		calendar.set(Calendar.YEAR, calendarModel.getYear());
		calendar.set(Calendar.MONTH, calendarModel.getMonth());
		calendar.set(Calendar.DAY_OF_MONTH, calendarModel.getDay());
	}

	private void setAutomaticTime(boolean state) {
		if(state == true) {
			automaticSystemTime = true;
		}
		else {
			automaticSystemTime = false;
		}
	}

	private void minuteSpinnerChanged() {
		setAutomaticTime(false);
		calendar.set(Calendar.MINUTE, (int) minuteSpinner.getValue());
	}

	private void hourSpinnerChanged() {
		setAutomaticTime(false);
		calendar.set(Calendar.HOUR_OF_DAY, (int) hourSpinner.getValue());
	}

	public void makeNewReservationClicked() {
		for(LayoutMiniPanel miniPanel :currentLayoutPanel.getMiniPanelMap().values()) {
			if(miniPanel.isSelected()) {
				selectedTables.add((Table) miniPanel.getLayoutItem());
			}
		}
		if(selectedTables.size() == 0) { // if nothing is selected
			JOptionPane.showMessageDialog(null, "You must select 1 or table in the overview if you want to make reservation!",
					"Error", JOptionPane.WARNING_MESSAGE);
		}
		else { // if 1 or more minipanel is selected
			new MakeReservationFrame(selectedTables, calendar, (Integer) personCB.getSelectedItem(), (Integer) durationSpinner.getValue());
		}
	}

	public void startTimerForGettingReservationInfoFromDB() {
		Timer timer = new Timer(); // create new timer
		System.out.println("automaticSystemTime is :" + automaticSystemTime);
		TimerTask task = new TimerTask() {
			@Override
			public void run() {
				if (automaticSystemTime == true) {// If user is not editing time by himself
					setCalendarCurrentTime();
					setValueOfMinuteSpinner();
					setValueOfHourSpinner();
					setDateOfDatePicker();
					for (RestaurantLayout rl : layoutList) {
						try {
							System.out.println("loading list");
							loadLayoutsReservedTableInfoListByTime(calendar);
						} catch (SQLException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
//					try {
////						wait(10000);// 10 seconds
//						setAvailabilityOfRestaurantLayout((Integer) personCB.getSelectedItem(), calendar,
//								(Integer) durationSpinner.getValue());
//					} catch (InterruptedException e) {
//						// TODO Auto-generated catch block
//						e.printStackTrace();
//					}
				}

			}// end of run

		}; // end of task
		timer.scheduleAtFixedRate(task, 60000, 60000);// set timer 1 minute
	}

	private void PersonComboBoxAction() {
		setAvailabilityOfRestaurantLayout((Integer) personCB.getSelectedItem(), calendar,
				(Integer) durationSpinner.getValue());
	}

	private void setAvailabilityOfRestaurantLayout(int noOfPerson, Calendar calendar, int duration) {
		for (LayoutPanel panel : layoutPanelMap.values()) {
			panel.setAvailabilityOfTables(noOfPerson, calendar, duration);
			this.repaint();
			this.revalidate();
		}
	}

	private void dayBackClicked() {
		setAutomaticTime(false);
		calendar.add(Calendar.DAY_OF_MONTH, -1);
		setValueOfMinuteSpinner();
		setValueOfHourSpinner();
		setDateOfDatePicker();
		setAvailabilityOfRestaurantLayout((Integer) personCB.getSelectedItem(), calendar,
				(Integer) durationSpinner.getValue());
	}

	private void dayForwardClicked() {
		setAutomaticTime(false);
		calendar.add(Calendar.DAY_OF_MONTH, +1);
		setValueOfMinuteSpinner();
		setValueOfHourSpinner();
		setDateOfDatePicker();
		setAvailabilityOfRestaurantLayout((Integer) personCB.getSelectedItem(), calendar,
				(Integer) durationSpinner.getValue());
	}

	private void nowBtnClicked() {
		setAutomaticTime(true);
		setCalendarCurrentTime();
		setValueOfMinuteSpinner();
		setValueOfHourSpinner();
		setDateOfDatePicker();
		setAvailabilityOfRestaurantLayout((Integer) personCB.getSelectedItem(), calendar,
				(Integer) durationSpinner.getValue());
	}

	private void setCalendarCurrentTime() {
		calendar = Calendar.getInstance();
		int minute = calendar.get(Calendar.MINUTE);
		minute = (int) (Math.round(minute/10.0) * 10) ;
		calendar.set(Calendar.MINUTE,minute);
	}

	private void setValueOfMinuteSpinner() {
		minuteSpinner.setValue(calendar.get(Calendar.MINUTE));
	}

	private void setValueOfHourSpinner() {
		hourSpinner.setValue(calendar.get(Calendar.HOUR_OF_DAY));
	}

	private void setDateOfDatePicker() {
		Date date = new Date(calendar.getTimeInMillis());
		calendarModel.setDate(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
				calendar.get(Calendar.DAY_OF_MONTH));
		calendarModel.setSelected(true);
	}

	public void deleteReservation(int reservationId) {
		new Thread(() -> {
			try {
				reservationController.deleteReservation(reservationId); // TREBA TO VYMAZAT AJ Z ReservedTableInfoList
																		// ... mozno treba jednotny calendar
				// refreshReservedTableInfoMap(); TO DO!
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}).start();
	}

	private void loadLayoutsReservedTableInfoListByTime(Calendar calendar) throws SQLException {
		new Thread(() -> {
			try {
				for(RestaurantLayout rl : layoutList) {
				reservedTableInfoMap.get(rl.getName()).clear();
				reservedTableInfoMap.get(rl.getName())
						.addAll(reservationController.getReservedTableInfo((int) rl.getId(), calendar));
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}).start();
	}

	private void loadLayouts(int noOfPerson, Calendar calendar, int duration) {
		reservedTableInfoMap = new HashMap<>();
		layoutPanelMap = new HashMap<>();
		// fill the map with panels ... Layout panel setup
		for (RestaurantLayout rl : layoutList) {

			reservedTableInfoMap.put(rl.getName(), new ArrayList<ReservedTableInfo>());
			try {
				loadLayoutsReservedTableInfoListByTime(calendar);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			LayoutPanel layoutPanel = new LayoutPanel();
			layoutPanel.loadExistingMiniPanels(rl);
			layoutPanel.updateReservedTablesByTime(rl, reservedTableInfoMap.get(rl.getName()));
			layoutPanel.setAvailabilityOfTables(noOfPerson, calendar, duration);
			layoutPanelMap.put(rl.getName(), layoutPanel);

		} // end of loop

		// if there is any layout in DB
		if (layoutList.size() != 0) {
			currentLayoutPanel = layoutPanelMap.get(layoutList.get(0).getName());
			this.add(currentLayoutPanel, BorderLayout.CENTER);
		}
		// if there is not layout in DB ... need to be done
		else {

		}
	}

	private void loadLayoutCB() {
		for (RestaurantLayout rl : layoutList) {
			layoutCB.addItem(rl.getName());
		}
		layoutCB.setSelectedItem(layoutList.get(0).getName());
	}

	private void switchLayout() {
		currentLayoutPanel.deselectAllMiniPanels();
		this.remove(currentLayoutPanel);
		currentLayoutPanel = layoutPanelMap.get(layoutCB.getSelectedItem().toString());
		this.add(currentLayoutPanel);
		this.repaint();
		this.revalidate();
	}

	private void populatePersonCB() {
		for (int i = 1; i < 30; i++) {
			personCB.addItem(i);
		}
	}

	public static OverviewPanel getInstance() {
		if (instance == null)
			return instance = new OverviewPanel();
		else
			return instance;
	}

}
