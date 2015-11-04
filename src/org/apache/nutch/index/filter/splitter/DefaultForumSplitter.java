package org.apache.nutch.index.filter.splitter;

// java imports
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

// nutch imports
import org.apache.nutch.index.filter.BasicPost;
import org.apache.nutch.index.filter.Post;
// jsoup imports
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

/**
 * Splits a jsoup document according to the phpBB forum convention.
 * 
 * @author jp242
 *
 */
public class DefaultForumSplitter implements ForumSplitter {

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
	public LinkedList<Post> split(Document doc) {
		
		LinkedList<Post> fThread = new LinkedList<Post>();

		// Retrieve the full post body
		List<Element> posts = doc.getElementsByClass(BODY_NAME);

		// Get the elements containing the individual forum post and add them to the linked list.
		for (Element post : posts) {
			fThread.add(new BasicPost(new Date(), post.getElementsByClass(CONTENT).text()));
		}

		return fThread;
	}

}
