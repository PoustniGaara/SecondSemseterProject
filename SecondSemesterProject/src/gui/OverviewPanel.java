package gui;

import java.awt.BorderLayout;
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

import javax.swing.BorderFactory;
import javax.swing.DefaultComboBoxModel;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerDateModel;
import javax.swing.SpinnerNumberModel;
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
import gui.layout.LayoutPanel;
import gui.layout.NoLayoutInfoPanel;
import gui.tools.DateLabelFormatter;
import gui.tools.FancyButtonMoreClick;
import gui.tools.FancyButtonOneClick;
import gui.tools.Fonts;
import gui.tools.ProjectColors;
import model.LayoutItem;
import model.Reservation;
import model.ReservedTableInfo;
import model.RestaurantLayout;
import model.Table;

public class OverviewPanel extends JPanel {
	
	private static OverviewPanel instance;
	private JComboBox<String> timeCB, layoutCB;
	private JSpinner hourSpinner, minuteSpinner;
	private JComboBox<Integer> personCB, durationCB;
	private FancyButtonMoreClick dayBackBtn, dayForwardBtn;
	private FancyButtonOneClick nowBtn,calendarBtn,makeReservationBtn;
	private Calendar calendar;
	private ArrayList<RestaurantLayout> layoutList;	
	private RestaurantLayoutController restaurantLayoutController;
	private HashMap<String, LayoutPanel> layoutPanelMap;
	private LayoutPanel currentLayoutPanel;
	private ReservationController reservationController;
	private HashMap<String, ArrayList<ReservedTableInfo>> reservedTableInfoMap;
	private UtilDateModel calendarModel;
	private JDatePickerImpl datePicker;
	
	
	private OverviewPanel() {
		
		//control
		restaurantLayoutController = new RestaurantLayoutController();
		reservationController = new ReservationController();
		
		//Panel dimensions setup
		int mainHeight = (int)MainFrame.height;
		int mainWidth = (int)MainFrame.width;
		int panelHeight = (int) ((int)mainHeight*0.84);
		int panelWidth = mainWidth;
		int toolPanelHeight = (int) (panelHeight*0.12);
		
		//Panel functional setup
		setPreferredSize(new Dimension(panelWidth, panelHeight));
		setBackground(ProjectColors.GREY.get());
		setLayout(new BorderLayout());
		setVisible(true);
		
		//load data
		try {
			layoutList = (ArrayList<RestaurantLayout>) restaurantLayoutController.read();
			System.out.print("RL FIRST ID: "+layoutList.get(0).getId());
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		loadLayouts();
		setCalendarCurrentTime();
		
		//Panel support classes
		Border borderGreen = BorderFactory.createLineBorder(ProjectColors.GREEN.get(), 1);
		Border borderBlack = BorderFactory.createLineBorder(ProjectColors.BLACK.get(), 2);
		Border borderRed = BorderFactory.createLineBorder(ProjectColors.RED.get(), 2);
		Font font1 = new Font("Monaco", Font.BOLD, 20);
		
		// -----------------------------------------------
		// ------------------ TOOL ---------------------
		// -----------------------------------------------
		
		//Tool Panel
		ToolPanel toolPanel = new ToolPanel();
//		toolPanel.setBorder(borderGreen);
		GridBagConstraints gbcTool = new GridBagConstraints();
		gbcTool.weightx = 1;
		gbcTool.weighty = 0;
		add(toolPanel, BorderLayout.NORTH);
		
		JLabel personLabel = new JLabel("No. of persons:");
		personLabel.setFont(font1);
		gbcTool.anchor = GridBagConstraints.LAST_LINE_START;
		gbcTool.gridx = 0;
		gbcTool.gridy = 0;
		toolPanel.add(personLabel,gbcTool);
		
		personCB = new JComboBox(new DefaultComboBoxModel());
		populatePersonCB();
		personCB.setMaximumRowCount(5);
		personCB.setFont(font1);
		personCB.setPreferredSize(new Dimension(panelWidth/12, ToolPanel.getPanelHeight()/3));
		gbcTool.anchor = GridBagConstraints.FIRST_LINE_START;
		gbcTool.gridx = 0;
		gbcTool.gridy = 1;
		toolPanel.add(personCB,gbcTool);
		
		dayBackBtn = new FancyButtonMoreClick(ProjectColors.BLACK.get(), ProjectColors.GREEN.get(),
				ProjectColors.GREY.get(),ProjectColors.GREY.get());
		FontIcon leftArrowIcon = FontIcon.of(CoreUiFree.ARROW_LEFT);
		leftArrowIcon.setIconSize(100);
		dayBackBtn.setIcon(leftArrowIcon);
		dayBackBtn.setPreferredSize(new Dimension(panelWidth/10, (int) (ToolPanel.getPanelHeight()*0.80)));
		gbcTool.anchor = GridBagConstraints.CENTER;
		gbcTool.gridheight = 2;
		gbcTool.gridx = 1;
		gbcTool.gridy = 0;
		toolPanel.add(dayBackBtn,gbcTool);
		
		JLabel durationLabel = new JLabel("Duration:");
		durationLabel.setFont(font1);
		gbcTool.anchor = GridBagConstraints.LAST_LINE_START;
		gbcTool.gridx = 2;
		gbcTool.gridy = 0;
		gbcTool.gridheight = 1;
		toolPanel.add(durationLabel,gbcTool);
		
		durationCB = getDurationCB();
		durationCB.setMaximumRowCount(5);
		durationCB.setFont(font1);
		durationCB.setPreferredSize(new Dimension(panelWidth/12, ToolPanel.getPanelHeight()/3));
		gbcTool.anchor = GridBagConstraints.FIRST_LINE_START;
		gbcTool.gridx = 2;
		gbcTool.gridy = 1;
		toolPanel.add(durationCB,gbcTool);
		
		// Hour label
		JLabel hourLabel = new JLabel("Hour:");
		hourLabel.setFont(font1);
		gbcTool.anchor = GridBagConstraints.LAST_LINE_START;
		gbcTool.gridx = 3;
		gbcTool.gridy = 0;
		gbcTool.gridheight = 1;
		toolPanel.add(hourLabel,gbcTool);
		
		// Hours spinner
		SpinnerNumberModel numberModelHours = new SpinnerNumberModel(17,17,23,1);
		hourSpinner = new JSpinner(numberModelHours);
		JSpinner.NumberEditor editorHours = new JSpinner.NumberEditor(hourSpinner);
		NumberFormatter formatterHours = (NumberFormatter) editorHours.getTextField().getFormatter();
		formatterHours.setAllowsInvalid(false);
		formatterHours.setOverwriteMode(true);
		hourSpinner.setEditor(editorHours);
		hourSpinner.setFont(Fonts.FONT20.get());
		hourSpinner.setPreferredSize(new Dimension(panelWidth/12, ToolPanel.getPanelHeight()/3));
//		hourSpinner.setBorder(new CompoundBorder(new LineBorder(darkGray, 1), new EmptyBorder(0, 10, 0, 0)));
		gbcTool.anchor = GridBagConstraints.FIRST_LINE_START;
		gbcTool.gridx = 3;
		gbcTool.gridy = 1;
		toolPanel.add(hourSpinner, gbcTool);
		
		setValueOfHourSpinner();
		
		// Separator label
		JLabel separatorLabel = new JLabel(":", JLabel.CENTER);
		separatorLabel.setFont(Fonts.FONT20.get());
		gbcTool.gridheight = 2;
		gbcTool.gridx = 4;
		toolPanel.add(separatorLabel, gbcTool);
		
		// Minute label
		JLabel minuteLabel = new JLabel("Minute: ");
		minuteLabel.setFont(font1);
		gbcTool.anchor = GridBagConstraints.LAST_LINE_START;
		gbcTool.gridx = 5;
		gbcTool.gridy = 0;
		gbcTool.gridheight = 1;
		toolPanel.add(minuteLabel,gbcTool);
		
		// Minute spinner
		SpinnerNumberModel numberModelMinutes = new SpinnerNumberModel(0,0,50,10);
		minuteSpinner = new JSpinner(numberModelMinutes);
		JSpinner.NumberEditor editorMinutes = new JSpinner.NumberEditor(minuteSpinner);
		NumberFormatter formatterMinutes = (NumberFormatter) editorMinutes.getTextField().getFormatter();
		minuteSpinner.setEditor(editorMinutes);
		formatterMinutes.setAllowsInvalid(false);
		formatterMinutes.setOverwriteMode(true);
		minuteSpinner.setFont(Fonts.FONT20.get());
		minuteSpinner.setPreferredSize(new Dimension(panelWidth/12, ToolPanel.getPanelHeight()/3));
//		minuteSpinner.setBorder(new CompoundBorder(new LineBorder(darkGray, 1), new EmptyBorder(0, 10, 0, 0)));
		gbcTool.anchor = GridBagConstraints.FIRST_LINE_START;
		gbcTool.gridx = 5;
		gbcTool.gridy = 1;
		toolPanel.add(minuteSpinner, gbcTool);
		
		setValueOfMinuteSpinner();
		
		// Date label
		JLabel dateLbl = new JLabel("Date: ");  
		dateLbl.setFont(font1);
		gbcTool.gridheight = 1;
		gbcTool.gridx = 6;
		gbcTool.gridy = 0;
		gbcTool.anchor = GridBagConstraints.LAST_LINE_START;
		toolPanel.add(dateLbl,gbcTool);
		
		// Date picker
		calendarModel = new UtilDateModel();
		Properties properties = new Properties();
		properties.put("text.today", "Today");
		properties.put("text.month", "Month");
		properties.put("text.year", "Year");
		JDatePanelImpl datePanel = new JDatePanelImpl(calendarModel,properties);
		datePicker = new JDatePickerImpl(datePanel, new DateLabelFormatter());
		datePicker.getComponent(0).setFont(Fonts.FONT20.get());
		datePicker.setButtonFocusable(false);
		datePicker.setPreferredSize(new Dimension(panelWidth/12, ToolPanel.getPanelHeight()/3));
	    datePicker.getComponent(0).setPreferredSize(new Dimension(panelWidth/12, ToolPanel.getPanelHeight()/3)); //JFormattedTextField
	    datePicker.getComponent(1).setPreferredSize(new Dimension(40,ToolPanel.getPanelHeight()/3));//JButton
	    
		gbcTool.anchor = GridBagConstraints.FIRST_LINE_START;
		gbcTool.gridheight = 1;
		gbcTool.gridx = 6;
		gbcTool.gridy = 1;
		toolPanel.add(datePicker,gbcTool);
	    
	    setDateOfDatePicker();
	    
//		Date selectedDate = (Date) datePicker.getModel().getValue();
//		calendarModel.setValue(selectedDate);

//		FontIcon calendarIcon = FontIcon.of(CoreUiFree.CALENDAR);
//		calendarIcon.setIconSize(32);
		
		// Now button
		nowBtn = new FancyButtonOneClick(ProjectColors.BLACK.get(), ProjectColors.RED.get(), ProjectColors.RED.get());
		nowBtn.setFont(font1);
		nowBtn.setBorderPainted(true);
		nowBtn.setPreferredSize(new Dimension(panelWidth/32, ToolPanel.getPanelHeight()/3));
		nowBtn.setText("now");
		nowBtn.setBorder(borderBlack);
		nowBtn.addActionListener(e -> nowBtnClicked());
		gbcTool.anchor = GridBagConstraints.FIRST_LINE_START;
		gbcTool.insets = new Insets(0,-panelWidth/26,0,0);
		gbcTool.gridy = 1;
		gbcTool.gridheight = 1;
		gbcTool.gridx = 7;
		toolPanel.add(nowBtn,gbcTool);
		
		dayForwardBtn = new FancyButtonMoreClick(ProjectColors.BLACK.get(), ProjectColors.GREEN.get(),
				ProjectColors.GREY.get(),ProjectColors.GREY.get());
		FontIcon rightArrowIcon = FontIcon.of(CoreUiFree.ARROW_RIGHT);
		rightArrowIcon.setIconSize(100);
		dayForwardBtn.setIcon(rightArrowIcon);
		dayForwardBtn.setPreferredSize(new Dimension(panelWidth/10, (int) (ToolPanel.getPanelHeight()*0.80)));
		gbcTool.anchor = GridBagConstraints.CENTER;
		gbcTool.insets = new Insets(0,0,0,0);
		gbcTool.gridy = 0;
		gbcTool.gridheight = 2;
		gbcTool.gridx = 8;
		toolPanel.add(dayForwardBtn,gbcTool);
		
		// -----------------------------------------------
		// ------------------ MAIN PANEL ---------------------
		// -----------------------------------------------
		
		
		// -----------------------------------------------
		// ------------------ FOOTER PANEL ---------------------
		// -----------------------------------------------
		
		//footer panel
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
		footerPanel.add(layoutLbl,gbcFooter);
		
		layoutCB = new JComboBox(new DefaultComboBoxModel());
		layoutCB.addActionListener(e -> switchLayout());
		layoutCB.setMaximumRowCount(5);
		layoutCB.setFont(font1);
		layoutCB.setPreferredSize(new Dimension(FooterPanel.panelWidth/8, FooterPanel.panelHeight/3));
		gbcFooter.anchor = GridBagConstraints.FIRST_LINE_START;
		gbcFooter.gridx = 0;
		gbcFooter.gridy = 1;
		footerPanel.add(layoutCB,gbcFooter);
		
		loadLayoutCB();
		
		makeReservationBtn = new FancyButtonOneClick(ProjectColors.BLACK.get(), ProjectColors.RED.get(),
				ProjectColors.RED.get());
		makeReservationBtn.setPreferredSize(new Dimension(FooterPanel.panelWidth/6,(int) (FooterPanel.panelHeight*0.8)));
		makeReservationBtn.setBorderPainted(true);
		makeReservationBtn.setFont(font1);
		makeReservationBtn.setBorder(borderBlack);
		makeReservationBtn.setText("Make Reservation");
		gbcFooter.anchor = GridBagConstraints.LINE_END;
		gbcFooter.gridy = 0;
		gbcFooter.gridx = 1;
		gbcFooter.gridheight = 2;
		gbcFooter.weightx = 1;
		footerPanel.add(makeReservationBtn,gbcFooter);

	} // end of constructor
	
	private void refreshAvailabilityOfRestaurantLayout() {
		
	}
	
	private void nowBtnClicked() {
		setCalendarCurrentTime();
		setValueOfMinuteSpinner();
		setValueOfHourSpinner();
		setDateOfDatePicker();
		refreshAvailabilityOfRestaurantLayout();
	}
	
	private void setCalendarCurrentTime() {
		calendar = Calendar.getInstance();
		if(calendar.get(Calendar.HOUR_OF_DAY) < 17) { // IF IT IS BEFORE 17
			calendar.set(Calendar.HOUR_OF_DAY,17);
			calendar.set(Calendar.MINUTE,0);
		}
		else { // IF IT IS AFTER 17
			int minute = calendar.get(Calendar.MINUTE);
			minute = (int) (Math.round(minute/10.0) * 10) ;
			calendar.set(Calendar.MINUTE,minute);
		}
	}
	
	private void setValueOfMinuteSpinner() {
		minuteSpinner.setValue(calendar.get(Calendar.MINUTE));	
	}
	
	private void setValueOfHourSpinner() {
		hourSpinner.setValue(calendar.get(Calendar.HOUR_OF_DAY));
	}
	
	private void setDateOfDatePicker() {
		Date date = new Date(calendar.getTimeInMillis());
		calendarModel.setDate(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),calendar.get(Calendar.DAY_OF_MONTH));
		calendarModel.setSelected(true);
	}
	
