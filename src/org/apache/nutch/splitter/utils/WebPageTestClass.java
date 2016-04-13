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

public class WebPageTestClass {
	
	private static final String BODY_NAME = "post";
	private static final String CONTENT = "post_content";
	
	
	public static void run(String page) {
		try {
			Document doc = Jsoup.connect(page).userAgent("Mozilla").get();
			String toks = doc.getElementsByClass("post-username").first().getElementsByClass("avatar-hover").first().text();
//			DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
//			LocalDate date = LocalDate.parse(toks,dtf);
//			LocalDate date = LocalDate.from(ta);
//			LocalDate date = LocalDate.parse("Tue Apr 2016",dtf);
			System.out.println(toks);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	public static void main(String[] args) {
		String page = "http://patient.info/forums/discuss/well-everyone-i-m-ready-to-take-the-plunge--506987";
		WebPageTestClass.run(page);
		
	}

}
