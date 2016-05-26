package com.eanurag.impl;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class WorkerManager {
	private static final Integer WORKER_LIMIT = 10;
	private final ExecutorService executor = Executors.newFixedThreadPool(WORKER_LIMIT);
	
	private void getNewWorkerThread(){
		executor.submit(new Scrapper());
	}

}
