package gui.layout;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.Border;

import org.kordamp.ikonli.coreui.CoreUiFree;
import org.kordamp.ikonli.icomoon.Icomoon;
import org.kordamp.ikonli.swing.FontIcon;

import gui.MainFrame;
import gui.tools.Fonts;
import gui.tools.ProjectColors;
import model.LayoutItem;
import model.ReservedTableInfo;
import model.Table;

public class LayoutMiniPanel extends JPanel implements MouseListener {
	
	private JLabel nameLabel,capacityLabel,iconLabel;
	private int locationX,locationY;
	private FontIcon currentIcon;
	private Border border;
	private boolean isSelected = false;
	private LayoutItem layoutItem;
	private ArrayList<ReservedTableInfo> reservedTableInfoList;
	private FontIcon icon;
	private boolean isAvailable;
	private int tableCapacity;
	private boolean hasTable = false;
	private HashMap<Integer, ArrayList<ReservedTableInfo>> reservedTableInfoMap;
	
	public LayoutMiniPanel(int sizeOfMiniPanel) {
		
		//panel setup
		int height = (int) (MainFrame.height*0.84);
		int width = MainFrame.width;
		addMouseListener(this);
		setBackground(ProjectColors.WHITE.get());
		
		setLayout(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();
		
		border = BorderFactory.createLineBorder(ProjectColors.BLACK.get(), 1);
		setPreferredSize(new Dimension(sizeOfMiniPanel,sizeOfMiniPanel));
		
		// reservedTableInfoMap setup
		reservedTableInfoMap = new HashMap<>();
		
		//capacity label setup
		capacityLabel = new JLabel("");
		capacityLabel.setPreferredSize(new Dimension(sizeOfMiniPanel/2, sizeOfMiniPanel/4));
		capacityLabel.setFont(Fonts.FONT18.get());
		capacityLabel.setHorizontalAlignment(SwingConstants.CENTER);
		capacityLabel.setVerticalTextPosition(SwingConstants.BOTTOM);
		capacityLabel.setHorizontalTextPosition(SwingConstants.RIGHT);
		gbc.weightx = 1;
		gbc.weighty = 1;
		gbc.gridx = 0;
		gbc.gridy = 0;
		add(capacityLabel, gbc);
		
//		//icon setup
//		FontIcon plusIcon = FontIcon.of(CoreUiFree.POOL);
//		plusIcon.setIconSize(Math.round(30));
//		currentIcon  = plusIcon;
		
		iconLabel = new JLabel();
		iconLabel.setIcon(currentIcon);
		iconLabel.setHorizontalAlignment(SwingConstants.CENTER);
		iconLabel.setVerticalAlignment(SwingConstants.CENTER);
		gbc.gridy = 1;
		add(iconLabel, gbc);
		
		//name label setup
		nameLabel = new JLabel("");
		nameLabel.setFont(Fonts.FONT18.get());
		nameLabel.setHorizontalAlignment(SwingConstants.CENTER);
		nameLabel.setVerticalTextPosition(SwingConstants.CENTER);
		nameLabel.setHorizontalTextPosition(SwingConstants.CENTER);
		gbc.gridy = 2;
		add(nameLabel, gbc);

	} // end of constructor
	
//	public boolean getTimeAvailability(Calendar calendar, int duration) {
		// compare reservedTableInfo list with input
//	}
	
//	public void updateReservedTableInfoList(ArrayList<ReservedTableInfo> reservedTableInfoList) {
//		this.reservedTableInfoList = reservedTableInfoList;
//	}
	
	public void addReservedTableInfoToMap(ReservedTableInfo rti, int tableId) {
		reservedTableInfoMap.put(tableId, reservedTableInfoList);
	}
	
	public void setReservedTableInfoMap(HashMap<Integer, ArrayList<ReservedTableInfo>> reservedTableInfoMap) {
		this.reservedTableInfoMap = reservedTableInfoMap;
	}
	
	public void setLayoutItem(LayoutItem layoutItem) {
		this.layoutItem = layoutItem;
		nameLabel.setText(layoutItem.getName());
		if(layoutItem instanceof Table) {
			hasTable = true;
			Table table  = (Table) layoutItem;
			setCapacityLabel(String.valueOf(table.getCapacity()));
			tableCapacity = ((Table) layoutItem).getCapacity();
			FontIcon tableIcon = FontIcon.of(Icomoon.ICM_SPOON_KNIFE);
			tableIcon.setIconSize(30);
			setIcon(tableIcon);
		}
		if(layoutItem.getType().equals("bar")) {
			FontIcon barIcon = FontIcon.of(Icomoon.ICM_GLASS2);
			hasTable = false;
			barIcon.setIconSize(30);
			setIcon(barIcon);
		}
		if(layoutItem.getType().equals("entrance")) {
			FontIcon entranceIcon = FontIcon.of(Icomoon.ICM_ENTER);
			hasTable = false;
			entranceIcon.setIconSize(30);
			setIcon(entranceIcon);
		}
		if(layoutItem.equals(null)) {
			hasTable = false;
			setIcon(null);
			setCapacityLabel("");
		}
	}
	
	public void setAvailable() {
		isAvailable = true;
		icon.setIconColor(ProjectColors.GREEN.get());
		this.repaint();
	}
	
	public void setUnavailable() {
		if(hasTable == true) {
		isAvailable = false;
		icon.setIconColor(ProjectColors.RED.get());
		this.repaint();
		}
	}
	
	public void setCapacityLabel(String capacity) {
		capacityLabel.setText(capacity);
		if(capacity.equals(""))
			capacityLabel.setBorder(null);
		else
			capacityLabel.setBorder(border);
	}
	
	public void setIcon(FontIcon icon) {
		this.icon = icon;
		iconLabel.setIcon(icon);
	}
	
	public void setLocation(int locationX, int locationY) {
		this.locationX = locationX;
		this.locationY = locationY;
	}
	
	public int getLocationX() {
		return locationX;
	}
	
	public int getLocationY() {
		return locationY;
	}
	
	public void setNameLabel(String name) {
		nameLabel.setText(name);
	}
	
	public boolean isSelected() {
		return isSelected;
	}
	
	public void setSelected(boolean state) {
		this.isSelected = state;
	}
	
	public boolean hasTable() {
		return hasTable;
	}
	
	public int getTableCapacity() {
		return tableCapacity;
	}
	
	public LayoutItem getLayoutItem() {
		return this.layoutItem;
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		if(iconLabel.getIcon() != null) {
			if (e.getClickCount() == 2 && e.getButton() == MouseEvent.BUTTON1) {
				new TableInfoFrame(layoutItem.getName(), reservedTableInfoList);
			}
			else {
				if(isAvailable) { // select only if it is available
					if(isSelected == true) {
						setSelected(false);
						setBackground(ProjectColors.WHITE.get());
						setBorder(null);
					}
					else {
						setSelected(true);
						setBackground(ProjectColors.SELECTED.get());
						setBorder(border);
				}
				}
			}
		}
	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

}
