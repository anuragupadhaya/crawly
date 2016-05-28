package com.eanurag.impl;

import java.io.IOException;
import java.util.Collections;
import java.util.HashSet;
import java.util.Properties;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.TimeUnit;

import com.eanurag.crawler.Crawler;
import com.eanurag.objects.URL;

public class CrawlerApp {

	private static Crawler crawler;

	public static void main(String[] args) {
		crawler = new Crawler();
		initializeApp();
		startCrawling();
	}

	private static void startCrawling() {
		crawler.setUrlVisited(Collections.synchronizedSet(new HashSet<URL>()));
		WorkerManager workers = WorkerManager.getInstance();
		while (!crawler.getUrlHorizon().isEmpty()) {
			workers.createWorker(crawler);
		}

		try {
			workers.getExecutor().shutdown();
			workers.getExecutor().awaitTermination(5, TimeUnit.SECONDS);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	private static void initializeApp() {

		Properties config = new Properties();
		try {
			config.load(CrawlerApp.class.getClassLoader().getResourceAsStream("url-horizon.properties"));
			String[] horizon = config.getProperty("urls").split(",");
			crawler.setUrlHorizon(new ConcurrentLinkedQueue<URL>());
			for (String link : horizon) {
				URL url = new URL();
				url.setURL(link);
				crawler.getUrlHorizon().add(url);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

}
