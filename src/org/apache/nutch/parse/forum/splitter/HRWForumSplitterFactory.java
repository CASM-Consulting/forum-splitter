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
public class HRWForumSplitterFactory implements IForumSplitterFactory {
	private static final Logger LOG = LoggerFactory.getLogger(HRWForumSplitterFactory.class);
	
	public static final String DOMAIN = "hrw.org";

	private final String BODY_NAME = "article-content";
	private final String CONTENT = "report-body";

	@Override
	public IForumSplitter create() {
		return new HRWForumSplitter();
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
	
	public class HRWForumSplitter extends AbstractForumSplitter {

		public HRWForumSplitter() {
			super(BODY_NAME, CONTENT);
		}

		@Override
		public void mapFields(LinkedList<Post> posts) {}
		
	}
	
}
