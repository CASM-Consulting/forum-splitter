package org.apache.nutch.parse.forum.splitter;

//java net imports
import java.net.URISyntaxException;
import java.util.LinkedList;

//Logging 
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.apache.nutch.parse.filter.Post;
import org.apache.nutch.splitter.utils.Utils;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

/**
* Forum splitter designed to scrape articles from the IrinNews site
* @author jp242
*/
public class RFIForumSplitterFactory implements IForumSplitterFactory {
	private static final Logger LOG = LoggerFactory.getLogger(RFIForumSplitterFactory.class);
	
	public static final String DOMAIN = "rfi.fr";

	private final String BODY_NAME = "article";

	@Override
	public IForumSplitter create() {
		return new RFIForumSplitter();
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
	
	public class RFIForumSplitter implements IForumSplitter {

		public void mapFields(LinkedList<Post> posts) {}

		@Override
		public LinkedList<Post> split(Document doc) {
			
			LinkedList<Post> fThread = new LinkedList<Post>();
			
			Element elem = doc.getElementsByTag(BODY_NAME).first();
			
			fThread.add(new Post(elem.html(),elem.text()));
			
			return fThread;
		}
		
	}
	
}
