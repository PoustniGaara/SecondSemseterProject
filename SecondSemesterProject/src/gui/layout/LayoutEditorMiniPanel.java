package gui.layout;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.Border;

import org.kordamp.ikonli.coreui.CoreUiFree;
import org.kordamp.ikonli.icomoon.Icomoon;
import org.kordamp.ikonli.swing.FontIcon;

import gui.HeaderPanel;
import gui.MainFrame;
import gui.tools.Fonts;
import gui.tools.ProjectColors;
import model.LayoutItem;
import model.Table;

public class LayoutEditorMiniPanel extends JPanel implements MouseListener {
	
	private JLabel nameLabel,capacityLabel,iconLabel;
	private int locationX,locationY;
	private FontIcon currentIcon;
	private Border capacityBorder;
	
	public LayoutEditorMiniPanel(int sizeOfMiniPanel) {
		
		//panel setup
		int height = (int) (MainFrame.height*0.84);
		int width = MainFrame.width;
		addMouseListener(this);
		
		setLayout(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();
		
		capacityBorder = BorderFactory.createLineBorder(ProjectColors.BLACK.get(), 1);
		setBackground(Color.white);
		setPreferredSize(new Dimension(sizeOfMiniPanel,sizeOfMiniPanel));
		
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
		
		//icon setup
		FontIcon plusIcon = FontIcon.of(CoreUiFree.POOL);
		plusIcon.setIconSize(Math.round(30));
		currentIcon  = plusIcon;
		
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

	}
	
	public void setLayoutItem(LayoutItem layoutItem) {
		nameLabel.setText(layoutItem.getName());
		if(layoutItem instanceof Table) {
			Table table  = (Table) layoutItem;
			setCapacityLabel(String.valueOf(table.getCapacity()));
			FontIcon tableIcon = FontIcon.of(Icomoon.ICM_SPOON_KNIFE);
			tableIcon.setIconSize(30);
			setIcon(tableIcon);
		}
		if(layoutItem.getType().equals("bar")) {
			FontIcon barIcon = FontIcon.of(Icomoon.ICM_GLASS2);
			barIcon.setIconSize(30);
			setIcon(barIcon);
		}
		if(layoutItem.getType().equals("entrance")) {
			FontIcon entranceIcon = FontIcon.of(Icomoon.ICM_ENTER);
			entranceIcon.setIconSize(30);
			setIcon(entranceIcon);
		}
		if(layoutItem.equals(null)) {
			setIcon(null);
			setCapacityLabel("");
		}
	}
	
	public void setCapacityLabel(String capacity) {
		capacityLabel.setText(capacity);
		if(capacity.equals(""))
			capacityLabel.setBorder(null);
		else
			capacityLabel.setBorder(capacityBorder);
	}
	
	public void setIcon(FontIcon icon) {
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

	@Override
	public void mouseClicked(MouseEvent e) {
		new AddLayoutItemDialog(this);
	}

	@Override
	public void mousePressed(MouseEvent e) {
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		
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
