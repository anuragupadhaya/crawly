package com.eanurag.utils;

import java.io.IOException;
import java.util.Properties;

import org.apache.log4j.Logger;

/**
 * Placeholder for common utility methods
 * 
 * @author anurag
 *
 */
public class Utils {
	private final static Logger logger = Logger.getLogger(Utils.class);

	public static Properties readConfiguration(String filename) {
		Properties config = new Properties();
		try {
			config.load(Utils.class.getClassLoader().getResourceAsStream(
					filename));
		} catch (IOException e) {
			logger.error("Error opening properties file:", e);
		}
		return config;
	}
}
