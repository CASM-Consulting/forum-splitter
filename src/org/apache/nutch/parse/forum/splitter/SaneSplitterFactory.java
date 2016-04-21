package org.apache.nutch.parse.forum.splitter;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.LinkedList;

import org.apache.nutch.parse.filter.Post;
import org.apache.nutch.splitter.utils.GlobalFieldValues;
import org.apache.nutch.splitter.utils.SplitterFactory;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

public class SaneSplitterFactory implements IForumSplitterFactory {
	
	private static final String BODY_NAME = "postbody";
	private static final String CONTENT = "content";
	private static final String USERNAME = "username";
	private static final String AUTHOR = "author";
	
	private static final String DOMAIN = "http://sane.org.uk/"; 

	@Override
	public IForumSplitter create() {
		return new SaneSplitter(BODY_NAME,CONTENT);
	}
	
	@Override
	public boolean correctDomain(String url) {
		return url.contains(DOMAIN);
	}
	
	public class SaneSplitter extends AbstractForumSplitter {

		public SaneSplitter(String bodyName, String contentName) {
			super(bodyName, contentName);
		}

		@Override
		public void mapFields(LinkedList<Post> posts) {

			for(Post post : posts){

				Document doc = Jsoup.parse(post.postHTML());

				// add member details
				Elements membs = doc.getElementsByClass(USERNAME);
				if(membs.first() != null) {
					final String member = membs.first().text();
					post.put(GlobalFieldValues.MEMBER, String.valueOf(member.hashCode()));
				}
				

				// add post-date information
				Elements author = doc.getElementsByClass(AUTHOR);
				if(author.first() != null) {
					String[] s = author.first().text().split("\\s");
					String dateS = s[4] + " " + s[5].replace(",","") + " " + s[6];
					DateTimeFormatter dtf = DateTimeFormatter.ofPattern("MMM dd yyyy");
					LocalDate date = LocalDate.parse(dateS,dtf);
					post.put(GlobalFieldValues.POST_DATE,date.atStartOfDay().toString() + ":00Z");
				}

			}

		}
		
	}

}
