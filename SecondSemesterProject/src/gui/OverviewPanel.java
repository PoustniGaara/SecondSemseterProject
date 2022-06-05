package gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;

import javax.swing.BorderFactory;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.Border;

import org.kordamp.ikonli.coreui.CoreUiFree;
import org.kordamp.ikonli.swing.FontIcon;

import controller.RestaurantLayoutController;
import gui.tools.FancyButtonMoreClick;
import gui.tools.FancyButtonOneClick;
import gui.tools.ProjectColors;
import model.RestaurantLayout;

public class OverviewPanel extends JPanel {
	
	private static OverviewPanel instance;
	private JComboBox<String> timeCB, layoutCB;
	private JComboBox<Integer> personCB;
	private FancyButtonMoreClick dayBackBtn, dayForwardBtn;
	private FancyButtonOneClick nowBtn,calendarBtn,makeReservationBtn;
	private Calendar calendar;
	private JLabel dateLbl;
	private ArrayList<RestaurantLayout> layoutList;	
	private RestaurantLayoutController restaurantLayoutController;
	
	private OverviewPanel() {
		
		//control
		restaurantLayoutController = new RestaurantLayoutController();
		
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
		
		//load layouts
		loadLayouts();
		
		//Panel support classes
		Border borderGreen = BorderFactory.createLineBorder(ProjectColors.GREEN.get(), 1);
		Border borderBlack = BorderFactory.createLineBorder(ProjectColors.BLACK.get(), 2);
		Border borderRed = BorderFactory.createLineBorder(ProjectColors.RED.get(), 2);
		Font font1 = new Font("Monaco", Font.BOLD, 20);
		
		//Tool Panel
		ToolPanel toolPanel = new ToolPanel();
//		toolPanel.setBorder(borderGreen);
		GridBagConstraints gbcTool = new GridBagConstraints();
		gbcTool.weightx = 0.5;
		gbcTool.weighty = 0;
		add(toolPanel, BorderLayout.NORTH);
		
		JLabel personLabel = new JLabel("no. of persons");
		personLabel.setFont(font1);
		gbcTool.anchor = GridBagConstraints.LAST_LINE_START;
		gbcTool.gridx = 0;
		gbcTool.gridy = 0;
		toolPanel.add(personLabel,gbcTool);
		
		personCB = new JComboBox(new DefaultComboBoxModel());
		populatePersonCB();
		personCB.setMaximumRowCount(5);
		personCB.setFont(font1);
		personCB.setPreferredSize(new Dimension(panelWidth/8, ToolPanel.getPanelHeight()/3));
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
		
		JLabel timeLabel = new JLabel("time");
		timeLabel.setFont(font1);
		gbcTool.anchor = GridBagConstraints.LAST_LINE_START;
		gbcTool.gridx = 2;
		gbcTool.gridy = 0;
		gbcTool.gridheight = 1;
		toolPanel.add(timeLabel,gbcTool);
		
		timeCB = getTimeCB();
		timeCB.setMaximumRowCount(5);
		timeCB.setFont(font1);
		timeCB.setPreferredSize(new Dimension(panelWidth/8, ToolPanel.getPanelHeight()/3));
		gbcTool.anchor = GridBagConstraints.FIRST_LINE_START;
		gbcTool.gridx = 2;
		gbcTool.gridy = 1;
		toolPanel.add(timeCB,gbcTool);
		
		dateLbl = new JLabel("05.05.2022");
		dateLbl.setFont(font1);
		gbcTool.gridheight = 2;
		gbcTool.gridx = 3;
		gbcTool.gridy = 0;
		gbcTool.anchor = GridBagConstraints.CENTER;
		toolPanel.add(dateLbl,gbcTool);
		
		calendarBtn = new FancyButtonOneClick(ProjectColors.BLACK.get(),ProjectColors.GREY.get(),
				ProjectColors.GREY.get());
		FontIcon calendarIcon = FontIcon.of(CoreUiFree.CALENDAR);
		calendarIcon.setIconSize(32);
		calendarBtn.setIcon(calendarIcon);
		calendarBtn.setPreferredSize(new Dimension(panelWidth/16, ToolPanel.getPanelHeight()/3));
		gbcTool.gridx = 4;
		gbcTool.anchor = GridBagConstraints.LINE_START;
		toolPanel.add(calendarBtn,gbcTool);
		
		nowBtn = new FancyButtonOneClick(ProjectColors.BLACK.get(), ProjectColors.RED.get(), ProjectColors.RED.get());
		nowBtn.setFont(font1);
		nowBtn.setBorderPainted(true);
		nowBtn.setPreferredSize(new Dimension(panelWidth/12, ToolPanel.getPanelHeight()/2));
		nowBtn.setText("now");
		nowBtn.setBorder(borderBlack);
		gbcTool.gridheight = 2;
		gbcTool.gridx = 5;
		toolPanel.add(nowBtn,gbcTool);
		
		dayForwardBtn = new FancyButtonMoreClick(ProjectColors.BLACK.get(), ProjectColors.GREEN.get(),
				ProjectColors.GREY.get(),ProjectColors.GREY.get());
		FontIcon rightArrowIcon = FontIcon.of(CoreUiFree.ARROW_RIGHT);
		rightArrowIcon.setIconSize(100);
		dayForwardBtn.setIcon(rightArrowIcon);
		dayForwardBtn.setPreferredSize(new Dimension(panelWidth/10, (int) (ToolPanel.getPanelHeight()*0.80)));
		gbcTool.anchor = GridBagConstraints.CENTER;
		gbcTool.gridheight = 2;
		gbcTool.gridx = 6;
		toolPanel.add(dayForwardBtn,gbcTool);
		
		//end of Tool panel*************************************
		
		//footer panel
		FooterPanel footerPanel = new FooterPanel();
		add(footerPanel, BorderLayout.SOUTH);
		footerPanel.setLayout(new GridBagLayout());
		GridBagConstraints gbcFooter = new GridBagConstraints();
		gbcFooter.weightx = 0.2;
		gbcFooter.weighty = 0.5;
		
		JLabel layoutLbl = new JLabel("Layout");
		layoutLbl.setFont(font1);
		gbcFooter.gridx = 0;
		gbcFooter.gridy = 0;
		gbcFooter.anchor = GridBagConstraints.LAST_LINE_START;
		footerPanel.add(layoutLbl,gbcFooter);
		
		layoutCB = new JComboBox(new DefaultComboBoxModel());
		layoutCB.setMaximumRowCount(5);
		layoutCB.setFont(font1);
		layoutCB.setPreferredSize(new Dimension(FooterPanel.panelWidth/8, FooterPanel.panelHeight/3));
		gbcFooter.anchor = GridBagConstraints.FIRST_LINE_START;
		gbcFooter.gridx = 0;
		gbcFooter.gridy = 1;
		footerPanel.add(layoutCB,gbcFooter);
		
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

		
	}
	
	private void loadLayouts() {
		try {
			layoutList = (ArrayList<RestaurantLayout>) restaurantLayoutController.read();
			if(layoutList.size() == 0) {
				System.out.print("I am trying to display info panel");
				this.add(new NoLayoutInfoPanel(), BorderLayout.CENTER);
			}
			else {
				System.out.print("Im here");
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private JComboBox getTimeCB() {
		String[] times = {"17:00","17:30","18:00","18:30","19:00","19:30","20:00","20:30","21:00","21:30",
				"22:00","22:30","23:00"};
		DefaultComboBoxModel<String> dm = new DefaultComboBoxModel<String>(times);
		return new JComboBox(dm);
		
	}
	
	private void populatePersonCB() {
		for(int i = 1; i <30; i++) {
			personCB.addItem(i);
		}
	}
	
	public static OverviewPanel getInstance() {
		if(instance == null) return new OverviewPanel();
		else return instance;
	}

}
