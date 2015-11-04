package org.apache.nutch.index.filter;

import java.util.Date;

public interface Post {
	
	/**
	 * @return The text content of the post.
	 */
	public String content();
	
	/**
	 * @return Confirms if this is the first post of a thread.
	 */
	public boolean question();
	
	/**
	 * @return The post-date or index-date of the post.
	 */
	public Date postDate();

}
