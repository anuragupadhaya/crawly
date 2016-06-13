package com.eanurag.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.apache.log4j.Logger;

import com.eanurag.crawler.Crawler;
import com.eanurag.objects.ScrapedURL;
import com.eanurag.objects.URL;

public class WorkerManager {
	private final static Logger logger = Logger.getLogger(WorkerManager.class);

	private static final Integer WORKER_LIMIT = 10;
	private final ExecutorService executor = Executors.newFixedThreadPool(WORKER_LIMIT);

	public ExecutorService getExecutor() {
		return executor;
	}

	private List<Future<ScrapedURL>> futures = new ArrayList<Future<ScrapedURL>>();

	private Crawler crawler;

	private static volatile WorkerManager instance = null;

	private WorkerManager(Crawler crawler) {
		this.crawler = crawler;
	}

	public static WorkerManager getInstance(Crawler crawler) {
		if (instance == null) {
			synchronized (WorkerManager.class) {
				if (instance == null) {
					instance = new WorkerManager(crawler);
				}
			}
		}

		return instance;
	}

	public Future<Set<URL>> createWorker(Callable<Set<URL>> call) {
		return executor.submit(call);
	}

	public List<Future<ScrapedURL>> getFutures() {
		return futures;
	}

	public void checkWorkerThreads() throws InterruptedException, ExecutionException {
		for (Future<ScrapedURL> future : getFutures()) {
			if (future.isDone()) {
				logger.info("Worker has finished");
				for (URL url : future.get().getScrapedLinks()) {
					if (!crawler.getUrlHorizon().contains(url)) {
						crawler.getUrlHorizon().add(url);
					}
					else{
						logger.warn("duplicate task caught in WorkerManager:"+url.getURL());
					}
				}
			} else {
				Thread.sleep(1000);
				if (!future.isDone()) {
					logger.error("Cancelling worker");
					future.cancel(true);
				} else {
					logger.warn("Worker has finished after waiting");
				}
			}
		}
		getFutures().clear();

	}

}
