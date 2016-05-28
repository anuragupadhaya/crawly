package com.eanurag.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

import com.eanurag.impl.WorkerManager;
import com.eanurag.objects.URL;

public class DataBaseManager {

	// TODO right now only 1 connection to db will be created
	// making it singleton

	private static volatile DataBaseManager dbInstance = null;

	private DataBaseManager() {
	}

	public static DataBaseManager getInstance() {
		if (dbInstance == null) {
			synchronized (WorkerManager.class) {
				if (dbInstance == null) {
					dbInstance = new DataBaseManager();
				}
			}
		}

		return dbInstance;
	}

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
	private static final String URL_TABLE = "url";

	private static final String SELECT_ALL_RECORDS = "SELECT * FROM `crawly`.`url`";

	private static Map<Integer, String> dbCache = null;

	public Connection getDBConnection() {
		if (null != connection) {
			return connection;
		} else {
			try {
				Class.forName("com.mysql.jdbc.Driver");
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

	public Boolean writeData(URL url) {
		StringBuffer writeDBStatement = new StringBuffer();
		writeDBStatement.append("insert into");
		writeDBStatement.append(" ");
		writeDBStatement.append(DB_NAME);
		writeDBStatement.append(".");
		writeDBStatement.append(URL_TABLE);
		writeDBStatement.append(" ");
		writeDBStatement.append("values (?,?,default)");

		Boolean dbWriteResult = false;

		try {
			preparedStatement = DataBaseManager.getInstance().getDBConnection()
					.prepareStatement(writeDBStatement.toString());
			preparedStatement.setString(1, url.getURL());
			preparedStatement.setString(2, String.valueOf(url.hashCode()));
			dbWriteResult = (preparedStatement.executeUpdate() == 1) ? true : false;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return dbWriteResult;
	}

	public ResultSet readData(String query) {
		connection = DataBaseManager.getInstance().getDBConnection();
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
		} 
//			finally {
//			DataBaseManager.getInstance().closeDBConnection();
//		}
		// TODO create an exception framework and return message
		return null;
	}

	// TODO how to use the dbCache
	public void buildDBCache() {
		dbCache = new HashMap<Integer, String>();
		resultSet = readData(SELECT_ALL_RECORDS);
		try {
			while (resultSet.next()) {
				dbCache.put(resultSet.getInt(2), resultSet.getString(1));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

}