	public void deleteReservation(int reservationId) {
		new Thread(() -> {
			try {
				reservationController.deleteReservation(reservationId);  // TREBA TO VYMAZAT AJ Z ReservedTableInfoList ... mozno treba jednotny calendar
				//refreshReservedTableInfoMap(); TO DO!
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}).start();
	}
	
	private void loadLayoutsReservedTableInfoList(RestaurantLayout rl, Calendar calendar) throws SQLException {
		new Thread(() -> {
			try {
				reservedTableInfoMap.get(rl.getName()).clear();
				reservedTableInfoMap.get(rl.getName()).addAll(reservationController.getReservedTableInfo((int) rl.getId(), calendar));
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}).start();
	}
	
	private void loadLayouts() {
		Calendar calendar = Calendar.getInstance();
		reservedTableInfoMap = new HashMap<>();
		layoutPanelMap = new HashMap<>();
		//fill the map with panels ... Layout panel setup
		for(RestaurantLayout rl : layoutList) {
		reservedTableInfoMap.put(rl.getName(), new ArrayList<ReservedTableInfo>());
		try {
			loadLayoutsReservedTableInfoList(rl, calendar);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		LayoutPanel layoutPanel = new LayoutPanel();
		layoutPanel.loadExistingMiniPanels(rl);
		layoutPanel.updateReservedTablesByTime(rl, reservedTableInfoMap.get(rl.getName())); /// INPUT LSIT HERE
		layoutPanelMap.put(rl.getName(), layoutPanel);
		}
		// if there is any layout in DB 
		if(layoutList.size() != 0) {
		currentLayoutPanel = layoutPanelMap.get(layoutList.get(0).getName());
		this.add(currentLayoutPanel, BorderLayout.CENTER);
		}
		// if there is not layout in DB ... need to be done
		else {
			
		}
	}
	
	private void loadLayoutCB() {
		for(RestaurantLayout rl : layoutList) {
			layoutCB.addItem(rl.getName());
		}
		layoutCB.setSelectedItem(layoutList.get(0).getName());
	}
	
	private void switchLayout() {
		this.remove(currentLayoutPanel);
		currentLayoutPanel = layoutPanelMap.get(layoutCB.getSelectedItem().toString());
		this.add(currentLayoutPanel);
		this.repaint();
		this.revalidate();
	}
	
	private JComboBox getTimeCB() {
		String[] times = {"17:00","17:30","18:00","18:30","19:00","19:30","20:00","20:30","21:00","21:30",
				"22:00","22:30","23:00"};
		DefaultComboBoxModel<String> dm = new DefaultComboBoxModel<String>(times);
		return new JComboBox(dm);
		
	}
	
	private JComboBox getDurationCB() {
		durationCB = new JComboBox();
		for(int i = 1; i <6; i++) {
			durationCB.addItem(i);
		}
		return durationCB;
	}
	
	private void populatePersonCB() {
		for(int i = 1; i <30; i++) {
			personCB.addItem(i);
		}
	}
	
	public static OverviewPanel getInstance() {
		if(instance == null) return instance = new OverviewPanel();
		else return instance;
	}

}
