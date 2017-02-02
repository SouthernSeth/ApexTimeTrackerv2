package com.jordan.apextimetrackerv2;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
//import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import com.jordan.apextimetrackerv2.util.Timecard;

public class SQLite {

	private Connection connection;

	public SQLite() throws ClassNotFoundException {
		Class.forName("org.sqlite.JDBC");

		try {
			connection = DriverManager.getConnection("jdbc:sqlite:C:/ApexTimeTracker/timecards.db");
			Statement statement = connection.createStatement();
			statement.setQueryTimeout(30);

			statement.execute("create table if not exists timecards (id integer primary key, week string, clockindate string, clockintime string, tolunchdate string, tolunchtime string, returnlunchdate string, returnlunchtime string, clockoutdate string, clockouttime string)");
			//			statement.executeUpdate("insert into person values(1, 'leo')");
			//			statement.executeUpdate("insert into person values(2, 'yui')");
			//			ResultSet rs = statement.executeQuery("select * from person");
			//			while (rs.next()) {
			//				System.out.println("name = " + rs.getString("name"));
			//				System.out.println("id = " + rs.getInt("id"));
			//			}
		} catch (SQLException e) {
			System.err.println(e.getMessage());
		}
	}

	public Connection getConnection() {
		return connection;
	}
	
