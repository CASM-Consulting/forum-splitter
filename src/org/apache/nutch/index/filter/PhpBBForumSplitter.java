package org.apache.nutch.index.filter;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

/**
 * Splits a jsoup document according to the phpBB forum convention.
 * 
 * @author jp242
 *
 */
public class PhpBBForumSplitter implements ForumSplitter {

	private static final String BODY_NAME = "postbody";
	private static final String CONTENT = "content";

	@Override
	public LinkedList<Post> split(Document doc) {
		
		LinkedList<Post> fThread = new LinkedList<Post>();

		List<Element> posts = doc.getElementsByClass(BODY_NAME);

		for (Element post : posts) {
			System.err.println(post.getElementsByClass(CONTENT).text());
			fThread.add(new Post(new Date(), post.getElementsByClass(CONTENT).text()));
		}

		return fThread;
	}

}
