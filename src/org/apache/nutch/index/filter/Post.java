package org.apache.nutch.index.filter;

import java.util.Date;

/**
 * Represents a post in a forum thread. Intention is for it to be stored in a
 * LinkedList representing a thread.
 */
public class Post {

	private final Date postDate; // Date posted or indexed if not available.
	private final CharSequence content; // Post content.

	public Post(Date date, CharSequence content) {
		this.postDate = date;
		this.content = content;
	}

	public String content() {
		return content.toString();
	}

}
