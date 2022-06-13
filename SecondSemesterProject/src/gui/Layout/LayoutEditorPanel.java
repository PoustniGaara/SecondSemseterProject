package gui.Layout;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Point;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.util.HashMap;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;

import gui.MainFrame;
import model.RestaurantLayout;

public class LayoutEditorPanel extends JPanel implements ComponentListener {
	
	private JScrollPane scrollPane;
	private GridBagConstraints gbc;
	int widthOfMiniPanel,heightOfMiniPanel, sizeOfMiniPanel;
	private JPanel contentPane;
	private HashMap<Point,LayoutMiniPanel> miniPanelMap;
	
	public LayoutEditorPanel() { //int sizeX, int sizeY
		
		//panel settings
		int width = MainFrame.width;
		int height = (int) (MainFrame.height*0.84);
		setLayout(new BorderLayout());
//		widthOfMiniPanel = Math.round(width/sizeX);
//		heightOfMiniPanel =  Math.round(height/sizeY);
//		sizeOfMiniPanel = widthOfMiniPanel-heightOfMiniPanel;
		sizeOfMiniPanel = 60;
		
		contentPane = new JPanel();
		contentPane.setLayout(new GridBagLayout());
		
		//gbc setup
		gbc = new GridBagConstraints();
		gbc.anchor = GridBagConstraints.CENTER;
//		gbc.fill = GridBagConstraints.BOTH;
		gbc.ipadx = sizeOfMiniPanel;
		gbc.ipady = sizeOfMiniPanel;  
		gbc.weightx = 1;
		gbc.weighty = 1;
		
		//scroll pane 
		scrollPane = new JScrollPane(contentPane);//this
		scrollPane.addComponentListener(this);
		scrollPane.setPreferredSize(new Dimension(width,height));
		scrollPane.setMaximumSize(new Dimension(3*width,3*height));
		scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		add(scrollPane, BorderLayout.CENTER);
		
		//load setup
		miniPanelMap = new HashMap<>();
//		loadEmptyMiniPanels(sizeX, sizeY); 
		
	}// end of second constructor
	
	public void loadExistingMiniPanels(RestaurantLayout rl) {
		for(int i = 0; i != rl.getSizeY(); i++) {
			for(int j = 0; j!= rl.getSizeX(); j++) {
				gbc.gridx = j;
				gbc.gridy = i;
				LayoutMiniPanel miniPanel = new LayoutMiniPanel(sizeOfMiniPanel);
				miniPanel.setLocation(j, i);
				if(rl.getItemMap().get(new Point(j,i)) != null) {
				miniPanel.setLayoutItem(rl.getItemMap().get(new Point(j,i)));
				}
				miniPanelMap.put(new Point(j,i), miniPanel);
				contentPane.add(miniPanel, gbc);
				scrollPane.repaint();
				scrollPane.revalidate();
			}
		}
	}
		
	public void loadEmptyMiniPanels(int sizeX, int sizeY) {
		for(int i = 0; i != sizeY; i++) {
			for(int j = 0; j != sizeX; j++) {
				gbc.gridx = j;
				gbc.gridy = i;
				LayoutMiniPanel miniPanel = new LayoutMiniPanel(sizeOfMiniPanel);
				miniPanel.setLocation(j, i);
				miniPanelMap.put(new Point(j,i), miniPanel);
				contentPane.add(miniPanel, gbc);
				scrollPane.repaint();
				scrollPane.revalidate();
			}
		}
	}
	
	

	@Override
	public void componentResized(ComponentEvent e) {
		contentPane.setPreferredSize(new Dimension(contentPane.getWidth(),contentPane.getHeight()));
		contentPane.revalidate();
		contentPane.repaint();
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