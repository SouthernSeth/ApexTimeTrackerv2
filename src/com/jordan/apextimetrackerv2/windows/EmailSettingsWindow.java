package com.jordan.apextimetrackerv2.windows;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.util.Properties;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.jordan.apextimetrackerv2.util.Strings;

public class EmailSettingsWindow {

	private Properties props;
	private File file;

	public EmailSettingsWindow(final JFrame window) {
		props = new Properties();
		file = new File(Strings.propertiesFilePath);

		props.put("email", "null");

		if (!file.exists()) {
			try {
				file.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else {
			try {
				props.load(new FileReader(file));
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		String emailInput = props.getProperty("email");

		Dimension size = new Dimension(250, 150);

		final JFrame email = new JFrame("Email Settings");
		email.setAlwaysOnTop(true);
		URL iconURL = getClass().getResource("/favicon.png");
		ImageIcon icon = new ImageIcon(iconURL);
		email.setIconImage(icon.getImage());
		email.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

		JPanel panel = new JPanel();
		panel.setPreferredSize(size);
		panel.setMaximumSize(size);
		panel.setMinimumSize(size);

		JButton save = new JButton("Save Settings");

		JLabel label_emailField = new JLabel("Email:");

		final JTextField emailField = new JTextField(emailInput);

		GridLayout layout = new GridLayout(4, 1, 5, 5);

		panel.setLayout(layout);

		panel.add(label_emailField);
		panel.add(emailField);
		panel.add(new JLabel());
		panel.add(save);

		email.setContentPane(panel);
		email.pack();
		email.setLocationRelativeTo(null);
		email.setMinimumSize(size);
		email.setResizable(false);

		email.setVisible(true);

		save.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				props.setProperty("email", emailField.getText());
				try {
					FileOutputStream fos = new FileOutputStream(file);
					props.store(fos, null);
					fos.close();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
				JOptionPane.showMessageDialog(email, "Your email has been saved!");
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

