package com.jordan.apextimetrackerv2;

import java.awt.Color;
import java.awt.Component;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Timer;
import java.util.TimerTask;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import com.jordan.apextimetrackerv2.listeners.EditTimeMouseListener;
import com.jordan.apextimetrackerv2.util.Settings;
import com.jordan.apextimetrackerv2.util.Strings;
import com.jordan.apextimetrackerv2.util.Timecard;
import com.jordan.apextimetrackerv2.windows.EmailTimesheet;
import com.jordan.apextimetrackerv2.windows.LoadTimecardWindow;
import com.jordan.apextimetrackerv2.windows.ManuallyAddTimeWindow;
import com.jordan.apextimetrackerv2.windows.SettingsWindow;
import com.jordan.apextimetrackerv2.windows.TimerWindow;
import com.jordan.apextimetrackerv2.windows.TimesheetWindow;

public class ApexTimeTrackerv2 {

	private ApexTimeTrackerv2 main;

	private TimerWindow timerWindow;

	private JFrame window;
	private JPanel panel;

	private SQLite sqlite;

	private JMenuBar menuBar;
	private JMenu file;
	private JMenu help;
	private JMenuItem exit;
	private JCheckBoxMenuItem editTime;
	private JMenuItem appSettings;
	private JMenuItem viewTimesheet;
	private JMenuItem deleteTimesheet;
	private JMenuItem deleteTime;
	private JMenuItem emailTimesheet;
	private JMenuItem about;
	private JMenuItem createNewTimecard;
	private JMenuItem loadTimecard;
	private JMenuItem manuallyEnterTime;

	private JButton clockIn;
	private JButton clockOut;
	private JButton toLunch;
	private JButton returnLunch;

	private JTextField clockInField;
	private JTextField clockOutField;
	private JTextField toLunchField;
	private JTextField returnLunchField;

	private JLabel clock;
	private JLabel timerButton;

	private int loadedID = 0;

	private JFrame bg = null;
	private boolean minimized = false;
	private boolean editMode = false;

	private ArrayList<Timecard> timecards = new ArrayList<Timecard>();

	public ApexTimeTrackerv2(SQLite sqlite) {
		main = this;
		Strings.username = System.getProperty("user.name");

		if (Strings.username.equalsIgnoreCase("jrjackson")) {
			Strings.username = "Joshua Jackson";
		}

		new Settings();
		this.sqlite = sqlite;
		loadTime();
		loadApp();
		startClock();
	}

	private void startClock() {
		final SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy kk:mm:ss");

		Timer taskTimer = new Timer();
		taskTimer.scheduleAtFixedRate(new TimerTask() {
			@Override
			public void run() {
				clock.setText(sdf.format(new Date()));
			}
		}, 0, 1000);
	}

	private void emailTimesheet() {
		new EmailTimesheet(window, this, sqlite);
	}

	private void editMode(final boolean enabled) {
		editMode = enabled;

		new Thread(){
			public void run() {
				if (enabled) {
					bg.setVisible(true);

					window.addWindowListener(new WindowListener() {

						@Override
						public void windowActivated(WindowEvent e) {
						}

						@Override
						public void windowClosed(WindowEvent e) {
						}

						@Override
						public void windowClosing(WindowEvent e) {

						}

						@Override
						public void windowDeactivated(WindowEvent e) {
						}

						@Override
						public void windowDeiconified(WindowEvent e) {
							minimized = false;
						}

						@Override
						public void windowIconified(WindowEvent e) {
							minimized = true;
						}

						@Override
						public void windowOpened(WindowEvent e) {
						}

					});

					while (editMode) {
						int w = (int) (window.getSize().getWidth() + 20);
						int h = (int) (window.getSize().getHeight() + 20);

						bg.setLocation(window.getX() - 10, window.getY() - 10);
						bg.setSize(w, h);

						if (minimized) {
							bg.setVisible(false);
						} else {
							bg.setVisible(true);
						}

						window.setAutoRequestFocus(true);
					}

					bg.setVisible(false);
				}
			}
		}.start();
	}

	private String getDate() {
		Date dateObj = new Date();
		SimpleDateFormat date = new SimpleDateFormat("MM/dd/yyyy");
		String date_output = date.format(dateObj);
		return date_output;
	}

