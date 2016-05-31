package com.eanurag.crawler;

import java.util.concurrent.Callable;

import org.apache.log4j.Logger;

import com.eanurag.objects.URL;
import com.eanurag.scaper.Scraper;

public class CrawlerTask implements Callable {

	private final static Logger logger = Logger.getLogger(CrawlerTask.class);

	public CrawlerTask(URL url, Crawler crawler) {
		this.url = url;
		this.crawler = crawler;
	}

	URL url;
	Crawler crawler;

	private void crawlTask() {

		if (crawler.getUrlVisited().contains(url)) {
			logger.warn("duplicate task caught in CrawlerTask");
		} else {
			logger.info("No duplicate task caught in CrawlerTask");
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
