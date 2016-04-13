package org.apache.nutch.parse.forum.splitter;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.LinkedList;

import org.apache.nutch.parse.filter.Post;
import org.apache.nutch.splitter.utils.GlobalFieldValues;
import org.apache.nutch.splitter.utils.SplitterFactory;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

public class PatientInfoSplitterFactory implements IForumSplitterFactory {
	
	public static final String POST = "post";
	public static final String CONTENT = "post-content";

	public static final String USER = "post-username";
	public static final String AVATAR = "avatar-hover";

	@Override
	public IForumSplitter create() {
		return new PatientInfoSplitter(POST,CONTENT);
	}
	
	public class PatientInfoSplitter extends AbstractForumSplitter {

		public PatientInfoSplitter(String bodyName, String contentName) {
			super(bodyName, contentName);
		}

		@Override
		public void mapFields(LinkedList<Post> posts) {

			for(Post post : posts) {

				Document doc = Jsoup.parse(post.postHTML());

				// Add member information
				String member = doc.getElementsByClass(USER).first().getElementsByClass(AVATAR).first().text();
				post.put(GlobalFieldValues.MEMBER,member);


				// Add post-date information
				String toks = doc.getElementsByClass("fuzzy").first().attr("datetime").split("T")[0];
				DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
				LocalDate date = LocalDate.parse(toks,dtf);
				post.put(GlobalFieldValues.POST_DATE,post.toString());
			}
		}
		
	}
	
}
