package org.apache.nutch.splitter.utils;

import java.io.IOException;
import java.util.Iterator;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

public class WebPageTestClass {
	
	private static final String BODY_NAME = "post_block";
	private static final String CONTENT = "author";
	
	
	public static void run(String page) {
		try {
			Document doc = Jsoup.connect(page).userAgent("Mozilla").get();
			System.out.println(doc.getElementsByClass(BODY_NAME).first().getElementsByClass(CONTENT).last().text().trim());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	public static void main(String[] args) {
		String page = "http://www.myproana.com/index.php/topic/84632-a-forum-dedicated-to-recovery";
		WebPageTestClass.run(page);
		
	}

}
