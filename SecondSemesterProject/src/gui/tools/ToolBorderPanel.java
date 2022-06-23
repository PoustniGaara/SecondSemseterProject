package gui.tools;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;

import javax.swing.JPanel;

	
	public class ToolBorderPanel extends JPanel {
		int x1,x2,x3,x4;
		public ToolBorderPanel(int x1, int x2, int x3, int x4) {
			this.x1 = x1;
			this.x2 = x2;
			this.x3 = x3;
			this.x4 =x4;
		}

		@Override
		protected void paintComponent(Graphics g) {
			super.paintComponent(g);
//			Graphics2D g2 = (Graphics2D) g;
			g.drawLine(x1, x2, x3, x4);
			// painting code goes here.
		}
		
	}
