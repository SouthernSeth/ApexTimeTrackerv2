package com.jordan.apextimetrackerv2.windows;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
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
		if (sqlite.getTimecards().size() == 0) {
			JOptionPane.showMessageDialog(window, "You have no times stored!", "The cake is a lie!", JOptionPane.ERROR_MESSAGE);
			window.setEnabled(true);
			return;
		}

		JFrame timeSheet = new JFrame("Timesheet");
		URL iconURL = getClass().getResource("/favicon.png");
		ImageIcon icon = new ImageIcon(iconURL);
		timeSheet.setIconImage(icon.getImage());
		timeSheet.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		timeSheet.setContentPane(new JPanel());
		timeSheet.setSize(new Dimension(800, 450));
		timeSheet.setLocationRelativeTo(window);
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

		HashMap<String, HashMap<Integer, Double>> storage = new HashMap<String, HashMap<Integer, Double>>();

		for (int i = 0;i<sqlite.getTimecards().size();i++) {
			Timecard timeCard = sqlite.getTimecards().get(i);

			HashMap<Integer, Double> hours = null;

			if (storage.containsKey(timeCard.getWeek())) {
				hours = storage.get(timeCard.getWeek());
			} else {
				hours = new HashMap<Integer, Double>();
			}

			if (timeCard.getClockOutDate() == null) {
				//Do nothing
			} else if (!timeCard.getClockInDate().equalsIgnoreCase(timeCard.getClockOutDate())) {
				int hour1 = Integer.parseInt(timeCard.getClockInTime().split(":")[0]);
				int minute1 = Integer.parseInt(timeCard.getClockInTime().split(":")[1]);

				int hour2 = Integer.parseInt(timeCard.getClockOutTime().split(":")[0]);
				int minute2 = Integer.parseInt(timeCard.getClockOutTime().split(":")[1]);

				String date1 = timeCard.getClockInDate();
				double newHour1 = 23 - hour1;
				double newMinute1 = (59 - minute1) / 60;

				String date2 = timeCard.getClockOutDate();
				double newHour2 = hour2;
				double newMinute2 = minute2 / 60;

				SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
				Calendar c = Calendar.getInstance();

				try {
					c.setTime(sdf.parse(date1));
				} catch (ParseException e) {
					e.printStackTrace();
				}
				int dayOfWeek1 = c.get(Calendar.DAY_OF_WEEK);

				try {
					c.setTime(sdf.parse(date2));
				} catch (ParseException e) {
					e.printStackTrace();
				}
				int dayOfWeek2 = c.get(Calendar.DAY_OF_WEEK);

				if (hours.containsKey(dayOfWeek1)) {
					hours.replace(dayOfWeek1, hours.get(dayOfWeek1) + (newHour1 + newMinute1));
				} else {
					hours.put(dayOfWeek1, (newHour1 + newMinute1));
				}

				if (hours.containsKey(dayOfWeek2)) {
					hours.replace(dayOfWeek2, hours.get(dayOfWeek2) + (newHour2 + newMinute2));
				} else {
					hours.put(dayOfWeek2, (newHour2 + newMinute2));
				}

				if (storage.containsKey(timeCard.getWeek())) {
					storage.replace(timeCard.getWeek(), hours);
				} else {
					storage.put(timeCard.getWeek(), hours);
				}
			} else {
				String clockInTime = timeCard.getClockInTime();
				int clockinHour = Integer.parseInt(clockInTime.split(":")[0]);
				int clockinMinute = Integer.parseInt(clockInTime.split(":")[1]);

				String clockOutTime = timeCard.getClockOutTime();
				int clockoutHour = Integer.parseInt(clockOutTime.split(":")[0]);
				int clockoutMinute = Integer.parseInt(clockOutTime.split(":")[1]);

				double hour = clockoutHour - clockinHour;
				double minutes = clockoutMinute - clockinMinute;

				minutes = minutes / 60;

				double total = hour + minutes;

				if (timeCard.getToLunchTime() != null && timeCard.getReturnLunchTime() != null) {
					String toLunchTime = timeCard.getToLunchTime();
					int toLunchHour = Integer.parseInt(toLunchTime.split(":")[0]);
					int toLunchMinute = Integer.parseInt(toLunchTime.split(":")[1]);

					String returnLunchTime = timeCard.getReturnLunchTime();
					int returnLunchHour = Integer.parseInt(returnLunchTime.split(":")[0]);
					int returnLunchMinute = Integer.parseInt(returnLunchTime.split(":")[1]);

					double hourss = returnLunchHour - toLunchHour;
					double minutess = returnLunchMinute - toLunchMinute;
					minutess = minutess / 60;

					double totall = hourss + minutess;
					total -= totall;
				}

				SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
				Calendar c = Calendar.getInstance();

				try {
					c.setTime(sdf.parse(timeCard.getClockInDate()));
				} catch (ParseException e) {
					e.printStackTrace();
				}

				if (hours.containsKey(c.get(Calendar.DAY_OF_WEEK))) {
					hours.replace(c.get(Calendar.DAY_OF_WEEK), (hours.get(c.get(Calendar.DAY_OF_WEEK)) + total));
				} else {
					hours.put(c.get(Calendar.DAY_OF_WEEK), total);
				}

				if (storage.containsKey(timeCard.getWeek())) {
					storage.replace(timeCard.getWeek(), hours);
				} else {
					storage.put(timeCard.getWeek(), hours);
				}
			}
		}

		// 615-498-2655

		Set<String> list = temp.keySet();
		Iterator<String> it = list.iterator();

		while (it.hasNext()) {
			String val = it.next();
			for (int i = 0;i<rowData.length;i++) {
				if (rowData[i][0] == null) {
					rowData[i][0] = val;
					ArrayList<Timecard> timeHandlers = temp.get(val);
					for (Timecard timeHandler : timeHandlers) {
						HashMap<Integer, Double> hours = storage.get(timeHandler.getWeek());

						if (timeHandler.getDay() == Calendar.SUNDAY) {
							if (hours.get(1) == null) {
								rowData[i][1] = "-";
							} else {
								BigDecimal a = new BigDecimal(String.valueOf(hours.get(1)));
								BigDecimal b = a.setScale(2, RoundingMode.DOWN);
								rowData[i][1] = String.valueOf(b);
							}
						} else if (timeHandler.getDay() == Calendar.MONDAY) {
							if (hours.get(2) == null) {
								rowData[i][2] = "-";
							} else {
								BigDecimal a = new BigDecimal(String.valueOf(hours.get(2)));
								BigDecimal b = a.setScale(2, RoundingMode.DOWN);
								rowData[i][2] = String.valueOf(b);
							}
						} else if (timeHandler.getDay() == Calendar.TUESDAY) {
							if (hours.get(3) == null) {
								rowData[i][3] = "-";
							} else {
								BigDecimal a = new BigDecimal(String.valueOf(hours.get(3)));
								BigDecimal b = a.setScale(2, RoundingMode.DOWN);
								rowData[i][3] = String.valueOf(b);
							}
						} else if (timeHandler.getDay() == Calendar.WEDNESDAY) {
							if (hours.get(4) == null) {
								rowData[i][4] = "-";
							} else {
								BigDecimal a = new BigDecimal(String.valueOf(hours.get(4)));
								BigDecimal b = a.setScale(2, RoundingMode.DOWN);
								rowData[i][4] = String.valueOf(b);
							}
						} else if (timeHandler.getDay() == Calendar.THURSDAY) {
							if (hours.get(5) == null) {
								rowData[i][5] = "-";
							} else {
								BigDecimal a = new BigDecimal(String.valueOf(hours.get(5)));
								BigDecimal b = a.setScale(2, RoundingMode.DOWN);
								rowData[i][5] = String.valueOf(b);
							}
						} else if (timeHandler.getDay() == Calendar.FRIDAY) {
							if (hours.get(6) == null) {
								rowData[i][6] = "-";
							} else {
								BigDecimal a = new BigDecimal(String.valueOf(hours.get(6)));
								BigDecimal b = a.setScale(2, RoundingMode.DOWN);
								rowData[i][6] = String.valueOf(b);
							}
						} else if (timeHandler.getDay() == Calendar.SATURDAY) {
							if (hours.get(7) == null) {
								rowData[i][7] = "-";
							} else {
								BigDecimal a = new BigDecimal(String.valueOf(hours.get(7)));
								BigDecimal b = a.setScale(2, RoundingMode.DOWN);
								rowData[i][7] = String.valueOf(b);
							}
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

