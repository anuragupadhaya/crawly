package com.eanurag.crawler;

import java.util.concurrent.Callable;

import org.apache.log4j.Logger;

import com.eanurag.objects.ScrapedURL;
import com.eanurag.objects.URL;
import com.eanurag.scaper.Scraper;

public class CrawlerTask implements Callable<ScrapedURL> {

	private final static Logger logger = Logger.getLogger(CrawlerTask.class);

	public CrawlerTask(URL url, Crawler crawler) {
		this.url = url;
		this.crawler = crawler;
	}

	URL url;
	Crawler crawler;

	private ScrapedURL crawlTask() {
		
		ScrapedURL scrapedlinks = url.getLinks();

		if (crawler.getUrlVisited().contains(url)) {
			logger.warn("duplicate task caught in CrawlerTask");
		} else {
			logger.info("No duplicate task caught in CrawlerTask");
			crawler.addURLToVisited(url);
			scrapedlinks = new Scraper().scrape(url);
		}
		return scrapedlinks;

	}

	@Override
	public ScrapedURL call(){
		return crawlTask();
	}

}
