package com.eanurag.interfaces;

public interface ICrawler {
	void URLConnect();

	void readHTML();

	void checkURL();

	void saveHTML();

	void extractData();
}
