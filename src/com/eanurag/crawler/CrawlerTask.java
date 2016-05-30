package com.eanurag.crawler;

import java.util.concurrent.Callable;

import com.eanurag.objects.URL;
import com.eanurag.scaper.Scraper;

public class CrawlerTask implements Callable {
	public CrawlerTask(URL url, Crawler crawler) {
		this.url = url;
		this.crawler = crawler;
	}

	URL url;
	Crawler crawler;

	private void crawlTask() {
		synchronized (crawler) {
			if (!crawler.getUrlVisited().contains(url)) {
				new Scraper().scrape(url);
				crawler.addURLToVisited(url);
			}
		}

	}

	@Override
	public Object call() throws Exception {
		crawlTask();
		return null;
	}

}
