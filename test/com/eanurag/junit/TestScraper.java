package com.eanurag.junit;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.eanurag.objects.URL;
import com.eanurag.scaper.Scraper;

public class TestScraper {
	
	URL url = new URL();
	
	Scraper scraper = new Scraper();

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public final void testScrape() {
		url.setURL("https://en.wikipedia.org/wiki/MD5");
		scraper.scrape(url);
	}

}