	private String getTime() {
		Date dateObj = new Date();
		SimpleDateFormat time = new SimpleDateFormat("kk:mm");
		String time_output = time.format(dateObj);
		return time_output;
	}

	private String getWeek() {
		Calendar now = Calendar.getInstance();
		Date date = new Date();
		SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy");

		now.setTime(date);

		String[] days = new String[7];
		int delta = -now.get(GregorianCalendar.DAY_OF_WEEK) + 1;
		now.add(Calendar.DAY_OF_MONTH, delta);
		for (int i = 0; i < 7; i++)
		{
			days[i] = format.format(now.getTime());
			now.add(Calendar.DAY_OF_MONTH, 1);
		}

		return days[0] + "-" + days[6];
	}

	public int getNumberOfWeeks() {
		ArrayList<String> weeks = new ArrayList<String>();

		if (timecards.size() == 0) {
			return 0;
		}

		for (int i = 0;i<timecards.size();i++) {
			if (!weeks.contains(timecards.get(i).getWeek())) {
				weeks.add(timecards.get(i).getWeek());
			}
		}

		return weeks.size();
	}

	public ArrayList<Timecard> getTimecards() {
		return timecards;
	}

	public void loadTimecard(int id) {
		Timecard timeCard = sqlite.getTimecard(id);

		clockInField.setText("");
		clockOutField.setText("");
		toLunchField.setText("");
		returnLunchField.setText("");
		clockIn.setEnabled(true);
		clockOut.setEnabled(false);
		toLunch.setEnabled(false);
		returnLunch.setEnabled(false);
		loadedID = timeCard.getID();

		clockInField.setText(timeCard.getClockInDate() + " " + timeCard.getClockInTime());
		clockIn.setEnabled(false);
		toLunch.setEnabled(true);
		clockOut.setEnabled(true);
		if (timeCard.getToLunchDate() != null) {
			toLunchField.setText(timeCard.getToLunchDate() + " " + timeCard.getToLunchTime());
			toLunch.setEnabled(false);
			returnLunch.setEnabled(true);
		}
		if (timeCard.getReturnLunchDate() != null) {
			returnLunchField.setText(timeCard.getReturnLunchDate() + " " + timeCard.getReturnLunchTime());
			returnLunch.setEnabled(false);
		}
		if (timeCard.getClockOutDate() != null) {
			clockOutField.setText(timeCard.getClockOutDate() + " " + timeCard.getClockOutTime());
			clockOut.setEnabled(false);
		}
	}

	public void clockIn() {
		if (loadedID != 0) {
			Timecard timecard = sqlite.getLastTimecard();
			timecard.setClockInDate(getDate());
			timecard.setClockInTime(getTime());
			sqlite.addTimecard(loadedID, timecard.getWeek());
			sqlite.setClockIn(loadedID, getDate(), getTime());
		} else {
			for (int i = 0;i<sqlite.getTimecards().size();i++) {
				Timecard time = sqlite.getTimecards().get(i);
				if (time.getClockInDate().equalsIgnoreCase(getDate())) {
					JOptionPane.showMessageDialog(window, "You cannot clock in because there is already a timecard with the same clock in date! If you are trying to add time for a previous day please use the File > Manually Add Time");
					return;
				}
			}
			
			int id = sqlite.generateID();
			Timecard timecard = new Timecard(id, getWeek(), getDate(), getTime());
			loadedID = id;
			sqlite.addTimecard(id, timecard.getWeek());
			sqlite.setClockIn(id, getDate(), getTime());
		}

		clockInField.setText(getDate() + " " + getTime());
		clockIn.setEnabled(false);
		clockOut.setEnabled(true);
		toLunch.setEnabled(true);
	}

	public void clockIn(Date date, int hour, int minute) {
		SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
		String date_output = sdf.format(date);
		String time_output = String.valueOf(hour)+":"+String.valueOf(minute);

		if (minute < 10) {
			time_output = String.valueOf(hour)+":0"+String.valueOf(minute);
		}

		if (sqlite.getLastTimecard() != null) {
			Timecard timecard = sqlite.getLastTimecard();
			timecard.setClockInDate(date_output);
			timecard.setClockInTime(time_output);
			sqlite.setClockIn(loadedID, date_output, time_output);
		}

		clockInField.setText(date_output + " " + time_output);
	}

