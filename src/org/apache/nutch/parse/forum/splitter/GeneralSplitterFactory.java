package org.apache.nutch.parse.forum.splitter;

import java.net.URISyntaxException;
import java.util.LinkedList;

import org.apache.nutch.parse.filter.Post;
import org.apache.nutch.splitter.utils.Utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GeneralSplitterFactory implements IForumSplitterFactory {
	
	private static final Logger LOG = LoggerFactory.getLogger(GeneralSplitterFactory.class);
	
	private final String DOMAIN;
	private final String BODY;
	private final String CONTENT;

	public GeneralSplitterFactory(String domain,String body, String content) {
		this.DOMAIN = domain;
		this.BODY = body;
		this.CONTENT = content;
	}

	@Override
	public IForumSplitter create() {
		return new GeneralSplitter(BODY,CONTENT);
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
	
	public class GeneralSplitter extends AbstractForumSplitter {

		public GeneralSplitter(String bodyName, String contentName) {
			super(bodyName, contentName);
		}

		@Override
		public void mapFields(LinkedList<Post> posts) {}
		
	}

}
