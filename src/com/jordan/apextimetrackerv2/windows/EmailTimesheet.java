package com.jordan.apextimetrackerv2.windows;

import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import com.jordan.apextimetrackerv2.ApexTimeTrackerv2;
import com.jordan.apextimetrackerv2.SQLite;
import com.jordan.apextimetrackerv2.util.Settings;
import com.jordan.apextimetrackerv2.util.Strings;
import com.jordan.apextimetrackerv2.util.Timecard;

public class EmailTimesheet {
	
	private JFrame frame;
	
	private JButton load;
	private JComboBox<String> select;
	
	public EmailTimesheet(final JFrame window, final ApexTimeTrackerv2 att, final SQLite sqlite) {
		if (sqlite.getTimecards().size() == 0) {
			JOptionPane.showMessageDialog(window, "You have no times stored!", "The cake is a lie!", JOptionPane.ERROR_MESSAGE);
			window.setEnabled(true);
			return;
		}
		
		frame = new JFrame("Email Timesheet");
		URL iconURL = getClass().getResource("/favicon.png");
		ImageIcon icon = new ImageIcon(iconURL);
		frame.setIconImage(icon.getImage());
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.setContentPane(new JPanel());
		frame.setSize(new Dimension(250, 150));
		frame.setLocationRelativeTo(window);
		frame.setAlwaysOnTop(true);
		frame.setResizable(false);
		
		frame.getContentPane().setLayout(new GridBagLayout());
		
		load = new JButton("Email");
		
		select = new JComboBox<String>();
		
		ArrayList<Timecard> temp = sqlite.getTimecards();
		select.addItem("Please choose the week you want to email...");
		
		ArrayList<String> temp1 = new ArrayList<String>();
		for (int i = 0;i<temp.size();i++) {
			Timecard time = temp.get(i);
			
			if (!temp1.contains(time.getWeek())) {
				temp1.add(time.getWeek());
			}
		}
		
		if (temp1.size() <= 1) {
			String selected = null;
			
			for (int i = 0;i<temp1.size();i++) {
				selected = temp1.get(i);
			}
			
			try {
				email(window, sqlite, selected);
			} catch (URISyntaxException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
			return;
		}
		
		Collections.sort(temp1); 
		
		for (int i = 0;i<temp1.size();i++) {
			select.addItem(temp1.get(i));
		}
		
		load.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				String selected = (String) select.getSelectedItem();
				
				try {
					email(window, sqlite, selected);
				} catch (URISyntaxException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
				
				frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
			}
		});
		
		GridBagConstraints gbc = new GridBagConstraints();
		
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.weightx = 0.5;
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.insets = new Insets(0,10,10,10);
		frame.getContentPane().add(select, gbc);
		
		gbc = new GridBagConstraints();
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.weightx = 0.5;
		gbc.gridx = 0;
		gbc.gridy = 1;
		gbc.insets = new Insets(0,10,0,10);
		frame.getContentPane().add(load, gbc);
		
		frame.setVisible(true);
		
		frame.addWindowListener(new WindowListener() {

			@Override
			public void windowActivated(WindowEvent arg0) {
			}

			@Override
			public void windowClosed(WindowEvent arg0) {
			}

			@Override
			public void windowClosing(WindowEvent arg0) {
				window.setEnabled(true);
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
	
	public void email(JFrame window, SQLite sqlite, String week) throws URISyntaxException, IOException {
		if (Settings.email.isEmpty() || Settings.email.trim().equalsIgnoreCase("")) {
			JOptionPane.showMessageDialog(window, "You need to set your email under File > Settings");
			return;
		}

		ArrayList<Timecard> temp = sqlite.getTimecards();
		String[] temp2 = new String[7];
		int increment = 0;

		for (Timecard timecard : temp) {
			if (timecard.getWeek().equalsIgnoreCase(week)) {
				week = timecard.getWeek();
				DecimalFormat df = new DecimalFormat();
				df.setMaximumFractionDigits(2);

				String day = null;
				if (timecard.getDay() == Calendar.MONDAY) {
					day = "MONDAY";
				} else if (timecard.getDay() == Calendar.TUESDAY) {
					day = "TUESDAY";
				} else if (timecard.getDay() == Calendar.WEDNESDAY) {
					day = "WEDNESDAY";
				} else if (timecard.getDay() == Calendar.THURSDAY) {
					day = "THURSDAY";
				} else if (timecard.getDay() == Calendar.FRIDAY) {
					day = "FRIDAY";
				} else if (timecard.getDay() == Calendar.SATURDAY) {
					day = "SATURDAY";
				} else if (timecard.getDay() == Calendar.SUNDAY) {
					day = "SUNDAY";
				}

				if (timecard.getHoursWorked() <= 0) {
					//Do nothing
				} else {
					temp2[increment] = day + ": " + df.format(timecard.getHoursWorked()) + " hour(s)";
				}
				
				increment++;
			}
		}

		String[] time = new String[7];
		for (int i = 0;i<temp2.length;i++) {
			if (temp2[i] != null) {
				System.out.println(temp2[i]);
				if (temp2[i] != null) {
					if (temp2[i].split(":")[0].equalsIgnoreCase("Monday")) {
						time[0] = temp2[i];
					}
					if (temp2[i].split(":")[0].equalsIgnoreCase("Tuesday")) {
						time[1] = temp2[i];
					}
					if (temp2[i].split(":")[0].equalsIgnoreCase("Wednesday")) {
						time[2] = temp2[i];
					}
					if (temp2[i].split(":")[0].equalsIgnoreCase("Thursday")) {
						time[3] = temp2[i];
					}
					if (temp2[i].split(":")[0].equalsIgnoreCase("Friday")) {
						time[4] = temp2[i];
					}
					if (temp2[i].split(":")[0].equalsIgnoreCase("Saturday")) {
						time[5] = temp2[i];
					}
					if (temp2[i].split(":")[0].equalsIgnoreCase("Sunday")) {
						time[6] = temp2[i];
					}
				}
			}
		}

		Desktop desktop;
		if (Desktop.isDesktopSupported() && (desktop = Desktop.getDesktop()).isSupported(Desktop.Action.MAIL)) {
			String email = Settings.email;
			String subject = "Apex Time Tracker v2 - " + Strings.username + " - " + week;
			StringBuilder temp_body = new StringBuilder();
			
			for (int i = 0;i<7;i++) {
				if (time[i] != null) {
					temp_body.append(time[i] + "%0D%0A");
				}
			}
			
			String body = temp_body.toString();
			if (body.isEmpty()) {
				temp_body.append("There is no schedule information that can be printed at this time");
				body = temp_body.toString();
			}
			
			URI mailto = new URI("mailto:"+email+"?subject="+subject.replaceAll(" ", "%20")+"&body="+body.replaceAll(" ", "%20"));
			desktop.mail(mailto);
		} else {
			throw new RuntimeException("mailto not supported on this system");
		}
	}

}

