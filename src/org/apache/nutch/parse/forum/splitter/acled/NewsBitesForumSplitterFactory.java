package org.apache.nutch.parse.forum.splitter.acled;

//java net imports
import java.net.URISyntaxException;
import java.util.LinkedList;

//logging imports
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.apache.nutch.parse.filter.Post;
import org.apache.nutch.parse.forum.splitter.AbstractForumSplitter;
import org.apache.nutch.parse.forum.splitter.IForumSplitter;
import org.apache.nutch.parse.forum.splitter.IForumSplitterFactory;
import org.apache.nutch.splitter.utils.Utils;

public class NewsBitesForumSplitterFactory implements IForumSplitterFactory {
	private static final Logger LOG = LoggerFactory.getLogger(NewTimeForumSplitterFactory.class);

	
	public static final String DOMAIN = "newsbitesfinance.com";
	
	private static final String BODY_NAME = "container";
	private static final String CONTENT = "xml_body";

	@Override
	public IForumSplitter create() {
		return new NewsBitesForumSplitter();
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
	
	public class NewsBitesForumSplitter extends AbstractForumSplitter {

		public NewsBitesForumSplitter() {
			super(BODY_NAME, CONTENT);
		}

		@Override
		public void mapFields(LinkedList<Post> posts) {}
		
	}

}
