package com.eanurag.crawler;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ConcurrentLinkedQueue;

import com.eanurag.objects.URL;

public class Crawler {

	private static final Integer CRAWLY_URL_HORIZON_LIMIT = 10;
	private static final Integer CRAWLY_URL_VISITED_LIMIT = 100;

	// TODO Check feasibility of LinkedBlockingQueue
	private volatile ConcurrentLinkedQueue<URL> urlHorizon = new ConcurrentLinkedQueue<URL>();

	public void setUrlHorizon(ConcurrentLinkedQueue<URL> urlHorizon) {
		this.urlHorizon = urlHorizon;
	}

	public ConcurrentLinkedQueue<URL> getUrlHorizon() {
		return urlHorizon;
	}

	// TODO make it concurrent
	// Collections.newSetFromMap(new ConcurrentHashMap<URL, Boolean>());
	private volatile Set<URL> urlVisited = Collections.synchronizedSet(new HashSet<URL>());

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

	public void addURLToVisited(URL url) {
		if (getUrlVisited().contains(url)) {
			System.out.println("Duplicate found in already visited:" + url.getURL());
			return;
		} else {
			System.out.println("Adding to visited set:" + url.getURL());
			getUrlVisited().add(url);
		}
	}

}
