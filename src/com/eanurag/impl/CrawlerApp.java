package com.eanurag.impl;

import java.io.IOException;
import java.util.Properties;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import com.eanurag.objects.URL;

public class CrawlerApp {

	private static Crawler crawler = new Crawler();

	private static Integer getHorizon_size() {
		return crawler.getUrl_horizon().size();
	}

	public static void main(String[] args) {
		initializeApp();
		startCrawling();

	}

	private static void startCrawling() {
		WorkerManager workers = WorkerManager.getInstance();
		while (getHorizon_size() != 0) {
			Future future = workers.submitNewWorkerThread(crawler);
		}
		try {
			workers.getExecutor().awaitTermination(10, TimeUnit.MINUTES);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	private static void initializeApp() {

		Properties config = new Properties();
		try {
			config.load(CrawlerApp.class.getClassLoader().getResourceAsStream("url-horizon.properties"));
			String[] horizon = config.getProperty("urls").split(",");
			
			for (String link : horizon) {
				URL url = new URL();
				url.setURL(link);
				crawler.getUrl_horizon().add(url);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

}
