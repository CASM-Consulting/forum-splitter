package org.apache.nutch.index.filter;

import java.util.Date;

/**
 * Represents a post in a forum thread. Intention is for it to be stored in a
 * LinkedList representing a thread.
 */
public class BasicPost implements Post {
	
	private final Date postDate; 		// Date posted or indexed if not available.
	private final CharSequence content; // Post content.
	private final boolean question;		// Is this the thread question or simply a post. Default: false

	/**
	 * Set the post or crawl date and forum post content.
	 * @param date
	 * @param content
	 */
	public BasicPost(Date date, CharSequence content) {
		this.postDate = date;
		this.content = content;
		this.question = false;
	}
	
	/**
	 * Set the post or crawl date and forum post content.
	 * @param date	Date posted if available or date indexed
	 * @param content Post content	
	 * @param question Thread question/first post.
	 */
	public BasicPost(Date date, CharSequence content, boolean question) {
		this.postDate = date;
		this.content = content;
		this.question = question;
	} 

	/**
	 * @return The text content of the post.
	 */
	public String content() {
		return content.toString();
	}
	
	/**
	 * @return Confirms if this is the first post of a thread.
	 */
	public boolean question() {
		return question;
	}
	
	/**
	 * @return The post-date or index-date of the post.
	 */
	public Date postDate() {
		return postDate;
	}

}
