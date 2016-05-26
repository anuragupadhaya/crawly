package com.eanurag.impl;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ConcurrentLinkedQueue;

import com.eanurag.objects.URL;

public class Crawler {

	private static final Integer CRAWLY_URL_HORIZON_LIMIT = 10;
	private static final Integer CRAWLY_URL_VISITED_LIMIT = 100;

	// TODO initialize the horizon from a property file
	private ConcurrentLinkedQueue<URL> url_horizon = new ConcurrentLinkedQueue<URL>();

	// TODO make it concurrent
	// Collections.newSetFromMap(new ConcurrentHashMap<URL, Boolean>());
	private Set<URL> url_visited = new HashSet<URL>();

	public Set<URL> getUrl_visited() {
		return Collections.synchronizedSet(url_visited);
	}

	public static Integer getCrawlyUrlHorizonLimit() {
		return CRAWLY_URL_HORIZON_LIMIT;
	}

	public static Integer getCrawlyUrlVisitedLimit() {
		return CRAWLY_URL_VISITED_LIMIT;
	}

}
