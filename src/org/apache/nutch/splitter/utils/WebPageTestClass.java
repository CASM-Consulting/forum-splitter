package org.apache.nutch.splitter.utils;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAccessor;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

public class WebPageTestClass {
	
	private static final String BODY_NAME = "cPost";
	private static final String CONTENT = "cPost_contentWrap";
	
	
	public static void run(String page) {
		try {
			Document doc = Jsoup.connect(page).userAgent("Mozilla").get();
			for(Element elem : doc.getElementsByClass(BODY_NAME)) {
				System.out.println(elem.getElementsByClass(CONTENT).text());
				System.out.println(elem.getElementsByTag("time").attr("datetime").split("T")[0]);
				System.out.println(elem.getElementsByClass("cAuthorPane").text().split("\\s")[0]);
			}
//			String[] s = doc.getElementsByClass("author").first().text().split("\\s");
//			String dateS = s[4] + " " + s[5].replace(",","") + " " + s[6];
////			String toks = doc.getElementsByClass("post-username").first().getElementsByClass("avatar-hover").first().text();
//			DateTimeFormatter dtf = DateTimeFormatter.ofPattern("MMM dd yyyy");
//			LocalDate date = LocalDate.parse(dateS,dtf);
////			LocalDate date = LocalDate.from(ta);
////			LocalDate date = LocalDate.parse("Tue Apr 2016",dtf);
//			System.out.println(doc.getElementsByClass(BODY_NAME).get(2).text());
//			System.out.println(doc.text());
		} catch (IOException e) {
			// TODO Auto-generated catch block.
			e.printStackTrace();
		}
	}


	public static void main(String[] args) {
		String page = "http://www.depressionforums.org/forums/topic/105694-wellbutrin-horrible-side-effects/";
		WebPageTestClass.run(page);
		
	}

}
