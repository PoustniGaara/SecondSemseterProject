package gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;

public class LayoutEditorPanel extends JPanel implements ComponentListener {
	
	private JScrollPane scrollPane;
	private GridBagConstraints gbc;
	
	public LayoutEditorPanel(int sizeX, int sizeY) {
		
		//panel settings
		int width = MainFrame.width;
		int height = (int) (MainFrame.height*0.8);
		setLayout(new GridBagLayout());
		
		//gbc setup
		gbc = new GridBagConstraints();
		
		//scroll pane 
		scrollPane = new JScrollPane(this);
		scrollPane.addComponentListener(this);
		scrollPane.setPreferredSize(new Dimension(width,height));
		scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		
		loadPanel(); // add sizeX and sizeY to parameter mby :P 
		
	}
		
	public void loadPanel() {
		int columns = 5;
//		ArrayList<Activity> activities = activityController.getAllActivities();
		int size = activities.size();
		for(int i = 0 ; i != size; i++) {
			int x = i % columns;
			int y = i / columns;
//			gbcAP.gridx = x;
//			gbcAP.gridy = y;
//			aPanel.add(new ActivityMiniPanel(activities.get(i)), gbcAP);
//			scrollPane.repaint();
			scrollPane.revalidate();
		}
	}

	@Override
	public void componentResized(ComponentEvent e) {
		// TODO Auto-generated method stub
		
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