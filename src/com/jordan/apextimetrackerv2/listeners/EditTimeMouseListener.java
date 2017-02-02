package com.jordan.apextimetrackerv2.listeners;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JCheckBoxMenuItem;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

import com.jordan.apextimetrackerv2.ApexTimeTrackerv2;
import com.jordan.apextimetrackerv2.windows.EditTimeWindow;

public class EditTimeMouseListener implements MouseListener {
	
	protected ApexTimeTrackerv2 att;
	protected JFrame window;
	protected JCheckBoxMenuItem checkBox;
	protected JTextField textField;
	protected JFrame bg;
	
	public EditTimeMouseListener(JFrame window, ApexTimeTrackerv2 att, JFrame bg, JCheckBoxMenuItem checkBox, JTextField textField) {
		this.att = att;
		this.window = window;
		this.checkBox = checkBox;
		this.textField = textField;
		this.bg = bg;
	}

	@Override
	public void mouseClicked(MouseEvent arg0) {
		if (checkBox.isSelected()) {
			if (textField.getText().isEmpty() || textField.getText().equalsIgnoreCase("")) {
				JOptionPane.showMessageDialog(window, "In order to edit time you must already have a time punched!");
				return;
			}
			
			window.setEnabled(false);
			new EditTimeWindow(window, att, bg, textField.getText(), textField.getName());
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

}