	public void clockOut() {
		if (sqlite.getLastTimecard() != null) {
			Timecard timecard = sqlite.getLastTimecard();
			timecard.setClockOutDate(getDate());
			timecard.setClockOutTime(getTime());
			sqlite.setClockOut(loadedID, getDate(), getTime());
		} 

		clockOutField.setText(getDate() + " " + getTime());
		clockOut.setEnabled(false);
	}

	public void clockOut(Date date, int hour, int minute) {
		SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
		String date_output = sdf.format(date);
		String time_output = String.valueOf(hour)+":"+String.valueOf(minute);

		if (minute < 10) {
			time_output = String.valueOf(hour)+":0"+String.valueOf(minute);
		}

		if (sqlite.getLastTimecard() != null) {
			Timecard timecard = sqlite.getLastTimecard();
			timecard.setClockOutDate(date_output);
			timecard.setClockOutTime(time_output);
			sqlite.setClockOut(loadedID, date_output, time_output);
		} 

		clockOutField.setText(date_output + " " + time_output);
	}

	public void toLunch() {
		if (sqlite.getLastTimecard() != null) {
			Timecard timecard = sqlite.getLastTimecard();
			timecard.setToLunchDate(getDate());
			timecard.setToLunchTime(getTime());
			sqlite.setToLunch(loadedID, getDate(), getTime());
		} 

		toLunchField.setText(getDate() + " " + getTime());
		returnLunch.setEnabled(true);
		toLunch.setEnabled(false);
	}

	public void toLunch(Date date, int hour, int minute) {
		SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
		String date_output = sdf.format(date);
		String time_output = String.valueOf(hour)+":"+String.valueOf(minute);

		if (minute < 10) {
			time_output = String.valueOf(hour)+":0"+String.valueOf(minute);
		}

		if (sqlite.getLastTimecard() != null) {
			Timecard timecard = sqlite.getLastTimecard();
			timecard.setToLunchDate(date_output);
			timecard.setToLunchTime(time_output);
			sqlite.setToLunch(loadedID, date_output, time_output);
		} 

		toLunchField.setText(date_output + " " + time_output);
	}

	public void returnLunch() {
		if (sqlite.getLastTimecard() != null) {
			Timecard timecard = sqlite.getLastTimecard();
			timecard.setReturnLunchDate(getDate());
			timecard.setReturnLunchTime(getTime());
			sqlite.setReturnLunch(loadedID, getDate(), getTime());
		} 

		returnLunchField.setText(getDate() + " " + getTime());
		returnLunch.setEnabled(false);
	}

	public void returnLunch(Date date, int hour, int minute) {
		SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
		String date_output = sdf.format(date);
		String time_output = String.valueOf(hour)+":"+String.valueOf(minute);

		if (minute < 10) {
			time_output = String.valueOf(hour)+":0"+String.valueOf(minute);
		}

		if (sqlite.getLastTimecard() != null) {
			Timecard timecard = sqlite.getLastTimecard();
			timecard.setReturnLunchDate(date_output);
			timecard.setReturnLunchTime(time_output);
			sqlite.setReturnLunch(loadedID, date_output, time_output);
		} 

		returnLunchField.setText(date_output + " " + time_output);
	}

	private void loadTime() {
		ResultSet rs = null;
		try {
			rs = sqlite.getConnection().createStatement().executeQuery("select * from timecards");
		} catch (SQLException e2) {
			e2.printStackTrace();
		}

		int i = 0;

		try {
			while (rs.next()) {
				i++;
				int id = rs.getInt("id");
				String clockindate = rs.getString("clockindate");
				String clockintime = rs.getString("clockintime");
				String tolunchdate = rs.getString("tolunchdate");
				String tolunchtime = rs.getString("tolunchtime");
				String returnlunchdate = rs.getString("returnlunchdate");
				String returnlunchtime = rs.getString("returnlunchtime");
				String clockoutdate = rs.getString("clockoutdate");
				String clockouttime = rs.getString("clockouttime");

				System.out.println("id = " + id);
				System.out.println("clockindate = " + clockindate);
				System.out.println("clockintime = " + clockintime);
				System.out.println("tolunchdate = " + tolunchdate);
				System.out.println("tolunchtime = " + tolunchtime);
				System.out.println("returnlunchdate = " + returnlunchdate);
				System.out.println("returnlunchtime = " + returnlunchtime);
				System.out.println("clockoutdate = " + clockoutdate);
				System.out.println("clockouttime = " + clockouttime);
				System.out.println("-----------------------------------------------");

				Timecard timeHandler = new Timecard(id, clockindate, clockintime, tolunchdate, tolunchtime, returnlunchdate, returnlunchtime, clockoutdate, clockouttime);
				timecards.add(timeHandler);
			}
		} catch (SQLException e2) {
			e2.printStackTrace();
		}

		System.out.println(i + " timecard(s) have been loaded!");
	}

