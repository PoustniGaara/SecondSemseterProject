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
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingConstants;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;
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

@SuppressWarnings("serial")
public class OverviewPanel extends JPanel {

	private static OverviewPanel instance;
	private JComboBox<String> layoutCB;
	private JSpinner hourSpinner, minuteSpinner, durationSpinner;
	private JComboBox<Integer> personCB;
	private FancyButtonMoreClick dayBackBtn, dayForwardBtn;
	private JButton nowBtn, makeReservationBtn;
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
	private ToolBorderPanel personBorderPanel, timeBorderPanel, layoutBorderPanel, durationBorderPanel,
			calendarBorderPanel;
	private ArrayList<Table> selectedTables;
	private JLabel systemTimeLabel;

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
		setBackground(Color.white);
		setLayout(new BorderLayout());
		setVisible(true);

		// load data
		selectedTables = new ArrayList<Table>();
		try {
			layoutList = (ArrayList<RestaurantLayout>) restaurantLayoutController.read();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		setCalendarCurrentTime();
		loadLayouts(1, calendar, 2);

		// -----------------------------------------------
		// ------------------ TOOL ---------------------
		// -----------------------------------------------

		// Tool Panel
		ToolPanel toolPanel = new ToolPanel();
		GridBagConstraints gbcTool = new GridBagConstraints();
		toolPanel.setBackground(ProjectColors.BG.get());
		gbcTool.weightx = 1;
		gbcTool.weighty = 1;
		add(toolPanel, BorderLayout.NORTH);

		// Calendar border panel
		calendarBorderPanel = new ToolBorderPanel(panelWidth / 3, ToolPanel.getPanelHeight(), true);
//		calendarBorderPanel.setBackground(Color.blue);
//		calendarBorderPanel.setPreferredSize(new Dimension(panelWidth / 3, ToolPanel.getPanelHeight()));
		calendarBorderPanel.setLayout(new GridBagLayout());
		GridBagConstraints gbcCalendarBP = new GridBagConstraints();
		gbcCalendarBP.weightx = 1;
		gbcCalendarBP.weighty = 1;
		gbcTool.gridx = 0;
		gbcTool.fill = GridBagConstraints.BOTH;
		toolPanel.add(calendarBorderPanel, gbcTool);

		// DayBack button
		dayBackBtn = new FancyButtonMoreClick(ProjectColors.BLUE3.get(), ProjectColors.BLUE3.get(),
				ProjectColors.BLUE3.get(), ProjectColors.BLUE3.get(), ProjectColors.BLUE3.get());
		FontIcon leftArrowIcon = FontIcon.of(CoreUiFree.ARROW_LEFT);
		leftArrowIcon.setIconSize(100);
		dayBackBtn.setIcon(leftArrowIcon);
		dayBackBtn.addActionListener(e -> dayBackClicked());
		dayBackBtn.setPreferredSize(new Dimension(panelWidth / 12, (int) (ToolPanel.getPanelHeight() * 0.80)));
		gbcCalendarBP.anchor = GridBagConstraints.CENTER;
		gbcCalendarBP.gridheight = 2;
		gbcCalendarBP.gridx = 0;
		gbcCalendarBP.gridy = 0;
		calendarBorderPanel.add(dayBackBtn, gbcCalendarBP);

		// Calendar IconLabel
		JLabel calendarLabel = new JLabel();
		calendarLabel.setLayout(new BorderLayout());
		FontIcon calendarIcon = FontIcon.of(CoreUiFree.CALENDAR);
		calendarIcon.setIconSize(32);
		calendarLabel.setIcon(calendarIcon);
		calendarLabel.setVerticalAlignment(SwingConstants.CENTER);
		calendarLabel.setHorizontalAlignment(SwingConstants.CENTER);
//	    gbcCalendarBP.insets = new Insets(0,0,-panelWidth/80,0);
		gbcCalendarBP.gridx = 1;
		calendarBorderPanel.add(calendarLabel, gbcCalendarBP);

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

		gbcCalendarBP.insets = new Insets(0, -panelWidth / 200, 0, 0);

		gbcCalendarBP.gridx = 2;
		calendarBorderPanel.add(datePicker, gbcCalendarBP);

		setDateOfDatePicker();

		// Now button
		nowBtn = new FancyButtonOneClick(ProjectColors.BLACK.get(), ProjectColors.RED.get(), ProjectColors.RED.get());
		nowBtn.setFont(Fonts.FONT18.get());
		nowBtn.setBackground(ProjectColors.BLUE.get());
		nowBtn.setForeground(Color.white);
		nowBtn.setBorderPainted(true);
		nowBtn.setPreferredSize(new Dimension(panelWidth / 26, ToolPanel.getPanelHeight() / 3));
		nowBtn.setText("Now");
		nowBtn.addActionListener(e -> nowBtnClicked());
		gbcCalendarBP.insets = new Insets(0, -panelWidth / 160, 0, 0);
		gbcCalendarBP.gridy = 0;
		gbcCalendarBP.gridx = 3;
		calendarBorderPanel.add(nowBtn, gbcCalendarBP);

		dayForwardBtn = new FancyButtonMoreClick(Color.white, ProjectColors.BLUE.get(), ProjectColors.BLUE.get(),
				ProjectColors.LIGHT_BLUE.get(), Color.white);
		FontIcon rightArrowIcon = FontIcon.of(CoreUiFree.ARROW_RIGHT);
		rightArrowIcon.setIconSize(100);
		dayForwardBtn.setIcon(rightArrowIcon);
		dayForwardBtn.addActionListener(e -> dayForwardClicked());
		dayForwardBtn.setPreferredSize(new Dimension(panelWidth / 12, (int) (ToolPanel.getPanelHeight() * 0.80)));
		gbcCalendarBP.anchor = GridBagConstraints.CENTER;
		gbcCalendarBP.insets = new Insets(0, 0, 0, 0);
		gbcCalendarBP.gridy = 0;
		gbcCalendarBP.gridx = 4;
		calendarBorderPanel.add(dayForwardBtn, gbcCalendarBP);

		// Date label
		JLabel dateLbl = new JLabel("Date");
		dateLbl.setFont(Fonts.FONT15.get());
		gbcCalendarBP.gridheight = 1;
		gbcCalendarBP.gridx = 0;
		gbcCalendarBP.gridwidth = 5;
		gbcCalendarBP.gridy = 1;
		gbcCalendarBP.anchor = GridBagConstraints.PAGE_END;
		calendarBorderPanel.add(dateLbl, gbcCalendarBP);

		// Time border panel
		timeBorderPanel = new ToolBorderPanel(panelWidth / 4, ToolPanel.getPanelHeight(), true);
		timeBorderPanel.setLayout(new GridBagLayout());
		GridBagConstraints gbcTimeBP = new GridBagConstraints();
		gbcTimeBP.weightx = 1;
		gbcTimeBP.weighty = 1;
		gbcTool.gridx = 1;
		gbcTool.gridy = 0;
		gbcTool.fill = GridBagConstraints.BOTH;
		toolPanel.add(timeBorderPanel, gbcTool);

		// Hour label
		JLabel hourLabel = new JLabel("Hours");
		hourLabel.setFont(Fonts.FONT15.get());
		gbcTimeBP.anchor = GridBagConstraints.LAST_LINE_START;
		gbcTimeBP.gridx = 0;
		gbcTimeBP.gridy = 0;
		timeBorderPanel.add(hourLabel, gbcTimeBP);

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
		hourSpinner.setPreferredSize(new Dimension(panelWidth / 19, ToolPanel.getPanelHeight() / 3));
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
		minuteLabel.setFont(Fonts.FONT15.get());
		gbcTimeBP.gridheight = 1;
		gbcTimeBP.anchor = GridBagConstraints.LAST_LINE_START;
		gbcTimeBP.gridx = 2;
		gbcTimeBP.gridy = 0;
		timeBorderPanel.add(minuteLabel, gbcTimeBP);

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
		minuteSpinner.setPreferredSize(new Dimension(panelWidth / 19, ToolPanel.getPanelHeight() / 3));
		gbcTimeBP.anchor = GridBagConstraints.FIRST_LINE_START;
		gbcTimeBP.gridx = 2;
		gbcTimeBP.gridy = 1;
		timeBorderPanel.add(minuteSpinner, gbcTimeBP);
		setValueOfMinuteSpinner();

		// Time label
		JLabel timeLabel = new JLabel("Time");
		timeLabel.setFont(Fonts.FONT15.get());
		gbcTimeBP.anchor = GridBagConstraints.PAGE_END;
		gbcTimeBP.gridheight = 1;
		gbcTimeBP.weighty = 0.1;
		gbcTimeBP.gridwidth = 3;
		gbcTimeBP.gridx = 0;
		gbcTimeBP.gridy = 2;
		timeBorderPanel.add(timeLabel, gbcTimeBP);

		// Duration Border panel
		durationBorderPanel = new ToolBorderPanel(panelWidth / 8, ToolPanel.getPanelHeight(), true);
		durationBorderPanel.setLayout(new GridBagLayout());
		GridBagConstraints gbcDurationBP = new GridBagConstraints();
		gbcDurationBP.weightx = 1;
		gbcDurationBP.weighty = 1;
		gbcTool.gridx = 2;
		gbcTool.gridy = 0;
		gbcTool.fill = GridBagConstraints.BOTH;
		toolPanel.add(durationBorderPanel, gbcTool);

		// Duration label2
		JLabel durationLabel2 = new JLabel("");
		durationLabel2.setFont(Fonts.FONT15.get());
		gbcDurationBP.anchor = GridBagConstraints.LAST_LINE_START;
		gbcDurationBP.gridx = 0;
		gbcDurationBP.gridy = 0;
		durationBorderPanel.add(durationLabel2, gbcDurationBP);

		// Duration spinner
		SpinnerNumberModel numberModelDuration = new SpinnerNumberModel(2, 1, 8, 1);
		durationSpinner = new JSpinner(numberModelDuration);
		JSpinner.NumberEditor editorDuration = new JSpinner.NumberEditor(durationSpinner);
		NumberFormatter formatterDuration = (NumberFormatter) editorDuration.getTextField().getFormatter();
		formatterDuration.setAllowsInvalid(false);
		formatterDuration.setOverwriteMode(true);
		durationSpinner.setEditor(editorDuration);
		durationSpinner.setFont(Fonts.FONT20.get());
		durationSpinner.setPreferredSize(new Dimension(panelWidth / 19, ToolPanel.getPanelHeight() / 3));
		gbcDurationBP.anchor = GridBagConstraints.FIRST_LINE_START;
		gbcDurationBP.gridx = 0;
		gbcDurationBP.gridy = 1;
		durationBorderPanel.add(durationSpinner, gbcDurationBP);

		// Duration label
		JLabel durationLabel = new JLabel("Duration");
		durationLabel.setFont(Fonts.FONT15.get());
		gbcDurationBP.anchor = GridBagConstraints.CENTER;
		gbcDurationBP.gridx = 0;
		gbcDurationBP.gridy = 2;
		durationBorderPanel.add(durationLabel, gbcDurationBP);

		// Person border panel
		personBorderPanel = new ToolBorderPanel(panelWidth / 8, ToolPanel.getPanelHeight(), true);
		personBorderPanel.setLayout(new GridBagLayout());
		GridBagConstraints gbcPersonBP = new GridBagConstraints();
		gbcPersonBP.weightx = 1;
		gbcPersonBP.weighty = 1;
		gbcPersonBP.insets = new Insets(0, (panelWidth / 8) / 3, 0, 0);
		gbcTool.gridx = 3;
		gbcTool.gridy = 0;
		gbcTool.fill = GridBagConstraints.BOTH;
		toolPanel.add(personBorderPanel, gbcTool);

		// Number of guest label
		JLabel noOfGuestLabel = new JLabel("");
		noOfGuestLabel.setFont(Fonts.FONT15.get());
		gbcPersonBP.anchor = GridBagConstraints.LAST_LINE_START;
		gbcPersonBP.gridx = 0;
		gbcPersonBP.gridy = 0;
		personBorderPanel.add(noOfGuestLabel, gbcPersonBP);

		// Person ComboBox
		personCB = new JComboBox<Integer>(new DefaultComboBoxModel<Integer>());
		populatePersonCB();
		personCB.setMaximumRowCount(5);
		personCB.addActionListener(e -> PersonComboBoxAction());
		personCB.setFont(Fonts.FONT20.get());
		personCB.setPreferredSize(new Dimension(panelWidth / 19, ToolPanel.getPanelHeight() / 3));
		gbcPersonBP.anchor = GridBagConstraints.FIRST_LINE_START;
		gbcPersonBP.gridx = 0;
		gbcPersonBP.gridy = 1;
		personBorderPanel.add(personCB, gbcPersonBP);

		// Guest Label
		JLabel guestLabel = new JLabel("Guests");
		guestLabel.setFont(Fonts.FONT15.get());
		gbcPersonBP.insets = new Insets(0, 0, 0, 0);
		gbcPersonBP.weighty = 0.1;
		gbcPersonBP.anchor = GridBagConstraints.PAGE_END;
		gbcPersonBP.gridx = 0;
		gbcPersonBP.gridy = 2;
		personBorderPanel.add(guestLabel, gbcPersonBP);

		// Layout border panel
		layoutBorderPanel = new ToolBorderPanel(panelWidth / 8, ToolPanel.getPanelHeight(), false);
		layoutBorderPanel.setLayout(new GridBagLayout());
		GridBagConstraints gbclayoutBP = new GridBagConstraints();
		gbclayoutBP.weightx = 1;
		gbclayoutBP.weighty = 1;
		gbcTool.gridx = 4;
		gbcTool.gridy = 0;
		gbcTool.fill = GridBagConstraints.BOTH;
		toolPanel.add(layoutBorderPanel, gbcTool);

		JLabel layoutLbl = new JLabel("");
		layoutLbl.setFont(Fonts.FONT15.get());
		gbclayoutBP.insets = new Insets(0, (panelWidth / 8) / 3, 0, 0);
		gbclayoutBP.gridx = 0;
		gbclayoutBP.gridy = 0;
		gbclayoutBP.anchor = GridBagConstraints.LAST_LINE_START;
		layoutBorderPanel.add(layoutLbl, gbclayoutBP);

		layoutCB = new JComboBox<String>(new DefaultComboBoxModel<String>());
		layoutCB.addActionListener(e -> switchLayout());
		layoutCB.setMaximumRowCount(5);
		layoutCB.setFont(Fonts.FONT20.get());
		layoutCB.setPreferredSize(new Dimension(panelWidth / 9, ToolPanel.getPanelHeight() / 3));
		gbclayoutBP.anchor = GridBagConstraints.FIRST_LINE_START;
		gbclayoutBP.gridx = 0;
		gbclayoutBP.gridy = 1;
		layoutBorderPanel.add(layoutCB, gbclayoutBP);
		loadLayoutCB();

		// Layout label
		JLabel layoutLabel = new JLabel("Layouts");
		layoutLabel.setFont(Fonts.FONT15.get());
		gbclayoutBP.insets = new Insets(0, 0, 0, 0);
		gbclayoutBP.anchor = GridBagConstraints.PAGE_END;
		gbclayoutBP.weighty = 0.1;
		gbclayoutBP.gridwidth = 1;
		gbclayoutBP.gridx = 0;
		gbclayoutBP.gridy = 1;
		layoutBorderPanel.add(layoutLabel, gbclayoutBP);

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

		systemTimeLabel = new JLabel("");
		setSystemTimeLabel();
		gbcFooter.gridy = 0;
		gbcFooter.gridx = 0;
		systemTimeLabel.setFont(Fonts.FONT15.get());
		gbcFooter.anchor = GridBagConstraints.LINE_START;
		footerPanel.add(systemTimeLabel, gbcFooter);

		gbcFooter.anchor = GridBagConstraints.LINE_START;
		gbcFooter.gridy = 1;
		JLabel conCheck = ConnectionCheckLabel.getInstance();
		footerPanel.add(conCheck, gbcFooter);

		makeReservationBtn = new JButton();
		makeReservationBtn.setBackground(ProjectColors.BLUE.get());
		makeReservationBtn.setForeground(Color.white);
		makeReservationBtn
				.setPreferredSize(new Dimension(FooterPanel.panelWidth / 6, (int) (FooterPanel.panelHeight * 0.6)));
//		makeReservationBtn.setBorderPainted(true);
		makeReservationBtn.setFont(Fonts.FONT18.get());
		makeReservationBtn.addActionListener(e -> makeNewReservationClicked());
		makeReservationBtn.setText("Make Reservation");
		gbcFooter.anchor = GridBagConstraints.LINE_END;
		gbcFooter.insets = new Insets(0, 0, 0, FooterPanel.panelWidth / 64);
		gbcFooter.gridy = 0;
		gbcFooter.gridx = 1;
		gbcFooter.gridheight = 2;
		gbcFooter.weightx = 1;
		footerPanel.add(makeReservationBtn, gbcFooter);

		// timer setup
		setAutomaticTime(true); // because its turned of in setup method
		startTimerForGettingReservationInfoFromDB();//

	} // end of constructor

