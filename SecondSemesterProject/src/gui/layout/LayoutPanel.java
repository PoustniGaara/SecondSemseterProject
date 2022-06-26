package gui.layout;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Point;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;

import controller.ReservationController;
import gui.MainFrame;
import gui.tools.ProjectColors;
import model.LayoutItem;
import model.ReservedTableInfo;
import model.RestaurantLayout;
import model.Table;

public class LayoutPanel extends JPanel implements ComponentListener {
	
	private JScrollPane scrollPane;
	private GridBagConstraints gbc;
	int widthOfMiniPanel,heightOfMiniPanel, sizeOfMiniPanel;
	private JPanel contentPane;
	private HashMap<Point,LayoutMiniPanel> miniPanelMap;
	
	public LayoutPanel() { 
		
		//panel settings
		int width = MainFrame.width;
		int height = (int) (MainFrame.height*0.84);
		setLayout(new BorderLayout());
		setBackground(Color.white);
//		widthOfMiniPanel = Math.round(width/sizeX);
//		heightOfMiniPanel =  Math.round(height/sizeY);
//		sizeOfMiniPanel = widthOfMiniPanel-heightOfMiniPanel;
		sizeOfMiniPanel = 60;
		
		contentPane = new JPanel();
		contentPane.setLayout(new GridBagLayout());
		
		//gbc setup
		gbc = new GridBagConstraints();
		gbc.anchor = GridBagConstraints.CENTER;
//		gbc.fill = GridBagConstraints.BOTH;
		gbc.ipadx = sizeOfMiniPanel;
		gbc.ipady = sizeOfMiniPanel;  
		gbc.weightx = 1;
		gbc.weighty = 1;
		
		//scroll pane 
		scrollPane = new JScrollPane(contentPane);//this
		scrollPane.addComponentListener(this);
		scrollPane.setPreferredSize(new Dimension(width,height));
		scrollPane.setMaximumSize(new Dimension(3*width,3*height));
		scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		add(scrollPane, BorderLayout.CENTER);
		
		//load setup
		miniPanelMap = new HashMap<>();
		
	}// end of second constructor
	
	public void deselectAllMiniPanels() {
		for(LayoutMiniPanel miniPanel: miniPanelMap.values()) {
			miniPanel.setSelected(false);
			miniPanel.repaint();
			miniPanel.revalidate();
		}
	}

	public HashMap<Point,LayoutMiniPanel> getMiniPanelMap(){
		return miniPanelMap;
	}
	
	public void setAvailabilityOfTables(int noOfPerson, Calendar calendar, int duration) {
		for(LayoutMiniPanel miniPanel : miniPanelMap.values()) {
			if(miniPanel.hasTable()) {
				if(miniPanel.getTableCapacity() >= noOfPerson && miniPanel.getTimeAvailability(calendar, duration) == true) {
					miniPanel.setAvailable();
					}
				else  { // if capacity is okay test for timeAvailability
//					System.out.println("Availability is "+miniPanel.getTimeAvailability(calendar, duration));
						miniPanel.setUnavailable();
				}	
			}

		}
	}
	// DEBUG NOTE: program can get into every part of the method if condition are met
	public void updateReservedTablesByTime(RestaurantLayout rl, ArrayList<ReservedTableInfo> reservedTableInfoList) {
		for(LayoutMiniPanel miniPanel : miniPanelMap.values()) { 
				if(miniPanel.getLayoutItem() != null) {
					for(ReservedTableInfo rti : reservedTableInfoList) {
//						System.out.println("RTI GET ID:" +rti.getId() );
//						System.out.println("miniPanel ID" +miniPanel.getLayoutItem().getId() );
						if(rti.getLayoutItemId() == miniPanel.getLayoutItem().getId()) {
							miniPanel.addToTableInfoList(rti);
//							miniPanel.addReservedTableInfoToMap(rti, rti.getLayoutItemId()); // checking this out
						}
					}
				}
		}
	}
	
	//DEBUG NOTE : This method can set layout item correctly
	public void loadExistingMiniPanels(RestaurantLayout rl) {
		for(int i = 0; i != rl.getSizeY(); i++) {
			for(int j = 0; j!= rl.getSizeX(); j++) {
				gbc.gridx = j;
				gbc.gridy = i;
				LayoutMiniPanel miniPanel = new LayoutMiniPanel(sizeOfMiniPanel);
				miniPanel.setLocation(j, i);
				if(rl.getItemMap().get(new Point(j,i)) != null) {
				miniPanel.setLayoutItem(rl.getItemMap().get(new Point(j,i))); // this also sets an icon and else
				miniPanel.setAvailable();// SET EVERY MINI PANEL AVAILABLE AND THEN DISABLE IT.
				}
				miniPanelMap.put(new Point(j,i), miniPanel);
				contentPane.add(miniPanel, gbc);
				scrollPane.repaint();
				scrollPane.revalidate();
			}
		}
	}

	@Override
	public void componentResized(ComponentEvent e) {
		contentPane.setPreferredSize(new Dimension(contentPane.getWidth(),contentPane.getHeight()));
		contentPane.revalidate();
		contentPane.repaint();
		scrollPane.repaint();
		scrollPane.revalidate();
	}

	@Override
	public void componentMoved(ComponentEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void componentShown(ComponentEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void componentHidden(ComponentEvent e) {
		// TODO Auto-generated method stub
		
	}

}
