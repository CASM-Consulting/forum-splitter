package org.apache.nutch.index.filter.splitter;

import java.util.LinkedList;

import org.apache.nutch.index.filter.IPost;
import org.jsoup.nodes.Document;

/**
 * Created by jp242 on 07/10/2015.
 */
public interface IForumSplitter {

	/**
	 * Retrieves individual forum posts that may be present in the page.
	 * @param doc jsoup Document object representing the web page.
	 * @return Linked list of forum posts
	 */
	public LinkedList<IPost> split(Document doc);

}
