package gui.tools;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JButton;

public class FButtonMoreC extends JButton {
	
	private boolean isClicked = false;
	private Color fDefault;
	private Color fClicked;
	private Color bClicked;
	private Color bEntered;
	
	public FButtonMoreC(Color fdefault, Color fclicked, Color bclicked, Color bentered) {
	this.fDefault = fdefault;
	this.fClicked = fclicked;
	this.bClicked = bclicked;
	this.bEntered = bentered;
	
	setForeground(fDefault);
	setOpaque(false);
	setContentAreaFilled(false);
	setBorderPainted(false);
	setFocusable(false);
	addMouseListener(new MouseAdapter() {
		@Override
		public void mousePressed(MouseEvent e) {
			setBackground(bClicked);
			setForeground(fClicked);
			setOpaque(true);
			setContentAreaFilled(true);
			isClicked = true;
		}
		@Override
		public void mouseClicked(MouseEvent e) {
			setBackground(bClicked);
			setForeground(fClicked);
			setOpaque(true);
			setContentAreaFilled(true);
			isClicked = true;
		}
		
		@Override
		public void mouseEntered(MouseEvent e) {
			if(isClicked == false) {
			setOpaque(true);
			setBackground(bEntered);
			}
		}
		@Override
		public void mouseExited(MouseEvent e) {
			if(isClicked == false) {
			setOpaque(false);
			}
		}

	});
	
	}//END OF CONSTRUCTOR
	
	public void isClicked(boolean state) {
		this.isClicked = state;
		if (state == true) {
		setBackground(bClicked);
		setForeground(fClicked);
		setOpaque(true);
		setContentAreaFilled(true);
		}
		else {
			setForeground(fDefault);
			setOpaque(false);
			setContentAreaFilled(false);
			setBorderPainted(false);
			setFocusable(false);
		}
	}

}
