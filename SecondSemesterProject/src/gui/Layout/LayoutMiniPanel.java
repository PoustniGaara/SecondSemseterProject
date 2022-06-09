package gui.Layout;

import java.awt.BorderLayout;
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
import gui.tools.ProjectColors;

public class LayoutMiniPanel extends JPanel implements MouseListener {
	
	public LayoutMiniPanel(int sizeOfMiniPanel) {
		
		//panel setup
		int height = (int) (MainFrame.height*0.84);
		int width = MainFrame.width;
		System.out.println(sizeOfMiniPanel);
		setPreferredSize(new Dimension(sizeOfMiniPanel,sizeOfMiniPanel));
		
		Border borderBlack = BorderFactory.createLineBorder(ProjectColors.BLACK.get(), 1);
		
		
		setLayout(new BorderLayout());
		setBorder(borderBlack);
		
		//icon setup
		FontIcon plusIcon = FontIcon.of(CoreUiFree.PLUS);
		plusIcon.setIconSize(Math.round(sizeOfMiniPanel/2));
		JLabel iconLabel = new JLabel();
		iconLabel.setIcon(plusIcon);
		iconLabel.setHorizontalTextPosition(SwingConstants.CENTER);
		iconLabel.setVerticalTextPosition(SwingConstants.CENTER);
		add(iconLabel, BorderLayout.CENTER);
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
		
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
