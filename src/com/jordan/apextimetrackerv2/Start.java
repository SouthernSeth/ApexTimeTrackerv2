package com.jordan.apextimetrackerv2;

import java.io.File;
import java.io.IOException;
import java.net.BindException;
import java.net.InetAddress;
import java.net.ServerSocket;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

public class Start {
	
	private static final int PORT = 9999;
	
	@SuppressWarnings("unused")
	private static ServerSocket socket;    

	public static void main(String[] args) {
		//check();
		
		File folder = new File("C:\\ApexTimeTracker");
		if (!folder.exists()) {
			folder.mkdirs();
		}
		
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (UnsupportedLookAndFeelException e) {
			e.printStackTrace();
		}

		SQLite sqlite = null;
		try {
			sqlite = new SQLite();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}

		new ApexTimeTrackerv2(sqlite);
	}
	
	private static void check() {
		try {
			socket = new ServerSocket(PORT,0,InetAddress.getByAddress(new byte[] {127,0,0,1}));
		}
		catch (BindException e) {
			JFrame dummy = new JFrame("");
			dummy.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
			dummy.setAlwaysOnTop(true);
			dummy.setSize(0, 0);
			JOptionPane.showMessageDialog(dummy, "There is already another instance of the Apex Time Tracker running!", "Error", JOptionPane.ERROR_MESSAGE);
			dummy.dispose();
			System.err.println("Already running.");
			System.exit(1);
		}
		catch (IOException e) {
			System.err.println("Unexpected error.");
			e.printStackTrace();
			System.exit(2);
		}
	}

}
