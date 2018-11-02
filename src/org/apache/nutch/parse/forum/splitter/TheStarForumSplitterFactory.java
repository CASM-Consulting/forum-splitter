package org.apache.nutch.parse.forum.splitter;

//java net imports
import java.net.URISyntaxException;
import java.util.LinkedList;

//Logging 
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.apache.nutch.parse.filter.Post;
import org.apache.nutch.splitter.utils.Utils;

/**
* Forum splitter designed to scrape articles from the IrinNews site
* @author jp242
*/
public class TheStarForumSplitterFactory implements IForumSplitterFactory {
	private static final Logger LOG = LoggerFactory.getLogger(TheStarForumSplitterFactory.class);
	
	public static final String DOMAIN = "africaintelligence.com";

	private final String BODY_NAME = "pane-page-content";
	private final String CONTENT = "pane-content";

	@Override
	public IForumSplitter create() {
		return new TheStarForumSplitter();
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
	
	public class TheStarForumSplitter extends AbstractForumSplitter {

		public TheStarForumSplitter() {
			super(BODY_NAME, CONTENT);
		}

		@Override
		public void mapFields(LinkedList<Post> posts) {}
		
	}
	
}
