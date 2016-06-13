package com.eanurag.dao;

import java.beans.PropertyVetoException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import com.eanurag.impl.WorkerManager;
import com.eanurag.objects.URL;
import com.mchange.v2.c3p0.ComboPooledDataSource;

public class DataBaseManager {

	private final static Logger logger = Logger.getLogger(DataBaseManager.class);

	private volatile static DataBaseManager dbInstance = null;

	private DataBaseManager() {
		cpds = new ComboPooledDataSource();
		try {
			cpds.setDriverClass("com.mysql.jdbc.Driver");
		} catch (PropertyVetoException e) {
			logger.error("Error in Initializing DB Driver class", e);
		}
		cpds.setJdbcUrl("jdbc:mysql://" + DB_HOST + "/" + DB_NAME);
		cpds.setUser(DB_USER);
		cpds.setPassword(DB_PASS);

		cpds.setMinPoolSize(MINIMUM_POOL_SIZE);
		cpds.setAcquireIncrement(INCREMENT_SIZE);
		cpds.setMaxPoolSize(MAXIMUM_POOL_SIZE);
		cpds.setMaxStatements(MAX_STATEMENTS);
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

	private ComboPooledDataSource cpds;

	private static final Integer MINIMUM_POOL_SIZE = 10;
	private static final Integer MAXIMUM_POOL_SIZE = 1000;
	private static final Integer INCREMENT_SIZE = 5;
	private static final Integer MAX_STATEMENTS = 200;

	private static final String DB_HOST = "localhost";
	private static final String DB_PORT = "3306";
	private static final String DB_USER = "root";
	private static final String DB_PASS = "";
	private static final String DB_NAME = "crawly";
	private static final String URL_TABLE = "url";

	private static final String SELECT_ALL_RECORDS = "SELECT * FROM `crawly`.`url`";

	private static Map<Integer, String> dbCache = null;

	public Connection getConnection() throws SQLException {
		logger.info("Creating connection to DB!");
		return this.cpds.getConnection();
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
		Connection connection = null;
		PreparedStatement preparedStatement = null;

		try {
			connection = DataBaseManager.getInstance().getConnection();
			preparedStatement = connection.prepareStatement(writeDBStatement.toString());
			preparedStatement.setString(1, url.getURL());
			preparedStatement.setString(2, String.valueOf(url.hashCode()));
			dbWriteResult = (preparedStatement.executeUpdate() == 1) ? true : false;
			if (dbWriteResult) {
				logger.info("Successfully written to DB!");
			}
		} catch (SQLException e) {
			logger.error("Error in writing to DB", e);
		} finally {
			try {
				preparedStatement.close();
				connection.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return dbWriteResult;
	}

	public boolean checkURLinDB(URL url) {
		boolean urlExists = false;
		Connection connection = null;
		Statement statement = null;
		ResultSet resultSet = null;
		try {
			connection = DataBaseManager.getInstance().getConnection();
			statement = connection.createStatement();
			resultSet = statement.executeQuery(SELECT_ALL_RECORDS + " where hashCode=" + url.hashCode());
			if (resultSet != null) {
				urlExists = true;
			}
		} catch (Exception e) {
			logger.error("Error in reading from Database!", e);
		} finally {
			try {
				statement.close();
				connection.close();
			} catch (SQLException e) {
				logger.error("Error closing connection to DB!", e);
			}
		}
		return urlExists;
	}

	// // TODO how to use the dbCache
	// public void buildDBCache() {
	// ResultSet resultSet = null;
	// dbCache = new HashMap<Integer, String>();
	// resultSet = readData(SELECT_ALL_RECORDS);
	// try {
	// while (resultSet.next()) {
	// dbCache.put(resultSet.getInt(2), resultSet.getString(1));
	// }
	// } catch (SQLException e) {
	// logger.error(e);
	// }
	// }

}
