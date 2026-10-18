package com.qa.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ConfigReader {
	
	private static final Properties properties = new Properties();
	
	static {
		try (InputStream ip = ConfigReader.class.getClassLoader().getResourceAsStream("config/config.properties")){
			
			if(ip==null) {
				throw new IllegalStateException("config file not found");
			}
			properties.load(ip);
			
		} catch (IOException e) {
			
			throw new IllegalStateException("unable to read confi.properties",e);
		}
		
	}
	
	public static String get(String Key) {
		
		String value = properties.getProperty(Key);
		if(value==null|value.isBlank()) {
			throw new IllegalArgumentException("Missing Configuration"+Key); 
		}
		
		return value.trim();
	}

}
