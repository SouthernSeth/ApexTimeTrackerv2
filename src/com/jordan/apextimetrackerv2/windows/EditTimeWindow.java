package com.jordan.apextimetrackerv2.windows;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerModel;
import javax.swing.SpinnerNumberModel;

import org.jdatepicker.impl.DateComponentFormatter;
import org.jdatepicker.impl.JDatePanelImpl;
import org.jdatepicker.impl.JDatePickerImpl;
import org.jdatepicker.impl.UtilDateModel;

import com.jordan.apextimetrackerv2.ApexTimeTrackerv2;

public class EditTimeWindow {
	
	private JButton save;
	private JSpinner spinnerHour;
	private JSpinner spinnerMinute;
	private UtilDateModel model;
	
	private JFrame editTime;
	
	public EditTimeWindow(final JFrame window, final ApexTimeTrackerv2 att, final JFrame bg, String current, final String type) {
		editTime = new JFrame("Edit Time");
		URL iconURL = getClass().getResource("/favicon.png");
		ImageIcon icon = new ImageIcon(iconURL);
		editTime.setIconImage(icon.getImage());
		editTime.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		editTime.setContentPane(new JPanel());
		editTime.setSize(new Dimension(250, 200));
		editTime.setLocationRelativeTo(null);
		editTime.setAlwaysOnTop(true);
		editTime.setResizable(false);
		
		save = new JButton("Save Time");
		
		Properties props = new Properties();
		props.put("text.today", "Today");
		props.put("text.month", "Month");
		props.put("text.year", "Year");
		
		model = new UtilDateModel();
		
		SpinnerModel sm = new SpinnerNumberModel(0, 0, 24, 1);
		spinnerHour = new JSpinner(sm);
		
		SpinnerModel sm2 = new SpinnerNumberModel(0, 0, 59, 1);
		spinnerMinute = new JSpinner(sm2);
		
		if (!current.isEmpty() || !current.equalsIgnoreCase("")) {
			String[] split = current.split(" ");
			String date = split[0];
			String time = split[1];
			
			String[] youNeedToCutIt = date.split("/");
			String day = youNeedToCutIt[1];
			String month = youNeedToCutIt[0];
			String year = youNeedToCutIt[2];
			
			SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy");
			try {
				model.setValue(format.parse(month + "/" + day + "/" + year));
			} catch (ParseException e) {
				e.printStackTrace();
			}
			
			String[] youNeedToCutItMore = time.split(":");
			String hour = youNeedToCutItMore[0];
			String minute = youNeedToCutItMore[1];
			
			spinnerHour.setValue(Integer.parseInt(hour));
			spinnerMinute.setValue(Integer.parseInt(minute));
		}
		
		JDatePanelImpl datePanel = new JDatePanelImpl(model, props);
		JDatePickerImpl datePicker = new JDatePickerImpl(datePanel, new DateComponentFormatter());
		
		save.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				Date date = model.getValue();
				int hour = (int) spinnerHour.getValue();
				int minute = (int) spinnerMinute.getValue();
				
				if (type.equalsIgnoreCase("clockin")) {
					att.clockIn(date, hour, minute);
				} else if (type.equalsIgnoreCase("clockout")) {
					att.clockOut(date, hour, minute);
				} else if (type.equalsIgnoreCase("tolunch")) {
					att.toLunch(date, hour, minute);
				} else if (type.equalsIgnoreCase("returnlunch")) {
					att.returnLunch(date, hour, minute);
				}
				
				editTime.dispatchEvent(new WindowEvent(editTime, WindowEvent.WINDOW_CLOSING));
			}
		});
		
		editTime.getContentPane().setLayout(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();
		
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.gridwidth = 1;
		gbc.gridx = 0;
		gbc.gridy = 0;
		editTime.getContentPane().add(new JLabel("Date: "), gbc);
		
		gbc = new GridBagConstraints();
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.gridwidth = 1;
		gbc.gridx = 1;
		gbc.gridy = 0;
		editTime.getContentPane().add(datePicker, gbc);
		
		gbc = new GridBagConstraints();
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.gridwidth = 1;
		gbc.gridx = 0;
		gbc.gridy = 1;
		editTime.getContentPane().add(new JLabel("Hour: "), gbc);
		
		gbc = new GridBagConstraints();
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.gridwidth = 1;
		gbc.gridx = 1;
		gbc.gridy = 1;
		editTime.getContentPane().add(spinnerHour, gbc);
		
		gbc = new GridBagConstraints();
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.gridwidth = 1;
		gbc.gridx = 0;
		gbc.gridy = 2;
		editTime.getContentPane().add(new JLabel("Minute: "), gbc);
		
		gbc = new GridBagConstraints();
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.gridwidth = 1;
		gbc.gridx = 1;
		gbc.gridy = 2;
		editTime.getContentPane().add(spinnerMinute, gbc);
		
		gbc = new GridBagConstraints();
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.gridwidth = 2;
		gbc.gridx = 0;
		gbc.gridy = 3;
		editTime.getContentPane().add(save, gbc);
		
		editTime.setVisible(true);
		
		editTime.addWindowListener(new WindowListener() {

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

}

