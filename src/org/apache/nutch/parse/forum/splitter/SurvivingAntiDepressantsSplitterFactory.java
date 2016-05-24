package org.apache.nutch.parse.forum.splitter;

// java imports
import java.util.LinkedList;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

// jsoup imports
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import org.apache.nutch.parse.filter.Post;
import org.apache.nutch.splitter.utils.GlobalFieldValues;

/**
 * @IForumSplitterFactory for http://survivingantidepressants.org
 * @author jp242
 */
public class SurvivingAntiDepressantsSplitterFactory implements IForumSplitterFactory {
	
	// domain where the forum resides
	private static final String DOMAIN = "survivingantidepressants.org";
	// main body/content html tag names
	private static final String BODY = "post_block";
	private static final String CONTENT = "post";
	// member tag name
	private static final String MEMBER = "author";
	// post-date tag/attr name
	private static final String POSTDATE = "published";
	private static final String ATTNAME = "title";

	@Override
	public IForumSplitter create() {
		return new SurvivingAntiDepressantsSplitter(BODY,CONTENT);
	}

	@Override
	public boolean correctDomain(String url) {
		return url.contains(DOMAIN);
	}
	
	public class SurvivingAntiDepressantsSplitter extends AbstractForumSplitter {

		public SurvivingAntiDepressantsSplitter(String bodyName, String contentName) {
			super(bodyName, contentName);
		}

		@Override
		public void mapFields(LinkedList<Post> posts) {
			
			for(Post post : posts) {
				Document doc = Jsoup.parse(post.postHTML());
				
				// Get the member information
				String member = doc.getElementsByClass(MEMBER).first().text().trim();
				post.put(GlobalFieldValues.MEMBER, member);
				
				// Get the post-date information	
				String dateS = doc.getElementsByClass(POSTDATE).first().attr(ATTNAME).split("T")[0];
				DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
				LocalDate date = LocalDate.parse(dateS,dtf);
				post.put(GlobalFieldValues.POST_DATE, date.atStartOfDay() + ":00Z");
				
				
			}
			
		}
		
	}

}
