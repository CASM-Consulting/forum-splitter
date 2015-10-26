package org.apache.nutch.index.filter;

import java.util.Date;

/**
 * Represents a post in a forum thread. Intention is for it to be stored in a
 * LinkedList representing a thread.
 */
public class Post {

	private final Date postDate; // Date posted or indexed if not available.
	private final CharSequence content; // Post content.
	private int id;
	private int parentId;

	/**
	 * Set the post or crawl date and forum post content.
	 * @param date
	 * @param content
	 */
	public Post(Date date, CharSequence content) {
		this.postDate = date;
		this.content = content;
	}

	/**
	 * @return The text content of the post
	 */
	public String content() {
		return content.toString();
	}
	
	/**
	 * Set the parent id of this post if it is a reply.
	 * @param id The id of the forum post posing the question.
	 */
	public void setParentId(int id) {
		parentId = id;
	}

}
