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

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.jordan.apextimetrackerv2.util.Settings;

public class SettingsWindow {
	
	private Settings settings;

	public SettingsWindow(final JFrame window) {
		settings = new Settings();

		Dimension size = new Dimension(250, 150);

		final JFrame email = new JFrame("Settings");
		email.setAlwaysOnTop(true);
		URL iconURL = getClass().getResource("/favicon.png");
		ImageIcon icon = new ImageIcon(iconURL);
		email.setIconImage(icon.getImage());
		email.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

		JPanel panel = new JPanel();
		panel.setPreferredSize(size);
		panel.setMaximumSize(size);
		panel.setMinimumSize(size);

		JButton save = new JButton("Save");

		JLabel label_emailField = new JLabel("Email:");
		JLabel label_lastDayOfWorkWeek = new JLabel("Last day of your work week:");

		final JTextField emailField = new JTextField(Settings.email, 30);
		final JComboBox<String> lastDayOfWorkWeekField = new JComboBox<String>();
		
		lastDayOfWorkWeekField.addItem("MONDAY");
		lastDayOfWorkWeekField.addItem("TUESDAY");
		lastDayOfWorkWeekField.addItem("WEDNESDAY");
		lastDayOfWorkWeekField.addItem("THURSDAY");
		lastDayOfWorkWeekField.addItem("FRIDAY");
		lastDayOfWorkWeekField.addItem("SATURDAY");
		lastDayOfWorkWeekField.addItem("SUNDAY");
		
		lastDayOfWorkWeekField.setSelectedItem(Settings.lastDayOfWorkWeek);

		panel.setLayout(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();

		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.gridwidth = 2;
		gbc.insets = new Insets(0,0,0,0);
		panel.add(label_emailField, gbc);
		
		gbc = new GridBagConstraints();
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.gridx = 0;
		gbc.gridy = 1;
		gbc.gridwidth = 2;
		gbc.insets = new Insets(0,0,10,0);
		panel.add(emailField, gbc);
		
		gbc = new GridBagConstraints();
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.gridx = 0;
		gbc.gridy = 2;
		gbc.gridwidth = 2;
		gbc.insets = new Insets(0,0,0,0);
		panel.add(label_lastDayOfWorkWeek, gbc);
		
		gbc = new GridBagConstraints();
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.gridx = 0;
		gbc.gridy = 3;
		gbc.gridwidth = 2;
		gbc.insets = new Insets(0,0,10,0);
		panel.add(lastDayOfWorkWeekField, gbc);
		
		gbc = new GridBagConstraints();
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.gridx = 0;
		gbc.gridy = 4;
		gbc.gridwidth = 2;
		gbc.insets = new Insets(10,0,0,0);
		panel.add(save, gbc);

		email.setContentPane(panel);
		email.pack();
		email.setLocationRelativeTo(window);
		email.setMinimumSize(size);
		email.setResizable(false);

		email.setVisible(true);

		save.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				settings.put("email", emailField.getText());
				settings.put("last_day_of_work_week", (String) lastDayOfWorkWeekField.getSelectedItem());
				JOptionPane.showMessageDialog(email, "Your settings have been saved!");
				email.dispatchEvent(new WindowEvent(email, WindowEvent.WINDOW_CLOSING));
			}
		});

		email.addWindowListener(new WindowListener() {

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

