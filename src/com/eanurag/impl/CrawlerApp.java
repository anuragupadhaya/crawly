package com.eanurag.impl;

import java.io.IOException;
import java.util.Properties;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import com.eanurag.crawler.Crawler;
import com.eanurag.crawler.CrawlerTask;
import com.eanurag.objects.URL;

public class CrawlerApp {

	private static Crawler crawler;

	public static void main(String[] args) {
		crawler = new Crawler();
		initializeApp();
		startCrawling();
	}

	private static void startCrawling() {
		WorkerManager workers = WorkerManager.getInstance();
		while (!crawler.getUrlHorizon().isEmpty()) {
			URL url = crawler.getUrlHorizon().poll();
			if(!crawler.getUrlVisited().contains(url)){
				Future future = workers.getExecutor().submit(new CrawlerTask(url, crawler));
			}
			
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
