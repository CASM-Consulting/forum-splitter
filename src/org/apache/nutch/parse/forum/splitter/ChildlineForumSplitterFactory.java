package org.apache.nutch.parse.forum.splitter;

import java.util.LinkedList;

import org.apache.nutch.parse.filter.Post;
import org.apache.nutch.splitter.utils.GlobalFieldValues;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ChildlineForumSplitterFactory implements IForumSplitterFactory {
	
	private static final Logger LOG = LoggerFactory.getLogger(ChildlineForumSplitterFactory.class);

	
	private static final String DOMAIN = "childline.org.uk/get-support/message-boards";
	
	private static final String BODY_NAME = "forum__post__content";
	private static final String CONTENT = "forum__post__content__inner";
	private static final String USER_DATE = "span";
	private static final String USER = "b";
	private static final String CD_DATA = "p";


	@Override
	public IForumSplitter create() {
		return new ChildlineForumSplitter(BODY_NAME,CONTENT);
	}

	@Override
	public boolean correctDomain(String url) {
		return url.contains(DOMAIN);
	}
	
	public class ChildlineForumSplitter extends AbstractForumSplitter {

		public ChildlineForumSplitter(String bodyName, String contentName) {
			super(bodyName, contentName);
		}

		@Override
		public void mapFields(LinkedList<Post> posts) {
			
			LOG.info("Childline message board posts found = "  + posts.size());

			for(Post post : posts){
				
				// Parse the inner post content
				Document doc = Jsoup.parse(post.get(GlobalFieldValues.CONTENT).get(0));
				
				// Get the post text
				final Elements text = doc.getElementsByTag(CD_DATA);
				final StringBuilder post_text = new StringBuilder();
				for(Element element : text) {
					post_text.append(element.text()).append(" ");
				}
				
				
			}
		}
		
	}

}
