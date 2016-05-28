package com.eanurag.impl;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ConcurrentLinkedQueue;

import com.eanurag.objects.URL;

public class Crawler implements Runnable {

	private static final Integer CRAWLY_URL_HORIZON_LIMIT = 10;
	private static final Integer CRAWLY_URL_VISITED_LIMIT = 100;

	// TODO initialize the horizon from a property file
	private ConcurrentLinkedQueue<URL> url_horizon = new ConcurrentLinkedQueue<URL>();

	public ConcurrentLinkedQueue<URL> getUrl_horizon() {
		return url_horizon;
	}

	// TODO make it concurrent
	// Collections.newSetFromMap(new ConcurrentHashMap<URL, Boolean>());
	private Set<URL> url_visited = new HashSet<URL>();

	public Set<URL> getUrl_visited() {
		return Collections.synchronizedSet(url_visited);
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
		scrap(url);
		addURLToVisited(url);

	}

	private URL nextURLFromHorizon() {
		if (!getUrl_horizon().isEmpty()) {
			URL url = url_horizon.poll();
			if (!getUrl_visited().contains(url)) {
				System.out.println("Horizon URL:" + url.getURL());
				return url;
			}
			nextURLFromHorizon();
		}
		return null;

	}

	private void scrap(URL url) {
		new Scrapper().scrap(url);
	}

	private void addURLToVisited(URL url) {
		System.out.println("Adding to visited set:" + url.getURL());
		getUrl_visited().add(url);
	}

}
