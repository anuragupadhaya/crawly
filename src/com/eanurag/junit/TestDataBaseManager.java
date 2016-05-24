package com.eanurag.junit;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.eanurag.impl.DataBaseManager;

public class TestDataBaseManager {
	
	DataBaseManager dbManager = new DataBaseManager();
	private static final String SELECT_ALL_RECORDS = "SELECT * FROM `crawly`.`url`";
	
	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public final void testGetDBInstance() {
		dbManager.getDBInstance();
	}
	
	@Test
	public final void testReadData(){
		dbManager.readData(SELECT_ALL_RECORDS);
	}

}
