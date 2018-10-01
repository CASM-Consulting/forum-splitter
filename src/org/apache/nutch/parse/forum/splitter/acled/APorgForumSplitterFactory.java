package org.apache.nutch.parse.forum.splitter.acled;

//java net imports
import java.net.URISyntaxException;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

//Logging 
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.apache.nutch.parse.filter.Post;
import org.apache.nutch.parse.forum.splitter.AbstractForumSplitter;
import org.apache.nutch.parse.forum.splitter.IForumSplitter;
import org.apache.nutch.parse.forum.splitter.IForumSplitterFactory;
import org.apache.nutch.splitter.utils.Utils;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

/**
* Forum splitter designed to scrape articles from the IrinNews site
* @author jp242
*/
public class APorgForumSplitterFactory implements IForumSplitterFactory {
	private static final Logger LOG = LoggerFactory.getLogger(APorgForumSplitterFactory.class);
	
	public static final String DOMAIN = "ap.org";

	private final String BODY_NAME = "main";
	private final String CONTENT = "article";

	@Override
	public IForumSplitter create() {
		return new APorgForumSplitter(BODY_NAME,CONTENT);
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
	
	public class APorgForumSplitter implements IForumSplitter {
		
		private final String body;
		private final String content;

		public APorgForumSplitter(String body, String content) {
			this.body = body;
			this.content = content;
		}
		
		@Override
		public LinkedList<Post> split(Document doc) {
			
			LinkedList<Post> fThread = new LinkedList<Post>();

			// Retrieve the full post body
			List<Element> posts = doc.getElementsByTag(body);
			if(posts.size() > 0) {
				fThread.add(new Post(posts.get(0).html(), posts.get(0).getElementsByTag(content).text()));
			}

			return fThread;
		}

		@Override
		public void mapFields(LinkedList<Post> posts) {}
		
	}
	
}
