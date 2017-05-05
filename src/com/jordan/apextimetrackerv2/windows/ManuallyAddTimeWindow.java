package com.jordan.apextimetrackerv2.windows;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Properties;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerListModel;
import javax.swing.SpinnerModel;

import org.jdatepicker.impl.DateComponentFormatter;
import org.jdatepicker.impl.JDatePanelImpl;
import org.jdatepicker.impl.JDatePickerImpl;
import org.jdatepicker.impl.UtilDateModel;

import com.jordan.apextimetrackerv2.ApexTimeTrackerv2;

public class ManuallyAddTimeWindow {

	private JButton save;
	private JSpinner spinnerHour;
	private JSpinner spinnerMinute;
	private UtilDateModel model;

	private JFrame editTime;

	public ManuallyAddTimeWindow(final JFrame window, final ApexTimeTrackerv2 att) {
		editTime = new JFrame("Manually Add Time");
		URL iconURL = getClass().getResource("/favicon.png");
		ImageIcon icon = new ImageIcon(iconURL);
		editTime.setIconImage(icon.getImage());
		editTime.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		editTime.setContentPane(new JPanel());
		editTime.setSize(new Dimension(250, 200));
		editTime.setLocationRelativeTo(window);
		editTime.setAlwaysOnTop(true);
		editTime.setResizable(false);

		save = new JButton("Save Time");

		Properties props = new Properties();
		props.put("text.today", "Today");
		props.put("text.month", "Month");
		props.put("text.year", "Year");

		model = new UtilDateModel();

		ArrayList<String> h = new ArrayList<String>();
		ArrayList<String> m = new ArrayList<String>();

		for (int i = 0;i<24;i++) {
			if (i < 10) {
				h.add(String.valueOf("0" + i));
			} else {
				h.add(String.valueOf(i));
			}
		}

		for (int i = 0;i<60;i++) {
			if (i < 10) {
				m.add(String.valueOf("0" + i));
			} else {
				m.add(String.valueOf(i));
			}
		}

		SpinnerModel sm = new SpinnerListModel(h);
		spinnerHour = new JSpinner(sm);

		SpinnerModel sm2 = new SpinnerListModel(m);
		spinnerMinute = new JSpinner(sm2);

		spinnerHour.setValue("00");
		spinnerMinute.setValue("00");

		JDatePanelImpl datePanel = new JDatePanelImpl(model, props);
		JDatePickerImpl datePicker = new JDatePickerImpl(datePanel, new DateComponentFormatter());

		save.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				//TODO: add new timecard
				editTime.dispatchEvent(new WindowEvent(editTime, WindowEvent.WINDOW_CLOSING));
			}
		});

		editTime.getContentPane().setLayout(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();

		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.gridwidth = 1;
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.insets = new Insets(0,0,3,0);
		editTime.getContentPane().add(new JLabel("Date: "), gbc);

		gbc = new GridBagConstraints();
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.gridwidth = 1;
		gbc.gridx = 1;
		gbc.gridy = 0;
		gbc.insets = new Insets(0,0,3,0);
		editTime.getContentPane().add(datePicker, gbc);

		gbc = new GridBagConstraints();
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.gridwidth = 1;
		gbc.gridx = 0;
		gbc.gridy = 1;
		gbc.insets = new Insets(0,0,3,0);
		editTime.getContentPane().add(new JLabel("Hour: "), gbc);

		gbc = new GridBagConstraints();
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.gridwidth = 1;
		gbc.gridx = 1;
		gbc.gridy = 1;
		gbc.insets = new Insets(0,0,3,0);
		editTime.getContentPane().add(spinnerHour, gbc);

		gbc = new GridBagConstraints();
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.gridwidth = 1;
		gbc.gridx = 0;
		gbc.gridy = 2;
		gbc.insets = new Insets(0,0,3,0);
		editTime.getContentPane().add(new JLabel("Minute: "), gbc);

		gbc = new GridBagConstraints();
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.gridwidth = 1;
		gbc.gridx = 1;
		gbc.gridy = 2;
		gbc.insets = new Insets(0,0,3,0);
		editTime.getContentPane().add(spinnerMinute, gbc);

		gbc = new GridBagConstraints();
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.gridwidth = 2;
		gbc.gridx = 0;
		gbc.gridy = 3;
		gbc.insets = new Insets(3,5,0,5);
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

