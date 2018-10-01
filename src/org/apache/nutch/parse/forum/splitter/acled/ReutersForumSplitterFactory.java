package org.apache.nutch.parse.forum.splitter.acled;

//java net imports
import java.net.URISyntaxException;
import java.util.LinkedList;

//Logging 
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.apache.nutch.parse.filter.Post;
import org.apache.nutch.parse.forum.splitter.AbstractForumSplitter;
import org.apache.nutch.parse.forum.splitter.IForumSplitter;
import org.apache.nutch.parse.forum.splitter.IForumSplitterFactory;
import org.apache.nutch.splitter.utils.Utils;

/**
* Forum splitter designed to scrape articles from the IrinNews site
* @author jp242
*/
public class ReutersForumSplitterFactory implements IForumSplitterFactory {
	private static final Logger LOG = LoggerFactory.getLogger(ReutersForumSplitterFactory.class);
	
	public static final String DOMAIN = "reuters.com";

	private final String BODY_NAME = "StandardArticleBody_container";
	private final String CONTENT = "StandardArticleBody_body";

	@Override
	public IForumSplitter create() {
		return new ReutersForumSplitter();
	}

	@Override
	public boolean correctDomain(String url) {
		try {
			return DOMAIN.equals(Utils.getDomain(url));
		} catch (URISyntaxException e) {
			LOG.error("ERROR: Unable to extract domain from: " + url);
		}
		return false;
	}
	
	public class ReutersForumSplitter extends AbstractForumSplitter {

		public ReutersForumSplitter() {
			super(BODY_NAME, CONTENT);
		}

		@Override
		public void mapFields(LinkedList<Post> posts) {}
		
	}
	
}
