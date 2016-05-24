package com.eanurag.impl;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;

import com.eanurag.interfaces.IDAO;

public class DataBaseManager implements IDAO {

	private Connection connection = null;
	private Statement statement = null;
	private PreparedStatement preparedStatement = null;
	private ResultSet resultSet = null;
	private ResultSetMetaData metaData = null;

	private static final String DB_HOST = "localhost";
	private static final String DB_PORT = "3306";
	private static final String DB_USER = "root";
	private static final String DB_PASS = "";
	private static final String DB_NAME = "crawly";

	private static final String SELECT_ALL_RECORDS = "SELECT * FROM `crawly`.`url`";

	@Override
	public Connection getDBInstance() {
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

	private void closeDBConnection() {
		try {
			if (null != connection && !connection.isClosed()) {
				connection.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void writeData() {

	}

	@Override
	public void readData(String query) {
		connection = getDBInstance();
		try {
			statement = connection.createStatement();
			resultSet = statement.executeQuery(SELECT_ALL_RECORDS);
			if (resultSet != null) {
				while (resultSet.next()) {
					metaData = resultSet.getMetaData();
					int columns = metaData.getColumnCount();
					for (int i = 1; i <= columns; i++) {
						System.out.println(resultSet.getString(i));
					}

				}
			}
			closeDBConnection();
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

}
