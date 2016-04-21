package org.apache.nutch.parse.forum.splitter;

// java imports
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.LinkedList;

// jsoup imports
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
// logging imports
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.apache.nutch.parse.filter.Post;
import org.apache.nutch.splitter.utils.GlobalFieldValues;

public class MentalHealthForumSplitterFactory implements IForumSplitterFactory {

	private static final Logger LOG = LoggerFactory.getLogger(MentalHealthForumSplitterFactory.class);

	private static final String DOMAIN = "mentalhealthforum.net";

	private static final String BODY_NAME = "postbitlegacy";
	private static final String CONTENT = "content";
	private static final String MEMBER = "username_container";
	private static final String DATE = "date";

	@Override
	public IForumSplitter create() {
		return new MentalHealthForumSplitter(BODY_NAME,CONTENT);
	}
	
	@Override
	public boolean correctDomain(String url) {
		return url.contains(DOMAIN);
	}

	public class MentalHealthForumSplitter extends AbstractForumSplitter {

		public MentalHealthForumSplitter(String bodyName, String contentName) {
			super(bodyName, contentName);
		}

		@Override
		public void mapFields(LinkedList<Post> posts) {

			LOG.info("mental health forum Posts size = "  + posts.size());
			for(Post post : posts) {

				// add member information
				Document doc = Jsoup.parse(post.postHTML());

				// add member details
				final Elements member = doc.getElementsByClass(MEMBER);
				if(member.first() != null) {
					final String mem =  member.first().text().split("\\s")[0].trim();
					LOG.info("MEMBER: " + member);
					post.put(GlobalFieldValues.MEMBER, String.valueOf(mem.hashCode()));
				}
				
				// add post-date information
				Elements dates = doc.getElementsByClass(DATE);
				if(dates.first() != null) {
					String toks = dates.first().text().split(",")[0];
					DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd-MM-yy");
					LocalDate date = LocalDate.parse(toks,dtf);
					LOG.info("DATE: " + date.toString());
					post.put(GlobalFieldValues.POST_DATE,date.atStartOfDay().toString() + ":00Z");
				}
				else{
														
				}

			}
		}


	}

}