	private void setSystemTimeLabel() {
		if (getAutomaticTime() == false) {
			systemTimeLabel.setText("System automatic time: OFF");
//			systemTimeLabel.setForeground(new Color(220, 48, 48));// red
		} else {
			systemTimeLabel.setText("System automatic time: ON");
//			systemTimeLabel.setForeground(new Color(56, 193, 114));// green
		}
	}

	private void DateChangeFromDatePicker() {
		setAutomaticTime(false);
		calendar.set(Calendar.YEAR, calendarModel.getYear());
		calendar.set(Calendar.MONTH, calendarModel.getMonth());
		calendar.set(Calendar.DAY_OF_MONTH, calendarModel.getDay());
		try {
			getLayoutsReservedTableInfoListByTime(calendar);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		loadUpdatedReservedTableInfo();
		setAvailabilityOfRestaurantLayout((Integer) personCB.getSelectedItem(), calendar,
				(Integer) durationSpinner.getValue());
	}

	private void setAutomaticTime(boolean state) { // for now set label also
		if (state == true) {
			automaticSystemTime = true;
		} else {
			automaticSystemTime = false;
		}
		setSystemTimeLabel();
	}

	private void minuteSpinnerChanged() {
		if (minuteSpinner != null && durationSpinner != null) {
			setAutomaticTime(false);
			calendar.set(Calendar.MINUTE, (int) minuteSpinner.getValue());
			try {
				getLayoutsReservedTableInfoListByTime(calendar);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			loadUpdatedReservedTableInfo();
			setAvailabilityOfRestaurantLayout((Integer) personCB.getSelectedItem(), calendar,
					(Integer) durationSpinner.getValue());
		}
	}

	private void hourSpinnerChanged() {
		if (hourSpinner != null && durationSpinner != null) {
			setAutomaticTime(false);
			calendar.set(Calendar.HOUR_OF_DAY, (int) hourSpinner.getValue());
			try {
				getLayoutsReservedTableInfoListByTime(calendar);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			loadUpdatedReservedTableInfo();
			setAvailabilityOfRestaurantLayout((Integer) personCB.getSelectedItem(), calendar,
					(Integer) durationSpinner.getValue());
		}
	}

	public void makeNewReservationClicked() {
		for (LayoutMiniPanel miniPanel : currentLayoutPanel.getMiniPanelMap().values()) {
			if (miniPanel.isSelected()) {
				selectedTables.add((Table) miniPanel.getLayoutItem());
			}
		}
		if (selectedTables.size() == 0) { // if nothing is selected
			JOptionPane.showMessageDialog(null,
					"You must select 1 or table in the overview if you want to make reservation!", "Error",
					JOptionPane.WARNING_MESSAGE);
		} else { // if 1 or more minipanel is selected
			new MakeReservationFrame(selectedTables, calendar, (Integer) personCB.getSelectedItem(),
					(Integer) durationSpinner.getValue());
		}
	}

	public void startTimerForGettingReservationInfoFromDB() {
		Timer timer = new Timer(); // create new timer
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
							getLayoutsReservedTableInfoListByTime(calendar);
						} catch (SQLException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
					loadUpdatedReservedTableInfo();
					setAvailabilityOfRestaurantLayout((Integer) personCB.getSelectedItem(), calendar,
							(Integer) durationSpinner.getValue());
				}

			}// end of run

		}; // end of task
		timer.scheduleAtFixedRate(task, 60000, 60000);// set timer 1 minute
	}

