package org.apache.nutch.index.filter.splitter;

// java imports
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

// nutch imports
import org.apache.nutch.index.filter.DefaultPost;
import org.apache.nutch.index.filter.IPost;
// jsoup imports
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

/**
 * Splits a jsoup document according to the phpBB forum convention.
 * @author jp242
 *
 */
public class DefaultForumSplitter implements IForumSplitter {

	private final String BODY_NAME;
	private final String CONTENT;
	
	/**
	 * @param bodyName The classname of the forum post body.
	 * @param contentName The classname of the forum post content
	 */
	public DefaultForumSplitter(String bodyName, String contentName) {
		this.BODY_NAME = bodyName;
		this.CONTENT = contentName;
	}


	@Override
	public LinkedList<IPost> split(Document doc) {
		
		LinkedList<IPost> fThread = new LinkedList<IPost>();

		// Retrieve the full post body
		List<Element> posts = doc.getElementsByClass(BODY_NAME);

		// Get the elements containing the individual forum post and the full html to the linked list.
		fThread.addAll(posts.stream()
				.map(post -> new DefaultPost(new Date(), post.getElementsByClass(CONTENT).html()))
				.collect(Collectors.toList()));

		return fThread;
	}

}