	public ArrayList<Timecard> getTimecards() {
		ArrayList<Timecard> timecards = new ArrayList<Timecard>();
		
		String query = "select * from timecards";
		
		try {
			ResultSet rs = connection.createStatement().executeQuery(query);
			
			while (rs.next()) {
				Timecard timeHandler = new Timecard(rs.getInt("id"), rs.getString("week"), rs.getString("clockindate"), rs.getString("clockintime"), rs.getString("tolunchdate"), rs.getString("tolunchtime"), rs.getString("returnlunchdate"), rs.getString("returnlunchtime"), rs.getString("clockoutdate"), rs.getString("clockouttime"));
				timecards.add(timeHandler);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return timecards;
	}

	public int generateID() {
		ArrayList<Integer> ids = new ArrayList<Integer>();

		String query = "select * from timecards";

		ResultSet rs = null;
		try {
			rs = connection.createStatement().executeQuery(query);
		} catch (SQLException e) {
			e.printStackTrace();
		}

		try {
			while (rs.next()) {
				ids.add(rs.getInt("id"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		int ran = 0;

		do {
			ran = (int) Math.floor(Math.random() * (999999 + 1));
		} while (ids.contains(ran));

		System.out.println("Generated ID: " + ran);
		return ran;
	}
	
	private boolean compareDates(String date1, String date2) {
		String[] dateSplit1 = date1.split("/");
		String[] dateSplit2 = date2.split("/");
		
		if (Integer.parseInt(dateSplit1[0]) >= Integer.parseInt(dateSplit2[0])) {
			if (Integer.parseInt(dateSplit1[0]) == Integer.parseInt(dateSplit2[0])) {
				if (Integer.parseInt(dateSplit1[1]) >= Integer.parseInt(dateSplit2[1])) {
					if (Integer.parseInt(dateSplit1[1]) == Integer.parseInt(dateSplit2[1])) {
						if (Integer.parseInt(dateSplit1[2]) >= Integer.parseInt(dateSplit2[2])) {
							return true;
						}
					} else {
						return true;
					}
				}
			} else {
				return true;
			}
		}
		
		return false;
	}
	
	public Timecard getTimecard(int id) {
		String query = "select * from timecards where id = " + id;
		
		try {
			ResultSet rs = connection.createStatement().executeQuery(query);
			
			if (rs.next()) {
				Timecard timeHandler = new Timecard(rs.getInt("id"), rs.getString("week"), rs.getString("clockindate"), rs.getString("clockintime"), rs.getString("tolunchdate"), rs.getString("tolunchtime"), rs.getString("returnlunchdate"), rs.getString("returnlunchtime"), rs.getString("clockoutdate"), rs.getString("clockouttime"));
				return timeHandler;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return null;
	}

	public Timecard getLastTimecard() {
		String query = "select * from timecards";
		try {
			ResultSet rs = connection.createStatement().executeQuery(query);

			String latestDate = null;
			while (rs.next()) {
				if (latestDate == null) {
					latestDate = rs.getString("clockindate");
				} else {
					if (compareDates(rs.getString("clockindate"), latestDate)) {
						latestDate = rs.getString("clockindate");
					}
				}
			}
			
			String query2 = "select * from timecards where clockindate = '" + latestDate + "'";
			ResultSet rs2 = connection.createStatement().executeQuery(query2);
			while (rs2.next()) {
				Timecard timeHandler = new Timecard(rs2.getInt("id"), rs2.getString("week"), rs2.getString("clockindate"), rs2.getString("clockintime"), rs2.getString("tolunchdate"), rs2.getString("tolunchtime"), rs2.getString("returnlunchdate"), rs2.getString("returnlunchtime"), rs2.getString("clockoutdate"), rs2.getString("clockouttime"));
				return timeHandler;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return null;
	}

	public void dropTable() {
		try {
			connection.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		File file = new File("C:\\ApexTimeTracker\\timecards.db");
		if (file.exists()) {
			file.delete();
		}
	}

	public void removeTimecard(int id) {
		Statement statement = null;

		try {
			statement = connection.createStatement();
		} catch (SQLException e) {
			e.printStackTrace();
		}

		try {
			statement.setQueryTimeout(30);
		} catch (SQLException e) {
			e.printStackTrace();
		}

		String query = "delete from timecards where id = " + id;

		try {
			statement.execute(query);
			System.out.println("Removed timecard with the ID: " + id);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void addTimecard(int id, String week) {
		Statement statement = null;

		try {
			statement = connection.createStatement();
		} catch (SQLException e) {
			e.printStackTrace();
		}

		try {
			statement.setQueryTimeout(30);
		} catch (SQLException e) {
			e.printStackTrace();
		}

		String query = "insert into timecards (id, week) values (" + id + ", '" + week + "')";

		try {
			statement.execute(query);
		} catch (SQLException e) {
			e.printStackTrace();
		}

		System.out.println("Timecard with the ID: " + id + " has been stored!");
	}

	public void setClockIn(int id, String date, String time) {
		String query = "update timecards set clockindate = '" + date + "', clockintime = '" + time + "' where id = " + id;
		try {
			Statement statement = connection.createStatement();
			statement.setQueryTimeout(30);
			statement.execute(query);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void setClockOut(int id, String date, String time) {
		String query = "update timecards set clockoutdate = '" + date + "', clockouttime = '" + time + "' where id = " + id;
		try {
			Statement statement = connection.createStatement();
			statement.setQueryTimeout(30);
			statement.execute(query);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void setToLunch(int id, String date, String time) {
		String query = "update timecards set tolunchdate = '" + date + "', tolunchtime = '" + time + "' where id = " + id;
		try {
			Statement statement = connection.createStatement();
			statement.setQueryTimeout(30);
			statement.execute(query);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void setReturnLunch(int id, String date, String time) {
		String query = "update timecards set returnlunchdate = '" + date + "', returnlunchtime = '" + time + "' where id = " + id;
		try {
			Statement statement = connection.createStatement();
			statement.setQueryTimeout(30);
			statement.execute(query);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void printTimecard(int id) {
		try {
			ResultSet rs = connection.createStatement().executeQuery("select * from timecards where id = " + id);

			while (rs.next()) {
				System.out.println("ID: " + rs.getInt("id"));
				System.out.println("Week: " + rs.getString("week"));
				System.out.println("Clockin Date: " + rs.getString("clockindate"));
				System.out.println("Clockin Time: " + rs.getString("clockintime"));
				System.out.println("Tolunch Date: " + rs.getString("tolunchdate"));
				System.out.println("Tolunch Time: " + rs.getString("tolunchtime"));
				System.out.println("Returnlunch Date: " + rs.getString("returnlunchdate"));
				System.out.println("Returnlunch Time: " + rs.getString("returnlunchtime"));
				System.out.println("Clockout Date: " + rs.getString("clockoutdate"));
				System.out.println("Clockout Time: " + rs.getString("clockouttime"));
				System.out.println("------------------------------------------------");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}

