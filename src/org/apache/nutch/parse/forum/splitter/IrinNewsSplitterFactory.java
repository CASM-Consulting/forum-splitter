package org.apache.nutch.parse.forum.splitter;


// java net imports
import java.net.URISyntaxException;
import java.util.LinkedList;

// Logging 
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.apache.nutch.parse.filter.Post;
import org.apache.nutch.splitter.utils.Utils;

/**
 * Forum splitter designed to scrape articles from the IrinNews site
 * @author jp242
 */
public class IrinNewsSplitterFactory implements IForumSplitterFactory {
	private static final Logger LOG = LoggerFactory.getLogger(IrinNewsSplitterFactory.class);
	
	public static final String DOMAIN = "irinnews.org";

	private final String BODY_NAME = "report-content";
	private final String CONTENT = "report-content--body";

	@Override
	public IForumSplitter create() {
		return new IrinNewsSplitter();
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
	
	public class IrinNewsSplitter extends AbstractForumSplitter {

		public IrinNewsSplitter() {
			super(BODY_NAME, CONTENT);
		}

		@Override
		public void mapFields(LinkedList<Post> posts) {}
		
	}
	
}
