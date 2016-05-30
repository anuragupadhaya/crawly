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

		if (crawler.getUrlVisited().contains(url)) {
			System.out.println("duplicate task caught in CrawlerTask");
			return;
		} else {
			System.out.println("No duplicate task caught in CrawlerTask");
			crawler.addURLToVisited(url);
			new Scraper().scrape(url);
		}

	}

	@Override
	public Object call() throws Exception {
		crawlTask();
		return null;
	}

}
