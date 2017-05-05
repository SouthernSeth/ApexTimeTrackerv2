package com.jordan.apextimetrackerv2.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

public class Settings {
	
	public static String email;
	public static String lastDayOfWorkWeek;
	
	private Properties props;
	private File file;
	
	public Settings() {
		props = new Properties();
		file = new File(Strings.propertiesFilePath);
		
		if (!file.exists()) {
			try {
				file.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			props.put("email", "first.last@boeing.com");
			props.put("last_day_of_work_week", "FRIDAY");
			
			email = "first.last@boeing.com";
			lastDayOfWorkWeek = "FRIDAY";
			
			try {
				FileOutputStream fos = new FileOutputStream(file);
				props.store(fos, null);
				fos.close();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
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
			
			email = props.getProperty("email");
			lastDayOfWorkWeek = props.getProperty("last_day_of_work_week");
		}
	}
	
	public void put(Object tag, Object data) {
		props.put(tag, data);
		
		try {
			FileOutputStream fos = new FileOutputStream(file);
			props.store(fos, null);
			fos.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
