package org.apache.nutch.parse.forum.splitter;

import java.util.LinkedList;
import java.util.List;

import org.apache.nutch.parse.filter.Post;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ChildlineForumSplitterFactory implements IForumSplitterFactory {
	
	private static final Logger LOG = LoggerFactory.getLogger(ChildlineForumSplitterFactory.class);
	
	// TODO: post-date format
	// Username
	
	private static final String DOMAIN = "childline.org.uk/get-support/message-boards";
	
	private static final String BODY_NAME = "js-reply-container";
	private static final String CONTENT = "forum__post__content__inner";
	private static final String QUOTE = "forum__quoted-message";
	private static final String USER_DATE = "span";
	private static final String USER = "b";
	private static final String CD_DATA = "p";
	private static final String TOPIC_ID = "data-topic-id";


	@Override
	public IForumSplitter create() {
		return new ChildlineForumSplitter(BODY_NAME,CONTENT);
	}

	@Override
	public boolean correctDomain(String url) {
		return url.contains(DOMAIN);
	}
	
	public class ChildlineForumSplitter implements IForumSplitter {
		
		private final String BODY_NAME;
		private final String CONTENT;

		public ChildlineForumSplitter(String bodyName, String contentName) {
			BODY_NAME = bodyName;
			CONTENT = contentName;
		}

		@Override
		public void mapFields(LinkedList<Post> posts) {
			
			LOG.info("Childline message board posts found = "  + posts.size());

			for(Post post : posts){
				
				Document doc = Jsoup.parse(post.postHTML());
				final String post_id = doc.getElementsByTag("a").get(0).attr("name");
				Elements quoted = doc.getElementsByClass(QUOTE);
				if(quoted.size() > 0) {
					final String[] quotedInfo = getQuotedInfo(quoted.get(0)); // complete this info
				}
			}
		}

		@Override
		public LinkedList<Post> split(Document doc) {
			
			// Gets the thread id to attribute to each post
			final String thread_ID = doc.getElementsByTag("ol").get(0).attr(TOPIC_ID, true).text();
			
			LinkedList<Post> fThread = new LinkedList<Post>();
			
			// Retrieve the full post body
			List<Element> posts = doc.getElementsByClass(BODY_NAME);
			
			// Get the post text
			for(Element post : posts) {
				final Elements text = post.getElementsByTag(CD_DATA);
				final StringBuilder post_text = new StringBuilder();
				for(Element element : text) {
					post_text.append(element.text()).append(" ");
				}
				fThread.add(new Post(post.html(),post_text.toString()));
			}
			return fThread;
		}
		
		/**
		 * @param html The html body of the quoted post
		 * @return The user and post date of the quoted/respondent post for cross referencing
		 */
		public String[] getQuotedInfo(Element html) {
			return null;
		}
		
	}

}
