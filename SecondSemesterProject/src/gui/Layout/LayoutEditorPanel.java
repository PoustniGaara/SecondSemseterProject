package gui.Layout;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;

import gui.MainFrame;

public class LayoutEditorPanel extends JPanel implements ComponentListener {
	
	private JScrollPane scrollPane;
	private GridBagConstraints gbc;
	int widthOfMiniPanel,heightOfMiniPanel, sizeOfMiniPanel;
	
	public LayoutEditorPanel(int sizeX, int sizeY) {
		
		//panel settings
		int width = MainFrame.width;
		int height = (int) (MainFrame.height*0.84);
		setLayout(new GridBagLayout());
		widthOfMiniPanel = Math.round(width/sizeX);
		heightOfMiniPanel =  Math.round(height/sizeY);
		sizeOfMiniPanel = widthOfMiniPanel-heightOfMiniPanel;
		
		//gbc setup
		gbc = new GridBagConstraints();
		gbc.anchor = GridBagConstraints.CENTER;
		gbc.fill = GridBagConstraints.BOTH;
		gbc.weightx = 1;
		gbc.weighty = 1;
		
		//scroll pane 
		scrollPane = new JScrollPane(this);
		scrollPane.addComponentListener(this);
		scrollPane.setPreferredSize(new Dimension(width,height));
		scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);//AS_NEEDED
		this.add(scrollPane);
		
		loadMiniPanels(sizeX, sizeY); 
		
	}
		
	public void loadMiniPanels(int sizeX, int sizeY) {
		for(int i = 0; i != sizeY; i++) {
			for(int j = 0; j != sizeX; j++) {
				gbc.gridx = j;
				gbc.gridy = i;
				this.add(new LayoutMiniPanel(sizeOfMiniPanel), gbc);
				scrollPane.repaint();
				scrollPane.revalidate();
			}
		}
	}

	@Override
	public void componentResized(ComponentEvent e) {
		setPreferredSize(new Dimension(scrollPane.getWidth(),scrollPane.getHeight()));
		revalidate();
		repaint();
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