	private void loadUI() {
		window = new JFrame(Strings.applicationName + " - " + Strings.username);
		panel = new JPanel();

		timerWindow = new TimerWindow(window);

		Font f = new Font("Arial", Font.BOLD, 18);
		timerButton = new JLabel(Character.toString((char) 9650));
		timerButton.setName("disabled");
		timerButton.setFont(f);
		timerButton.setHorizontalAlignment(SwingConstants.CENTER);

		clockInField = new JTextField();
		clockInField.setName("clockin");
		clockInField.setEditable(false);
		clockInField.setColumns(20);

		clockOutField = new JTextField();
		clockOutField.setName("clockout");
		clockOutField.setEditable(false);
		clockOutField.setColumns(20);

		toLunchField = new JTextField();
		toLunchField.setName("tolunch");
		toLunchField.setEditable(false);
		toLunchField.setColumns(20);

		returnLunchField = new JTextField();
		returnLunchField.setName("returnlunch");
		returnLunchField.setEditable(false);
		returnLunchField.setColumns(20);

		clockIn = new JButton(Strings.clockInButton);
		clockOut = new JButton(Strings.clockOutButton);
		toLunch = new JButton(Strings.toLunchButton);
		returnLunch = new JButton(Strings.returnLunchButton);

		clockIn.setFocusable(false);
		clockOut.setFocusable(false);
		toLunch.setFocusable(false);
		returnLunch.setFocusable(false);

		clockOut.setEnabled(false);
		toLunch.setEnabled(false);
		returnLunch.setEnabled(false);

		menuBar = new JMenuBar();
		file = new JMenu("File");
		help = new JMenu("Help");
		about = new JMenuItem("About");
		exit = new JMenuItem("Exit");
		editTime = new JCheckBoxMenuItem("Edit Time");
		appSettings = new JMenuItem("Settings");
		viewTimesheet = new JMenuItem(Strings.viewTimesheetButton);
		deleteTime = new JMenuItem(Strings.deleteTimesheetButton);
		deleteTimesheet = new JMenuItem(Strings.deleteAllTimeButton);
		emailTimesheet = new JMenuItem(Strings.emailTimesheetButton);
		createNewTimecard = new JMenuItem("Create New Timecard");
		loadTimecard = new JMenuItem("Load Timecard");
		manuallyEnterTime = new JMenuItem("Manually Add Time");

		help.add(about);
		file.add(createNewTimecard);
		file.add(manuallyEnterTime);
		file.add(loadTimecard);
		file.add(new JSeparator());
		file.add(viewTimesheet);
		file.add(emailTimesheet);
		file.add(deleteTime);
		file.add(deleteTimesheet);
		file.add(new JSeparator());
		file.add(appSettings);
		file.add(editTime);
		file.add(new JSeparator());
		file.add(exit);
		menuBar.add(file);
		menuBar.add(help);

		bg = new JFrame();
		bg.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		bg.getContentPane().setBackground(Color.RED);
		bg.setResizable(false);
		bg.setUndecorated(true);
		bg.setFocusableWindowState(false);

		clockInField.addMouseListener(new EditTimeMouseListener(window, this, bg, editTime, clockInField));
		clockOutField.addMouseListener(new EditTimeMouseListener(window, this, bg, editTime, clockOutField));
		toLunchField.addMouseListener(new EditTimeMouseListener(window, this, bg, editTime, toLunchField));
		returnLunchField.addMouseListener(new EditTimeMouseListener(window, this, bg, editTime, returnLunchField));

		manuallyEnterTime.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				new ManuallyAddTimeWindow(window, main);
			}
		});
		
		loadTimecard.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				window.setEnabled(false);
				new LoadTimecardWindow(window, main, sqlite);
			}
		});

		emailTimesheet.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				emailTimesheet();
			}
		});

		viewTimesheet.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				new TimesheetWindow(window, main, sqlite);
			}
		});

		deleteTime.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (sqlite.getTimecards().size() == 0) {
					JOptionPane.showMessageDialog(window, "You have no times stored!", "The cake is a lie!", JOptionPane.ERROR_MESSAGE);
					window.setEnabled(true);
					return;
				}
				
				int res = JOptionPane.showConfirmDialog(window, "Are you sure you want to delete this time?", "Delete Time", JOptionPane.YES_NO_OPTION);

				if (res == JOptionPane.OK_OPTION) {
					int result = JOptionPane.showConfirmDialog(window, "Do you want to load the last timecard stored?", "Load Time", JOptionPane.YES_NO_OPTION);

					if (result == JOptionPane.YES_OPTION) {
						sqlite.removeTimecard(loadedID);
						clockInField.setText("");
						clockOutField.setText("");
						toLunchField.setText("");
						returnLunchField.setText("");
						clockIn.setEnabled(true);
						clockOut.setEnabled(false);
						toLunch.setEnabled(false);
						returnLunch.setEnabled(false);
						loadedID = 0;

						Timecard timeHandler = sqlite.getLastTimecard();
						if (timeHandler != null) {
							loadedID = timeHandler.getID();
							clockInField.setText(timeHandler.getClockInDate() + " " + timeHandler.getClockInTime());
							clockIn.setEnabled(false);
							toLunch.setEnabled(true);
							clockOut.setEnabled(true);
							if (timeHandler.getToLunchDate() != null) {
								toLunchField.setText(timeHandler.getToLunchDate() + " " + timeHandler.getToLunchTime());
								toLunch.setEnabled(false);
								returnLunch.setEnabled(true);
							}
							if (timeHandler.getReturnLunchDate() != null) {
								returnLunchField.setText(timeHandler.getReturnLunchDate() + " " + timeHandler.getReturnLunchTime());
								returnLunch.setEnabled(false);
							}
							if (timeHandler.getClockOutDate() != null) {
								clockOutField.setText(timeHandler.getClockOutDate() + " " + timeHandler.getClockOutTime());
								clockOut.setEnabled(false);
							}
						}
					} else {
						sqlite.removeTimecard(loadedID);
						clockInField.setText("");
						clockOutField.setText("");
						toLunchField.setText("");
						returnLunchField.setText("");
						clockIn.setEnabled(true);
						clockOut.setEnabled(false);
						toLunch.setEnabled(false);
						returnLunch.setEnabled(false);
						loadedID = 0;
					}
				}
			}
		});

		createNewTimecard.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				int res = JOptionPane.showConfirmDialog(window, "Are you sure you want to create a new timecard?", "Create New Timecard", JOptionPane.YES_NO_OPTION);

				if (res == JOptionPane.OK_OPTION) {
					clockInField.setText("");
					clockOutField.setText("");
					toLunchField.setText("");
					returnLunchField.setText("");
					clockIn.setEnabled(true);
					clockOut.setEnabled(false);
					toLunch.setEnabled(false);
					returnLunch.setEnabled(false);
					loadedID = 0;
				}
			}
		});

		deleteTimesheet.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if (sqlite.getTimecards().size() == 0) {
					JOptionPane.showMessageDialog(window, "You have no times stored!", "The cake is a lie!", JOptionPane.ERROR_MESSAGE);
					window.setEnabled(true);
					return;
				}
				
				int res = JOptionPane.showConfirmDialog(window, Strings.deleteTimesheetMessage, "Are you sure you want to do that?", JOptionPane.YES_NO_OPTION);
				if (res == JOptionPane.YES_OPTION) {
					JOptionPane.showMessageDialog(window, "The timesheet has been deleted!", "Poof! It's gone!", JOptionPane.WARNING_MESSAGE);
					sqlite.dropTable();
					window.dispatchEvent(new WindowEvent(window, WindowEvent.WINDOW_CLOSING));
				}
			}
		});

		editTime.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if (sqlite.getTimecards().size() == 0) {
					JOptionPane.showMessageDialog(window, "You have no times stored!", "The cake is a lie!", JOptionPane.ERROR_MESSAGE);
					window.setEnabled(true);
					editTime.setSelected(false);
					return;
				}
				
				if (!editTime.isSelected()) {
					editMode(false);
				} else {
					editMode(true);
				}
			}
		});

		about.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				JOptionPane.showMessageDialog(window, Strings.aboutMessage, "About", JOptionPane.QUESTION_MESSAGE);
			}
		});

		clockIn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (editMode) {
					JOptionPane.showMessageDialog(window, "You are in edit mode and cannot use the time stamp feature right now", "Error", JOptionPane.ERROR_MESSAGE);
					return;
				}
				clockIn();
			}
		});

		toLunch.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (editMode) {
					JOptionPane.showMessageDialog(window, "You are in edit mode and cannot use the time stamp feature right now", "Error", JOptionPane.ERROR_MESSAGE);
					return;
				}
				toLunch();

				int ans = JOptionPane.showConfirmDialog(window, "Do you want to start a lunch timer?", "Timer", JOptionPane.YES_NO_OPTION);

				if (ans == JOptionPane.YES_OPTION) {
					timerWindow.setVisible(true, timerButton);
					timerWindow.startTimer(60);
				}
			}
		});

		returnLunch.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (editMode) {
					JOptionPane.showMessageDialog(window, "You are in edit mode and cannot use the time stamp feature right now", "Error", JOptionPane.ERROR_MESSAGE);
					return;
				}
				returnLunch();
			}
		});

		clockOut.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (editMode) {
					JOptionPane.showMessageDialog(window, "You are in edit mode and cannot use the time stamp feature right now", "Error", JOptionPane.ERROR_MESSAGE);
					return;
				}

				clockOut();

				Calendar cal = Calendar.getInstance();
				cal.setTime(new Date());
				boolean ldoww = cal.get(Calendar.DAY_OF_WEEK) == Calendar.FRIDAY;

				if (Settings.lastDayOfWorkWeek.equalsIgnoreCase("MONDAY")) {
					ldoww = cal.get(Calendar.DAY_OF_WEEK) == Calendar.MONDAY;
				} else if (Settings.lastDayOfWorkWeek.equalsIgnoreCase("TUESDAY")) {
					ldoww = cal.get(Calendar.DAY_OF_WEEK) == Calendar.TUESDAY;
				} else if (Settings.lastDayOfWorkWeek.equalsIgnoreCase("WEDNESDAY")) {
					ldoww = cal.get(Calendar.DAY_OF_WEEK) == Calendar.WEDNESDAY;
				} else if (Settings.lastDayOfWorkWeek.equalsIgnoreCase("THURSDAY")) {
					ldoww = cal.get(Calendar.DAY_OF_WEEK) == Calendar.THURSDAY;
				} else if (Settings.lastDayOfWorkWeek.equalsIgnoreCase("FRIDAY")) {
					ldoww = cal.get(Calendar.DAY_OF_WEEK) == Calendar.FRIDAY;
				} else if (Settings.lastDayOfWorkWeek.equalsIgnoreCase("SATURDAY")) {
					ldoww = cal.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY;
				} else if (Settings.lastDayOfWorkWeek.equalsIgnoreCase("SUNDAY")) {
					ldoww = cal.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY;
				}

				if (ldoww) {
					JOptionPane.showMessageDialog(window, "It's the last day of the work week! Time to put that time in! I have composed an email so you can send your hours to your email just incase, as well as I have opened a browser and navigated to the contractor login page for Apex Systems Inc!");
					try {
						emailTimesheet();
						Desktop.getDesktop().browse(new URI("https://myapex.apexsystemsinc.com/psp/MYAPEX/CONTRACTOR/HRMS/c/APEX.AX_HOME_PAGE.GBL?FolderPath=PORTAL_ROOT_OBJECT.AX_HOME_PAGE&IsFolder=false&IgnoreParamTempl=FolderPath%2cIsFolder"));
					} catch (URISyntaxException e1) {
						e1.printStackTrace();
					} catch (IOException e1) {
						e1.printStackTrace();
					}
				}
			}
		});

		exit.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				System.exit(0);
			}
		});

		appSettings.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				new SettingsWindow(window);
			}
		});

		if (sqlite.getLastTimecard() != null) {
			Timecard timeHandler = sqlite.getLastTimecard();
			loadedID = timeHandler.getID();
			clockInField.setText(timeHandler.getClockInDate() + " " + timeHandler.getClockInTime());
			clockIn.setEnabled(false);
			toLunch.setEnabled(true);
			clockOut.setEnabled(true);
			if (timeHandler.getToLunchDate() != null) {
				toLunchField.setText(timeHandler.getToLunchDate() + " " + timeHandler.getToLunchTime());
				toLunch.setEnabled(false);
				returnLunch.setEnabled(true);
			}
			if (timeHandler.getReturnLunchDate() != null) {
				returnLunchField.setText(timeHandler.getReturnLunchDate() + " " + timeHandler.getReturnLunchTime());
				returnLunch.setEnabled(false);
			}
			if (timeHandler.getClockOutDate() != null) {
				clockOutField.setText(timeHandler.getClockOutDate() + " " + timeHandler.getClockOutTime());
				clockOut.setEnabled(false);
			}
		}
	}

	private void loadPanel() {
		loadUI();

		panel = new JPanel();
		panel.setBackground(Color.WHITE);
		panel.setLayout(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();

		Dimension dim = new Dimension(425, 225);

		panel.setPreferredSize(dim);
		panel.setMaximumSize(dim);
		panel.setMinimumSize(dim);

		Component[] components = new Component[] {
				clockIn,clockInField,toLunch,toLunchField,returnLunch,returnLunchField, clockOut, clockOutField
		};

		String[] locations = new String[] {
				"0,0","1,0","0,1","1,1","0,2","1,2","0,3","1,3"
		};

		for (int i = 0;i<components.length;i++) {
			gbc = new GridBagConstraints();
			gbc.fill = GridBagConstraints.HORIZONTAL;
			gbc.gridx = Integer.parseInt(locations[i].split(",")[0]);
			gbc.gridy = Integer.parseInt(locations[i].split(",")[1]);
			gbc.gridwidth = 1;
			gbc.insets = new Insets(0,0,15,5);
			panel.add(components[i], gbc);
		}

		gbc = new GridBagConstraints();
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.gridwidth = 2;
		gbc.gridx = 0;
		gbc.gridy = 4;

		clock = new JLabel();
		Font f = new Font("Arial", Font.BOLD, 18);
		clock.setFont(f);
		clock.setHorizontalAlignment(SwingConstants.CENTER);
		panel.add(clock, gbc);

		gbc = new GridBagConstraints();
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.gridwidth = 2;
		gbc.gridx = 0;
		gbc.gridy = 6;

		panel.add(timerButton, gbc);

		timerButton.addMouseListener(new MouseListener() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				if (timerButton.getName().equalsIgnoreCase("disabled")) {
					timerWindow.setVisible(true, timerButton);
				} else {
					timerWindow.setVisible(false, timerButton);
				}
			}

			@Override
			public void mouseEntered(MouseEvent arg0) {
			}

			@Override
			public void mouseExited(MouseEvent arg0) {
			}

			@Override
			public void mousePressed(MouseEvent arg0) {
			}

			@Override
			public void mouseReleased(MouseEvent arg0) {
			}
		});

		window.setJMenuBar(menuBar);
	}

	private void loadApp() {
		loadPanel();

		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.setResizable(false);

		try {
			window.setIconImage(ImageIO.read(ApexTimeTrackerv2.class.getResourceAsStream(Strings.faviconPath)));
		} catch (IOException e) {
			e.printStackTrace();
		}

		window.add(panel);
		window.pack();
		window.setLocationRelativeTo(null);
		window.setFocusable(true);
		window.setAlwaysOnTop(true);
		window.setVisible(true);

		window.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent ev) {
				int i = JOptionPane.showConfirmDialog(window, "Are you sure you want to close the Apex Time Tracker?", "Apex Time Tracker Exiting", JOptionPane.YES_NO_OPTION);
				if (i == JOptionPane.YES_OPTION) {
					window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				} else {
					window.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
				}
			}
		});
	}

}