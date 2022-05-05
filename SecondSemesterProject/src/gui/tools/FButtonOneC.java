package gui.tools;

import java.awt.Color;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JButton;

public class FButtonOneC extends JButton {
	private boolean isClicked = false;
	private Color fDefault;
	private Color bDefault;
	private Color bEntered;
	
	public FButtonOneC(Color fdefault, Color bentered, Color bdefault) {
	this.fDefault = fdefault;
	this.bDefault = bdefault;
	this.bEntered = bentered;
	setForeground(fDefault);
	setBackground(bDefault);
	setOpaque(true);
	setContentAreaFilled(true);
	setBorderPainted(false);
	setFocusable(false);
	
	}//end of constructor
	
	public void setState(boolean state) {
		if(state == true) {
			addMouseListener(new MouseAdapter() {
				@Override
				public void mousePressed(MouseEvent e) {
					isClicked = true;
				}
				@Override
				public void mouseClicked(MouseEvent e) {
					isClicked = true;
				}
				
				@Override
				public void mouseEntered(MouseEvent e) {
					setOpaque(true);
					setBackground(bEntered);
					
				}
				@Override
				public void mouseExited(MouseEvent e) {
					setBackground(bDefault);
				}

			});

		}//end of true
		else {
					setOpaque(true);
					setBackground(bDefault);

		}
	}//end of method 
	

}
