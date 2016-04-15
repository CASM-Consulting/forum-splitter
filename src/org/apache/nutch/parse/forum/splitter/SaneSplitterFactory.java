package org.apache.nutch.parse.forum.splitter;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.LinkedList;

import org.apache.nutch.parse.filter.Post;
import org.apache.nutch.splitter.utils.GlobalFieldValues;
import org.apache.nutch.splitter.utils.SplitterFactory;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

public class SaneSplitterFactory implements IForumSplitterFactory {
	
	private static final String BODY_NAME = "postbody";
	private static final String CONTENT = "content";
	private static final String USERNAME = "username";

	@Override
	public IForumSplitter create() {
		return new SaneSplitter(BODY_NAME,CONTENT);
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
				final String member = doc.getElementsByClass(USERNAME).first().text();
				post.put(GlobalFieldValues.MEMBER,member);

				// add post-date information
				String[] s = doc.getElementsByClass("author").first().text().split("\\s");
				String dateS = s[4] + " " + s[5].replace(",","") + " " + s[6];
				DateTimeFormatter dtf = DateTimeFormatter.ofPattern("MMM dd yyyy");
				LocalDate date = LocalDate.parse(dateS,dtf);
				post.put(GlobalFieldValues.POST_DATE,date.toString());
			}

		}
		
	}

}
