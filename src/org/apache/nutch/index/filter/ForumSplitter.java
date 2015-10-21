package org.apache.nutch.index.filter;

import java.util.List;

import org.jsoup.nodes.Document;

/**
 * Created by jp242 on 07/10/2015.
 */
public interface ForumSplitter {

	/**
	 * Splits a doc into its composite posts.
	 * 
	 * @param doc
	 * @return
	 */
	public List<Post> split(Document doc);

}
