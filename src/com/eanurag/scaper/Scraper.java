package com.eanurag.scaper;

import org.apache.log4j.Logger;

import com.eanurag.objects.URL;

public class Scraper {

	private final static Logger logger = Logger.getLogger(Scraper.class);

	public void scrape(URL url) {
		// FIXME dummy job
		logger.info("Done scrapping:" + url.getURL());
	}

}
