package com.eanurag.utils;

public class Constants {

	public static final String FILTERS_FILE = "filters.properties";
	public static final String URL_HORIZON_FILE = "url-horizon.properties";
	public static final String DB_PROPERTIES_FILE = "db.properties";

	public static final String DRIVER_PROTOCOL = "jdbc:mysql://";
	public static final String DRIVER_CLASS = "com.mysql.jdbc.Driver";

	public static final String DB_USER = "db-user";
	public static final String DB_PASS = "db-pass";
	public static final String DB_HOST = "db-host";
	public static final String DB_PORT = "db-port";
	public static final String DB_NAME = "db-name";
	public static final String URL_TABLE = "url-table-name";
	
	public static final Integer DBPOOL_MINIMUM_POOL_SIZE = 10;
	public static final Integer DBPOOL_MAXIMUM_POOL_SIZE = 100;
	public static final Integer DBPOOL_MAX_STATEMENTS = 200;
	public static final Integer DBPOOL_INCREMENT_SIZE = 5;

}
