package com.jordan.apextimetrackerv2.windows;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.net.URL;
import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import com.jordan.apextimetrackerv2.ApexTimeTrackerv2;
import com.jordan.apextimetrackerv2.SQLite;
import com.jordan.apextimetrackerv2.util.Timecard;

public class LoadTimecardWindow {
	
	private JFrame frame;
	
	private JButton load;
	private JComboBox<String> select;
	
	public LoadTimecardWindow(final JFrame window, final ApexTimeTrackerv2 att, SQLite sqlite) {
		frame = new JFrame("Load Timecard");
		URL iconURL = getClass().getResource("/favicon.png");
		ImageIcon icon = new ImageIcon(iconURL);
		frame.setIconImage(icon.getImage());
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.setContentPane(new JPanel());
		frame.setSize(new Dimension(250, 150));
		frame.setLocationRelativeTo(null);
		frame.setAlwaysOnTop(true);
		frame.setResizable(false);
		
		frame.getContentPane().setLayout(new GridLayout(2,1,2,30));
		
		load = new JButton("Load");
		
		select = new JComboBox<String>();
		
		ArrayList<Timecard> temp = sqlite.getTimecards();
		select.addItem("Please choose a timecard to load...");
		for (int i = 0;i<temp.size();i++) {
			Timecard time = temp.get(i);
			select.addItem(time.getID() + " - " + time.getClockInDate());
		}
		
		//Load the selected timecard into the program
		load.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				String selected = (String) select.getSelectedItem();
				int id = Integer.parseInt(selected.split(" - ")[0]);
				att.loadTimecard(id);
				JOptionPane.showMessageDialog(window, "Timecard with the ID: " + id + " has been loaded!");
				frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
			}
		});
		
		frame.getContentPane().add(select);
		frame.getContentPane().add(load);
		
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

}

