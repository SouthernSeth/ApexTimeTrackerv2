package com.jordan.apextimetrackerv2.windows;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.RowSorter;
import javax.swing.SortOrder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

import com.jordan.apextimetrackerv2.ApexTimeTrackerv2;
import com.jordan.apextimetrackerv2.SQLite;
import com.jordan.apextimetrackerv2.util.Timecard;

public class TimesheetWindow {
	
	public String rowData[][];
	public Object columnNames[] = { "Week", "Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"};

	public TimesheetWindow(final JFrame window, ApexTimeTrackerv2 att, SQLite sqlite) {
		JFrame timeSheet = new JFrame("Timesheet");
		URL iconURL = getClass().getResource("/favicon.png");
		ImageIcon icon = new ImageIcon(iconURL);
		timeSheet.setIconImage(icon.getImage());
		timeSheet.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		timeSheet.setContentPane(new JPanel());
		timeSheet.setSize(new Dimension(800, 450));
		timeSheet.setLocationRelativeTo(null);
		timeSheet.setAlwaysOnTop(true);
		timeSheet.setResizable(true);

		JTable table = new JTable();
		
		rowData = new String[att.getNumberOfWeeks()][8];

		HashMap<String, ArrayList<Timecard>> temp = new HashMap<String, ArrayList<Timecard>>();
		for (int i = 0;i<sqlite.getTimecards().size();i++) {
			Timecard timeHandler = sqlite.getTimecards().get(i);
			
			if (temp.containsKey(timeHandler.getWeek())) {
				temp.get(timeHandler.getWeek()).add(timeHandler);
			} else {
				temp.put(timeHandler.getWeek(), new ArrayList<Timecard>());
				temp.get(timeHandler.getWeek()).add(timeHandler);
			}
		}
		
		Set<String> list = temp.keySet();
		Iterator<String> it = list.iterator();
		
		while (it.hasNext()) {
			String val = it.next();
			for (int i = 0;i<rowData.length;i++) {
				if (rowData[i][0] == null) {
					rowData[i][0] = val;
					ArrayList<Timecard> timeHandlers = temp.get(val);
					for (Timecard timeHandler : timeHandlers) {
						BigDecimal a = new BigDecimal(String.valueOf(timeHandler.getHoursWorked()));
						BigDecimal b = a.setScale(2, RoundingMode.DOWN);
						
						String num = b.toPlainString();
						
						if (timeHandler.getHoursWorked()<=0.0) {
							num = "Not Calculatable Yet!";
						}
						
						if (timeHandler.getDay().equalsIgnoreCase("Sunday")) {
							rowData[i][1] = num;
						} else if (timeHandler.getDay().equalsIgnoreCase("Monday")) {
							rowData[i][2] = num;
						} else if (timeHandler.getDay().equalsIgnoreCase("Tuesday")) {
							rowData[i][3] = num;
						} else if (timeHandler.getDay().equalsIgnoreCase("Wednesday")) {
							rowData[i][4] = num;
						} else if (timeHandler.getDay().equalsIgnoreCase("Thursday")) {
							rowData[i][5] = num;
						} else if (timeHandler.getDay().equalsIgnoreCase("Friday")) {
							rowData[i][6] = num;
						} else if (timeHandler.getDay().equalsIgnoreCase("Saturday")) {
							rowData[i][7] = num;
						}
					}
					break;
				}
			}
		}

		TableModel model = new DefaultTableModel(rowData, columnNames) {
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};
		
		table.setModel(model);
		TableRowSorter<TableModel> sorter = new TableRowSorter<TableModel>(table.getModel());
		table.setRowSorter(sorter);
		
		List<RowSorter.SortKey> sortKeys = new ArrayList<>(25);
		sortKeys.add(new RowSorter.SortKey(0, SortOrder.DESCENDING));
		sorter.setSortKeys(sortKeys);

		table.setColumnSelectionAllowed(false);

		table.getTableHeader().setReorderingAllowed(false);
		
		table.setEnabled(false);
		table.setRowSelectionInterval(0, 0);

		JScrollPane scrollPaneTable = new JScrollPane(table);

		timeSheet.getContentPane().setLayout(new BorderLayout());
		timeSheet.getContentPane().add(table.getTableHeader(), BorderLayout.PAGE_START);
		timeSheet.getContentPane().add(scrollPaneTable, BorderLayout.CENTER);

		timeSheet.setVisible(true);

		timeSheet.addWindowListener(new WindowListener() {
			@Override
			public void windowClosing(WindowEvent arg0) {
				window.setEnabled(true);
			}

			@Override
			public void windowActivated(WindowEvent arg0) {
			}

			@Override
			public void windowClosed(WindowEvent arg0) {
			}

			@Override
			public void windowDeactivated(WindowEvent arg0) {
			}

			@Override
			public void windowDeiconified(WindowEvent arg0) {
			}

			@Override
			public void windowIconified(WindowEvent arg0) {
			}

			@Override
			public void windowOpened(WindowEvent arg0) {
			}
		});
	}

}

