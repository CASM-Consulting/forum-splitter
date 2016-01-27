package org.apache.nutch.index.filter;

// java imports
import java.util.HashMap;

/**
 * Represents a single post in a forum thread.
 */
public class Post extends HashMap<String,String> {
	
	private static final long serialVersionUID = 1442372085805974485L;
	
	private String POST;		// The entire post html
	private String CONTENT; 	// The post content (html expected)
	
	/**
	 * Empty constructor for @Gson "serialization"
	 */
	public Post(){}
	
	/**
	 * For standard instantiation purposes.
	 * @param post
	 * @param content
	 */
	public Post(String post, String content) {
		this.POST = post;
		this.CONTENT = content;
	}
	
	/**
	 * @return The post in its entirety.
	 */
	public String post() {
		return POST;
	}
	
	/**
	 * @return The html content of the post body.
	 */
	public String content() {
		return CONTENT;
	}
	
}
