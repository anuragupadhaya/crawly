package com.eanurag.impl;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

//Executor Service will be a good candidate for Singleton class
public class WorkerManager {
	private static final Integer WORKER_LIMIT = 10;
	private final ExecutorService executor = Executors.newFixedThreadPool(WORKER_LIMIT);

	public ExecutorService getExecutor() {
		return executor;
	}

	// Singleton
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

	// TODO can also be submit(Callable) if call() method needs to return a
	// value
	public Future createWorker(Runnable run) {
		return executor.submit(run);
	}

	public void checkWorkerThread(Future future) throws InterruptedException, ExecutionException {
		if (future.get() == null) {
			// TODO add the actual handling code here and remove syso
			System.out.println("Worker has finished");
		}
	}

}
