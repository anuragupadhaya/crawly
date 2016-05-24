package com.eanurag.interfaces;

import java.sql.Connection;

public interface IDAO {
	Connection getDBInstance();

	void writeData();

	void readData(String query);
}
