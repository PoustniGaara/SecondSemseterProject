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
		int x1,x2,x3,x4, width, height;
		boolean isPainted = true;
		public ToolBorderPanel(int width,int height, boolean isPainted) {
			setPreferredSize(new Dimension(width,height));
			//int x1, int x2, int x3, int x4,  
			this.isPainted = isPainted;
			this.width = width;
			this.height = height;
			this.x1 = x1;
			this.x2 = x2;
			this.x3 = x3;
			this.x4 =x4;
			setBackground(ProjectColors.BG.get());
		}

//		@Override
//		protected void paintComponent(Graphics g) {
//			if(isPainted) {
//			super.paintComponent(g);
////			Graphics2D g2 = (Graphics2D) g;
//			Double d1 = width * 0.99;
//			Double d2 = height * 0.02;
//			Double d3 = width * 0.99;
//			Double d4 = height * 0.98;
//			
//			g.drawLine( d1.intValue() ,d2.intValue(), d3.intValue(),d4.intValue());
////			g.drawLine(x1, x2, x3, x4);
//			// painting code goes here.
//			}
//		}
		
	}
