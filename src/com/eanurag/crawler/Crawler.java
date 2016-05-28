package com.eanurag.crawler;

import java.util.Set;
import java.util.concurrent.ConcurrentLinkedQueue;

import com.eanurag.objects.URL;
import com.eanurag.scaper.Scraper;

public class Crawler implements Runnable {

	private static final Integer CRAWLY_URL_HORIZON_LIMIT = 10;
	private static final Integer CRAWLY_URL_VISITED_LIMIT = 100;

	// TODO initialize the horizon from a property file
	private ConcurrentLinkedQueue<URL> urlHorizon;

	public void setUrlHorizon(ConcurrentLinkedQueue<URL> urlHorizon) {
		this.urlHorizon = urlHorizon;
	}

	public ConcurrentLinkedQueue<URL> getUrlHorizon() {
		return urlHorizon;
	}

	// TODO make it concurrent
	// Collections.newSetFromMap(new ConcurrentHashMap<URL, Boolean>());
	private Set<URL> urlVisited;

	public void setUrlVisited(Set<URL> urlVisited) {
		this.urlVisited = urlVisited;
	}

	public Set<URL> getUrlVisited() {
		return urlVisited;
	}

	public Integer getCrawlyUrlHorizonLimit() {
		return CRAWLY_URL_HORIZON_LIMIT;
	}

	public Integer getCrawlyUrlVisitedLimit() {
		return CRAWLY_URL_VISITED_LIMIT;
	}

	@Override
	public void run() {
		URL url = nextURLFromHorizon();
		scrape(url);
		addURLToVisited(url);

	}

	private URL nextURLFromHorizon() {
		if (!getUrlHorizon().isEmpty()) {
			URL url = urlHorizon.poll();
			if (getUrlVisited().contains(url)) {
				nextURLFromHorizon();
			}
			System.out.println("Horizon URL:" + url.getURL());
			return url;

		}
		return null;

	}

	private void scrape(URL url) {
		new Scraper().scrape(url);
	}

	private void addURLToVisited(URL url) {
		System.out.println("Adding to visited set:" + url.getURL());
		getUrlVisited().add(url);
	}

}
