package com.eanurag.dao;

import java.beans.PropertyVetoException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Map;

import org.apache.log4j.Logger;

import com.eanurag.impl.WorkerManager;
import com.eanurag.objects.ScrapedURL;
import com.eanurag.objects.URL;
import com.eanurag.utils.Constants;
import com.mchange.v2.c3p0.ComboPooledDataSource;

public class DataBaseManager {

	private final static Logger logger = Logger
			.getLogger(DataBaseManager.class);

	private volatile static DataBaseManager dbInstance = null;

	private ComboPooledDataSource cpds;

	private static String host;
	private static String port;
	private static String user;
	private static String pass;
	private static String db_name;
	private static String url_table;

	private static final String SELECT_ALL_RECORDS = "SELECT * FROM `crawly`.`url`";

	private DataBaseManager() {
		cpds = new ComboPooledDataSource();
		try {
			cpds.setDriverClass(Constants.DRIVER_CLASS);
		} catch (PropertyVetoException e) {
			logger.error("Error in Initializing DB Driver class", e);
		}
		cpds.setJdbcUrl(Constants.DRIVER_PROTOCOL + host + "/" + db_name);
		cpds.setUser(user);
		cpds.setPassword(pass);

		cpds.setMinPoolSize(Constants.DBPOOL_MINIMUM_POOL_SIZE);
		cpds.setAcquireIncrement(Constants.DBPOOL_INCREMENT_SIZE);
		cpds.setMaxPoolSize(Constants.DBPOOL_MAXIMUM_POOL_SIZE);
		cpds.setMaxStatements(Constants.DBPOOL_MAXIMUM_POOL_SIZE);
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

	public Connection getConnection() throws SQLException {
		logger.info("Creating connection to DB!");
		return this.cpds.getConnection();
	}

	public void writeData(URL url) throws SQLException {
		StringBuffer writeDBStatement = new StringBuffer();
		writeDBStatement.append("insert into");
		writeDBStatement.append(" ");
		writeDBStatement.append(db_name);
		writeDBStatement.append(".");
		writeDBStatement.append(url_table);
		writeDBStatement.append(" ");
		writeDBStatement.append("values (?,?,?,default)");

		Boolean dbWriteResult = false;
		Connection connection = null;
		PreparedStatement preparedStatement = null;

		try {
			connection = DataBaseManager.getInstance().getConnection();
			connection.setAutoCommit(false);
			preparedStatement = connection.prepareStatement(writeDBStatement
					.toString());
			ScrapedURL links = url.getLinks();
			for (URL link : links.getScrapedLinks()) {
				preparedStatement.setString(1, url.getURL());
				preparedStatement.setString(2, link.getURL());
				preparedStatement.setString(3, String.valueOf(link.hashCode()));
				preparedStatement.addBatch();
			}
			preparedStatement.executeBatch();
			connection.commit();
			// dbWriteResult = (preparedStatement.executeUpdate() == 1) ? true :
			// false;
			// if (dbWriteResult) {
			// logger.info("Successfully written to DB!");
			// }
		} catch (SQLException e) {
			logger.error("Error in writing to DB", e);
			connection.rollback();
		} finally {
			try {
				preparedStatement.close();
				connection.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		// return dbWriteResult;
	}

	public boolean checkURLinDB(URL url) {
		boolean urlExists = false;
		Connection connection = null;
		Statement statement = null;
		ResultSet resultSet = null;
		try {
			connection = DataBaseManager.getInstance().getConnection();
			statement = connection.createStatement();
			resultSet = statement.executeQuery(SELECT_ALL_RECORDS
					+ " where hashCode=" + url.hashCode());
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
}
