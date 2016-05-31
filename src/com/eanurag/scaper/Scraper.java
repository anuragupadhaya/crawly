package com.eanurag.scaper;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.eanurag.objects.URL;

public class Scraper {

	private final static Logger logger = Logger.getLogger(Scraper.class);

	public void scrape(URL url) {
		// FIXME dummy job
//		logger.info("Done scrapping:" + url.getURL());
		
		Document doc = null;
		
		List<String> urls = new ArrayList<String>();
		
		try {
			doc = Jsoup.connect(url.getURL()).get();
			
			Elements links = doc.getElementsByTag("a");
			
			for(Element link: links){
				logger.info(link.attr("href"));
				urls.add(link.attr("href"));
			}
			
			while(!urls.isEmpty()){
				String link = urls.remove(0);
				URL toVisit = new URL();
				toVisit.setURL(link);
				scrape(toVisit);
			}
			
		} catch (Exception e) {
			logger.error(e);
		}
	}

}
