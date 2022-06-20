package gui.layout;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.ScrollPaneConstants;
import javax.swing.ScrollPaneLayout;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;

import gui.OverviewPanel;
import gui.tools.FancyButtonOneClick;
import gui.tools.Fonts;
import gui.tools.ProjectColors;
import model.Meal;
import model.Menu;
import model.ReservedTableInfo;

public class TableInfoFrame extends JFrame{
	
	private JTable table;
	private DefaultTableModel tableModel;
	
	public TableInfoFrame(String tableName, ArrayList<ReservedTableInfo> reservedTableInfoList) {
		
		//frame setup
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setTitle(tableName);
		setResizable(false);
		setBounds(100, 100, 700, 450);
		setVisible(true);
		
		//main panel setup
		JPanel mainPanel = new JPanel();
		mainPanel.setLayout(new BorderLayout());
		mainPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(mainPanel);
		
		// Table
		table = new JTable() {
			@Override
			public Component prepareRenderer(TableCellRenderer renderer, int row, int column) {
				Component component = super.prepareRenderer(renderer, row, column);
				int rendererWidth = component.getPreferredSize().width;
				TableColumn tableColumn = getColumnModel().getColumn(column);
				tableColumn.setPreferredWidth(
						Math.max(rendererWidth + getIntercellSpacing().width, tableColumn.getPreferredWidth()));
				return component;
			}
		};
		tableModel = new DefaultTableModel();
		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		table.setPreferredScrollableViewportSize(new Dimension(getWidth(), getHeight() - 100));
		table.setFont(new Font("Tahoma", Font.PLAIN, 16));
		table.setModel(tableModel);
		table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
		table.setDefaultEditor(Object.class, null);
		table.setRowHeight((int) (table.getRowHeight() * 2));
		JTableHeader tableHeader = table.getTableHeader();
		tableHeader.setFont(new Font("Tahoma", Font.BOLD, 16));
		tableHeader.setResizingAllowed(false);
		tableHeader.setReorderingAllowed(false);
		table.setTableHeader(tableHeader);
		createTableColumns();
		populateTable(reservedTableInfoList);
		
		// Menu scroll pane
		JScrollPane scrollPane = new JScrollPane(table);
		scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
//		menuScrollPane.setPreferredSize(new Dimension(Integer.valueOf((int) (panelWidth*0.9)) , 
//				Integer.valueOf((int) ((int) panelHeight*0.8))));
		scrollPane.setBorder(null);
		scrollPane.setLayout(new ScrollPaneLayout());
		mainPanel.add(scrollPane, BorderLayout.CENTER);
		
		//button pane 
		JPanel contentPane = new JPanel();
		contentPane.setLayout(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();
		mainPanel.add(contentPane, BorderLayout.SOUTH);
		
		FancyButtonOneClick removeBtn = new FancyButtonOneClick(ProjectColors.BLACK.get(), ProjectColors.RED.get(), ProjectColors.RED.get());
		removeBtn.setFont(Fonts.FONT20.get());
		removeBtn.setBorderPainted(true);
//		removeBtn.setPreferredSize(new Dimension(panelWidth/12, ToolPanel.getPanelHeight()/2));
		removeBtn.addActionListener(e -> removeReservation());
		removeBtn.setText("Remove");
		removeBtn.setBorder(BorderFactory.createLineBorder(ProjectColors.BLACK.get(), 1));
		gbc.anchor = GridBagConstraints.FIRST_LINE_START;
		gbc.gridx = 0;
		gbc.gridy = 6;
		contentPane.add(removeBtn,gbc);
		
		FancyButtonOneClick OkBtn = new FancyButtonOneClick(ProjectColors.BLACK.get(), ProjectColors.RED.get(), ProjectColors.RED.get());
		OkBtn.setFont(Fonts.FONT20.get());
		OkBtn.setBorderPainted(true);
//		OkBtn.setPreferredSize(new Dimension(panelWidth/12, ToolPanel.getPanelHeight()/2));
		OkBtn.addActionListener(e -> cancel());
		OkBtn.setText("Ok");
		OkBtn.setBorder(BorderFactory.createLineBorder(ProjectColors.BLACK.get(), 1));
		gbc.anchor = GridBagConstraints.LAST_LINE_END;
		gbc.gridx = 1;
		gbc.gridy = 6;
		contentPane.add(OkBtn,gbc);

	}// end of constructor
	
	private void removeReservation() {
		if (table.getSelectedRow() != -1) {
			if (JOptionPane.showConfirmDialog(null,
					"Are you sure you want to delete the reservation?\nThis action is permanent!",
					"Reservation deletion", JOptionPane.YES_NO_OPTION,
					JOptionPane.WARNING_MESSAGE) == JOptionPane.YES_OPTION) {
				int id = Integer.parseInt(table.getValueAt(table.getSelectedRow(), 0).toString());
				tableModel.removeRow(table.getSelectedRow());
				OverviewPanel.getInstance().deleteReservation(id);
			}
		} else {
			JOptionPane.showMessageDialog(null, "You must select a reservation in the list you want to delete!",
					"Error", JOptionPane.WARNING_MESSAGE);
		}

	}
	
	private void cancel() {
		dispose();
	}
	
	private void populateTable(ArrayList<ReservedTableInfo> reservedTableInfoList) {
		tableModel.setRowCount(0);
		String string = new String("");
		if (!reservedTableInfoList.isEmpty()) {
			for (ReservedTableInfo rti : reservedTableInfoList) {
				String date = String.valueOf(rti.getTimestamp().get(Calendar.DAY_OF_MONTH)) +"."+ 
						String.valueOf(rti.getTimestamp().get(Calendar.MONTH));
				String time = String.valueOf(rti.getTimestamp().get(Calendar.HOUR_OF_DAY)) +":"+ 
						String.valueOf(rti.getTimestamp().get(Calendar.MINUTE));
				tableModel.addRow(new Object[] {rti.getId() ,date, time, rti.getDuration(), rti.getName(), rti.getPhone(), rti.getNote() });
			}
			tableModel.fireTableDataChanged();
		}
	}
	
	private void createTableColumns() {
		Object[] columns = {"ID", "Date", "Time", "Duration", "Name", "Phone", "note"};
		for (Object o : columns) {
			tableModel.addColumn(o);
		}
	}

}
