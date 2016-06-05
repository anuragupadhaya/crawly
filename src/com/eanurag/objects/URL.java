package com.eanurag.objects;

public class URL {
	private String url;
	
	private ScrapedURL links = new ScrapedURL();

	public ScrapedURL getLinks() {
		return links;
	}

	public void setLinks(ScrapedURL links) {
		this.links = links;
	}

	public URL(String url){
		this.url = url;
	}

	public String getURL() {
		return url;
	}

	public void setURL(String url) {
		this.url = url;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((url == null) ? 0 : url.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		URL other = (URL) obj;
		if (url == null) {
			if (other.url != null)
				return false;
		} else if (!url.equals(other.url))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return super.toString();
	}
}
