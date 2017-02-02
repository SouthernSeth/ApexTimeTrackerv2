package com.jordan.apextimetrackerv2;

import java.io.File;

import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

public class Start {
	
//	private static final int PORT = 9999;
//	private static ServerSocket socket;    

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
	
//	private static void check() {
//		try {
//			socket = new ServerSocket(PORT,0,InetAddress.getByAddress(new byte[] {127,0,0,1}));
//		}
//		catch (BindException e) {
//			JOptionPane.showMessageDialog(null, "There is already another instance of the Apex Time Tracker running!", "Error", JOptionPane.ERROR_MESSAGE);
//			Desktop.getDesktop().notifyAll();
//			System.err.println("Already running.");
//			System.exit(1);
//		}
//		catch (IOException e) {
//			System.err.println("Unexpected error.");
//			e.printStackTrace();
//			System.exit(2);
//		}
//	}

}
