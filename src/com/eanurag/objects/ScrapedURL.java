package com.eanurag.objects;

import java.util.LinkedHashSet;
import java.util.Set;

public class ScrapedURL {
	private Set<URL> scrapedLinks = new LinkedHashSet<URL>();

	public Set<URL> getScrapedLinks() {
		return scrapedLinks;
	}

	public void setScrapedLinks(Set<URL> scrapedLinks) {
		this.scrapedLinks = scrapedLinks;
	}
}
