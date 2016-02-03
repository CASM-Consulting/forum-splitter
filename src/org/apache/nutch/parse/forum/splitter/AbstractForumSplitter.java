package org.apache.nutch.parse.forum.splitter;

// java imports
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.nutch.parse.filter.Post;
// jsoup imports
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

/**
 * Splits a jsoup document according to the phpBB forum convention.
 * Extends @HashSet to allow additional html tags/classname/ids to be added and later parsed
 * @author jp242
 */
public abstract class AbstractForumSplitter implements IForumSplitter {

	private final String BODY_NAME;
	private final String CONTENT;
	
	
	/**
	 * @param bodyName The classname of the forum post body.
	 * @param contentName The classname of the forum post content
	 * @param tags Any additional tags/classes/ids to check for within a post
	 */
	public AbstractForumSplitter(String bodyName, String contentName) {
		this.BODY_NAME = bodyName;
		this.CONTENT = contentName;
	}

	@Override
	public LinkedList<Post> split(Document doc) {
		
		LinkedList<Post> fThread = new LinkedList<Post>();

		// Retrieve the full post body
		List<Element> posts = doc.getElementsByClass(BODY_NAME);
				
		// Create a @DefaultPost containing the entire post and the element which contains the post content (assumes there is only one of these).
		fThread.addAll(posts.parallelStream()
				.map(post -> new Post(post.html(), post.getElementsByClass(CONTENT).text()))
				.collect(Collectors.toList()));
		
		mapFields(fThread);

		return fThread;
	}

}