	private void PersonComboBoxAction() {
		try {
			getLayoutsReservedTableInfoListByTime(calendar);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		loadUpdatedReservedTableInfo();
		setAvailabilityOfRestaurantLayout((Integer) personCB.getSelectedItem(), calendar,
				(Integer) durationSpinner.getValue());
	}

	private void loadUpdatedReservedTableInfo() {
		for (RestaurantLayout rl : layoutList) {
			layoutPanelMap.get(rl.getName()).updateReservedTablesByTime(rl, reservedTableInfoMap.get(rl.getName()));
		}
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
		setDateOfDatePicker();
		loadUpdatedReservedTableInfo();
		setAvailabilityOfRestaurantLayout((Integer) personCB.getSelectedItem(), calendar,
				(Integer) durationSpinner.getValue());
	}

	private void dayForwardClicked() {
		setAutomaticTime(false);
		calendar.add(Calendar.DAY_OF_MONTH, +1);
		setDateOfDatePicker();
		loadUpdatedReservedTableInfo();
		setAvailabilityOfRestaurantLayout((Integer) personCB.getSelectedItem(), calendar,
				(Integer) durationSpinner.getValue());
	}

	private void nowBtnClicked() {
		setAutomaticTime(true);
		setCalendarCurrentTime();
		setValueOfMinuteSpinner();
		setValueOfHourSpinner();
		setDateOfDatePicker();
		try {
			getLayoutsReservedTableInfoListByTime(calendar);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		loadUpdatedReservedTableInfo();
		setAvailabilityOfRestaurantLayout((Integer) personCB.getSelectedItem(), calendar,
				(Integer) durationSpinner.getValue());
	}

	private void setCalendarCurrentTime() {
		calendar = Calendar.getInstance();
		int minute = calendar.get(Calendar.MINUTE);
		minute = (int) (Math.round(minute / 10.0) * 10);
		calendar.set(Calendar.MINUTE, minute);
	}

	private void setValueOfMinuteSpinner() {
		minuteSpinner.setValue(calendar.get(Calendar.MINUTE));
	}

	private void setValueOfHourSpinner() {
		hourSpinner.setValue(calendar.get(Calendar.HOUR_OF_DAY));
	}

	private void setDateOfDatePicker() {
//		Date date = new Date(calendar.getTimeInMillis());
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

	private void getLayoutsReservedTableInfoListByTime(Calendar calendar) throws SQLException {
		Thread dbThread = new Thread(new Runnable() {
			public void run() {
				try {
					for (RestaurantLayout rl : layoutList) {
						if (reservedTableInfoMap.values().size() != 0 && durationSpinner != null)
							reservedTableInfoMap.get(rl.getName()).clear();
						reservedTableInfoMap.get(rl.getName()).addAll(reservationController
								.getReservedTableInfo((int) rl.getId(), calendar, (int) durationSpinner.getValue()));
					}
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		});

		dbThread.start();
		try {
			dbThread.join();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void refreshLayouts() {
		// refresh is called only after is the first load done
		int noOfPerson = (int) personCB.getSelectedItem();
		int duration = (int) durationSpinner.getValue();
		layoutList.clear();
		new Thread(() -> {
			try {
				layoutList = (ArrayList<RestaurantLayout>) restaurantLayoutController.read();
				loadLayouts(noOfPerson, calendar, duration);
				refreshLayoutCB();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
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
				getLayoutsReservedTableInfoListByTime(calendar);
			} catch (SQLException e) {
				e.printStackTrace();
			}
			LayoutPanel layoutPanel = new LayoutPanel();
			layoutPanel.loadExistingMiniPanels(rl);
			layoutPanel.updateReservedTablesByTime(rl, reservedTableInfoMap.get(rl.getName()));
			layoutPanel.setAvailabilityOfTables(noOfPerson, calendar, duration); // second inspect
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

	private void refreshLayoutCB() {
		layoutCB.removeAllItems();
		for (RestaurantLayout rl : layoutList) {
			layoutCB.addItem(rl.getName());
		}
		layoutCB.setSelectedItem(layoutList.get(0).getName());
	}

	private void loadLayoutCB() {
		for (RestaurantLayout rl : layoutList) {
			layoutCB.addItem(rl.getName());
		}
		layoutCB.setSelectedItem(layoutList.get(0).getName());
	}

	private void switchLayout() {
		if (layoutCB.getSelectedItem() != null) {
			currentLayoutPanel.deselectAllMiniPanels();
			this.remove(currentLayoutPanel);
			currentLayoutPanel = layoutPanelMap.get(layoutCB.getSelectedItem().toString());
			this.add(currentLayoutPanel);
			this.repaint();
			this.revalidate();
		}
	}

	private void populatePersonCB() {
		for (int i = 1; i < 30; i++) {
			personCB.addItem(i);
		}
	}

	private boolean getAutomaticTime() {
		return automaticSystemTime;
	}

	public static OverviewPanel getInstance() {
		if (instance == null)
			return instance = new OverviewPanel();
		else
			return instance;
	}

}
