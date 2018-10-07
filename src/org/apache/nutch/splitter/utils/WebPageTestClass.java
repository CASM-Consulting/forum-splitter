package org.apache.nutch.splitter.utils;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAccessor;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;

import org.apache.nutch.parse.filter.Post;
import org.apache.nutch.parse.forum.splitter.AFPForumSplitterFactory;
import org.apache.nutch.parse.forum.splitter.APNewsForumSplitterFactory;
import org.apache.nutch.parse.forum.splitter.APorgForumSplitterFactory;
import org.apache.nutch.parse.forum.splitter.AfricaNewsForumSplitterFactory;
import org.apache.nutch.parse.forum.splitter.BusinessDailyAfricaForumSplitterFactory;
import org.apache.nutch.parse.forum.splitter.HRWForumSplitterFactory;
import org.apache.nutch.parse.forum.splitter.IrinNewsSplitterFactory;
import org.apache.nutch.parse.forum.splitter.NationCoKeForumSplitterFactory;
import org.apache.nutch.parse.forum.splitter.NewTimeForumSplitterFactory;
import org.apache.nutch.parse.forum.splitter.NewVisionForumSplitterFactory;
import org.apache.nutch.parse.forum.splitter.NewsBitesForumSplitterFactory;
import org.apache.nutch.parse.forum.splitter.RFIForumSplitterFactory;
import org.apache.nutch.parse.forum.splitter.ReliefWebForumSplitterFactory;
import org.apache.nutch.parse.forum.splitter.ReutersForumSplitterFactory;
import org.apache.nutch.parse.forum.splitter.StandardMediaForumSplitterFactory;
import org.apache.nutch.parse.forum.splitter.TheCitizenForumSplitterFactory;
import org.apache.nutch.parse.forum.splitter.TheEastAfricanForumSplitterFactory;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

public class WebPageTestClass {
	
//	private static final String BODY_NAME = "post_block";
//	private static final String CONTENT = "post";
	
	
	public static void run(String page) {
		try {
			Document doc = Jsoup.connect(page).userAgent("Mozilla").get();
			RFIForumSplitterFactory splitter = new RFIForumSplitterFactory();
			Post post = splitter.create().split(doc).getFirst();
			System.out.println("Document content");
			System.out.println(doc.text());
			System.out.println("Post content");
			System.out.println(post.content());
			
//			for(Element elem : doc.getElementsByClass(BODY_NAME)) {
//				
//				String dateS = elem.getElementsByClass("published").first().attr("title").split("T")[0];
//				DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
//				LocalDate date = LocalDate.parse(dateS,dtf);
//				
//				System.out.println(date.atStartOfDay() + ":00Z");
//
//				
////				System.out.println(elem.getElementsByClass("published").first().attr("title"));
//				
//				System.out.println(elem.getElementsByClass("author").first().text().trim());
//				
//				System.out.println(elem.getElementsByClass(CONTENT).first().text());
//				System.out.println();		
//				System.out.println(elem.getElementsByClass(CONTENT).text());
	//				System.out.println(elem.getElementsByClass("username").first().text());
	//				System.out.println(elem.getElementsByClass("author").text());
//			}
//			String[] s = doc.getElementsByClass("author").first().text().split("\\s");
//			String dateS = s[4] + " " + s[5].replace(",","") + " " + s[6];
//			String dateS = elem.getElementsByClass("published").first().attr("title");
////			String toks = doc.getElementsByClass("post-username").first().getElementsByClass("avatar-hover").first().text();
//			DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
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
		
//		System.out.println(LocalDate.now().atStartOfDay().toString());
		String page = "http://www.rfi.fr/afrique/20181007-rechauffement-climatique-urgence-action-jean-jouzel";
		WebPageTestClass.run(page);
		
	}

}
