package com.eanurag.utils.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

import org.apache.log4j.Logger;

import com.eanurag.utils.Constants;
import com.eanurag.utils.Utils;

public class DBUtils {
	private final static Logger logger = Logger.getLogger(DBUtils.class);
	private static Connection connection;

	private static String driver;
	private static String prefix;
	private static String host;
	private static String port;
	private static String database;
	private static String user;
	private static String password;

	private DBUtils() {
	}

	/**
	 * One-Stop shop to get DB connection
	 * 
	 * @return DB Connection
	 * 
	 */
	public static Connection getConnection() {
		try {
			if (connection == null || connection.isClosed()) {
				connection = getNewDataBaseConnection();
			}
		} catch (SQLException e) {
			logger.warn("Connection was closed previously:", e);
		}

		return connection;
	}

	private static Connection getNewDataBaseConnection() {
		initializeDBProperties();
		connection = null;
		StringBuilder connectionString = new StringBuilder();
		connectionString.append(prefix);
		connectionString.append(host);
		connectionString.append(":");
		connectionString.append(port);
		connectionString.append("/");
		connectionString.append(database);
		logger.debug("Connection String:" + connectionString);
		try {
			Class.forName(driver);
			connection = DriverManager.getConnection(connectionString.toString(), user, password);
		} catch (ClassNotFoundException e) {
			logger.error("Error loading Database Driver class:", e);
		} catch (SQLException e) {
			logger.error("Error establishing Database connection:", e);
		}

		if (connection != null) {
			logger.debug("Successfully connected to Database!");
			return connection;
		}

		return null;
	}

	/**
	 * Initialize DB properties from properties file. If values are not present,
	 * take defaults.
	 */
	private static void initializeDBProperties() {
		Properties config = Utils.readConfiguration(Constants.DB_PROPERTIES_FILE);
		driver = config.getProperty(Constants.DRIVER_CLASS, "com.mysql.jdbc.Driver");
		prefix = config.getProperty(Constants.DRIVER_PROTOCOL, "jdbc:mysql://");
		host = config.getProperty(Constants.DB_HOST, "localhost");
		port = config.getProperty(Constants.DB_PORT, "3306");
		database = config.getProperty(Constants.DB_NAME, "todo-app");
		user = config.getProperty(Constants.DB_USER, "root");
		password = config.getProperty(Constants.DB_PASS, "");
		logger.debug("DB Properties read:" + config.toString());
	}

	public static void close() {
		if (connection != null) {
			try {
				connection.close();
				logger.debug("Database connection closed!");
			} catch (SQLException e) {
				logger.error("Error in closing Database connection:", e);
			}
		} else {
			logger.warn("Database connection is not initialized yet! Cannot close the connection.");
		}
	}
}
