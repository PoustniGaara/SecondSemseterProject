package gui.tools;

import java.awt.Color;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JButton;

@SuppressWarnings("serial")
public class FancyButtonMoreClick extends JButton {

	private boolean isClicked = false;
	private Color textmain;
	private Color textsecond;
	private Color background;
	private Color hover;
	private Color clicked;

	public FancyButtonMoreClick(Color textmain, Color textsecond, Color background, Color hover, Color clicked) {

		setForeground(textmain);
		setBackground(background);

		setOpaque(false);
		setContentAreaFilled(false);
		setBorderPainted(false);
		setFocusable(false);

		addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				if (isClicked) {
					setBackground(clicked);
					setForeground(textsecond);
					setContentAreaFilled(true);
					isClicked = false;
				} else {
					setForeground(textmain);
					setBackground(background);
					setOpaque(false);
					setContentAreaFilled(false);
					isClicked = true;
				}
			}

			@Override
			public void mouseClicked(MouseEvent e) {
				if (isClicked) {
					setBackground(clicked);
					setForeground(textsecond);
					setContentAreaFilled(true);
				} else {
					setForeground(textmain);
					setBackground(background);
					setContentAreaFilled(false);
				}
			}

			@Override
			public void mouseEntered(MouseEvent e) {
				if (isClicked) {
					setBackground(hover);
				} else {
					setBackground(hover);
					setContentAreaFilled(true);
				}
			}

			@Override
			public void mouseExited(MouseEvent e) {
				if (isClicked) {
					setBackground(clicked);
				} else {
					setBackground(background);
					setContentAreaFilled(false);
				}
			}

		});

	}// END OF CONSTRUCTOR

	public void clicked(boolean isClicked) {
		this.isClicked = isClicked;
		if (isClicked == true) {
			setBackground(clicked);
			setForeground(textsecond);
			setOpaque(true);
			setContentAreaFilled(true);
		} else {
			setBackground(background);
			setForeground(textmain);
			setContentAreaFilled(false);
			setBorderPainted(false);
			setFocusable(false);
		}
	}

}
