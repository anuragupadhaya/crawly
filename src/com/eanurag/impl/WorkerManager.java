package com.eanurag.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class WorkerManager {
	private static final Integer WORKER_LIMIT = 10;
	private final ExecutorService executor = Executors.newFixedThreadPool(WORKER_LIMIT);

	public ExecutorService getExecutor() {
		return executor;
	}

	private List<Future> futures = new ArrayList<Future>();

	private static volatile WorkerManager instance = null;

	private WorkerManager() {
	}

	public static WorkerManager getInstance() {
		if (instance == null) {
			synchronized (WorkerManager.class) {
				if (instance == null) {
					instance = new WorkerManager();
				}
			}
		}

		return instance;
	}

	public Future createWorker(Callable call) {
		return executor.submit(call);
	}

	public List<Future> getFutures() {
		return futures;
	}

	public void checkWorkerThreads() throws InterruptedException {
		for (Future future : getFutures()) {
			if (future.isDone()) {
				// TODO add the code here to take the future.get()
				// which will give the scrapped data and save it to db?
				System.out.println("Worker has finished");
			} else {
				Thread.sleep(100);
				if (!future.isDone()) {
					System.out.println("Cancelling worker");
					future.cancel(true);
				} else {
					System.out.println("Worker has finished after waiting");
				}
			}
		}
		getFutures().clear();

	}

}
