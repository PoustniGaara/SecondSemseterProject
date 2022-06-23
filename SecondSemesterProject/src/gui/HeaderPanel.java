package gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.JButton;
import javax.swing.JPanel;

import gui.layout.LayoutEditorFrame;
import gui.tools.FancyButtonMoreClick;
import gui.tools.FancyButtonOneClick;
import gui.tools.ProjectColors;

import org.kordamp.ikonli.coreui.CoreUiFree;
import org.kordamp.ikonli.swing.FontIcon;

public class HeaderPanel extends JPanel {

	private static HeaderPanel instance;
	private FancyButtonMoreClick overviewButton, reservationButton, menuButton, currentButton;
	private FancyButtonOneClick optionButton;
	private LayoutEditorFrame layoutEditorFrame;

	private HeaderPanel() {

		// Panel dimensions setup
		int mainHeight = (int) MainFrame.height;
		int mainWidth = (int) MainFrame.width;
		int panelHeight = (int) ((int) mainHeight * 0.08);
		int panelWidth = mainWidth;

		// Panel functional setup
		setPreferredSize(new Dimension(panelWidth, panelHeight));
		setBackground(ProjectColors.GREEN.get());
		setLayout(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.weightx = 0.0;
		gbc.weighty = 1;
		gbc.anchor = GridBagConstraints.FIRST_LINE_START;

		// Panel support classes
		Font font1 = new Font("Monaco", Font.PLAIN, 30);
		Font font2 = new Font("Monaco", Font.PLAIN, 20);

		// Panel buttons setup
		overviewButton = new FancyButtonMoreClick(Color.white, ProjectColors.BLUE.get(), ProjectColors.BLUE.get(),
				ProjectColors.LIGHT_BLUE.get(), Color.white);
		overviewButton.setText("Overview");
		overviewButton.setFont(font1);
		overviewButton.setPreferredSize(new Dimension((int) (mainWidth * 0.2), panelHeight));
		currentButton = overviewButton;
		overviewButton.clicked(true);
		overviewButton.addActionListener(e -> showOverview());
		gbc.gridheight = 2;
		gbc.gridx = 0;
		gbc.gridy = 0;
		add(overviewButton, gbc);

		reservationButton = new FancyButtonMoreClick(Color.white, ProjectColors.BLUE.get(), ProjectColors.BLUE.get(),
				ProjectColors.LIGHT_BLUE.get(), Color.white);
		reservationButton.setText("Reservations");
		reservationButton.setFont(font1);
		reservationButton.setPreferredSize(new Dimension((int) (mainWidth * 0.2), panelHeight));
		reservationButton.addActionListener(e -> showReservations());
		gbc.gridx = 1;
		add(reservationButton, gbc);

		menuButton = new FancyButtonMoreClick(Color.white, ProjectColors.BLUE.get(), ProjectColors.BLUE.get(),
				ProjectColors.LIGHT_BLUE.get(), Color.white);
		menuButton.setText("Menus");
		menuButton.setFont(font1);
		menuButton.setPreferredSize(new Dimension((int) (mainWidth * 0.2), panelHeight));
		menuButton.addActionListener(e -> showMenu());
		gbc.gridx = 2;
		add(menuButton, gbc);

		optionButton = new FancyButtonOneClick(ProjectColors.RED.get(), new Color(7, 100, 90),
				ProjectColors.GREEN.get());
		optionButton.setState(true);
		optionButton.addActionListener(e -> openLayoutEditor());
		FontIcon endIcon = FontIcon.of(CoreUiFree.COG);
		endIcon.setIconSize(30);
		optionButton.setIcon(endIcon);
		optionButton.setText("Layout editor");
		optionButton.setFont(font2);
		optionButton.setPreferredSize(new Dimension((int) (mainWidth * 0.12), (int) (panelHeight * 0.65)));
		gbc.anchor = GridBagConstraints.LINE_END;
		gbc.gridheight = 1;
		gbc.weighty = 0.5;
		gbc.gridx = 3;
		gbc.gridy = 0;
		gbc.weightx = 1;
		add(optionButton, gbc);



	}// end of constructor

	public LayoutEditorFrame getLayoutEditorFrame() {
		return layoutEditorFrame;
	}

	private void openLayoutEditor() {
//		LayoutEditorFrame.getInstance().maximize();
		layoutEditorFrame = new LayoutEditorFrame();
	}

	private void showOverview() {
		MainFrame.getInstance().showOverviewPanel();
		currentButton.clicked(false);
		overviewButton.clicked(true);
		currentButton = overviewButton;
	}

	private void showReservations() {
		MainFrame.getInstance().showReservationsPanel();
		currentButton.clicked(false);
		reservationButton.clicked(true);
		currentButton = reservationButton;
	}

	private void showMenu() {
		MainFrame.getInstance().showMenuPanel();
		currentButton.clicked(false);
		menuButton.clicked(true);
		currentButton = menuButton;
	}

	public static HeaderPanel getInstance() {
		if (instance == null)
			return instance = new HeaderPanel();
		else
			return instance;
	}
}
