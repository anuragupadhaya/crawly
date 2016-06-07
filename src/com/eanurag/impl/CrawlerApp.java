package com.eanurag.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;

import com.eanurag.crawler.Crawler;
import com.eanurag.crawler.CrawlerTask;
import com.eanurag.objects.ScrapedURL;
import com.eanurag.objects.URL;

public class CrawlerApp {

	private final static Logger logger = Logger.getLogger(CrawlerApp.class);

	private static Crawler crawler;

	private static List<String> filters = new ArrayList<String>();

	public static void main(String[] args) {
		crawler = new Crawler();
		initializeApp();
		initializeFilterList();
		startCrawling();
	}

	private static void initializeFilterList() {
		Properties config = new Properties();
		try {
			config.load(CrawlerApp.class.getClassLoader().getResourceAsStream("filters.properties"));
			for (String filter : config.getProperty("filters").split(",")) {
				filters.add(filter);
			}
		} catch (Exception e) {
			logger.error("Error in initializing filter list in CrawlerApp:", e);
		}

	}

	private static void startCrawling() {
		WorkerManager workers = WorkerManager.getInstance(crawler);
		while (!crawler.getUrlHorizon().isEmpty()) {
			URL url = crawler.getUrlHorizon().poll();
			if (crawler.getUrlVisited().contains(url)) {
				logger.warn("duplicate task caught in CrawlerApp");
			} else if (isURLfiltered(url)) {
				logger.warn("filtered URL" + url.getURL());
			} else {
				logger.info("New Thread");
				Future<ScrapedURL> future = workers.getExecutor().submit(new CrawlerTask(url, crawler));
				workers.getFutures().add(future);
			}

		}

		try {
			Thread.sleep(10000);
			workers.checkWorkerThreads();
		} catch (InterruptedException e) {
			logger.error("Interrupted thread in CrawlerApp:", e);
		} catch (ExecutionException e) {
			logger.error("Execution exception in CrawlerApp", e);
		}

		if (!crawler.getUrlHorizon().isEmpty()) {
			startCrawling();
		}

		try {
			workers.getExecutor().shutdown();
			workers.getExecutor().awaitTermination(1, TimeUnit.SECONDS);
		} catch (InterruptedException e) {
			logger.error("Error in CrawlerApp:", e);
		}
	}

	private static boolean isURLfiltered(URL url) {
		for (String filter : filters) {
			if (url.getURL().contains(filter + ".")) {
				return true;
			}
		}
		return false;
	}

	private static void initializeApp() {

		Properties config = new Properties();
		try {
			config.load(CrawlerApp.class.getClassLoader().getResourceAsStream("url-horizon.properties"));
			String[] horizon = config.getProperty("urls").split(",");
			for (String link : horizon) {
				URL url = new URL(link);
				crawler.getUrlHorizon().add(url);
			}
		} catch (Exception e) {
			logger.error("Error in initializing URL Horizon in CrawlerApp:", e);
		}

	}

}
