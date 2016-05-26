package com.eanurag.impl;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;

public class DataBaseManager {

	private static volatile Connection connection = null;
	private static volatile Statement statement = null;
	private static volatile PreparedStatement preparedStatement = null;
	private static volatile ResultSet resultSet = null;
	private static volatile ResultSetMetaData metaData = null;

	private static final String DB_HOST = "localhost";
	private static final String DB_PORT = "3306";
	private static final String DB_USER = "root";
	private static final String DB_PASS = "";
	private static final String DB_NAME = "crawly";

	private static final String SELECT_ALL_RECORDS = "SELECT * FROM `crawly`.`url`";

	public static Connection getDBInstance() {
		if (null != connection) {
			return connection;
		} else {
			try {
				// This will load the MySQL driver, each DB has its own driver
				Class.forName("com.mysql.jdbc.Driver");
				// Setup the connection with the DB
				connection = DriverManager.getConnection("jdbc:mysql://" + DB_HOST + ":" + DB_PORT + "/" + DB_NAME,
						DB_USER, DB_PASS);

				if (connection != null) {
					System.out.println("You made it, take control of your database now!");
				} else {
					System.out.println("Failed to make connection!");
				}

			} catch (SQLException e) {
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
			return connection;
		}
	}

	private static void closeDBConnection() {
		try {
			if (null != connection && !connection.isClosed()) {
				connection.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public static void writeData() {

	}

	public static ResultSet readData(String query) {
		connection = getDBInstance();
		statement = null;
		resultSet = null;
		try {
			statement = connection.createStatement();
			resultSet = statement.executeQuery(query);
			if (resultSet != null) {
				return resultSet;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally {
			closeDBConnection();
		}
		// TODO create an exception framework and return message
		return null;
	}

	public static void buildDBCache() {
		resultSet = readData(SELECT_ALL_RECORDS);
		//TODO building cache
	}

}
