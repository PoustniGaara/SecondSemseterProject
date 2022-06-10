package gui.Layout;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.Border;

import org.kordamp.ikonli.coreui.CoreUiFree;
import org.kordamp.ikonli.swing.FontIcon;

import gui.MainFrame;
import gui.tools.Fonts;
import gui.tools.ProjectColors;

public class LayoutMiniPanel extends JPanel implements MouseListener {
	
	private JLabel nameLabel,capacityLabel;
	private int locationX,locationY;
	
	public LayoutMiniPanel(int sizeOfMiniPanel) {
		
		//panel setup
		int height = (int) (MainFrame.height*0.84);
		int width = MainFrame.width;
		System.out.println(this.getPreferredSize());
		addMouseListener(this);
		setLayout(new BorderLayout());
		
		Border borderBlack = BorderFactory.createLineBorder(ProjectColors.BLACK.get(), 1);
//		setBorder(borderBlack);
		
		//capacity label setup
		capacityLabel = new JLabel();
		capacityLabel.setFont(Fonts.FONT15.get());
		capacityLabel.setHorizontalAlignment(SwingConstants.RIGHT);
		capacityLabel.setVerticalTextPosition(SwingConstants.NORTH);
		capacityLabel.setHorizontalTextPosition(SwingConstants.RIGHT);
		add(capacityLabel, BorderLayout.NORTH);
		
		//name label setup
		nameLabel = new JLabel("empty");
		nameLabel.setFont(Fonts.FONT15.get());
		nameLabel.setHorizontalAlignment(SwingConstants.CENTER);
		nameLabel.setVerticalTextPosition(SwingConstants.CENTER);
		nameLabel.setHorizontalTextPosition(SwingConstants.CENTER);
		add(nameLabel, BorderLayout.SOUTH);
		
		//icon setup
		FontIcon plusIcon = FontIcon.of(CoreUiFree.POOL);
		plusIcon.setIconSize(Math.round(sizeOfMiniPanel/2));
		JLabel iconLabel = new JLabel();
		iconLabel.setIcon(plusIcon);
		iconLabel.setHorizontalAlignment(SwingConstants.CENTER);
		iconLabel.setVerticalAlignment(SwingConstants.CENTER);
		add(iconLabel, BorderLayout.CENTER);
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
	}

	@Override
	public void mousePressed(MouseEvent e) {
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		new AddLayoutItemDialog(this);
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
