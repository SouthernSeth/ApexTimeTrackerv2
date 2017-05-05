package com.jordan.apextimetrackerv2.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class Timecard {

	private int id = 0;
	private String clockInDate;
	private String clockOutDate;
	private String toLunchDate;
	private String returnLunchDate;
	private String clockInTime;
	private String clockOutTime;
	private String toLunchTime;
	private String returnLunchTime;

	public Timecard(int id, String clockInDate, String clockInTime, String toLunchDate, String toLunchTime, String returnLunchDate, String returnLunchTime, String clockOutDate, String clockOutTime) {
		this.id = id;
		this.clockInTime = clockInTime;
		this.clockOutTime = clockOutTime;
		this.toLunchTime = toLunchTime;
		this.returnLunchTime = returnLunchTime;
		this.clockInDate = clockInDate;
		this.clockOutDate = clockOutDate;
		this.toLunchDate = toLunchDate;
		this.returnLunchDate = returnLunchDate;
	}

	public Timecard(int id, String week, String clockInDate, String clockInTime) {
		this.id = id;
		this.clockInDate = clockInDate;
		this.clockInTime = clockInTime;
	}

	public int getID() {
		return id;
	}

	public String getWeek() {
		Calendar now = Calendar.getInstance();
		String input = clockInDate;
		SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy");

		Date date = null;
		try {
			date = format.parse(input);
		} catch (ParseException e) {
			e.printStackTrace();
		}

		now.setTime(date);

		String[] days = new String[7];
		int delta = -now.get(GregorianCalendar.DAY_OF_WEEK) + 1;
		now.add(Calendar.DAY_OF_MONTH, delta);
		for (int i = 0; i < 7; i++)
		{
			days[i] = format.format(now.getTime());
			now.add(Calendar.DAY_OF_MONTH, 1);
		}

		return days[0] + "-" + days[6];
	}

	public int getDay() {
		Calendar cal = Calendar.getInstance();
		try {
			cal.setTime(new SimpleDateFormat("MM/dd/yyyy").parse(clockInDate));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		int d = cal.get(Calendar.DAY_OF_WEEK);
		return d;
	}

	public String getClockInDate() {
		if (clockInDate == null) {
			return null;
		}
		return clockInDate;
	}

	public String getClockInTime() {
		if (clockInTime == null) {
			return null;
		}
		return clockInTime;
	}

	public String getClockOutDate() {
		if (clockOutDate == null) {
			return null;
		}
		return clockOutDate;
	}

	public String getClockOutTime() {
		if (clockOutTime == null) {
			return null;
		}
		return clockOutTime;
	}

	public String getToLunchDate() {
		if (toLunchDate == null) {
			return null;
		}
		return toLunchDate;
	}

	public String getToLunchTime() {
		if (toLunchTime == null) {
			return null;
		}
		return toLunchTime;
	}

	public String getReturnLunchDate() {
		if (returnLunchDate == null) {
			return null;
		}
		return returnLunchDate;
	}

	public String getReturnLunchTime() {
		if (returnLunchTime == null) {
			return null;
		}
		return returnLunchTime;
	}

	public void setClockInDate(String clockin) {
		this.clockInDate = clockin;
	}

	public void setClockInTime(String clockin) {
		this.clockInTime = clockin;
	}

	public void setClockOutDate(String clockout) {
		this.clockOutDate = clockout;
	}

	public void setClockOutTime(String clockout) {
		this.clockOutTime = clockout;
	}

	public void setToLunchDate(String toLunch) {
		this.toLunchDate = toLunch;
	}

	public void setToLunchTime(String toLunch) {
		this.toLunchTime = toLunch;
	}

	public void setReturnLunchDate(String returnLunch) {
		this.returnLunchDate = returnLunch;
	}

	public void setReturnLunchTime(String returnLunch) {
		this.returnLunchTime = returnLunch;
	}

	public void setID(int id){
		this.id = id;
	}

	public double getHoursWorked() {
		if (clockInDate != null && clockOutDate != null) {
			if (clockInDate.equalsIgnoreCase(clockOutDate)) {
				String clockInTime = getClockInTime();
				int clockinHour = Integer.parseInt(clockInTime.split(":")[0]);
				int clockinMinute = Integer.parseInt(clockInTime.split(":")[1]);

				String clockOutTime = getClockOutTime();
				int clockoutHour = Integer.parseInt(clockOutTime.split(":")[0]);
				int clockoutMinute = Integer.parseInt(clockOutTime.split(":")[1]);

				double hours = clockoutHour - clockinHour;
				double minutes = clockoutMinute - clockinMinute;

				minutes = minutes / 60;

				double total = hours + minutes;

				if (toLunchTime != null && returnLunchTime != null) {
					String toLunchTime = getToLunchTime();
					int toLunchHour = Integer.parseInt(toLunchTime.split(":")[0]);
					int toLunchMinute = Integer.parseInt(toLunchTime.split(":")[1]);

					String returnLunchTime = getReturnLunchTime();
					int returnLunchHour = Integer.parseInt(returnLunchTime.split(":")[0]);
					int returnLunchMinute = Integer.parseInt(returnLunchTime.split(":")[1]);

					double hourss = returnLunchHour - toLunchHour;
					double minutess = returnLunchMinute - toLunchMinute;
					minutess = minutess / 60;

					double totall = hourss + minutess;
					total -= totall;
				}

				return total;
			} else if (!clockInDate.equalsIgnoreCase(clockOutDate)) {
				
			}
			return 0;
		} else {
			return 0;
		}
	}
}

